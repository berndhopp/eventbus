package com.google.common.eventbus;

import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import java.util.Iterator;

final class UIDispatcher extends Dispatcher {
    void dispatch(final Object event, Iterator<Subscriber> subscribers) {
        while (subscribers.hasNext()) {
            final Subscriber subscriber = subscribers.next();
            if (subscriber.target instanceof Component) {
                UI ui = ((Component) subscriber.target).getUI();
                if (ui != null) {
                    ui.access(() -> subscriber.dispatchEvent(event));
                } else {
                    subscriber.dispatchEvent(event);
                }
            } else {
                subscriber.dispatchEvent(event);
            }
        }

    }
}
