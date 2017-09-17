package com.google.common.eventbus;

import org.vaadin.guice.bus.api.CancellableEvent;

import java.util.Iterator;

public class CancellableEventDispatcher extends Dispatcher{

    @Override
    void dispatch(Object event, Iterator<Subscriber> subscribers) {

        CancellableEvent cancellableEvent = (CancellableEvent)event;

        while (subscribers.hasNext()){
            final Subscriber subscriber = subscribers.next();

            subscriber.dispatchEvent(cancellableEvent);

            if(cancellableEvent.isCancelled()){
                return;
            }
        }
    }
}
