package sustain.geosieve.create;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import sustain.geosieve.create.uniform.Extents;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Parameters {
    private static final ArgumentParser parser;
    public static final Map<String, Object> defaults;

    public enum GridSource {
        UNIFORM,
        FILE
    }

    public enum GisjoinSource {
        SUSTAIN_MONGO
    }

    public enum DatabaseDestination {
        REDIS
    }

    static {
        Map<String, Object> defaultParameters = new HashMap<>();
        defaultParameters.put("gridSource", GridSource.UNIFORM);
        defaultParameters.put("gisjoinSource", GisjoinSource.SUSTAIN_MONGO);
        defaultParameters.put("dbType", DatabaseDestination.REDIS);
        defaultParameters.put("gridGranularity", 0.1);
        defaultParameters.put("gridOffset", 0.0);
        defaultParameters.put("concurrency", 10);
        defaultParameters.put("latProperty", "latitude");
        defaultParameters.put("lngProperty", "longitude");
        defaultParameters.put("gridExtents", Extents.COLORADO.asNESWList());
        defaults = Collections.unmodifiableMap(defaultParameters);

        parser = ArgumentParsers.newFor("Geosieve").build()
                .description("Create and store bloom filters for fast GISJOIN lookup in gridded datasets.")
                .defaultHelp(true);
        parser.addArgument("-s", "--gridSource")
                .help("How to get the points that should be added to the filter - UNIFORM to create a grid, FILE to grab them from a file")
                .setDefault(defaults.get("gridSource"))
                .type(GridSource.class);
        parser.addArgument("-j", "--gisjoinSource")
                .help("How to compute the GISJOIN for each point")
                .setDefault(defaults.get("gisjoinSource"))
                .type(GisjoinSource.class);
        parser.addArgument("-t", "--dbType")
                .help("Where to store the resulting bloom filters")
                .setDefault(defaults.get("dbType"))
                .type(DatabaseDestination.class);
        parser.addArgument("-g", "--gridGranularity")
                .help("With UNIFORM gridSource, how far apart in decimal degrees each point will be")
                .setDefault(defaults.get("gridGranularity"))
                .type(double.class);
        parser.addArgument("-e", "--gridExtents")
                .help("With UNIFORM gridSource, specify the rectangular extents of the grid (default is over Colorado)")
                .setDefault(defaults.get("gridExtents"))
                .nargs(4)
                .type(double.class);
        parser.addArgument("-c", "--concurrency")
                .help("How many threads to start")
                .setDefault(defaults.get("concurrency"))
                .type(int.class);
        parser.addArgument("-a", "--latProperty")
                .help("With FILE gridSource, what the latitude property of each record is")
                .setDefault(defaults.get("latProperty"))
                .type(String.class);
        parser.addArgument("-n", "--lngProperty")
                .help("With FILE gridSource, what the longitude property of each record is")
                .setDefault(defaults.get("lngProperty"))
                .type(String.class);
    }

    public static Namespace parse(String[] args) {
        try {
            return parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void help() {
        parser.printHelp();
    }
}
