package sustain.geosieve.create;

import org.junit.jupiter.api.Test;
import sustain.geosieve.create.datasource.JsonDatasource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonDatasourceTest {
    @Test
    public void readSmallExample() throws IOException {
        JsonDatasource ds = new JsonDatasource("y", "x", new File("src/test/resources/smallExample.json"));
        List<LatLng> expected = new ArrayList<>();
        expected.add(new LatLngPoint(3, 2));
        expected.add(new LatLngPoint(7, 3));
        expected.add(new LatLngPoint(1, 2));
        expected.add(new LatLngPoint(7, 6));

        int i = 0;
        for (LatLng point : ds) {
            assertEquals(point, expected.get(i++));
        }
    }

    @Test
    public void skipQuietlyOverMissingValues() throws IOException {
        JsonDatasource ds = new JsonDatasource("lat", "lng", new File("src/test/resources/missingExample.json"));
        List<LatLng> expected = new ArrayList<>();
        expected.add(new LatLngPoint(22.8, 22.7));
        expected.add(new LatLngPoint(23.0, 22.9));
        expected.add(new LatLngPoint(23.4, 23.3));

        int i = 0;
        for (LatLng point : ds) {
            assertEquals(point, expected.get(i++));
        }
    }
}
