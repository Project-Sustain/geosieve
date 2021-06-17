package sustain.geosieve.create;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DatasourcePointProvider implements Iterable<LatLng> {
    private final String latProperty;
    private final String lngProperty;
    private final List<File> files;

    public DatasourcePointProvider(String latProperty, String lngProperty, String... filenames) {
        this.latProperty = latProperty;
        this.lngProperty = lngProperty;
        files = new LinkedList<>();
        for (String filename : filenames) {
            files.add(new File(filename));
        }
    }

    @Override
    public Iterator<LatLng> iterator() {
        return new Iterator<LatLng>() {
            private int currentFileIndex = 0;

            @Override
            public boolean hasNext() {
                return currentFileIndex != files.size();
            }

            @Override
            public LatLng next() {
                return null;
            }
        };
    }
}
