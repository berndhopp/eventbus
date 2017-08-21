package com.google.common.eventbus;

import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import java.util.Iterator;

final class UIDispatcher extends Dispatcher {
    void dispatch(final Object event, Iterator<Subscriber> subscribers) {
        for (Subscriber subscriber : (Iterable<Subscriber>) () -> subscribers) {

            UI ui = subscriber.target instanceof Component ? ((Component) subscriber.target).getUI() : null;

            if (ui != null) {
                ui.access(() -> subscriber.dispatchEvent(event));
            } else {
                subscriber.dispatchEvent(event);
            }
        }
    }
}
