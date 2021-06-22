package sustain.geosieve.create;

public interface FilterDatabase {
    // Makes the filter if it doesn't exist.
    void add(String filterName, String e);
    // Returns false if the filter doesn't exist.
    boolean contains(String filterName, String e);
    void clear(String filterName, String e);
    // Implement if the subclass needs to perform a cleanup operation when done.
    default void cleanup() { }
}
