package sustain.geosieve.create;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class SustainMongoGisJoinMapperTest {
    private static GisJoinMapper mapper;

    @BeforeAll
    public static void setup() {
        assumeTrue(shouldDoTest());
        mapper = new SustainMongoGisJoinMapper();
    }

    public static boolean shouldDoTest() {
        return System.getenv("GEOSIEVE_SUSTAIN_MONGO_AVAILABLE") != null;
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
