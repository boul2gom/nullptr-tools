package io.github.nullptr.tools.receiver.basic;

/**
 * Base class for a receiver.
 * @param <RawData> The type of the raw data.
 * @param <Data> The type of the data.
 */
public abstract class BasicReceiver<RawData, Data> {

    /**
     * Receive the raw data and convert it to the data type.
     * @param rawData The raw data.
     */
    public abstract void receive(final RawData rawData);

    /**
     * Receive the raw data and the data type.
     * @param rawData The raw data.
     * @param convertedData The converted data.
     */
    public abstract void receive(final RawData rawData, final Data convertedData);
}
