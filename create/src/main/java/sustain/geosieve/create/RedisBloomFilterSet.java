package sustain.geosieve.create;

import io.rebloom.client.Client;

public class RedisBloomFilterSet implements BloomFilterSet<String> {
    private final Client client;

    public RedisBloomFilterSet() {
        client = new Client("localhost", 6379);
    }

    @Override
    public synchronized void add(String filterName, String e) {
        client.cfAdd(filterName, e);
    }

    @Override
    public synchronized boolean contains(String filterName, String e) {
        return client.cfExists(filterName, e);
    }

    @Override
    public synchronized void clear(String filterName, String e) {
        client.cfDel(filterName, e);
    }
}
