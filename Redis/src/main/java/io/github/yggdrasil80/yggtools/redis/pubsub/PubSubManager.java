package io.github.yggdrasil80.yggtools.redis.pubsub;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.yggdrasil80.yggtools.builder.BuilderArgument;
import io.github.yggdrasil80.yggtools.builder.IBuilder;
import io.github.yggdrasil80.yggtools.message.IMessage;
import io.github.yggdrasil80.yggtools.message.MessageReceiver;
import io.github.yggdrasil80.yggtools.receiver.keyed.KeyedReceiverManager;
import io.github.yggdrasil80.yggtools.redis.connection.RedisConnection;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.function.Supplier;

/**
 * The manager used to manage the Redis pubsub.
 */
public class PubSubManager extends KeyedReceiverManager<String, String, IMessage, MessageReceiver<? extends IMessage>> {

    /**
     * The Gson used to serialize and deserialize the messages.
     */
    private final Gson gson;
    /**
     * The messages listener.
     */
    private final PubSubListener psl;
    /**
     * The Redis connection.
     */
    private final RedisConnection redis;

    /**
     * The  manager constructor.
     * @param redis The Redis connection.
     * @param gson The Gson used to serialize and deserialize the messages.
     * @param debug <code>true</code> if the debug mode is enabled, <code>false</code> otherwise.
     */
    PubSubManager(final RedisConnection redis, final GsonBuilder gson, final boolean debug) {
        super(LoggerFactory.getLogger(PubSubManager.class), debug);

        this.gson = gson.serializeNulls().create();
        this.psl = new PubSubListener(this);
        this.redis = redis;

        Runtime.getRuntime().addShutdownHook(new Thread(this::unregisterReceivers));
    }

    /**
     * Register a new receiver.
     * @param channel The channel.
     * @param receiver The receiver to register.
     * @param <R> The type of the receiver.
     */
    @Override
    public <R extends MessageReceiver<? extends IMessage>> void registerReceiver(String channel, R receiver) {
        super.registerReceiver(channel, receiver);
        this.redis.executeAsync(jedis -> jedis.subscribe(this.psl, channel));
    }

    /**
     * Send a message to a channel.
     * @param channel The channel.
     * @param message The message.
     */
    public void sendMessage(final String channel, final IMessage message) {
        this.redis.executeAsync(jedis -> jedis.publish(channel, this.gson.toJson(message)));
    }

    /**
     * The method called when a message is received.
     * @param channel The channel.
     * @param message The message.
     */
    public void onMessage(final String channel, final String message) {
        this.fireEvent(channel, message);
    }

    /**
     * The manager builder.
     */
    public static class Builder implements IBuilder<PubSubManager> {

        /**
         * The Redis connection.
         */
        private final BuilderArgument<RedisConnection> redis = new BuilderArgument<RedisConnection>("RedisManager").required();
        /**
         * The Gson used to serialize and deserialize the messages.
         */
        private final BuilderArgument<GsonBuilder> gson = new BuilderArgument<>("Gson", GsonBuilder::new).optional();
        /**
         * The debug mode flag.
         */
        private final BuilderArgument<Boolean> debug = new BuilderArgument<>("Debug", () -> false).optional();

        /**
         * Set the Redis connection.
         * @param redis The Redis connection.
         * @return The builder.
         */
        public Builder withRedis(final Supplier<RedisConnection> redis) {
            this.redis.set(redis);
            return this;
        }

        /**
         * Set the Gson used to serialize and deserialize the messages.
         * @param gson The Gson.
         * @return The builder.
         */
        public Builder withGson(final Supplier<GsonBuilder> gson) {
            this.gson.set(gson);
            return this;
        }

        /**
         * Set the debug mode flag, <code>true</code> if the debug mode is enabled, <code>false</code> otherwise.
         * @param debug The debug mode flag.
         * @return The builder.
         */
        public Builder withDebug(final Supplier<Boolean> debug) {
            this.debug.set(debug);
            return this;
        }

        /**
         * Build the pubsub manager.
         * @return The built pubsub manager.
         */
        @Override
        public PubSubManager build() {
            return new PubSubManager(this.redis.get(), this.gson.get(), this.debug.get());
        }
    }

    /**
     * The pubsub listener.
     */
    private static class PubSubListener extends JedisPubSub {

        /**
         * The pubsub manager.
         */
        private final PubSubManager manager;

        /**
         * The pubsub listener constructor.
         * @param manager The pubsub manager.
         */
        public PubSubListener(PubSubManager manager) {
            this.manager = manager;
        }

        /**
         * The method called when a message is received.
         * @param channel The channel.
         * @param message The message.
         */
        @Override
        public void onMessage(final String channel, final String message) {
            this.manager.onMessage(channel, message);
        }
    }
}
