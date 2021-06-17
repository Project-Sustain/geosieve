package sustain.geosieve.create;

import java.io.File;
import java.util.*;

public class DatasourcePointProvider implements Iterable<LatLng> {
    private final String latProperty;
    private final String lngProperty;
    private final List<Datasource> files;

    public DatasourcePointProvider(String latProperty, String lngProperty, String... filenames) {
        this.latProperty = latProperty;
        this.lngProperty = lngProperty;
        files = new LinkedList<>();
        for (String filename : filenames) {
            files.add(Datasource.fromFile(latProperty, lngProperty, new File(filename)));
        }
    }

    @Override
    public Iterator<LatLng> iterator() {
        if (files.isEmpty()) {
            return Collections.emptyIterator();
        }

        return new Iterator<LatLng>() {
            private int currentSourceIndex = 0;
            private Iterator<LatLng> currentIterator = files.get(currentSourceIndex).iterator();

            @Override
            public boolean hasNext() {
                return currentSourceIndex < files.size() && currentIterator.hasNext();
            }

            @Override
            public LatLng next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                if (!currentIterator.hasNext()) {
                    currentSourceIndex++;
                    if (currentSourceIndex >= files.size()) {
                        throw new NoSuchElementException();
                    }
                    currentIterator = files.get(currentSourceIndex).iterator();
                }
                return currentIterator.next();
            }
        };
    }
}
