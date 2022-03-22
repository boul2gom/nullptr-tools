package io.github.yggdrasil80.yggtools.redis.pubsub;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.yggdrasil80.yggtools.builder.BuilderArgument;
import io.github.yggdrasil80.yggtools.builder.IBuilder;
import io.github.yggdrasil80.yggtools.message.IMessage;
import io.github.yggdrasil80.yggtools.message.MessageReceiver;
import io.github.yggdrasil80.yggtools.receiver.ReceiverManager;
import io.github.yggdrasil80.yggtools.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.function.Supplier;

/**
 * The PubSubManager class used to manage the Redis pubsub.
 */
public class PubSubManager extends ReceiverManager<String, String, IMessage, MessageReceiver<? extends IMessage>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PubSubManager.class);

    private final Gson gson;
    private final PubSub pubSub;
    private final RedisManager redis;

    PubSubManager(final RedisManager redis, final GsonBuilder gson, final boolean debug) {
        super(LOGGER, debug);

        this.gson = gson.serializeNulls().create();
        this.pubSub = new PubSub(this);
        this.redis = redis;

        Runtime.getRuntime().addShutdownHook(new Thread(this::unregisterReceivers));
    }

    @Override
    public <R extends MessageReceiver<? extends IMessage>> void registerReceiver(String channel, R receiver) {
        super.registerReceiver(channel, receiver);
        this.redis.execute(jedis -> jedis.subscribe(this.pubSub, channel));
    }

    public void sendMessage(final String channel, final IMessage message) {
        this.redis.execute(jedis -> jedis.publish(channel, this.gson.toJson(message)));
    }

    public void onMessage(final String channel, final String message) {
        this.fireEvent(channel, message);
    }

    /**
     * Get the builder instance
     * @return The builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IBuilder<PubSubManager> {

        private final BuilderArgument<RedisManager> redis = new BuilderArgument<RedisManager>("RedisManager").required();

        private final BuilderArgument<GsonBuilder> gson = new BuilderArgument<>("Gson", GsonBuilder::new).optional();
        private final BuilderArgument<Boolean> debug = new BuilderArgument<>("Debug", () -> false).optional();

        Builder() {}

        public Builder withRedis(final Supplier<RedisManager> redis) {
            this.redis.set(redis);
            return this;
        }

        public Builder withGson(final Supplier<GsonBuilder> gson) {
            this.gson.set(gson);
            return this;
        }

        public Builder withDebug(final Supplier<Boolean> debug) {
            this.debug.set(debug);
            return this;
        }

        @Override
        public PubSubManager build() {
            return new PubSubManager(this.redis.get(), this.gson.get(), this.debug.get());
        }
    }

    private static class PubSub extends JedisPubSub {

        private final PubSubManager manager;

        public PubSub(PubSubManager manager) {
            this.manager = manager;
        }

        @Override
        public void onMessage(final String channel, final String message) {
            this.manager.onMessage(channel, message);
        }
    }
}
