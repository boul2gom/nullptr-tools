package io.github.nullptr.tools.dispatcher.keyed;

import java.util.*;

/**
 * A dispatcher that can be used to dispatch data to multiple receivers, by key.
 * @param <Key> The type of the key used to identify receivers, usually a {@link String}.
 * @param <Receiver> The type of the receiver, usually a listener.
 * @param <Data> The type of the data that is dispatched, usually a message, an event, etc.
 */
public abstract class KeyedDispatcher<Key, Receiver, Data> {

    /**
     * The map of receivers, indexed by key.
     */
    protected final Map<Key, Set<Receiver>> receivers;

    /**
     * The keyed dispatcher constructor.
     */
    public KeyedDispatcher() {
        this.receivers = new HashMap<>();
    }

    /**
     * Registers a receiver to a key.
     * @param key The key to register the receiver to.
     * @param receiver The receiver to register.
     * @param <R> The type of the receiver.
     */
    public <R extends Receiver> void registerReceiver(final Key key, final R receiver) {
        if (!this.receivers.containsKey(key)) {
            this.receivers.put(key, new HashSet<>());
        }
        this.receivers.get(key).add(receiver);
    }

    /**
     * Registers multiple receivers.
     * @param key The key to register the receivers to.
     * @param receivers The receivers to register.
     * @param <R> The type of the receivers.
     */
    @SafeVarargs
    public final <R extends Receiver> void registerReceivers(final Key key, final R... receivers) {
        Arrays.asList(receivers).forEach(receiver -> this.registerReceiver(key, receiver));
    }

    /**
     * Unregisters a receiver.
     * @param key The key to unregister the receiver from.
     * @param receiver The receiver to unregister.
     * @param <R> The type of the receiver.
     */
    public <R extends Receiver> void unregisterReceiver(final Key key, final R receiver) {
        if (this.receivers.containsKey(key)) {
            this.receivers.get(key).remove(receiver);
        }
    }

    /**
     * Unregister multiple receivers.
     * @param key The key to unregister the receivers from.
     * @param receivers The receivers to unregister.
     * @param <R> The type of the receivers.
     */
    @SafeVarargs
    public final <R extends Receiver> void unregisterReceivers(final Key key, final R... receivers) {
        Arrays.asList(receivers).forEach(receiver -> this.unregisterReceiver(key, receiver));
    }

    /**
     * Unregisters all receivers from a key.
     * @param key The key to unregister the receivers from.
     */
    public void unregisterReceivers(final Key key) {
        this.receivers.remove(key);
    }

    /**
     * Unregisters all receivers from multiple keys.
     * @param keys The keys to unregister the receivers from.
     */
    @SafeVarargs
    public final void unregisterReceivers(final Key... keys) {
        Arrays.asList(keys).forEach(this::unregisterReceivers);
    }

    /**
     * Unregisters all receivers.
     */
    public void unregisterReceivers() {
        this.receivers.clear();
    }

    /**
     * Dispatches the data to all receivers that are registered to the key.
     * @param key The key to dispatch the data to.
     * @param data The data to dispatch.
     */
    public abstract void dispatch(final Key key, final Data data);
}
