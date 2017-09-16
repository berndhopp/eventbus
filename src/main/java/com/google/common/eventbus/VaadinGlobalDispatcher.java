package com.google.common.eventbus;

import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import org.vaadin.guice.bus.api.GlobalEvent;

import java.util.Iterator;

final class VaadinGlobalDispatcher extends Dispatcher {
    @Override
    void dispatch(Object event, Iterator<Subscriber> subscribers) {
        GlobalEvent globalEvent = (GlobalEvent)event;

        while (subscribers.hasNext()){
            final Subscriber subscriber = subscribers.next();

            if(subscriber.target instanceof Component){
                UI ui = ((Component)subscriber.target).getUI();

                if(ui != null){
                    ui.access(
                        () -> {
                            if(!globalEvent.isCancelled()){
                                subscriber.dispatchEvent(globalEvent);
                            }
                        }
                    );

                    if(globalEvent.isCancelled()){
                        break;
                    }
                }
            } else {
                subscriber.dispatchEvent(globalEvent);
            }
        }
    }
}
