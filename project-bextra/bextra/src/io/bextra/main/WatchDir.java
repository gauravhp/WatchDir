package io.bextra.main;

import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;

import io.bextra.dboperations.TableOperationsImpl;
import io.bextra.events.EventManager;
import io.bextra.events.FileCreationEventListener;
import io.bextra.events.FileOperations;
import io.bextra.events.FileUpdateEventListener;
import io.bextra.events.TableOperations;
import io.bextra.fileoperations.FileOperationsImpl;

/**
 * Example to watch a directory (or tree) for changes to files.
 */

public class WatchDir {

    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private final boolean recursive;
    private boolean trace = false;
    private EventManager eventManager;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException
            {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Creates a WatchService and registers the given directory
     */
    WatchDir(Path dir, boolean recursive) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
        this.recursive = recursive;

        if (recursive) {
            System.out.format("Scanning %s ...\n", dir);
            registerAll(dir);
            System.out.println("Done.");
        } else {
            register(dir);
        }

        // enable trace after initial registration
        this.trace = true;
        eventManager = new EventManager();
        TableOperations tableOperations = new TableOperationsImpl();
        FileOperations fileOperations = new FileOperationsImpl();
        FileCreationEventListener fileCreationEventListener = new FileCreationEventListener(tableOperations,fileOperations);
        FileUpdateEventListener fileUpdateEventListener = new FileUpdateEventListener(tableOperations,fileOperations);
        eventManager.subscribe(EventManager.EventType.FILE_CREATE, fileCreationEventListener);
        eventManager.subscribe(EventManager.EventType.FILE_UPDATE, fileUpdateEventListener);
    }

    /**
     * Process all events for keys queued to the watcher
     */
    void processEvents() {
        for (;;) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);
                
                //System.out.format("%s: %s\n", event.kind().name(), child);
                
                if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                    //registerAll(child);
                	System.out.format("Directory related event. Do nothing.\n");
                	continue;
                }
                
                if(kind == ENTRY_CREATE) {
                	//System.out.format("\nNew file created:" + child + "\n");
                	eventManager.notifyListeners(EventManager.EventType.FILE_CREATE, child);
                	
                }
                
                if(kind == ENTRY_MODIFY) {
                	// System.out.format("\nFile modified:" + child + "\n");
                	eventManager.notifyListeners(EventManager.EventType.FILE_UPDATE, child);
                }
                // print out event
                

                // if directory is created, and watching recursively, then
                // register it and its sub-directories

            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    static void usage() {
        System.err.println("usage: java WatchDir [-r] dir");
        System.exit(-1);
    }

    public static void main(String[] args) throws IOException {
        // parse arguments
        if (args.length == 0 || args.length > 2)
            usage();
        boolean recursive = false;
        int dirArg = 0;
        if (args[0].equals("-r")) {
            if (args.length < 2)
                usage();
            recursive = true;
            dirArg++;
        }

        // register directory and process its events
        Path dir = Paths.get(args[dirArg]);
        new WatchDir(dir, recursive).processEvents();
    }
}
