package io.github.yggdrasil80.yggtools.redis;

public enum IChannelDefault implements IChannel {

    DEFAULT("default");

    private final String channel;

    IChannelDefault(final String channel) {
        this.channel = channel;
    }

    @Override
    public String getChannel() {
        return this.channel;
    }
}
