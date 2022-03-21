package io.github.yggdrasil80.yggtools.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.yggdrasil80.yggtools.handling.Receiver;

/**
 * Represents a receiver for messages, sent by any network tool like Redis, Netty, Kafka, etc.
 * @param <Data> The type of the data that is received by the receiver.
 */
public abstract class MessageReceiver<Data extends IMessage> extends Receiver<String, String, Data> {

    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    private final Class<Data> clazz;

    /**
     * The MessageReceiver constructor.
     * @param clazz The class of the data that is received by the receiver.
     */
    public MessageReceiver(Class<Data> clazz) {
        this.clazz = clazz;
    }

    /**
     * {@inheritDoc}
     * @param channel The channel of the message.
     * @param rawMessage The raw message.
     */
    @Override
    public void receive(final String channel, final String rawMessage) {
        if (rawMessage.equals("{}")) {
            this.receive(channel, rawMessage, null);
            return;
        }
        this.receive(channel, rawMessage, GSON.fromJson(rawMessage, this.clazz));
    }
}
