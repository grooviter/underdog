package underdog.ml.extensions

import com.github.grooviter.underdog.Underdog
import underdog.ml.ML

class UnderdogExtensions {

    static ML ml(Underdog underdog) {
        return new ML()
    }
}
