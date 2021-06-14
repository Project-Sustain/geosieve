package sustain.geosieve.create;

public class GriddedExtents extends Extents {
    public final double lngStep;
    public final double latStep;

    public GriddedExtents(Extents e, double lngStep, double latStep) {
        super(e.north, e.west, e.south, e.east);
        this.lngStep = lngStep;
        this.latStep = latStep;
    }

    public GriddedExtents(Extents e, double step) {
        this(e, step, step);
    }
}
