package underdog.sd.cli.extension

import underdog.sd.cli.Images

class StringExtensions {

    static File writeToFile(String base64, File destination) {
        return Images.base64ToFile(base64, destination)
    }

    static File base64ToTempFile(String base64) {
        return Images.base64ToTempFile(base64)
    }

    static String base64ToBrowserImage(String base64) {
        return Images.base64ToBrowserImage(base64)
    }
}
