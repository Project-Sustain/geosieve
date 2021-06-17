package sustain.geosieve.create;

import com.fasterxml.jackson.core.JsonFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

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
                Map<String, Object> coordinates = extractor.getFromNextObject(lngProperty, latProperty);
                return attemptDoubleExtraction(coordinates);
            }

            private LatLng attemptDoubleExtraction(Map<String, Object> vals) {
                try {
                    double lng = (double) vals.get(lngProperty);
                    double lat = (double) vals.get(latProperty);
                    return new LatLngPoint(lng, lat);
                } catch (ClassCastException e) {
                    return attemptIntExtraction(vals);
                }
            }

            private LatLng attemptIntExtraction(Map<String, Object> vals) {
                try {
                    double lng = (int) vals.get(lngProperty);
                    double lat = (int) vals.get(latProperty);
                    return new LatLngPoint(lng, lat);
                } catch (ClassCastException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
