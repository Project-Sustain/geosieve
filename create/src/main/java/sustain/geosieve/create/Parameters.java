package sustain.geosieve.create;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parameters {
    private static final Pattern shortPattern = Pattern.compile("-(\\w)=(\\S+)");
    private static final Pattern longPattern = Pattern.compile("--(\\w+)=(\\S+)");

    // Define new parameters here.
    private static final Map<String, FlagMarshaller> flags = new HashMap<>();
    static {
        flags.put("c", new FlagMarshaller(
                Util::coercibleToInt,
                Util::biPredicateNoop,
                Parameters.getApplicator("concurrency"),
                "c",
                "Thread concurrency must be an integer"
        ));
        flags.put("g", new FlagMarshaller(
                Util::coercibleToDouble,
                Util::biPredicateNoop,
                Parameters.getApplicator("gridGranularity"),
                "g",
                "Grid granularity must be a number"
        ));
        flags.put("o", new FlagMarshaller(
                Util::coercibleToDouble,
                Util::biPredicateNoop,
                Parameters.getApplicator("gridOffset"),
                "o",
                "Grid offset must be a number"
        ));
    }

    private final Map<String, String> entries;

    private static class FlagMarshaller {
        final String flagName;
        final String flagInvalidMessage;
        final Predicate<String> isValid;
        final BiPredicate<String, Parameters> postParseTest;
        final BiConsumer<String, Parameters> applyFlag;

        FlagMarshaller(Predicate<String> isValid,
                       BiPredicate<String, Parameters> postParseTest,
                       BiConsumer<String, Parameters> applyFlag,
                       String name,
                       String invalidMessage) {
            this.isValid = isValid;
            this.applyFlag = applyFlag;
            this.postParseTest = postParseTest;
            flagName = name;
            flagInvalidMessage = invalidMessage;
        }

        void process(String value, Parameters params) {
            if (!isValid.test(value)) {
                throw new IllegalArgumentException(String.format(
                        "\"%s\" is invalid for flag \"%s\": %s", value, flagName, flagInvalidMessage));
            } else {
                applyFlag.accept(value, params);
            }
        }
    }


    public static Parameters parse(List<String> args) {
        Parameters defaults = new Parameters();
        for (String arg : args) {
            Matcher argMatcher = shortPattern.matcher(arg);
            if (argMatcher.matches()) {
                processArgument(defaults, argMatcher.group(1), argMatcher.group(2));
            } else {
                throw new IllegalArgumentException(String.format("Improper argument: %s", arg));
            }
        }
        return defaults;
    }

    private static void processArgument(Parameters params, String flag, String value) {
        if (!flags.containsKey(flag)) {
            throw new IllegalArgumentException(String.format("Unrecognized flag \"%s\"", flag));
        } else {
            flags.get(flag).process(value, params);
        }
    }

    private static BiConsumer<String, Parameters> getApplicator(String key) {
        return (String s, Parameters p) -> p.setString(key, s);
    }

    private static Map<String, String> createDefaults() {
        HashMap<String, String> defaults = new HashMap<>();
        defaults.put("concurrency", "10");
        defaults.put("gridGranularity", "0.1");
        defaults.put("gridOffset", "0.0");
        return defaults;
    }

    public Parameters() {
        entries = createDefaults();
    }

    private void set(String key, String value) {
        if (entries.containsKey(key)) {
            entries.replace(key, value);
        } else {
            entries.put(key, value);
        }
    }

    private String get(String key) {
        return entries.getOrDefault(key, "");
    }

    public void setString(String key, String value) {
        set(key, value);
    }

    public void setDouble(String key, double value) {
        set(key, Double.toString(value));
    }

    public void setInt(String key, int value) {
        set(key, Integer.toString(value));
    }

    public String getString(String key) {
        return get(key);
    }

    public double getDouble(String key) {
        return Double.parseDouble(get(key));
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
