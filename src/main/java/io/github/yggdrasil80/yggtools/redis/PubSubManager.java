package io.github.yggdrasil80.yggtools.redis;

import io.github.yggdrasil80.yggtools.logger.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;

public final class PubSubManager extends JedisPubSub {

    private final RedisManager redis;
    private final Class<? extends IChannel> channels;

    private final Logger logger;
    private final boolean debug;

    public PubSubManager(final RedisManager redis, final Class<? extends IChannel> channels, final Logger logger, final boolean debug) {
        this.redis = redis;
        this.channels = channels;
        this.logger = logger;
        this.debug = debug;
    }

    public void start() {
        try (final Jedis jedis = this.redis.getJedis()){
            Arrays.asList(this.channels.getEnumConstants()).forEach(channel -> jedis.subscribe(this, channel.getChannel()));
        }
    }

    public <T extends IChannel> void publish(final T channel, final String message) {
        try (final Jedis jedis = this.redis.getJedis()){
            jedis.publish(channel.getChannel(), message);
        }
    }

    @Override
    public void onMessage(final String channel, final String message) {
        if (this.debug) this.logger.debug("Channel " + channel + " has sent a message: " + message);
    }

    @Override
    public void onPMessage(final String pattern, final String channel, final String message) {
        if (this.debug) this.logger.debug("Channel " + channel + " has sent a message: " + message + " with the pattern " + pattern);
    }

    @Override
    public void onSubscribe(final String channel, final int subscribedChannels) {
        if (this.debug) this.logger.debug("Client is Subscribed to channel: " + channel + ", total: " + subscribedChannels);
    }

    @Override
    public void onPSubscribe(final String pattern, final int subscribedChannels) {
        if (this.debug) this.logger.debug("Client is Subscribed to pattern: " + pattern + ", total: " + subscribedChannels);
    }

    @Override
    public void onUnsubscribe(final String channel, final int subscribedChannels) {
        if (this.debug) this.logger.debug("Client is Unsubscribed to channel: " + channel + ", total: " + subscribedChannels);
    }

    @Override
    public void onPUnsubscribe(final String pattern, final int subscribedChannels) {
        if (this.debug) this.logger.debug("Client is Unsubscribed to pattern: " + pattern + ", total: " + subscribedChannels);
    }

    public void stop() {
        Arrays.asList(this.channels.getEnumConstants()).forEach(channel -> this.unsubscribe(channel.getChannel()));
    }
}
