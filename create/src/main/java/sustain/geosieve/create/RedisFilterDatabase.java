package sustain.geosieve.create;

import io.rebloom.client.Client;

public class RedisFilterDatabase implements FilterDatabase {
    private static final int DEFAULT_REDIS_PORT = 6379;
    private final Client client;

    public RedisFilterDatabase() {
        this("localhost", DEFAULT_REDIS_PORT);
    }

    public RedisFilterDatabase(String host) {
        this(host, DEFAULT_REDIS_PORT);
    }

    public RedisFilterDatabase(String host, int port) {
        client = new Client(host, port);
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
