package underdog.ml.extensions

import underdog.Underdog
import underdog.ml.ML

class UnderdogExtensions {

    static ML ml(Underdog underdog) {
        return new ML()
    }
}
