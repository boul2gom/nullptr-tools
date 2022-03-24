package io.github.yggdrasil80.yggtools.receiver.basic;

import com.google.gson.Gson;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The receiver manager is responsible for managing the receivers.
 * @param <RawData> The raw data type of the event, usually a {@link String} or a {@link Byte}[].
 * @param <Data> The data type of the event, converted, usually with {@link Gson}, from the raw data.
 * @param <Rcv> The type of the receiver, extending from {@link BasicReceiver}
 */
public abstract class BasicReceiverManager<RawData, Data, Rcv extends BasicReceiver<RawData, ? extends Data>> {

    /**
     * The logger.
     */
    protected final Logger logger;
    /**
     * The debug mode flag.
     */
    protected final boolean debug;
    /**
     * The receivers.
     */
    protected final Set<Rcv> receivers;

    /**
     * The receiver manager constructor.
     * @param logger The logger to use.
     * @param debug <code>true</code> if the manager should log debug messages, <code>false</code> otherwise.
     */
    public BasicReceiverManager(final Logger logger, final boolean debug) {
        this.logger = logger;
        this.debug = debug;
        this.receivers = new HashSet<>();
    }

    /**
     * Registers a receiver.
     * @param receiver The receiver to register.
     * @param <R> The type of the receiver.
     */
    public <R extends Rcv> void registerReceiver(final R receiver) {
        this.receivers.add(receiver);
    }

    /**
     * Registers multiple receivers.
     * @param receivers The receivers to register.
     * @param <R> The type of the receivers.
     */
    @SafeVarargs
    public final <R extends Rcv> void registerReceivers(final R... receivers) {
        Arrays.asList(receivers).forEach(this::registerReceiver);
    }

    /**
     * Unregisters a receiver.
     * @param receiver The receiver to unregister.
     * @param <R> The type of the receiver.
     */
    public <R extends Rcv> void unregisterReceiver(final R receiver) {
        this.receivers.remove(receiver);
    }

    /**
     * Unregisters multiple receivers.
     * @param receivers The receivers to unregister.
     * @param <R> The type of the receivers.
     */
    @SafeVarargs
    public final <R extends Rcv> void unregisterReceivers(final R... receivers) {
        Arrays.asList(receivers).forEach(this::unregisterReceiver);
    }

    /**
     * Unregisters all receivers.
     */
    public void unregisterReceivers() {
        this.receivers.clear();
    }

    /**
     * Notifies all receivers of a received event.
     * @param data The received event to notify.
     */
    protected void fireEvent(final RawData data) {
        this.receivers.forEach(receiver -> receiver.receive(data));
        if (this.debug) this.logger.debug("Received: " + data);
    }
}
