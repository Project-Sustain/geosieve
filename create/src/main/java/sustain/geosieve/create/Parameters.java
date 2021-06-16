package sustain.geosieve.create;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import javax.xml.crypto.Data;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        defaults = Collections.unmodifiableMap(defaultParameters);

        parser = ArgumentParsers.newFor("Geosieve").build()
                .description("Create and store bloom filters for fast GISJOIN lookup in gridded datasets.");
        parser.addArgument("-s", "--gridSource")
                .setDefault(defaults.get("gridSource")).type(GridSource.class);
        parser.addArgument("-j", "--gisjoinSource")
                .setDefault(defaults.get("gisjoinSource")).type(GisjoinSource.class);
        parser.addArgument("-t", "--dbType")
                .setDefault(defaults.get("dbType")).type(DatabaseDestination.class);
        parser.addArgument("-g", "--gridGranularity")
                .setDefault(defaults.get("gridGranularity")).type(double.class);
        parser.addArgument("-o", "--gridOffset")
                .setDefault(defaults.get("gridOffset")).type(double.class);
        parser.addArgument("-c", "--concurrency")
                .setDefault(defaults.get("concurrency")).type(int.class);
    }

    public static Namespace parse(String[] args) {
        try {
            return parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
