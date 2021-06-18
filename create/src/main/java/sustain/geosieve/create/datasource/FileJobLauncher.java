package sustain.geosieve.create.datasource;

import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sustain.geosieve.create.JobLauncher;
import sustain.geosieve.create.LatLng;
import sustain.geosieve.create.Main;
import sustain.geosieve.create.Util;
import sustain.geosieve.create.uniform.GridWorker;

import java.util.List;

public class FileJobLauncher extends JobLauncher {
    private final static Logger logger = LoggerFactory.getLogger(FileJobLauncher.class);

    public FileJobLauncher(Namespace params) {
        super(params);
    }

    @Override
    public void launch() {
        Iterable<LatLng> pointSource = new DatasourcePointProvider(
                params.getString("latProperty"),
                params.getString("lngProperty"),
                params.<List<String>>get("files").toArray(new String[]{}));

        logger.info("Launching {} threads.", params.getInt("concurrency"));
        Util.doThreads(() -> new GridWorker(pointSource, filters, mapper), params.getInt("concurrency"));
        logger.info("Finished.");
    }
}
