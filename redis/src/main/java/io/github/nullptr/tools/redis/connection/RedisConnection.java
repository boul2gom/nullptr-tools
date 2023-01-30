package io.github.nullptr.tools.redis.connection;

import io.github.nullptr.tools.builder.BuilderArgument;
import io.github.nullptr.tools.builder.IBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A connection and request manager for the Jedis library.
 */
public class RedisConnection {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConnection.class);

    /**
     * Executor, used for asynchronous requests.
     */
    private final ExecutorService executor;
    /**
     * The Redis host.
     */
    private final String redisHost;
    /**
     * The Redis port.
     */
    private final int redisPort;
    /**
     * The Redis password.
     */
    private final String redisPass;
    /**
     * The connected Jedis pool.
     */
    private JedisPool jedisPool;

    /**
     * Reconnections counter.
     */
    private int reconnections;
    /**
     * The connection status.
     */
    private boolean connected;

    /**
     * The constructor of the Redis manager.
     * @param executor The executor used for asynchronous requests.
     * @param redisHost The host of the Redis server.
     * @param redisPort The port of the Redis server.
     * @param redisPass The password of the Redis server.
     */
    public RedisConnection(ExecutorService executor, String redisHost, int redisPort, String redisPass) {
        this.executor = executor;
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.redisPass = redisPass;

        this.reconnections = 0;
        this.connected = false;

        this.start();
    }

    /**
     * The method that starts the connection to the Redis server.
     */
    public void start() {
        this.connected = false;
        this.jedisPool = new JedisPool(new JedisPoolConfig(), this.redisHost, this.redisPort, 5000, this.redisPass);
        this.reconnections += 1;

        if (this.reconnections > 3) {
            LOGGER.error("Could not connect to Redis after 3 attempts. Exiting...");
            throw new RuntimeException("Could not connect to Redis after 3 attempts.");
        }

        this.connected = true;
        this.execute(jedis -> {
            LOGGER.info("Connection set with Redis, on database " + jedis.getDB());
            return null;
        });
    }

    /**
     * The method that executes a function on the Redis server, and return a result.
     * @param function The function to execute.
     * @param <R> The type of the object to return, use Void if you don't want to return anything.
     * @return The object returned by the function.
     */
    public <R> R execute(Function<Jedis, R> function) {
        if (!this.connected) {
            LOGGER.error("You are trying to execute a function on a disconnected Redis server ! Trying to reconnect...");
            this.start();
            this.execute(function);
        }

        try {
            try (final Jedis jedis = this.jedisPool.getResource()) {
                return function.apply(jedis);
            }
        } catch (final Exception e) {
            LOGGER.error("An error occurred during connecting to Redis ! Trying to reconnect...", e);
            e.printStackTrace();
            if (this.reconnections > 3) return null;

            this.start();
            return this.execute(function);
        }
    }

    /**
     * The method that executes asynchronously a function on the Redis server, and return a result.
     * @param function The function to execute.
     * @param <R> The type of the object to return, use Void if you don't want to return anything.
     * @return A CompletableFuture that will contain the object returned by the function.
     */
    public <R> CompletableFuture<R> executeAsync(Function<Jedis, R> function) {
        return CompletableFuture.supplyAsync(() -> this.execute(function), this.executor);
    }

    /**
     * The method that stops the connection to the Redis server.
     */
    public void stop() {
        LOGGER.info("Stopping Redis connection...");

        this.jedisPool.close();
        this.jedisPool.destroy();
    }

    /**
     * The builder for the Redis connection.
     */
    public static class Builder implements IBuilder<RedisConnection> {

        /**
         * The executor used for asynchronous requests.
         */
        public final BuilderArgument<ExecutorService> executor = new BuilderArgument<>("Executor", Executors::newCachedThreadPool).required();
        /**
         * The host of the Redis server.
         */
        public final BuilderArgument<String> redisHost = new BuilderArgument<String>("RedisHost").required();
        /**
         * The port of the Redis server.
         */
        public final BuilderArgument<Integer> redisPort = new BuilderArgument<>("RedisPort", () -> 6379).required();
        /**
         * The password of the Redis server.
         */
        public final BuilderArgument<String> redisPass = new BuilderArgument<String>("RedisPass").required();

        /**
         * Builds the Redis connection.
         * @return The built Redis connection.
         */
        @Override
        public RedisConnection build() {
            return new RedisConnection(this.executor.get(), this.redisHost.get(), this.redisPort.get(), this.redisPass.get());
        }
    }
}
