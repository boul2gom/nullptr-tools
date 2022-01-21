package io.github.yggdrasil80.yggtools.redis;

import io.github.yggdrasil80.yggtools.logger.Logger;
import io.github.yggdrasil80.yggtools.utils.BuilderArgument;
import io.github.yggdrasil80.yggtools.utils.IBuilder;

public class PubSubManagerBuilder implements IBuilder<PubSubManager> {

    private final BuilderArgument<RedisManager> redis = new BuilderArgument<>("RedisManager", () -> new RedisManagerBuilder().build(), true);
    private final BuilderArgument<Class<? extends IChannel>> channels = new BuilderArgument<>("Channels", () -> IChannelDefault.class, true);

    private final BuilderArgument<Logger> logger = new BuilderArgument<>("Logger", () -> new Logger("PubSubManager"), false);
    private final BuilderArgument<Boolean> debug = new BuilderArgument<>("Debug", () -> false, false);

    public PubSubManagerBuilder withRedis(RedisManager redis) {
        this.redis.set(redis);
        return this;
    }

    public PubSubManagerBuilder withChannels(Class<? extends IChannel> channels) {
        this.channels.set(channels);
        return this;
    }

    public PubSubManagerBuilder withLogger(Logger logger) {
        this.logger.set(logger);
        return this;
    }

    public PubSubManagerBuilder withDebug(boolean debug) {
        this.debug.set(debug);
        return this;
    }

    @Override
    public PubSubManager build() {
        return new PubSubManager(this.redis.get(), this.channels.get(), this.logger.get(), this.debug.get());
    }
}
