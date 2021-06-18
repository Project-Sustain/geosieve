package sustain.geosieve.create.uniform;

import sustain.geosieve.create.FilterDatabase;
import sustain.geosieve.create.GisJoinMapper;
import sustain.geosieve.create.LatLng;

public class GridWorker implements Runnable {
    private final Iterable<LatLng> points;
    private final FilterDatabase filters;
    private final GisJoinMapper mapper;

    public GridWorker(Iterable<LatLng> points, FilterDatabase filters, GisJoinMapper mapper) {
        this.points = points;
        this.filters = filters;
        this.mapper = mapper;
    }

    @Override
    public void run() {
        for (LatLng point : points) {
            String gisJoin = mapper.map(point);
            filters.add(gisJoin, String.format("%.2f,%.2f", point.lng(), point.lat()));
        }
    }
}
