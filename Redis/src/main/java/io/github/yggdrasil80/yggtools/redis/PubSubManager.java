package io.github.yggdrasil80.yggtools.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.yggdrasil80.yggtools.builder.BuilderArgument;
import io.github.yggdrasil80.yggtools.builder.IBuilder;
import io.github.yggdrasil80.yggtools.receiver.ReceiverManager;
import io.github.yggdrasil80.yggtools.logger.Logger;
import io.github.yggdrasil80.yggtools.message.IMessage;
import io.github.yggdrasil80.yggtools.message.MessageReceiver;
import redis.clients.jedis.JedisPubSub;

/**
 * The PubSubManager class used to manage the Redis pubsub.
 */
public class PubSubManager extends ReceiverManager<String, String, IMessage, MessageReceiver<? extends IMessage>> {



    private final Gson gson;
    private final PubSub pubSub;
    private final RedisManager redis;

    PubSubManager(final RedisManager redis, final GsonBuilder gson, final Logger logger, final boolean debug) {
        super(logger, debug);

        this.gson = gson.serializeNulls().create();
        this.pubSub = new PubSub(this);
        this.redis = redis;
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

        if (this.debug) this.logger.debug("[" + channel + "] Received: " + message);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IBuilder<PubSubManager> {

        private final BuilderArgument<RedisManager> redis = new BuilderArgument<RedisManager>("RedisManager").required();

        private final BuilderArgument<GsonBuilder> gson = new BuilderArgument<>("Gson", GsonBuilder::new).optional();
        private final BuilderArgument<Logger> logger = new BuilderArgument<>("Logger", () -> new Logger("PubSubManager")).optional();
        private final BuilderArgument<Boolean> debug = new BuilderArgument<>("Debug", () -> false).optional();

        Builder() {}

        public Builder withRedis(final RedisManager redis) {
            this.redis.set(() -> redis);
            return this;
        }

        public Builder withGson(final GsonBuilder gson) {
            this.gson.set(() -> gson);
            return this;
        }

        public Builder withLogger(final Logger logger) {
            this.logger.set(() -> logger);
            return this;
        }

        public Builder withDebug(final boolean debug) {
            this.debug.set(() -> debug);
            return this;
        }

        @Override
        public PubSubManager build() {
            return new PubSubManager(this.redis.get(), this.gson.get(), this.logger.get(), this.debug.get());
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
