package org.vaadin.guice.bus;

import org.vaadin.guice.bus.api.Event;

/**
 * an interface that mimicks the guava EventBus
 *
 * @author Bernd Hopp
 * @see com.google.common.eventbus.EventBus
 */
public interface EventBus<T extends Event> {
    void register(Object o);

    void unregister(Object o);

    void post(T t);
}
