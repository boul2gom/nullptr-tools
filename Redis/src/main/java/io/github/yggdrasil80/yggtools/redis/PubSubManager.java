package io.github.yggdrasil80.yggtools.redis;

import io.github.yggdrasil80.yggtools.logger.Logger;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;

/**
 * The PubSubManager class used to manage the redis pubsub.
 */
public final class PubSubManager extends JedisPubSub {

    private final RedisManager redis;
    private final Class<? extends IChannel> channels;
    private final Class<? extends IPattern> patterns;

    private final Logger logger;
    private final boolean debug;

    /**
     * The PubSubManager constructor.
     * @param redis The {@link RedisManager} instance.
     * @param channels An Enum implementing of {@link IChannel} and containing the Channels list.
     * @param patterns An Enum implementing of {@link IPattern} and containing the Patterns list.
     * @param logger The {@link Logger} instance.
     * @param debug <code>true</code> if the debug mode is enabled, <code>false</code> otherwise.
     */
    public PubSubManager(final RedisManager redis, final Class<? extends IChannel> channels, final Class<? extends IPattern> patterns, final Logger logger, final boolean debug) {
        this.redis = redis;
        this.channels = channels;
        this.patterns = patterns;
        this.logger = logger;
        this.debug = debug;
    }

    /**
     * Start the PubSubManager, connecting to Redis and subscribe Channels and Patterns.
     */
    public void start() {
        Arrays.asList(this.channels.getEnumConstants()).forEach(channel -> System.out.println("Subscribed to channel: " + channel.getChannel()));
        this.redis.execute(jedis -> {
            Arrays.asList(this.channels.getEnumConstants()).forEach(channel -> {
                if (channel != null)
                    jedis.subscribe(this, channel.getChannel());
            });
            Arrays.asList(this.patterns.getEnumConstants()).forEach(pattern -> {
                if (pattern != null)
                    jedis.psubscribe(this, pattern.getPattern());
            });
        });
    }

    /**
     * Publish a message to a channel.
     * @param channel The channel to publish on.
     * @param message The message to publish.
     * @param <T> The type of the channel.
     */
    public <T extends IChannel> void publish(final T channel, final String message) {
        this.redis.execute(jedis -> jedis.publish(channel.getChannel(), message));
    }

    /**
     * Publish a message to a pattern.
     * @param pattern The pattern to publish on.
     * @param message The message to publish.
     * @param <T> The type of the pattern.
     */
    public <T extends IPattern> void publish(final T pattern, final String message) {
        this.redis.execute(jedis -> jedis.publish(pattern.getPattern(), message));
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

    /**
     * Shutdown the PubSubManager, disconnecting from Redis and unsubscribing Channels and Patterns.
     */
    public void stop() {
        Arrays.asList(this.channels.getEnumConstants()).forEach(channel -> this.unsubscribe(channel.getChannel()));
        Arrays.asList(this.patterns.getEnumConstants()).forEach(pattern -> this.punsubscribe(pattern.getPattern()));
    }
}
