package io.github.yggdrasil80.yggtools.redis;

import io.github.yggdrasil80.yggtools.builder.BuilderArgument;
import io.github.yggdrasil80.yggtools.builder.IBuilder;
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
 * The RedisManager class is a wrapper for the Jedis library.
 */
public class RedisManager {

    private static final ExecutorService EXECUTORS = Executors.newCachedThreadPool();
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisManager.class);

    private final String redisHost;
    private final int redisPort;
    private final String redisPass;
    private JedisPool jedisPool;

    private int reconnections;
    private boolean connected;

    /**
     * The constructor of the RedisManager class.
     * @param redisHost The host of the Redis server.
     * @param redisPort The port of the Redis server.
     * @param redisPass The password of the Redis server.
     */
    RedisManager(final String redisHost, final int redisPort, final String redisPass) {
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
     * @param consumer The function to execute.
     */
    public void execute(Consumer<Jedis> consumer) {
        if (!this.connected) {
            LOGGER.error("You are trying to execute a function on a disconnected Redis server ! Trying to reconnect...");
            this.start();
            this.execute(consumer);
        }

        EXECUTORS.execute(() -> {
            try (final Jedis jedis = this.jedisPool.getResource()) {
                consumer.accept(jedis);
            } catch (final Exception e) {
                LOGGER.error("An error occurred during connecting to Redis ! Trying to reconnect...", e);
                e.printStackTrace();
                if (this.reconnections > 3) return;

                this.start();
                this.execute(consumer);
            }
        });
    }

    /**
     * The method that executes a function on the Redis server, and return an object.
     * @param function The function to execute.
     * @param <R> The type of the object to return.
     * @return The object returned by the function.
     */
    public <R> R executeWithReturn(Function<Jedis, R> function) {
        if (!this.connected) {
            LOGGER.error("You are trying to execute a function on a disconnected Redis server ! Trying to reconnect...");
            this.start();
            this.executeWithReturn(function);
        }

        try {
            return EXECUTORS.submit(() -> {
                try (final Jedis jedis = this.jedisPool.getResource()) {
                    return function.apply(jedis);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("An error occurred during connecting to Redis ! Trying to reconnect...", e);
            e.printStackTrace();
            if (this.reconnections > 3) return null;

            this.start();
            return this.executeWithReturn(function);
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
     * Get the builder instance
     * @return The builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IBuilder<RedisManager> {

        private final BuilderArgument<String> redisHost = new BuilderArgument<String>("RedisHost").required();
        private final BuilderArgument<Integer> redisPort = new BuilderArgument<Integer>("RedisPort").required();
        private final BuilderArgument<String> redisPass = new BuilderArgument<String>("RedisPass").required();

        Builder() {}

        public Builder withHost(final Supplier<String> redisHost) {
            this.redisHost.set(redisHost);
            return this;
        }

        public Builder withPort(final Supplier<Integer> redisPort) {
            this.redisPort.set(redisPort);
            return this;
        }

        public Builder withPass(final Supplier<String> redisPass) {
            this.redisPass.set(redisPass);
            return this;
        }

        @Override
        public RedisManager build() {
            return new RedisManager(this.redisHost.get(), this.redisPort.get(), this.redisPass.get());
        }
    }
}
