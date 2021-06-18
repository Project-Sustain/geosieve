package sustain.geosieve.create;

import net.sourceforge.argparse4j.inf.Namespace;

public class Factories {
    public static FilterDatabase getFilters(Namespace params) {
        switch (params.<Parameters.DatabaseDestination>get(params.get("dbType"))) {
            default: case REDIS: {
                return new RedisFilterDatabase();
            }
        }
    }

    public static GisJoinMapper getMapper(Namespace params) {
        switch (params.<Parameters.GisjoinSource>get(params.get("gisjoinSource"))) {
            default: case SUSTAIN_MONGO: {
                return new SustainMongoGisJoinMapper();
            }
        }
    }

    public static JobLauncher getLauncher(Namespace params) {
        switch (params.<Parameters.GridSource>get("gridSource")) {
            default: case UNIFORM: {
                return new GridJobLauncher(params);
            }
        }
    }
}
