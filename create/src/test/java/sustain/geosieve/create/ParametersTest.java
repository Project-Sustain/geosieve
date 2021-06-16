package sustain.geosieve.create;

import net.sourceforge.argparse4j.inf.Namespace;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParametersTest {

    @Test
    public void argumentsDefaultIfNotProvided() {
        Namespace params = Parameters.parse(new String[] {});
        assertEquals(Parameters.defaults.get("gridSource"), params.get("gridSource"));
        assertEquals(Parameters.defaults.get("gisjoinSource"), params.get("gisjoinSource"));
        assertEquals(Parameters.defaults.get("dbType"), params.get("dbType"));
        assertEquals(Parameters.defaults.get("gridGranularity"), params.get("gridGranularity"));
        assertEquals(Parameters.defaults.get("concurrency"), params.get("concurrency"));
    }

    @Test
    public void invalidArgumentsFail() {
        assertThrows(IllegalArgumentException.class, () -> Parameters.parse(new String[] {"-c", "0.5"}));
        assertThrows(IllegalArgumentException.class, () -> Parameters.parse(new String[] {"-t", "1"}));
        assertThrows(IllegalArgumentException.class, () -> Parameters.parse(new String[] {"-t", "NotADBType"}));
        assertThrows(IllegalArgumentException.class, () -> Parameters.parse(new String[] {"-s", "REDIS"}));
        assertThrows(IllegalArgumentException.class, () -> Parameters.parse(new String[] {"-s", "5.0"}));
        assertThrows(IllegalArgumentException.class, () -> Parameters.parse(new String[] {"-j", "5.0"}));
        assertThrows(IllegalArgumentException.class, () -> Parameters.parse(new String[] {"-j"}));
        assertThrows(IllegalArgumentException.class, () -> Parameters.parse(new String[] {"--notaflag"}));
    }

    @Test
    public void argumentsSetProperly() {
        Namespace params = Parameters.parse(new String[] {"-s", "FILE", "-g", "0.5", "-c", "80", "-o", "0.7"});
        assertEquals(Parameters.GridSource.FILE, params.get("gridSource"));
        assertEquals(0.5, params.getDouble("gridGranularity"));
        assertEquals(80, params.getInt("concurrency"));
        assertEquals(0.7, params.getDouble("gridOffset"));
    }
}
