package underdog.impl.extensions

import underdog.DataFrames
import underdog.Underdog

class UnderdogExtensions {
    static DataFrames df(Underdog underdog) {
        return new DataFrames()
    }
}
