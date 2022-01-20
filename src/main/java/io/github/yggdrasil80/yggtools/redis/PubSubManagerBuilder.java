package io.github.yggdrasil80.yggtools.redis;

import io.github.yggdrasil80.yggtools.utils.BuilderArgument;
import io.github.yggdrasil80.yggtools.utils.IBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class PubSubManagerBuilder implements IBuilder<PubSubManager> {

    private final BuilderArgument<Supplier<RedisManager>> redis = new BuilderArgument<>("RedisManager", () -> () -> new RedisManagerBuilder().build(), true);
    private final BuilderArgument<Enum<? extends IChannel>> channels = new BuilderArgument<>("Channels", () -> IChannelDefault.DEFAULT, true);
    private final BuilderArgument<Enum<? extends IChannel>> patterns = new BuilderArgument<>("Patterns", () -> IChannelDefault.DEFAULT_PATTERN, false);

    private final BuilderArgument<Logger> logger = new BuilderArgument<>("Logger", () -> LoggerFactory.getLogger(PubSubManager.class), false);
    private final BuilderArgument<Boolean> debug = new BuilderArgument<>("Debug", () -> false, false);

    public PubSubManagerBuilder withRedis(Supplier<RedisManager> redis) {
        this.redis.set(redis);
        return this;
    }

    public PubSubManagerBuilder withChannels(Enum<? extends IChannel> channels) {
        this.channels.set(channels);
        return this;
    }

    public PubSubManagerBuilder withPatterns(Enum<? extends IChannel> patterns) {
        this.patterns.set(patterns);
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
        return new PubSubManager(this.redis.get().get(), this.channels.get(), this.patterns.get(), this.logger.get(), this.debug.get());
    }
}
