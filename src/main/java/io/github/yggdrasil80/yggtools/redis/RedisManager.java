package io.github.yggdrasil80.yggtools.redis;

import io.github.yggdrasil80.yggtools.logger.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisManager {

    private final String redisHost;
    private final int redisPort;
    private final String redisPass;
    private final int redisDB;
    private JedisPool jedisPool;

    private final Logger logger;

    public RedisManager(final String redisHost, final int redisPort, final String redisPass, final int redisDB, final Logger logger) {
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.redisPass = redisPass;
        this.redisDB = redisDB;
        this.logger = logger;
    }

    public void start() {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), this.redisHost, this.redisPort, 2000, this.redisPass, this.redisDB, this.getClass().getSimpleName());

        try (final Jedis jedis = this.jedisPool.getResource()){
            this.logger.info("Connection set with Redis, on database " + jedis.getDB());
        } catch (final Exception e) {
            this.logger.error("An error occurred during connecting to Redis ! (" + e.getMessage() + "). Shutdown...");
        }
    }

    public void stop() {
        this.logger.info("Stopping Redis connection...");

        this.jedisPool.close();
        this.jedisPool.destroy();
    }

    public String get(String key) {
        try (final Jedis jedis = this.jedisPool.getResource()){
            return jedis.get(key);
        }
    }

    public void set(String key, String value) {
        try (final Jedis jedis = this.jedisPool.getResource()){
            jedis.set(key, value);
        }
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }
}
