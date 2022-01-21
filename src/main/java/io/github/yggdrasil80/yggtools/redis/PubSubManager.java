package io.github.yggdrasil80.yggtools.redis;

import io.github.yggdrasil80.yggtools.logger.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;

public class PubSubManager extends JedisPubSub {

    private final RedisManager redis;
    private final Class<? extends IChannel> channels;

    private final Logger logger;
    private final boolean debug;

    public PubSubManager(RedisManager redis, Class<? extends IChannel> channels, Logger logger, boolean debug) {
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

    public <T extends IChannel> void publish(T channel, String message) {
        try (final Jedis jedis = this.redis.getJedis()){
            jedis.publish(channel.getChannel(), message);
        }
    }

    @Override
    public void onMessage(String channel, String message) {
        if (this.debug) this.logger.info("Channel " + channel + " has sent a message: " + message);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        if (this.debug) this.logger.info("Channel " + channel + " has sent a message: " + message + " with the pattern " + pattern);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        if (this.debug) this.logger.info("Client is Subscribed to channel: " + channel + ", total: " + subscribedChannels);
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        if (this.debug) this.logger.info("Client is Subscribed to pattern: " + pattern + ", total: " + subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        if (this.debug) this.logger.info("Client is Unsubscribed to channel: " + channel + ", total: " + subscribedChannels);
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        if (this.debug) this.logger.info("Client is Unsubscribed to pattern: " + pattern + ", total: " + subscribedChannels);
    }

    public void stop() {
        Arrays.asList(this.channels.getEnumConstants()).forEach(channel -> this.unsubscribe(channel.getChannel()));
    }
}
