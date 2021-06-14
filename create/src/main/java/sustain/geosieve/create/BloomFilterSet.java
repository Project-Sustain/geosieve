package sustain.geosieve.create;

public interface BloomFilterSet<ElementType> {
    // Makes the filter if it doesn't exist.
    void add(String filterName, ElementType e);
    // Returns false if the filter doesn't exist.
    boolean contains(String filterName, ElementType e);
    void clear(String filterName, ElementType e);
}
