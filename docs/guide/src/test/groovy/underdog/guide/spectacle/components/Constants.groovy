package underdog.guide.spectacle.components

import underdog.DataFrame
import underdog.Underdog

class Constants {
    static final String CSS_3_COLS_RESPONSIVE = "col-md-12 col-lg-4 col-sm-12 "

    static DataFrame loadDataFrame() {
        return Underdog.df()
            .read_csv(Constants.class.classLoader.getResource("data/baseball.csv").file)
            .head(5)
    }
}
