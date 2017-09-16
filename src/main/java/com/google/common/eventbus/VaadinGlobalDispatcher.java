package com.google.common.eventbus;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import org.vaadin.guice.bus.api.GlobalEvent;

import java.util.Iterator;
import java.util.function.Predicate;

final class VaadinGlobalDispatcher extends Dispatcher {
    @Override
    void dispatch(Object event, Iterator<Subscriber> subscribers) {
        GlobalEvent globalEvent = (GlobalEvent)event;

        while (subscribers.hasNext()){
            final Subscriber subscriber = subscribers.next();

            if(subscriber.target instanceof Component){
                UI ui = ((Component)subscriber.target).getUI();

                if(ui != null){

                    final Predicate<VaadinSession> vaadinSessionPredicate = globalEvent.sessionPredicate();

                    if(vaadinSessionPredicate != null){
                        final VaadinSession session = ui.getSession();

                        if(vaadinSessionPredicate.test(session)){
                            subscriber.dispatchEvent(globalEvent);

                            if(globalEvent.cancelAfterDispatch()){
                                return;
                            }
                        }
                    } else {
                        ui.access(
                            () -> subscriber.dispatchEvent(globalEvent)
                        );
                    }

                }
            } else {
                subscriber.dispatchEvent(globalEvent);
            }
        }
    }
}
