package io.github.yggdrasil80.yggtools.events;

import io.github.yggdrasil80.yggtools.dispatcher.basic.BasicDispatcher;
import io.github.yggdrasil80.yggtools.events.listener.EventHandler;
import io.github.yggdrasil80.yggtools.events.listener.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Class used to manage events.
 */
public class EventManager extends BasicDispatcher<Listener, Event> {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EventManager.class);

    /**
     * Call and dispatch an event to all listeners.
     * @param event The event to dispatch.
     */
    public void callEvent(Event event) {
        this.dispatch(event);
    }

    /**
     * Dispatch an event to all listeners.
     * @param event The data to dispatch.
     */
    @Override
    public void dispatch(Event event) {
        this.receivers.forEach(receiver -> {
            for (Method method : receiver.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(EventHandler.class)) {
                    try {
                        method.invoke(receiver, event);
                    } catch (Exception e) {
                        LOGGER.error("An error has occurred while calling event handler method", e);
                    }
                }
            }
        });
    }
}