package sustain.geosieve.create.uniform;

import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sustain.geosieve.create.*;

import java.util.ArrayList;
import java.util.List;

public class GridJobLauncher extends JobLauncher {
    private final static Logger logger = LoggerFactory.getLogger(GridJobLauncher.class);

    private final Extents extents;

    public GridJobLauncher(Namespace params) {
        super(params);
        List<Double> limits = params.get("gridExtents");
        extents = new Extents(limits.get(0), limits.get(1), limits.get(2), limits.get(3));
    }

    public void launch() {
        List<Iterable<LatLng>> points = getPoints(extents, params);

        logger.info("Launching {} threads.", params.getInt("concurrency"));
        Util.doThreads((Iterable<LatLng> part) -> new GridWorker(part, filters, mapper), points);
        logger.info("Finished.");
    }

    private List<Iterable<LatLng>> getPoints(Extents e, Namespace params) {
        int concurrency = params.getInt("concurrency");
        double granularity = params.getDouble("gridGranularity");

        List<Iterable<LatLng>> points = new ArrayList<>();
        List<Extents> partitions = new GridPartitioner(e).getPartitions(concurrency, granularity);
        for (int i = 0; i < params.getInt("concurrency"); i++) {
            points.add(new UniformPointProvider(granularity, granularity, partitions.get(i)));
        }
        return points;
    }

    private void startAndJoin(List<Thread> threads) {
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