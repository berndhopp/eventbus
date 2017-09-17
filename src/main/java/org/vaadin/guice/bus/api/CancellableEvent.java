package org.vaadin.guice.bus.api;

public interface CancellableEvent extends Event{

    void cancel();
    boolean isCancelled();
}
