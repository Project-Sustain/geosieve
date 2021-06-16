package sustain.geosieve.create;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GridPartitioner {
    private final Extents totalExtents;

    public GridPartitioner(Extents extents) {
        totalExtents = extents;
    }

    public List<Extents> getPartitions(int count, double granularity) {
        if (count == 1) {
            return Collections.singletonList(totalExtents);
        } else if (count == 0) {
            return Collections.emptyList();
        }
        return getPartitions(totalExtents, count, granularity);
    }

    private List<Extents> getPartitions(Extents from, int count, double granularity) {
        List<Extents> parts = new ArrayList<>(split(from, granularity));
        while (count > 2) {
            List<Extents> split = split(parts.get(0), granularity);
            parts.addAll(split);
            parts.remove(0);
            parts.sort(Extents::compareBySize);
            count--;
        }
        return parts;
    }

    // The larger of the two is returned first.
    private List<Extents> split(Extents e, double granularity) {
        if (e.width() > e.height()) {
            return splitHorizontal(e, granularity);
        } else {
            return splitVertical(e, granularity);
        }
    }

    private List<Extents> splitVertical(Extents e, double granularity) {
        long granularitiesToMiddle = (long) Math.ceil((e.height() / 2.0) / granularity);
        double midpoint = granularity * granularitiesToMiddle;

        return Arrays.asList(
                new Extents(e.startlat, e.startlng,e.startlat + midpoint, e.endlng),
                new Extents(e.startlat + midpoint, e.startlat, e.endlat, e.endlng)
        );
    }

    private List<Extents> splitHorizontal(Extents e, double granularity) {
        long granularitiesToMiddle = (long) Math.ceil((e.width() / 2.0) / granularity);
        double midpoint = granularity * granularitiesToMiddle;

        return Arrays.asList(
                new Extents(e.startlat, e.startlng, e.endlat, e.startlng + midpoint),
                new Extents(e.startlat, e.startlng + midpoint, e.endlat, e.endlng));
    }
}
