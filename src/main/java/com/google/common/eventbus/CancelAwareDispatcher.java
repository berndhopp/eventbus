package com.google.common.eventbus;

import org.vaadin.guice.bus.api.Cancellable;

import java.util.Iterator;

class CancelAwareDispatcher extends Dispatcher {
    @Override
    void dispatch(Object event, Iterator<Subscriber> subscribers) {
        final Iterable<Subscriber> subscriberIterable = () -> subscribers;

        if(event instanceof Cancellable){
            Cancellable cancellable = (Cancellable)event;

            for (Subscriber subscriber : subscriberIterable) {

                dispatch(event, subscriber);

                if(cancellable.isCancelled()){
                    break;
                }
            }
        } else {
            for (Subscriber subscriber : subscriberIterable) {
                dispatch(event, subscriber);
            }
        }
    }

    protected void dispatch(Object event, Subscriber subscriber){
        subscriber.dispatchEvent(event);
    }
}
