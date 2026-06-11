package underdog.sd.cli.extension

import underdog.sd.cli.Images
import underdog.sd.cli.openai.request.Image

class FileExtensions {
    static String fileToBase64(File file) {
        return Images.fileToBase64(file)
    }

    static Image toImage(File file) {
        return Image.builder().imageURL(fileToBase64(file)).build()
    }

    static Tuple2<Integer, Integer> getImageDimensions(File file) {
        return Images.getDimensions(file)
    }
}
