package underdog.spectacle.jetty


import groovy.transform.builder.Builder
import groovy.util.logging.Slf4j

import java.nio.file.FileSystems
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.nio.file.WatchService
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.Executors

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY

/**
 * This class watches to current development environment and triggers functions
 *
 * - onWatch: just before watching files
 * - onChange: whenever a dev file has been changed, created
 *
 * @since 0.1.0
 */
@Slf4j
@Builder
class DevWatcher {
    File filePath
    Closure onWatchFunction, onChangeFunction

    /**
     * Builds a new {@link DevWatcher}
     *
     * @param dir directory to watch
     * @param onWatch function triggered just before starting to watch
     * @Param onChange function triggered whenever a dev file has changed, or created
     * @return a new instance of {@link DevWatcher}
     * @since 0.1.0
     */
    @Builder(
        builderClassName = 'WatcherBuilder',
        builderMethodName = 'watcherBuilder',
        buildMethodName = 'build')
    static DevWatcher build(
        File dir,
        Closure onWatch,
        Closure onChange
    ){
        return new DevWatcher(filePath: dir, onWatchFunction: onWatch, onChangeFunction: onChange)
    }

    /**
     * Starts watching the file system in one thread while executes the onWatch function in
     * the current thread
     *
     * @since 0.1.0
     */
    void launch() {
        Executors.newSingleThreadExecutor().execute {
            log.debug("launching watcher")
            this.watch()
        }
        this.onWatchFunction()
    }

    private void watch() {
        WatchService watchService = FileSystems.getDefault().newWatchService()
        Path watched = filePath.toPath()
        registerRecursive(watched, watchService)

        WatchKey key
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                log.debug("Event kind: ${event.kind()}. File affected: ${event.context()}.");
            }
            key.reset();
            log.debug("triggering onChange")
            this.onChangeFunction()
        }
    }

    private static void registerRecursive(final Path root, final WatchService watchService) throws IOException {
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY)
                return FileVisitResult.CONTINUE;
            }
        })
    }
}
