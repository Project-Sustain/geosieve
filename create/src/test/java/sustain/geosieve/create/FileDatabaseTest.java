package sustain.geosieve.create;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FileDatabaseTest {
    @AfterAll
    public static void cleanupFile() throws IOException {
        Files.delete(Paths.get("./test.txt"));
    }

    @Test
    public void simpleWrites() {
        FilterDatabase db = new FileDatabase("./test.txt");
        db.add("hello", "world");
        db.add("second", "best");

        assertTrue(db.contains("hello", "world"));
        assertTrue(db.contains("second", "best"));
        assertFalse(db.contains("hello", "moto"));
    }
}
