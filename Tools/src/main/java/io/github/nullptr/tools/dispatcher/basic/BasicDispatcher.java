package io.github.nullptr.tools.dispatcher.basic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Basic dispatcher is responsible for dispatching data to the registered receivers.
 * @param <Receiver> Type of the receivers, usually a listener.
 * @param <Data> Type of the data, usually a message, an event, etc.
 */
public abstract class BasicDispatcher<Receiver, Data> {

    /**
     * List of receivers.
     */
    protected final Set<Receiver> receivers;

    /**
     * The basic dispatcher constructor.
     */
    public BasicDispatcher() {
        this.receivers = new HashSet<>();
    }

    /**
     * Registers the receiver.
     * @param receiver The receiver to register.
     * @param <R> Type of the receiver.
     */
    public <R extends Receiver> void registerReceiver(final R receiver) {
        this.receivers.add(receiver);
    }

    /**
     * Registers multiple receivers.
     * @param receivers The receivers to register.
     * @param <R> Type of the receivers.
     */
    @SafeVarargs
    public final <R extends Receiver> void registerReceivers(final R... receivers) {
        Arrays.asList(receivers).forEach(this::registerReceiver);
    }

    /**
     * Unregisters the receiver.
     * @param receiver The receiver to unregister.
     * @param <R> Type of the receiver.
     */
    public <R extends Receiver> void unregisterReceiver(final R receiver) {
        this.receivers.remove(receiver);
    }

    /**
     * Unregisters multiple receivers.
     * @param receivers The receivers to unregister.
     * @param <R> Type of the receivers.
     */
    @SafeVarargs
    public final <R extends Receiver> void unregisterReceivers(final R... receivers) {
        Arrays.asList(receivers).forEach(this::unregisterReceiver);
    }

    /**
     * Unregisters all the receivers.
     */
    public void unregisterReceivers() {
        this.receivers.clear();
    }

    /**
     * Dispatches the data to the registered receivers.
     * @param data The data to dispatch.
     */
    public abstract void dispatch(final Data data);
}
