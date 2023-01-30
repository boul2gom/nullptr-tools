package io.github.nullptr.tools.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.nullptr.tools.receiver.keyed.KeyedReceiver;

/**
 * Represents a receiver for messages, sent by any network tool like Redis, Netty, Kafka, etc.
 * @param <Data> The type of the data that is received by the receiver.
 */
public abstract class MessageReceiver<Data extends IMessage> extends KeyedReceiver<String, String, Data> {

    /**
     * The Gson instance used to deserialize the received data.
     */
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    /**
     * The class of the data that is received by the receiver.
     */
    private final Class<Data> clazz;

    /**
     * The MessageReceiver constructor.
     * @param clazz The class of the data that is received by the receiver.
     */
    public MessageReceiver(Class<Data> clazz) {
        this.clazz = clazz;
    }

    /**
     * Receives a message.
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
