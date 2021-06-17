package sustain.geosieve.create;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class JsonDatasource extends Datasource {
    private final JsonParser parser;

    public JsonDatasource(String latProperty, String lngProperty, File file) {
        super(latProperty, lngProperty, file);
        try {
            parser = new JsonFactory().createParser(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<LatLng> iterator() {
        return new Iterator<LatLng>() {
            @Override
            public boolean hasNext() {
                try {
                    return parser.nextToken() != null;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public LatLng next() {
                return null;
            }
        };
    }
}
