package underdog.ta.extensions

import groovy.util.logging.Slf4j
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseBarSeriesBuilder
import org.ta4j.core.indicators.AbstractIndicator
import org.ta4j.core.num.Num
import underdog.DataFrame
import underdog.DataFrames
import underdog.Series

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Includes functions to convert instances between Underdog and Ta4j
 *
 * @since 0.1.0
 */
@Slf4j
class DataframeExtensions {

    private static final List<String> SERIES_MANDATORY_FIELDS = ['DATE', 'OPEN', 'HIGH', 'LOW', 'CLOSE']
    private static final String SERIES_OPTIONAL_FIELD = 'VOLUME'
    private static final List<String> SERIES_ALL_FIELDS = SERIES_MANDATORY_FIELDS + [SERIES_OPTIONAL_FIELD]
    /**
     * Converts from an Underdog's {@link DataFrame} to a Ta4j {@link BarSeries}. It requires the dataframe
     * having the series:
     *
     * - Date
     * - Open
     * - High
     * - Low
     * - Close
     * - Volume
     *
     * @param dataFrame an instance of {@link DataFrame}
     * @return an instance of {@link BarSeries}
     * @since 0.1.0
     */
    static BarSeries toBarSeries(DataFrame dataFrame, ZoneId zoneId = ZoneId.systemDefault()) {
        DataFrame df = dataFrame.rename(fn: String.&toUpperCase)

        if (!SERIES_MANDATORY_FIELDS.every { it in df.columns}) {
            throw new RuntimeException("missing required series: $SERIES_MANDATORY_FIELDS")
        }

        BarSeries series = new BaseBarSeriesBuilder().withName(df.name).build()

        if (SERIES_OPTIONAL_FIELD !in df.columns*.toUpperCase()){
            List<?> dataFrameAsList = df[SERIES_MANDATORY_FIELDS].toList()
            dataFrameAsList.<List>each{ LocalDate date, Number open, Number high, Number low, Number close ->
                ZonedDateTime zonedDateTime = date.atStartOfDay().atZone(zoneId)
                if ([open, high, low, close].every()) {
                    series.addBar(zonedDateTime ,open, high, low, close)
                } else {
                    log.warn("Missing data at ${zonedDateTime.format("dd/MM/yyyy")}")
                }
            }
        } else {
            List<?> dataFrameAsList = df[SERIES_ALL_FIELDS].toList()
            dataFrameAsList.<List>each{ LocalDate date, Number open, Number high, Number low, Number close, Number volume ->
                ZonedDateTime zonedDateTime = date.atStartOfDay().atZone(zoneId)
                if ([open, high, low, close].every()) {
                    series.addBar(zonedDateTime ,open, high, low, close, volume ?: 0)
                } else {
                    log.warn("Missing data at ${zonedDateTime.format("dd/MM/yyyy")}")
                }
            }
        }

        return series
    }

    /**
     * Converts instances of type {@link AbstractIndicator} numeric to Underdog's {@link Series}
     *
     * @param indicator an instance of {@link AbstractIndicator} of type {@link Num}
     * @return an instance of {@link Series}
     * @since 0.1.0
     */
    static Series toUnderdogSeries(AbstractIndicator<Num> indicator) {
        def list = []
        indicator.barSeries.barCount.times {
            list << indicator.getValue(it).doubleValue()
        }
        return new DataFrames().from([A: list], "")['A']
    }
}
