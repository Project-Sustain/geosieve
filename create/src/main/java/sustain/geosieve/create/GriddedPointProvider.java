package sustain.geosieve.create;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class GriddedPointProvider implements Iterable<LatLng> {
    private final double latStep;
    private final double lngStep;
    private final Extents extents;

    public GriddedPointProvider(double lngStep, double latStep, Extents extents) {
        this.latStep = latStep;
        this.lngStep = lngStep;
        this.extents = extents;
    }

    public GriddedPointProvider(GriddedExtents griddedExtents) {
        this.latStep = griddedExtents.latStep;
        this.lngStep = griddedExtents.lngStep;
        this.extents = griddedExtents;
    }

    @Override
    public @NotNull Iterator<LatLng> iterator() {
        return new Iterator<>() {
            private double lng = extents.startlng;
            private double lat = extents.startlat;

            @Override
            public boolean hasNext() {
                return lat < extents.endlat && !Util.doubleCloseEnough(lat, extents.endlat);
            }

            @Override
            public LatLng next() {
                LatLng result = new LatLngPoint(lng, lat);
                lng += lngStep;
                if (lng > extents.endlng || Util.doubleCloseEnough(lng, extents.endlng)) {
                    lng = extents.startlng;
                    lat += latStep;
                }
                return result;
            }
        };
    }
}
