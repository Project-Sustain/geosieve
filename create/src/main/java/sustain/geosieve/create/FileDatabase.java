package sustain.geosieve.create;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

// This class is mostly intended for testing and prototyping purposes -
// it is not necessarily very efficient.
public class FileDatabase implements FilterDatabase {
    private final String pathname;
    private final BufferedWriter fileWriter;

    public FileDatabase(String pathname) {
        this.pathname = pathname;
        try {
            fileWriter = Files.newBufferedWriter(Paths.get(pathname));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(String filterName, String e) {
        try {
            fileWriter.write(String.format("%s : %s", filterName, e));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean contains(String filterName, String e) {
        // gross
        try {
            Scanner reader = new Scanner(new File(pathname));
            String line;
            while ((line = nextWithPrefix(filterName, reader)) != null) {
                if (line.contains(e)) {
                    return true;
                }
            }
            reader.close();
            return false;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String nextWithPrefix(String prefix, Scanner scanner) {
        while (scanner.hasNext()) {
            if (scanner.next().startsWith(prefix)) {
                return prefix;
            }
        }
        return null; // you be careful...
    }

    @Override
    public void clear(String filterName, String e) {

    }
}
