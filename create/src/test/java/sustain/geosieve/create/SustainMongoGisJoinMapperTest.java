package sustain.geosieve.create;

import sustain.geosieve.create.GisJoinMapper;
import sustain.geosieve.create.LatLngPoint;
import sustain.geosieve.create.SustainMongoGisJoinMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

public class SustainMongoGisJoinMapperTest {
    private static GisJoinMapper mapper;

    @BeforeAll
    public static void setup() {
        mapper = new SustainMongoGisJoinMapper();
    }

    @Test
    public void basicLookupTest() {
        assertEquals("G0800690001709", mapper.map(new LatLngPoint(-105, 40.4)));
        assertEquals("G0801210924200", mapper.map(new LatLngPoint(-103.43, 40.1)));
    }

    @Test
    public void lookupFailTest() {
        assertEquals("", mapper.map(new LatLngPoint(0, 0)));
    }
}
