package io.github.yggdrasil80.yggtools.redis;

import io.github.yggdrasil80.yggtools.logger.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The RedisManager class is a wrapper for the Jedis library.
 */
public final class RedisManager {

    private final String redisHost;
    private final int redisPort;
    private final String redisPass;
    private final int redisDB;
    private JedisPool jedisPool;

    private final Logger logger;
    private int reconnections;

    /**
     * The constructor of the RedisManager class.
     * @param redisHost The host of the Redis server.
     * @param redisPort The port of the Redis server.
     * @param redisPass The password of the Redis server.
     * @param redisDB The database number of the Redis server.
     * @param logger The {@link Logger} instance.
     */
    public RedisManager(final String redisHost, final int redisPort, final String redisPass, final int redisDB, final Logger logger) {
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.redisPass = redisPass;
        this.redisDB = redisDB;
        this.logger = logger;

        this.reconnections = 0;
    }

    /**
     * The method that starts the connection to the Redis server.
     */
    public void start() {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), this.redisHost, this.redisPort, 2000, this.redisPass, this.redisDB, this.getClass().getSimpleName());
        this.reconnections += 1;

        if (this.reconnections > 3) {
            this.logger.error("Could not connect to Redis after 3 attempts. Exiting...");
            throw new RuntimeException("Could not connect to Redis after 3 attempts.");
        }

        this.execute(jedis -> this.logger.info("Connection set with Redis, on database " + jedis.getDB()));
    }

    /**
     * The method that executes a function on the Redis server.
     * @param consumer The function to execute.
     */
    public void execute(Consumer<Jedis> consumer) {
        try (final Jedis jedis = this.jedisPool.getResource()) {
            consumer.accept(jedis);
        } catch (final Exception e) {
            this.logger.error("An error occurred during connecting to Redis ! (" + e.getMessage() + "). Trying to reconnect...");
            if (this.reconnections > 3) return;

            this.start();
            this.execute(consumer);
        }
    }

    /**
     * The method that executes a function on the Redis server, and return an object.
     * @param function The function to execute.
     * @param <R> The type of the object to return.
     * @return The object returned by the function.
     */
    public <R> R executeAndGet(Function<Jedis, R> function) {
        try (final Jedis jedis = this.jedisPool.getResource()) {
            return function.apply(jedis);
        } catch (final Exception e) {
            this.logger.error("An error occurred during connecting to Redis ! (" + e.getMessage() + "). Trying to reconnect...");
            if (this.reconnections > 3) return null;

            this.start();
            return this.executeAndGet(function);
        }
    }

    /**
     * The method that stops the connection to the Redis server.
     */
    public void stop() {
        this.logger.info("Stopping Redis connection...");

        this.jedisPool.close();
        this.jedisPool.destroy();
    }

    /**
     * The method that returns the {@link Jedis} resource from the pool.
     * @return The {@link Jedis} resource from the pool.
     */
    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }
}
