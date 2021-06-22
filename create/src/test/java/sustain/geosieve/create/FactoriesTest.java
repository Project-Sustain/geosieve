package sustain.geosieve.create;

import net.sourceforge.argparse4j.inf.Namespace;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FactoriesTest {
    private static Map<String, Object> params;

    @BeforeAll
    public static void setupParams() {
        params = new HashMap<>();
    }

    @Test
    public void getFilterDatabases() {
        params.put("dbType", Parameters.DatabaseDestination.REDIS);
        FilterDatabase db = Factories.getFilters(new Namespace(params));
        assertTrue(db instanceof RedisFilterDatabase);

        params.put("dbType", Parameters.DatabaseDestination.FILE);
        params.put("filterOutputFile", "./test.txt");
        db = Factories.getFilters(new Namespace(params));
        assertTrue(db instanceof FileDatabase);
    }
}
