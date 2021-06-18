package sustain.geosieve.create;

import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sustain.geosieve.create.uniform.Extents;
import sustain.geosieve.create.uniform.GridPartitioner;
import sustain.geosieve.create.uniform.GridWorker;
import sustain.geosieve.create.uniform.UniformPointProvider;

import java.util.ArrayList;
import java.util.List;

public class GridJobLauncher implements JobLauncher {
    private final static Logger logger = LoggerFactory.getLogger(GridJobLauncher.class);

    private final Namespace params;
    private final Extents extents;
    private final FilterDatabase filters;
    private final GisJoinMapper mapper;

    public GridJobLauncher(Namespace params) {
        this.params = params;
        List<Double> limits = params.get("gridExtents");
        extents = new Extents(limits.get(0), limits.get(1), limits.get(2), limits.get(3));
        filters = Factories.getFilters(params);
        mapper = Factories.getMapper(params);
    }

    public void launch() {
        List<Iterable<LatLng>> points = getPoints(extents, params);
        List<Thread> threads = new ArrayList<>(params.getInt("concurrency"));

        for (int i = 0; i < params.getInt("concurrency"); i++) {
            threads.add(new Thread(new GridWorker(points.get(i), filters, mapper)));
        }

        logger.info("Launching {} threads.", params.getInt("concurrency"));
        startAndJoin(threads);
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
