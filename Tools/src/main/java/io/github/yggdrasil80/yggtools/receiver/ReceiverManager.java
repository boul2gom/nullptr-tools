package io.github.yggdrasil80.yggtools.receiver;

import com.google.gson.Gson;
import io.github.yggdrasil80.yggtools.data.IData;
import org.slf4j.Logger;

import java.util.*;

/**
 * The receiver manager is responsible for managing the receivers.
 * Multiple receivers can be registered for one key, and will be notified when the {@link #fireEvent(Object, Object)} is called, usually on message received.
 * @param <Key> The type of the key in the receivers map entry, usually a {@link String} with a channel name. Its optional, use {@link String} as default if you don't need it.
 * @param <RawData> The raw data type of the event, usually a {@link String} or a {@link Byte}[].
 * @param <Data> The data type of the event, converted, usually with {@link Gson}, from the raw data.
 * @param <Rcv> The type of the receiver, extending from {@link Receiver}.
 */
public abstract class ReceiverManager<Key, RawData, Data extends IData, Rcv extends Receiver<Key, RawData, ? extends Data>> {

    protected final Logger logger;
    protected final boolean debug;
    protected final Map<Key, Set<Rcv>> receivers;

    /**
     * The receiver manager constructor.
     * @param logger The logger to use.
     * @param debug <code>true</code> if the manager should log debug messages, <code>false</code> otherwise.
     */
    public ReceiverManager(final Logger logger, final boolean debug) {
        this.logger = logger;
        this.debug = debug;
        this.receivers = new HashMap<>();
    }

    /**
     * Registers a receiver.
     * @param key The key of the receiver.
     * @param receiver The receiver to register.
     * @param <R> The type of the receiver.
     */
    public <R extends Rcv> void registerReceiver(final Key key, final R receiver) {
        if (!this.receivers.containsKey(key)) {
            this.receivers.put(key, new HashSet<>());
        }
        this.receivers.get(key).add(receiver);
    }

    /**
     * Registers multiple receivers.
     * @param key The key of the receiver.
     * @param receivers The receivers to register.
     * @param <R> The type of the receiver.
     */
    @SafeVarargs
    public final <R extends Rcv> void registerReceivers(final Key key, final R... receivers) {
        Arrays.asList(receivers).forEach(receiver -> this.registerReceiver(key, receiver));
    }

    /**
     * Unregisters a receiver.
     * @param key The key of the receiver.
     * @param receiver The receiver to unregister.
     * @param <R> The type of the receiver.
     */
    public <R extends Rcv> void unregisterReceiver(final Key key, final R receiver) {
        if (this.receivers.containsKey(key)) {
            this.receivers.get(key).remove(receiver);
        }
    }

    /**
     * Unregister multiple receivers.
     * @param key The key of the receiver.
     * @param receivers The receivers to unregister.
     * @param <R> The type of the receiver.
     */
    @SafeVarargs
    public final <R extends Rcv> void unregisterReceivers(final Key key, final R... receivers) {
        Arrays.asList(receivers).forEach(receiver -> this.unregisterReceiver(key, receiver));
    }

    /**
     * Unregister all listeners associated with multiples keys.
     * @param keys The keys of the listeners to unregister.
     */
    @SafeVarargs
    public final void unregisterReceivers(final Key... keys) {
        Arrays.asList(keys).forEach(this::unregisterReceivers);
    }

    /**
     * Unregisters all listeners associated with a key.
     * @param key The key of the listeners to unregister.
     */
    public void unregisterReceivers(final Key key) {
        this.receivers.remove(key);
    }

    /**
     * Unregisters all listeners.
     */
    public void unregisterReceivers() {
        this.receivers.clear();
    }

    /**
     * Notifies all receivers associated with a key.
     * @param key The key of the receivers to notify.
     * @param data The raw data to notify.
     */
    protected void fireEvent(final Key key, final RawData data) {
        if (this.receivers.containsKey(key)) {
            this.receivers.get(key).forEach(receiver -> receiver.receive(key, data));
        }
        if (this.debug) this.logger.debug("[" + key + "] Received: " + data);
    }
}
