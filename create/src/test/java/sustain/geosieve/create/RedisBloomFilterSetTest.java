package sustain.geosieve.create;

import sustain.geosieve.create.BloomFilterSet;
import sustain.geosieve.create.RedisBloomFilterSet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RedisBloomFilterSetTest {
    static BloomFilterSet<String> bfs;
    static Map<String, String> addedItems;

    @BeforeAll
    public static void setup() {
        bfs = new RedisBloomFilterSet();
        addedItems = new HashMap<>();
    }

    @Test
    public void simpleOperations() {
        List<String> testItems = List.of(
                "loxxe", "metis", "aemil", "sillh", "maezs"
        );
        String testFilter = "test";

        for (String item : testItems) {
            bfs.add(testFilter, item);
            addedItems.put(testFilter, item);
        }

        for (String item : testItems) {
            assertTrue(bfs.contains(testFilter, item));
        }
    }

    @AfterAll
    public static void clean() {
        for (Map.Entry<String, String> e : addedItems.entrySet()) {
            bfs.clear(e.getKey(), e.getValue());
        }
    }
}
