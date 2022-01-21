package io.github.yggdrasil80.yggtools.redis;

import io.github.yggdrasil80.yggtools.logger.Logger;
import io.github.yggdrasil80.yggtools.utils.BuilderArgument;
import io.github.yggdrasil80.yggtools.utils.IBuilder;

public class RedisManagerBuilder implements IBuilder<RedisManager> {

    private final BuilderArgument<String> redisHost = new BuilderArgument<>("RedisHost", () -> "127.0.0.1", true);
    private final BuilderArgument<Integer> redisPort = new BuilderArgument<>("RedisPort", () -> 6379, false);
    private final BuilderArgument<String> redisPass = new BuilderArgument<>("RedisPass", () -> "", true);
    private final BuilderArgument<Integer> redisDB = new BuilderArgument<>("RedisDB", () -> 0, false);

    private final BuilderArgument<Logger> logger = new BuilderArgument<>("Logger", () -> new Logger("RedisManager"), false);

    public RedisManagerBuilder withRedis(String redisHost, int redisPort, String redisPass, int redisDB) {
        return this.withRedisHost(redisHost).withRedisPort(redisPort).withRedisPass(redisPass).withRedisDB(redisDB);
    }

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

    public RedisManagerBuilder withLogger(Logger logger) {
        this.logger.set(logger);
        return this;
    }

    @Override
    public RedisManager build() {
        return new RedisManager(this.redisHost.get(), this.redisPort.get(), this.redisPass.get(), this.redisDB.get(), this.logger.get());
    }
}
