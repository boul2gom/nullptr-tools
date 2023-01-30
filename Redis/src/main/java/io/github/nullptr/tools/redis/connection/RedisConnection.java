package io.github.nullptr.tools.redis.connection;

import io.github.nullptr.tools.builder.BuilderArgument;
import io.github.nullptr.tools.builder.IBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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
     * Executors, used for asynchronous requests.
     */
    private static final ExecutorService EXECUTORS = Executors.newCachedThreadPool();
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConnection.class);

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
     * @param redisHost The host of the Redis server.
     * @param redisPort The port of the Redis server.
     * @param redisPass The password of the Redis server.
     */
    RedisConnection(String redisHost, int redisPort, String redisPass) {
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
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        this.execute(jedis -> LOGGER.info("Connection set with Redis, on database " + jedis.getDB()));
    }

    /**
     * The method that executes a function on the Redis server.
     * @param consumer The consumer that will be executed.
     */
    public void execute(Consumer<Jedis> consumer) {
        if (!this.connected) {
            LOGGER.error("You are trying to execute a function on a disconnected Redis server ! Trying to reconnect...");
            this.start();
            this.execute(consumer);
        }

        try (final Jedis jedis = this.jedisPool.getResource()) {
            consumer.accept(jedis);
        } catch (final Exception e) {
            LOGGER.error("An error occurred during connecting to Redis ! Trying to reconnect...", e);
            e.printStackTrace();
            if (this.reconnections > 3) return;

            this.start();
            this.execute(consumer);
        }
    }

    /**
     * The method that executes a request to the Redis server asynchronously.
     * @param consumer The consumer that will be executed.
     */
    public void executeAsync(Consumer<Jedis> consumer) {
        EXECUTORS.execute(() -> this.execute(consumer));
    }

    /**
     * The method that executes a function on the Redis server, and return a result.
     * @param function The function to execute.
     * @param <R> The type of the object to return.
     * @return The object returned by the function.
     */
    public <R> R executeAndReturn(Function<Jedis, R> function) {
        if (!this.connected) {
            LOGGER.error("You are trying to execute a function on a disconnected Redis server ! Trying to reconnect...");
            this.start();
            this.executeAndReturn(function);
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
            return this.executeAndReturn(function);
        }
    }

    /**
     * The method that executes a request to the Redis server, and returns a result asynchronously.
     * @param function The function that will be executed.
     * @param <R> The type of the result.
     * @return The result of the function.
     */
    public <R> R executeAndReturnAsync(Function<Jedis, R> function) {
        try {
            return EXECUTORS.submit(() -> this.executeAndReturn(function)).get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("An error occurred during connecting to Redis ! Trying to reconnect...", e);
            return null;
        }
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
         * The host of the Redis server.
         */
        private final BuilderArgument<String> redisHost = new BuilderArgument<String>("RedisHost").required();
        /**
         * The port of the Redis server.
         */
        private final BuilderArgument<Integer> redisPort = new BuilderArgument<Integer>("RedisPort", () -> 6379).required();
        /**
         * The password of the Redis server.
         */
        private final BuilderArgument<String> redisPass = new BuilderArgument<String>("RedisPass").required();

        /**
         * Sets the host of the Redis server.
         * @param redisHost The host of the Redis server.
         * @return The builder.
         */
        public Builder withHost(final Supplier<String> redisHost) {
            this.redisHost.set(redisHost);
            return this;
        }

        /**
         * Sets the port of the Redis server.
         * @param redisPort The port of the Redis server.
         * @return The builder.
         */
        public Builder withPort(final Supplier<Integer> redisPort) {
            this.redisPort.set(redisPort);
            return this;
        }

        /**
         * Sets the password of the Redis server.
         * @param redisPass The password of the Redis server.
         * @return The builder.
         */
        public Builder withPass(final Supplier<String> redisPass) {
            this.redisPass.set(redisPass);
            return this;
        }

        /**
         * Builds the Redis connection.
         * @return The built Redis connection.
         */
        @Override
        public RedisConnection build() {
            return new RedisConnection(this.redisHost.get(), this.redisPort.get(), this.redisPass.get());
        }
    }
}
