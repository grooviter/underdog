package underdog.sd.cli

import javax.imageio.ImageIO
import java.nio.file.Files

class Images {

    static String fileToBase64(File file) {
        return file.bytes.encodeBase64()
    }

    static File base64ToFile(String base64, File destination) {
        destination << base64.decodeBase64()
        return destination
    }

    static String inputStreamToBase64(InputStream inputStream) {
        return inputStream.bytes.encodeBase64().toString()
    }

    static File base64ToTempFile(String base64) {
        File temporalFile = Files.createTempFile("underdog-sd-", ".png").toFile()
        temporalFile << base64.decodeBase64()
        return temporalFile
    }

    static Tuple2<Integer, Integer> getDimensions(File file) {
        def input = ImageIO.createImageInputStream(file)
        try {
            def reader = ImageIO.getImageReaders(input).next()
            try {
                reader.input = input
                return [reader.getWidth(0), reader.getHeight(0)]
            } finally {
                reader.dispose()
            }
        } finally {
            input.close()
        }
    }

    static String base64ToBrowserImage(String base64) {
        return "data:image/png;base64,$base64"
    }
}
