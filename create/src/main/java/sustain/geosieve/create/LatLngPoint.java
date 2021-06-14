package sustain.geosieve.create;

public class LatLngPoint implements LatLng {
    private double lat;
    private double lng;

    public LatLngPoint(double lng, double lat) {
        set(lng, lat);
    }

    public LatLngPoint() {
        this(0, 0);
    }

    @Override
    public void set(double lng, double lat) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public double lat() {
        return lat;
    }

    @Override
    public double lng() {
        return lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LatLngPoint)) return false;
        if (lat != ((LatLngPoint) o).lat) return false;
        if (lng != ((LatLngPoint) o).lng) return false;
        return true;
    }

    @Override
    public int hashCode() {
        long lngBits = Double.doubleToLongBits(lng);
        long latBits = Double.doubleToLongBits(lat);
        return (int) ((lngBits ^ latBits) & 0xFF);
    }

    @Override
    public String toString() {
        return String.format("%f, %f", lng, lat);
    }
}
