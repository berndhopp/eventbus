package org.vaadin.guice.bus;

public interface EventBus {
    void post(Object event);
    void register(Object listener);
}
