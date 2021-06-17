package sustain.geosieve.create;

import com.fasterxml.jackson.core.JsonFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class JsonDatasource extends Datasource {
    private final JsonExtractor extractor;

    public JsonDatasource(String latProperty, String lngProperty, File file) {
        super(latProperty, lngProperty, file);
        JsonFactory factory = new JsonFactory();

        try {
            extractor = new JsonExtractor(factory.createParser(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<LatLng> iterator() {
        return new Iterator<LatLng>() {
            @Override
            public boolean hasNext() {
                return extractor.moreObjectsExist();
            }

            @Override
            public LatLng next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                LatLng point = null;
                while (point == null) {
                    Map<String, Object> coords = getNextCoordinates();
                    point = toLatLng(coords);
                }

                return point;
            }

            private Map<String, Object> getNextCoordinates() {
                try {
                    Map<String, Object> coordinates = extractor.getFromNextObject(lngProperty, latProperty);
                    while (coordinates.get(lngProperty) == null || coordinates.get(latProperty) == null) {
                        coordinates = extractor.getFromNextObject(lngProperty, latProperty);
                    }
                    return coordinates;
                } catch (NoSuchElementException e) {
                    Map<String, Object> dummyCoords = new HashMap<>();
                    dummyCoords.put(latProperty, Double.NaN);
                    dummyCoords.put(lngProperty, Double.NaN);
                    return dummyCoords;
                }
            }

            private LatLng toLatLng(Map<String, Object> vals) {
                try {
                    return attemptDoubleExtraction(vals);
                } catch (ClassCastException ignored) { }

                try {
                    return attemptIntExtraction(vals);
                } catch (ClassCastException e) {
                    return null;
                }
            }

            private LatLng attemptDoubleExtraction(Map<String, Object> vals) {
                double lng = (double) vals.get(lngProperty);
                double lat = (double) vals.get(latProperty);
                return new LatLngPoint(lng, lat);
            }

            private LatLng attemptIntExtraction(Map<String, Object> vals) {
                double lng = (int) vals.get(lngProperty);
                double lat = (int) vals.get(latProperty);
                return new LatLngPoint(lng, lat);
            }
        };
    }
}
