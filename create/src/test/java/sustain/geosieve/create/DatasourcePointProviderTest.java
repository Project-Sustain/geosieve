package sustain.geosieve.create;

import org.junit.jupiter.api.Test;
import sustain.geosieve.create.datasource.DatasourcePointProvider;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatasourcePointProviderTest {
    @Test
    public void sourceMultipleFiles() {
        Iterable<LatLng> points = new DatasourcePointProvider("y", "x",
                "src/test/resources/smallExample.json",
                "src/test/resources/smallExample2.json",
                "src/test/resources/smallExample3.json");


        List<LatLng> expected = new ArrayList<LatLng>();
        expected.add(new LatLngPoint(3, 2));
        expected.add(new LatLngPoint(7, 3));
        expected.add(new LatLngPoint(1, 2));
        expected.add(new LatLngPoint(7, 6));
        expected.add(new LatLngPoint(1, 1));
        expected.add(new LatLngPoint(2.54, 4.11));
        expected.add(new LatLngPoint(3.4265, 6.72));
        expected.add(new LatLngPoint(4.1, 8.0));
        expected.add(new LatLngPoint(100, 345));
        expected.add(new LatLngPoint(582, 618));
        expected.add(new LatLngPoint(403, 209));

        int i = 0;
        for (LatLng point : points) {
            assertEquals(point, expected.get(i++));
        }

    }
}
