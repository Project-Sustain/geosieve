package sustain.geosieve.create;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        logger.info("Starting...");
        Parameters params = null;

        try {
            params = Parameters.parse(List.of(args));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
            usage();
            System.exit(1);
        }

        List<Iterable<LatLng>> points = getPoints(Extents.COLORADO, params);
        BloomFilterSet<String> bfs = new RedisBloomFilterSet();
        GisJoinMapper mapper = new SustainMongoGisJoinMapper();
        List<Thread> threads = new ArrayList<>(params.getInt("concurrency"));

        for (int i = 0; i < params.getInt("concurrency"); i++) {
            threads.add(new Thread(new GridWorker(points.get(i), bfs, mapper)));
        }

        logger.info("Launching {} threads.", params.getInt("concurrency"));
        startAndJoin(threads);
        logger.info("Finished.");
    }

    private static List<Iterable<LatLng>> getPoints(Extents e, Parameters params) {
        int concurrency = params.getInt("concurrency");
        double granularity = params.getDouble("gridGranularity");

        List<Iterable<LatLng>> points = new ArrayList<>();
        List<Extents> partitions = new GridPartitioner(e).getPartitions(concurrency, granularity);
        for (int i = 0; i < params.getInt("concurrency"); i++) {
            points.add(new GriddedPointProvider(granularity, granularity, partitions.get(i)));
        }
        return points;
    }

    private static void usage() {
        System.out.println("use");
    }

    private static void startAndJoin(List<Thread> threads) {
        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException ignored) { }
        }
    }
}
