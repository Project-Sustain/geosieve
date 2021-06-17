package sustain.geosieve.create.uniform;

import sustain.geosieve.create.LatLng;

public class Extents {
    public final double north;
    public final double east;
    public final double south;
    public final double west;
    public final double startlng;
    public final double endlng;
    public final double startlat;
    public final double endlat;

    public static Extents CONUS
            = new Extents(41.384358, -66.885444, 24.396308, -124.848974);

    public static Extents COLORADO
            = new Extents(41, -109, 37, -102);

    public static int compareBySize(Extents a, Extents b) {
        return (int) Math.signum(b.size() - a.size());
    }

    public Extents(double n, double e, double s, double w) {
        north = n;
        east = e;
        south = s;
        west = w;
        startlng = Math.min(e, w);
        endlng = Math.max(e, w);
        startlat = Math.min(s, n);
        endlat = Math.max(s, n);
    }

    public boolean isIn(LatLng point) {
        boolean inLat = startlat < point.lat() && point.lat() < endlat;
        boolean inLng = startlng < point.lng() && point.lng() < endlng;
        return inLat && inLng;
    }

    public double width() {
        return endlng - startlng;
    }

    public double height() {
        return endlat - startlat;
    }

    public double size() {
        return width() * height();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Extents)) return false;
        Extents e = (Extents) o;
        return e.north == north && e.east == east && e.south == south && e.west == west;
    }
}
