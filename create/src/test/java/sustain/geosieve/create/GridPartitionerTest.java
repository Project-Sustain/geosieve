package sustain.geosieve.create;

import sustain.geosieve.create.Extents;
import sustain.geosieve.create.GridPartitioner;
import sustain.geosieve.create.LatLngPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GridPartitionerTest {
    private static Extents e;
    private static GridPartitioner parts;

    @BeforeAll
    public static void setup() {
        e = new Extents(1, 1, 0, 0);
        parts = new GridPartitioner(e);
    }

    @Test
    public void trivialPartitions() {
        assertEquals(0, parts.getPartitions(0, 0.1).size());
        assertEquals(1, parts.getPartitions(1, 0.1).size());
        assertEquals(e, parts.getPartitions(1, 0.1).get(0));
    }

    @Test
    public void partitionAmounts() {
        assertEquals(2, parts.getPartitions(2, 0.1).size());
        assertEquals(3, parts.getPartitions(3, 0.1).size());
        assertEquals(5, parts.getPartitions(5, 0.1).size());
        assertEquals(8, parts.getPartitions(8, 0.1).size());
    }

    @Test
    public void partitionsRoughlyEqualSize() {
        int amount = 8;
        List<Extents> octs = parts.getPartitions(amount, 0.1);
        octs.sort(Extents::compareBySize);
        assertEquals(octs.get(0).size(), octs.get(octs.size() - 1).size(), 0.2);
    }

    @Test
    public void partitionsDontOverlap() {
        int amount = 8;
        List<Extents> octs = parts.getPartitions(amount, 0.1);
        for (Extents e1 : octs) {
            for (Extents e2 : octs) {
                if (e1 != e2) {
                    boolean intersects = e2.isIn(new LatLngPoint(e1.startlng, e1.startlat)) ||
                            e2.isIn(new LatLngPoint(e1.startlng, e1.endlat)) ||
                            e2.isIn(new LatLngPoint(e1.endlng, e1.startlat)) ||
                            e2.isIn(new LatLngPoint(e1.endlng, e1.endlat));
                    if (intersects) {
                        fail();
                    }
                }
            }
        }
    }

    @Test
    public void partitionsDontChangeSize() {
        int amount = 16;
        List<Extents> octs = parts.getPartitions(amount, 0.1);
        double total = 0.0;
        for (Extents e : octs) {
            total += e.size();
        }
        assertEquals(total, e.size(), 0.1);
    }
}
