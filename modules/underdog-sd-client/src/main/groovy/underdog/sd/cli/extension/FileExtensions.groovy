package underdog.sd.cli.extension

import underdog.sd.cli.Images

class FileExtensions {
    static String fileToBase64(File file) {
        return Images.fileToBase64(file)
    }

    static Tuple2<Integer, Integer> getImageDimensions(File file) {
        return Images.getDimensions(file)
    }
}
