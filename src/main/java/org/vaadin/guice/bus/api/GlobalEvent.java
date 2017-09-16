package org.vaadin.guice.bus.api;

public interface GlobalEvent extends Event {
    default boolean isCancelled(){
        return false;
    }
}
