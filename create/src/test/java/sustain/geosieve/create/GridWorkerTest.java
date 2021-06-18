package sustain.geosieve.create;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sustain.geosieve.create.uniform.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DummyFilters implements FilterDatabase {
    private final HashMap<String, Set<String>> filters;
    public DummyFilters() {
        filters = new HashMap<>();
    }

    @Override
    public synchronized void add(String filterName, String e) {
        if (!filters.containsKey(filterName)) {
            filters.put(filterName, new HashSet<>());
        }
        filters.get(filterName).add(e);
    }

    @Override
    public synchronized boolean contains(String filterName, String e) {
        if (!filters.containsKey(filterName)) {
            return false;
        }
        return filters.get(filterName).contains(e);
    }

    @Override
    public void clear(String filterName, String e) {
        filters.get(filterName).remove(e);
    }

    public void clearAll() {
        filters.clear();
    }
}

public class GridWorkerTest {
    private static DummyFilters filters;
    private static GriddedExtents extents;
    private static GridPartitioner partitioner;
    private static GisJoinMapper mapper;

    @BeforeAll
    public static void setup() {
        filters = new DummyFilters();
        extents = new GriddedExtents(new Extents(0, 0, 1, 1), 0.1 );
        partitioner = new GridPartitioner(extents);
        mapper = (LatLng p) -> String.format("%.2f", p.lat());
    }

    @AfterEach
    public void cleanup() {
        filters.clearAll();
    }

    @Test
    public void singleThread() {
        Iterable<LatLng> points = new UniformPointProvider(extents);
        Thread t = new Thread(new GridWorker(points, filters, mapper));
        t.start();
        try {
            t.join();
        } catch (InterruptedException ignored) { }

        assertTrue(filters.contains("0.00", "0.00,0.00"));
        assertTrue(filters.contains("0.10", "0.00,0.10"));
        assertTrue(filters.contains("0.20", "0.00,0.20"));
        assertTrue(filters.contains("0.30", "0.10,0.30"));
        assertTrue(filters.contains("0.40", "0.30,0.40"));
        assertTrue(filters.contains("0.50", "0.80,0.50"));
        assertTrue(filters.contains("0.60", "0.10,0.60"));
        assertTrue(filters.contains("0.60", "0.20,0.60"));
        assertTrue(filters.contains("0.60", "0.30,0.60"));
        assertTrue(filters.contains("0.90", "0.90,0.90"));
        assertFalse(filters.contains("1.00", "0.90,1.00"));
        assertFalse(filters.contains("0.90", "1.00,0.90"));
        assertFalse(filters.contains("0.00", "0.91,0.00"));
    }

    @Test
    public void manyThreads() {
        int numThreads = 10;
        List<Thread> threads = new ArrayList<>();

        List<Extents> parts = partitioner.getPartitions(10, 0.1);

        for (int i = 0; i < numThreads; i++) {
            Iterable<LatLng> points = new UniformPointProvider(0.1, 0.1, parts.get(i));
            threads.add(new Thread(new GridWorker(points, filters, mapper)));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException ignored) { }
        }

        assertTrue(filters.contains("0.00", "0.00,0.00"));
        assertTrue(filters.contains("0.10", "0.00,0.10"));
        assertTrue(filters.contains("0.20", "0.00,0.20"));
        assertTrue(filters.contains("0.30", "0.10,0.30"));
        assertTrue(filters.contains("0.40", "0.30,0.40"));
        assertTrue(filters.contains("0.50", "0.80,0.50"));
        assertTrue(filters.contains("0.60", "0.10,0.60"));
        assertTrue(filters.contains("0.60", "0.20,0.60"));
        assertTrue(filters.contains("0.60", "0.30,0.60"));
        assertTrue(filters.contains("0.90", "0.90,0.90"));
        assertFalse(filters.contains("1.00", "0.90,1.00"));
        assertFalse(filters.contains("0.90", "1.00,0.90"));
        assertFalse(filters.contains("0.00", "0.91,0.00"));
    }
}
