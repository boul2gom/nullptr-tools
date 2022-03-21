package io.github.yggdrasil80.yggtools.receiver;

import com.google.gson.Gson;

/**
 * This interface is used to receive events.
 * @param <Key> The type of the key in the receivers map entry, usually a {@link String} with a channel name.
 * @param <RawData> The raw data type of the event, usually a {@link String} or a {@link byte[]}.
 * @param <Data> The data type of the event, converted, usually with {@link Gson}, from the raw data.
 */
public abstract class Receiver<Key, RawData, Data> {

    /**
     * Converts the raw data to the data type, and calls receive(Key, RawData, Data).
     * @param key The key of the receiver.
     * @param rawData The raw data to convert.
     */
    public abstract void receive(final Key key, final RawData rawData);

    /**
     * Receives the event.
     * @param key The key of the receiver.
     * @param rawData The raw data of the event.
     * @param convertedData The converted data.
     */
    public abstract void receive(final Key key, final RawData rawData, final Data convertedData);
}
