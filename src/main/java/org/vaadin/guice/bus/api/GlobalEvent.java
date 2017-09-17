package org.vaadin.guice.bus.api;

public abstract class GlobalEvent implements CancellableEvent {

    private boolean isCancelled;

    public void cancel(){
        isCancelled = true;
    }

    public boolean isCancelled(){
        return isCancelled;
    }
}
