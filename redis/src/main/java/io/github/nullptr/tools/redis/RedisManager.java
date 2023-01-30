package io.github.nullptr.tools.redis;

import io.github.nullptr.tools.data.IKeyValueDataManager;
import io.github.nullptr.tools.redis.connection.RedisConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for Redis of {@link IKeyValueDataManager}.
 */
public class RedisManager implements IKeyValueDataManager<String, String> {

    /**
     * The base key to store the data.
     */
    private final String baseKey;
    /**
     * The connection to the Redis server.
     */
    private final RedisConnection redis;

    /**
     * The default redis manager constructor.
     * @param redis The redis connection.
     * @param baseKey The base key.
     */
    public RedisManager(RedisConnection redis, String baseKey) {
        this.redis = redis;
        this.baseKey = baseKey + ":";
    }

    /**
     * Gets the data for the given key.
     * @param key The key of the data.
     * @return The data associated with the key.
     */
    @Override
    public String getData(String key) {
        return this.redis.execute(jedis -> jedis.get(this.baseKey + key));
    }

    /**
     * Sets the data for the given key.
     * @param key The key of the data.
     * @param data The data to save.
     */
    @Override
    public void saveData(String key, String data) {
        this.redis.execute(jedis -> jedis.set(this.baseKey + key, data));
    }

    /**
     * Deletes the data for the given key.
     * @param key The key of the data.
     */
    @Override
    public void deleteData(String key) {
        this.redis.execute(jedis -> jedis.del(this.baseKey + key));
    }
}
