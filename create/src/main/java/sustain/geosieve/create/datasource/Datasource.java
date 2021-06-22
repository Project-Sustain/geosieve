package sustain.geosieve.create.datasource;

import sustain.geosieve.create.LatLng;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Datasource implements Iterable<LatLng> {
    public final String latProperty;
    public final String lngProperty;
    public final File file;

    public Datasource(String latProperty, String lngProperty, File file) {
        this.latProperty = latProperty;
        this.lngProperty = lngProperty;
        this.file = file;
    }

    public enum FileType {
        JSON,
        CSV
    }

    private static final Pattern extensionPattern = Pattern.compile("^.*\\.(\\w+)$");

    public static Datasource fromFile(String latProperty, String lngProperty, File file) {
        switch (getType(file)) {
            case JSON: {
                return new JsonDatasource(latProperty, lngProperty, file);
            } default: {
                throw new IllegalArgumentException(String.format("File type of %s is not supported", file.getName()));
            }
        }
    }

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