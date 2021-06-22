package sustain.geosieve.create;

import net.sourceforge.argparse4j.inf.Namespace;
import sustain.geosieve.create.datasource.FileJobLauncher;
import sustain.geosieve.create.uniform.GridJobLauncher;

public class Factories {
    public static FilterDatabase getFilters(Namespace params) {
        switch (params.<Parameters.DatabaseDestination>get("dbType")) {
            default: case REDIS: {
                return new RedisFilterDatabase();
            } case FILE: {
                return new FileDatabase(params.get("filterOutputFile"));
            }
        }
    }

    public static GisJoinMapper getMapper(Namespace params) {
        switch (params.<Parameters.GisjoinSource>get("gisjoinSource")) {
            default: case SUSTAIN_MONGO: {
                return new SustainMongoGisJoinMapper();
            }
        }
    }

    public static JobLauncher getLauncher(Namespace params) {
        switch (params.<Parameters.GridSource>get("gridSource")) {
            default: case UNIFORM: {
                return new GridJobLauncher(params);
            } case FILE: {
                return new FileJobLauncher(params);
            }
        }
    }
}
