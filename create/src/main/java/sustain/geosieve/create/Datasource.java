package sustain.geosieve.create;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Datasource implements Iterable<LatLng> {
    public final String latProperty;
    public final String lngProperty;
    public final File file;
    public final FileType type;

    public Datasource(String latProperty, String lngProperty, File file) {
        this.latProperty = latProperty;
        this.lngProperty = lngProperty;
        this.file = file;
        this.type = getType(file);
    }

    public enum FileType {
        JSON,
        CSV
    }

    private static final Pattern extensionPattern = Pattern.compile("^.*\\.(\\w+)$");

    public static FileType getType(File file) {
        String basename = file.getName();
        Matcher extMatcher = extensionPattern.matcher(basename);
        extMatcher.matches();
        String extension = extMatcher.group(1).toLowerCase();
        switch (extension) {
            case "json": {
                return FileType.JSON;
            }
            case "csv": {
                return FileType.CSV;
            }
            default: {
                throw new IllegalArgumentException(String.format("Unsupported file type %s", extension));
            }
        }
    }
}
