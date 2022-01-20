package io.github.yggdrasil80.yggtools.redis;

import io.github.yggdrasil80.yggtools.utils.BuilderArgument;
import io.github.yggdrasil80.yggtools.utils.IBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisManagerBuilder implements IBuilder<RedisManager> {

    private final BuilderArgument<String> redisHost = new BuilderArgument<>("RedisHost", () -> "127.0.0.1", true);
    private final BuilderArgument<Integer> redisPort = new BuilderArgument<>("RedisPort", () -> 6379, false);
    private final BuilderArgument<String> redisPass = new BuilderArgument<>("RedisPass", () -> "", true);
    private final BuilderArgument<Integer> redisDB = new BuilderArgument<>("RedisDB", () -> 0, false);

    private final BuilderArgument<PubSubManager> pubSub = new BuilderArgument<>("PubSub", () -> new PubSubManagerBuilder().build(), false);

    private final BuilderArgument<Logger> logger = new BuilderArgument<>("Logger", () -> LoggerFactory.getLogger(RedisManager.class), false);

    public RedisManagerBuilder withRedisHost(String redisHost) {
        this.redisHost.set(redisHost);
        return this;
    }

    public RedisManagerBuilder withRedisPort(int redisPort) {
        this.redisPort.set(redisPort);
        return this;
    }

    public RedisManagerBuilder withRedisPass(String redisPass) {
        this.redisPass.set(redisPass);
        return this;
    }

    public RedisManagerBuilder withRedisDB(int redisDB) {
        this.redisDB.set(redisDB);
        return this;
    }

    public RedisManagerBuilder withPubSub(PubSubManager pubSub) {
        this.pubSub.set(pubSub);
        return this;
    }

    public RedisManagerBuilder withLogger(Logger logger) {
        this.logger.set(logger);
        return this;
    }

    @Override
    public RedisManager build() {
        return new RedisManager(this.redisHost.get(), this.redisPort.get(), this.redisPass.get(), this.redisDB.get(), this.pubSub.get(), this.logger.get());
    }
}
