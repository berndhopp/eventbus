package org.vaadin.guice.bus;

/**
 * an interface that mimicks the guava EventBus
 * @author Bernd Hopp
 * @see com.google.common.eventbus.EventBus
 */
@SuppressWarnings("unused")
public interface EventBus {
    void register(Object o);

    void unregister(Object o);

    void post(Object o);
}
