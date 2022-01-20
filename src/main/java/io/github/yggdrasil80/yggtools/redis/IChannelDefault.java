package io.github.yggdrasil80.yggtools.redis;

public enum IChannelDefault implements IChannel {

    DEFAULT("default"),
    DEFAULT_PATTERN("pattern:default");

    private final String channel;

    IChannelDefault(String channel) {
        this.channel = channel;
    }

    @Override
    public String getChannel() {
        return this.channel;
    }
}
