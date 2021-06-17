package sustain.geosieve.create.uniform;

import sustain.geosieve.create.LatLng;
import sustain.geosieve.create.LatLngPoint;
import sustain.geosieve.create.Util;

import java.util.Iterator;

public class UniformPointProvider implements Iterable<LatLng> {
    private final double latStep;
    private final double lngStep;
    private final Extents extents;

    public UniformPointProvider(double lngStep, double latStep, Extents extents) {
        this.latStep = latStep;
        this.lngStep = lngStep;
        this.extents = extents;
    }

    public UniformPointProvider(GriddedExtents griddedExtents) {
        this.latStep = griddedExtents.latStep;
        this.lngStep = griddedExtents.lngStep;
        this.extents = griddedExtents;
    }

    @Override
    public Iterator<LatLng> iterator() {
        return new Iterator<LatLng>() {
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
