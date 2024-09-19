package com.github.grooviter.underdog.impl

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory

import static org.apache.poi.ss.usermodel.CellType.BLANK
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

import com.google.common.collect.Iterables;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.concurrent.Immutable;
import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.format.CellGeneralFormatter;
import org.apache.poi.ss.format.CellNumberFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.LongColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.io.DataReader;
import tech.tablesaw.io.ReaderRegistry;
import tech.tablesaw.io.RuntimeIOException;
import tech.tablesaw.io.Source;

@Immutable
public class TSExcelReader implements DataReader<TSExcelReaderOptions> {

    private static final TSExcelReader INSTANCE = new TSExcelReader();

    static {
        register(Table.defaultReaderRegistry);
    }

    public static void register(ReaderRegistry registry) {
        registry.registerExtension("xlsx", INSTANCE);
        registry.registerMimeType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", INSTANCE);
        registry.registerOptions(TSExcelReaderOptions.class, INSTANCE);
    }

    @Override
    public Table read(TSExcelReaderOptions options) {
        List<Table> tables = null;
        try {
            tables = readMultiple(options, true);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
        if (options.sheetIndex() != null) {
            int index = options.sheetIndex();
            if (index < 0 || index >= tables.size()) {
                throw new IndexOutOfBoundsException(
                        String.format("Sheet index %d outside bounds. %d sheets found.", index, tables.size()));
            }

            Table table = tables.get(index);
            if (table == null) {
                throw new IllegalArgumentException(
                        String.format("No table found at sheet index %d.", index));
            }
            return table;
        }
        // since no specific sheetIndex asked, return first table
        return tables.stream()
                .filter(t -> t != null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No tables found."));
    }

    public List<Table> readMultiple(TSExcelReaderOptions options) throws IOException {
        return readMultiple(options, false);
    }

    /**
     * Read at most a table from every sheet.
     *
     * @param includeNulls include nulls for sheets without a table
     * @return a list of tables, at most one for every sheet
     */
    protected List<Table> readMultiple(TSExcelReaderOptions options, boolean includeNulls)
            throws IOException {
        byte[] bytes = null;
        InputStream input = getInputStream(options, bytes);
        List<Table> tables = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(input)) {
            for (Sheet sheet : workbook) {
                TableRange tableArea = findTableArea(sheet, options);
                if (tableArea != null) {
                    Table table = createTable(sheet, tableArea, options);
                    tables.add(table);
                } else if (includeNulls) {
                    tables.add(null);
                }
            }
            return tables;
        } finally {
            if (options.source().reader() == null) {
                // if we get a reader back from options it means the client opened it, so let
                // the client close it
                // if it's null, we close it here.
                input.close();
            }
        }
    }

    private Boolean isBlank(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                if (cell.getRichStringCellValue().length() > 0) {
                    return false;
                }
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)
                        ? cell.getDateCellValue() != null
                        : cell.getNumericCellValue() != 0) {
                    return false;
                }
                break;
            case BOOLEAN:
                if (cell.getBooleanCellValue()) {
                    return false;
                }
                break;
            case BLANK:
                return true;
            default:
                break;
        }
        return null;
    }

    private static class TableRange {
        private int startRow, endRow, startColumn, endColumn;

        TableRange(int startRow, int endRow, int startColumn, int endColumn) {
            this.startRow = startRow;
            this.endRow = endRow;
            this.startColumn = startColumn;
            this.endColumn = endColumn;
        }

        public int getColumnCount() {
            return endColumn - startColumn + 1;
        }
    }

    private TableRange findTableArea(Sheet sheet, TSExcelReaderOptions options) {
        int firstCol = Integer.MAX_VALUE, lastCol = 0

        for (Row row : sheet) {
            if (row.firstCellNum < firstCol) {
                firstCol = row.firstCellNum
            }
            if (row.lastCellNum > lastCol) {
                lastCol = row.lastCellNum - 1
            }
        }

        int firstRow = sheet.firstRowNum + options.skipRows
        int lastRow = sheet.lastRowNum - options.skipFooter

        return new TableRange(firstRow, lastRow, firstCol, lastCol)
    }

    private InputStream getInputStream(TSExcelReaderOptions options, byte[] bytes)
            throws FileNotFoundException {
        if (bytes != null) {
            return new ByteArrayInputStream(bytes);
        }
        if (options.source().inputStream() != null) {
            return options.source().inputStream();
        }
        return new FileInputStream(options.source().file());
    }

    private Table createTable(Sheet sheet, TableRange tableArea, TSExcelReaderOptions options) {
        Optional<List<String>> optHeaderNames = getHeaderNames(sheet, tableArea);
        optHeaderNames.ifPresent(h -> tableArea.startRow++);
        List<String> headerNames = optHeaderNames.orElse(calculateDefaultColumnNames(tableArea));

        Table table = Table.create(options.tableName() + "#" + sheet.getSheetName());
        List<Column<?>> columns = new ArrayList<>(Collections.nCopies(headerNames.size(), null));
        for (int rowNum = tableArea.startRow; rowNum <= tableArea.endRow; rowNum++) {
            Row row = sheet.getRow(rowNum);
            for (int colNum = 0; colNum < headerNames.size(); colNum++) {
                int excelColNum = colNum + tableArea.startColumn;
                Cell cell = row.getCell(excelColNum, MissingCellPolicy.RETURN_BLANK_AS_NULL);
                Column<?> column = columns.get(colNum);
                String columnName = headerNames.get(colNum);

                if (cell != null) {
                    if (column == null) {
                        column = createColumn(colNum, columnName, sheet, excelColNum, tableArea, options);
                        columns.set(colNum, column);
                        while (column.size() < rowNum - tableArea.startRow) {
                            column.appendMissing();
                        }
                    }
                    Column<?> altColumn = appendValue(column, cell);
                    if (altColumn != null && altColumn != column) {
                        column = altColumn;
                        columns.set(colNum, column);
                    }
                } else {
                    boolean hasCustomizedType = options.columnTypeReadOptions().columnType(colNum, columnName).isPresent();
                    if (column == null && hasCustomizedType) {
                        ColumnType columnType = options.columnTypeReadOptions().columnType(colNum, columnName).get();
                        column = columnType.create(columnName).appendMissing();
                        columns.set(colNum, column);
                    } else if (column == null && !hasCustomizedType) {
                        column = ColumnType.STRING.create(columnName).appendMissing();
                        columns.set(colNum, column);
                    } else if (hasCustomizedType) {
                        column.appendMissing();
                    }
                }

                if (column != null) {
                    while (column.size() <= rowNum - tableArea.startRow) {
                        column.appendMissing();
                    }
                }
            }
        }
        columns.removeAll(Collections.singleton(null));
        table.addColumns(columns.toArray(new Column<?>[columns.size()]));
        return table;
    }

    private Optional<List<String>> getHeaderNames(Sheet sheet, TableRange tableArea) {
        // assume header row if all cells are of type String
        Row row = sheet.getRow(tableArea.startRow);
        List<String> headerNames =
                IntStream.range(tableArea.startColumn, tableArea.endColumn)
                        .mapToObj(row::getCell)
                        .filter(cell -> cell?.getCellType() == STRING)
                        .map(cell -> cell.getRichStringCellValue().getString())
                        .collect(Collectors.toList());
        return headerNames.size() == tableArea.getColumnCount()
                ? Optional.of(headerNames)
                : Optional.empty();
    }

    private List<String> calculateDefaultColumnNames(TableRange tableArea) {
        return IntStream.range(tableArea.startColumn, tableArea.endColumn)
                .mapToObj(i -> "Unnamed: " + i)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private Column<?> appendValue(Column<?> column, Cell cell) {
        CellType cellType =
                cell.getCellType() == FORMULA ? cell.getCachedFormulaResultType() : cell.getCellType();
        switch (cellType) {
            case STRING:
                column.appendCell(cell.getRichStringCellValue().getString());
                return null;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    // This will return inconsistent results across time zones, but that matches Excel's
                    // behavior
                    LocalDateTime localDate =
                            date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    if (column.type() == ColumnType.STRING) {
                        // If column has String type try to honor it and leave the value as an string as similar
                        // as posible as seen in Excel
                        String dataFormatStyle = cell.getCellStyle().getDataFormatString();
                        String val;
                        if ("general".equalsIgnoreCase(dataFormatStyle)) {
                            val = new CellGeneralFormatter().format(cell.getNumericCellValue());
                        } else {
                            val = new CellDateFormatter(dataFormatStyle).format(cell.getDateCellValue());
                        }
                        column.appendCell(val);
                    } else {
                        column.appendCell(localDate.toString());
                    }
                    return null;
                } else {
                    double num = cell.getNumericCellValue();
                    if (column.type() == ColumnType.INTEGER) {
                        Column<Integer> intColumn = (Column<Integer>) column;
                        if ((int) num == num) {
                            intColumn.append((int) num);
                            return null;
                        } else if ((long) num == num) {
                            Column<Long> altColumn = LongColumn.create(column.name(), column.size());
                            altColumn = intColumn.mapInto(s -> (long) s, altColumn);
                            altColumn.append((long) num);
                            return altColumn;
                        } else {
                            Column<Double> altColumn = DoubleColumn.create(column.name(), column.size());
                            altColumn = intColumn.mapInto(s -> (double) s, altColumn);
                            altColumn.append(num);
                            return altColumn;
                        }
                    } else if (column.type() == ColumnType.LONG) {
                        Column<Long> longColumn = (Column<Long>) column;
                        if ((long) num == num) {
                            longColumn.append((long) num);
                            return null;
                        } else {
                            Column<Double> altColumn = DoubleColumn.create(column.name(), column.size());
                            altColumn = longColumn.mapInto(s -> (double) s, altColumn);
                            altColumn.append(num);
                            return altColumn;
                        }
                    } else if (column.type() == ColumnType.DOUBLE) {
                        Column<Double> doubleColumn = (Column<Double>) column;
                        doubleColumn.append(num);
                        return null;
                    } else if (column.type() == ColumnType.STRING) {
                        // If column has String type try to honor it and leave the value as an string as similar
                        // as posible as seen in Excel
                        Column<String> stringColumn = (Column<String>) column;
                        String dataFormatStyle = cell.getCellStyle().getDataFormatString();
                        String val;
                        if ("general".equalsIgnoreCase(dataFormatStyle)) {
                            val = new CellGeneralFormatter().format(cell.getNumericCellValue());
                        } else {
                            val = new CellNumberFormatter(dataFormatStyle).format(cell.getNumericCellValue());
                        }
                        stringColumn.append(val);
                    }
                }
                break;
            case BOOLEAN:
                if (column.type() == ColumnType.BOOLEAN) {
                    Column<Boolean> booleanColumn = (Column<Boolean>) column;
                    booleanColumn.append(cell.getBooleanCellValue());
                    return null;
                } else if (column.type() == ColumnType.STRING) {
                    // If column has String type try to honor it and leave the value as an string as similar
                    // as posible as seen in Excel
                    Column<String> stringColumn = (Column<String>) column;
                    String val = new CellGeneralFormatter().format(cell.getBooleanCellValue());
                    stringColumn.append(val);
                }
            default:
                break;
        }
        return null;
    }

    private Column<?> createColumn(
            int colNum,
            String name,
            Sheet sheet,
            int excelColNum,
            TableRange tableRange,
            TSExcelReaderOptions options) {
        Column<?> column;

        ColumnType columnType =
                options
                        .columnTypeReadOptions()
                        .columnType(colNum, name)
                        .orElse(
                                calculateColumnTypeForColumn(sheet, excelColNum, tableRange)
                                        .orElse(ColumnType.STRING));

        column = columnType.create(name);
        return column;
    }

    @Override
    public Table read(Source source) {
        return read(TSExcelReaderOptions.builder(source).build());
    }

    private Optional<ColumnType> calculateColumnTypeForColumn(
            Sheet sheet, int col, TableRange tableRange) {
        Set<CellType> cellTypes = getCellTypes(sheet, col, tableRange);

        if (cellTypes.size() != 1) {
            return Optional.empty();
        }

        CellType cellType = Iterables.get(cellTypes, 0);
        switch (cellType) {
            case STRING:
                return Optional.of(ColumnType.STRING);
            case NUMERIC:
                return allNumericFieldsDateFormatted(sheet, col, tableRange)
                        ? Optional.of(ColumnType.LOCAL_DATE_TIME)
                        : Optional.of(ColumnType.INTEGER);
            case BOOLEAN:
                return Optional.of(ColumnType.BOOLEAN);
            default:
                return Optional.empty();
        }
    }

    private Set<CellType> getCellTypes(Sheet sheet, int col, TableRange tableRange) {
        return IntStream.range(tableRange.startRow, tableRange.endRow + 1)
                .mapToObj(sheet::getRow)
                .filter(Objects::nonNull)
                .map(row -> row.getCell(col))
                .filter(Objects::nonNull)
                .filter(cell -> !Optional.ofNullable(isBlank(cell)).orElse(false))
                .map(
                        cell ->
                                cell.getCellType() == FORMULA
                                        ? cell.getCachedFormulaResultType()
                                        : cell.getCellType())
                .collect(Collectors.toSet());
    }

    private boolean allNumericFieldsDateFormatted(Sheet sheet, int col, TableRange tableRange) {
        return IntStream.range(tableRange.startRow, tableRange.endRow + 1)
                .mapToObj(sheet::getRow)
                .filter(Objects::nonNull)
                .map(row -> row.getCell(col))
                .filter(Objects::nonNull)
                .filter(
                        cell ->
                                cell.getCellType() == NUMERIC
                                        || (cell.getCellType() == FORMULA
                                        && cell.getCachedFormulaResultType() == NUMERIC))
                .allMatch(DateUtil::isCellDateFormatted);
    }
}

