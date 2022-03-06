package io.github.yggdrasil80.yggtools.redis;

/**
 * Default implementation of {@link IChannel}.
 */
public enum IChannelDefault implements IChannel {

    /**
     * The default channel.
     */
    DEFAULT("default");

    private final String channel;

    /**
     * The IChannelDefault constructor.
     * @param channel The channel name.
     */
    IChannelDefault(final String channel) {
        this.channel = channel;
    }

    /**
     * {@inheritDoc}
     * @return The channel name.
     */
    @Override
    public String getChannel() {
        return this.channel;
    }
}
