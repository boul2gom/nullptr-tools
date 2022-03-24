package io.github.yggdrasil80.yggtools.redis;

import io.github.yggdrasil80.yggtools.data.IKeyValueDataManager;
import io.github.yggdrasil80.yggtools.redis.connection.RedisConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation for Redis of {@link IKeyValueDataManager}.
 */
public class DefaultRedisManager implements IKeyValueDataManager<String, String> {

    /**
     * The database index.
     */
    private final int dbIndex;
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
     * @param dbIndex The database index.
     * @param baseKey The base key.
     */
    DefaultRedisManager(RedisConnection redis, int dbIndex, String baseKey) {
        this.redis = redis;
        this.dbIndex = dbIndex;
        this.baseKey = baseKey + ":";
    }

    /**
     * Gets the data for the given key.
     * @param key The key of the data.
     * @return The data associated with the key.
     */
    @Override
    public String getData(String key) {
        return this.redis.executeWithReturn(jedis -> {
            jedis.select(this.dbIndex);
            return jedis.get(this.baseKey + key);
        });
    }

    /**
     * Sets the data for the given key.
     * @param key The key of the data.
     * @param data The data to save.
     */
    @Override
    public void saveData(String key, String data) {
        this.redis.execute(jedis -> {
            jedis.select(this.dbIndex);
            jedis.set(this.baseKey + key, data);
        });
    }

    /**
     * Deletes the data for the given key.
     * @param key The key of the data.
     */
    @Override
    public void deleteData(String key) {
        this.redis.execute(jedis -> {
            jedis.select(this.dbIndex);
            jedis.del(this.baseKey + key);
        });
    }

    /**
     * Gets all the keys.
     * @return All the keys.
     */
    @Override
    public List<String> getAllKeys() {
        return this.redis.executeWithReturn(jedis -> {
            jedis.select(this.dbIndex);
            return new ArrayList<>(jedis.keys(this.baseKey + "*"));
        });
    }

    /**
     * Gets all the data
     * @return All the data.
     */
    @Override
    public List<String> getAllData() {
        return this.getAllKeys();
    }
}
