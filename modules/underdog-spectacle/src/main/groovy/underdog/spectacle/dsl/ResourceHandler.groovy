package underdog.spectacle.dsl

import java.nio.file.Paths

/**
 * A resource handler is a handler exposing certain static resources. In the
 * backend a user can get the reference of a resource handler and store
 * or retrieve resources handled by it.
 *
 * @since 0.1.0
 */
class ResourceHandler {
    /**
     * Resource handler identifying name
     *
     * @since 0.1.0
     */
    String name

    /**
     * Where in the filesystem will the handler store/retrieve resources
     *
     * @since 0.1.0
     */
    String dir

    /**
     * Path where resources will be exposed via HTTP
     *
     * @since 0.1.0
     */
    String path

    /**
     * Whether this handler allows listing directory via HTTP (default -> false)
     *
     * @since 0.1.0
     */
    Boolean allowListing = false

    /**
     * Saving a file with a specific name
     *
     * @param filename name of the file
     * @param bytes content of the file as bytes
     * @return the path of the file inside the resource handler space
     * @since 0.1.0
     */
    String save(String fileName, byte[] bytes) {
        File file = new File(destinationDir, fileName)
        file << bytes
        return Paths.get(path, file.name)
    }

    /**
     * Saving a file with no specific name
     *
     * @param bytes content of the file as bytes
     * @return the path of the file inside the resource handler space
     * @since 0.1.0
     */
    String save(byte[] bytes){
        return save(bytes.md5(), bytes)
    }

    private File getDestinationDir() {
        File dirFile = new File(this.dir)
        if (!dirFile.exists()) {
            dirFile.mkdirs()
        }
        return dirFile
    }
}