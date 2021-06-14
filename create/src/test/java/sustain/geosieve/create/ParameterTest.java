package sustain.geosieve.create;

import sustain.geosieve.create.Parameters;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ParameterTest {
    @Test
    public void simpleShortParameters() {
        Parameters p = Parameters.parse(List.of("-c=3", "-g=0.2", "-o=0.1"));
        assertEquals(3, p.getInt("concurrency"));
        assertEquals(0.2, p.getDouble("gridGranularity"));
        assertEquals(0.1, p.getDouble("gridOffset"));
    }

    @Test
    public void omittedParametersAreDefault() {
        Parameters p = Parameters.parse(List.of("-c=3"));
        assertEquals(0.1, p.getDouble("gridGranularity"));
        assertEquals(0.0, p.getDouble("gridOffset"));
    }

    @Test
    public void invalidParametersFail() {
        assertThrows(IllegalArgumentException.class, () -> Parameters.parse(List.of("-c=3", "-g=0.9", "-k=12")));
        assertThrows(IllegalArgumentException.class, () -> Parameters.parse(List.of("-c=notanumber")));
        assertThrows(IllegalArgumentException.class, () -> Parameters.parse(List.of("-c=0.4")));
    }
}
