package sustain.geosieve.create;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

public class JsonExtractorTest {
    private static JsonFactory factory;

    @BeforeAll
    public static void setup() {
        factory = new JsonFactory();
    }

    @Test
    public void failImmediatelyOnEmptyJson() throws IOException {
        String json = "";
        JsonExtractor e = new JsonExtractor(factory.createParser(json));
        assertThrows(NoSuchElementException.class, () -> e.getFromNextObject("latitude"));
    }

    @Test
    public void readSingleObject() throws IOException {
        String json = "{ \"a\": 3.0, \"b\": \"beta\", \"c\": false }";
        JsonExtractor e = new JsonExtractor(factory.createParser(json));

        Map<String, Object> read = e.getFromNextObject("b", "c");
        assertEquals("beta", read.get("b"));
        assertEquals(false, read.get("c"));

        assertThrows(NoSuchElementException.class, () -> e.getFromNextObject("b", "c"));
    }

    @Test
    public void readNewlineSeparatedObjects() throws IOException {
        String json = "{ \"a\": 3.0, \"b\": \"sillh\" }\n";
              json += "{ \"a\": 7.0, \"b\": \"loxxe\" }\n";
              json += "{ \"a\": 1.5, \"b\": \"lotus\" }\n";

        JsonExtractor e = new JsonExtractor(factory.createParser(json));

        Map<String, Object> read = e.getFromNextObject("a", "b");
        assertEquals(3.0, read.get("a"));
        assertEquals("sillh", read.get("b"));

        read = e.getFromNextObject("a", "b");
        assertEquals(7.0, read.get("a"));
        assertEquals("loxxe", read.get("b"));

        read = e.getFromNextObject("a", "b");
        assertEquals(1.5, read.get("a"));
        assertEquals("lotus", read.get("b"));

        assertThrows(NoSuchElementException.class, () -> e.getFromNextObject("a", "b"));
    }

    @Test
    public void readArraySeparatedObjects() throws IOException {
        String json = "[{ \"a\": 3.0, \"b\": \"sillh\" },";
        json += "{ \"a\": 7.0, \"b\": \"loxxe\" },";
        json += "{ \"a\": 1.5, \"b\": \"lotus\" }]";

        JsonExtractor e = new JsonExtractor(factory.createParser(json));

        Map<String, Object> read = e.getFromNextObject("a", "b");
        assertEquals(3.0, read.get("a"));
        assertEquals("sillh", read.get("b"));

        read = e.getFromNextObject("a", "b");
        assertEquals(7.0, read.get("a"));
        assertEquals("loxxe", read.get("b"));

        read = e.getFromNextObject("a", "b");
        assertEquals(1.5, read.get("a"));
        assertEquals("lotus", read.get("b"));

        assertThrows(NoSuchElementException.class, () -> e.getFromNextObject("a", "b"));
    }
}
