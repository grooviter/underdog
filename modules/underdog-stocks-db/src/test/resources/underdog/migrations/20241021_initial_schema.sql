CREATE TABLE historical_dividend (
    symbol varchar(100) NOT NULL,
    date timestamp NOT NULL,
    adjDividend decimal
);

CREATE TABLE historical_quote (
    symbol varchar(100) NOT NULL,
    date timestamp NOT NULL,
    open decimal,
    low decimal,
    high decimal,
    close decimal,
    adjClose decimal,
    volume bigint
);

CREATE TABLE historical_split (
    symbol varchar(100) NOT NULL,
    date timestamp NOT NULL,
    numerator decimal,
    denominator decimal
);

CREATE TABLE stock (
  symbol TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  stockExchange TEXT NOT NULL
);

CREATE TABLE stock_dividend (
    symbol varchar(100) NOT NULL,
    payDate timestamp,
    exDate timestamp,
    annualYield decimal,
    annualYieldPercent decimal
);

CREATE TABLE stock_quote (
    symbol varchar(100) NOT NULL,
    timeZone varchar(200),
    ask decimal,
    askSize bigint,
    bid decimal,
    bidSize bigint,
    price decimal,
    lastTradeSize bigint,
    lastTradeDateStr varchar(100),
    lastTradeTimeStr varchar(100),
    lastTradeTime timestamp,
    open decimal,
    previousClose decimal,
    dayLow decimal,
    dayHigh decimal,
    yearLow decimal,
    yearHigh decimal,
    priceAvg50 decimal,
    priceAvg200 decimal,
    volume bigint,
    avgVolume bigint
);

CREATE TABLE stock_stats (
    marketCap decimal,
    sharesFloat bigint,
    sharesOutstanding bigint,
    sharesOwned bigint,
    eps decimal,
    pe decimal,
    peg decimal,
    epsEstimateCurrentYear decimal,
    epsEstimateNextQuarter decimal,
    epsEstimateNextYear decimal,
    priceBook decimal,
    priceSales decimal,
    bookValuePerShare decimal,
    revenue decimal,
    ebitda decimal,
    oneYearTargetPrice decimal,
    shortRatio decimal,
    earningsAnnouncement timestamp
);
