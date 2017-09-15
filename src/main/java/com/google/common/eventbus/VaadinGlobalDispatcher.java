package com.google.common.eventbus;

import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

public final class VaadinGlobalDispatcher extends CancelAwareDispatcher {
    @Override
    protected void dispatch(Object event, Subscriber subscriber) {
        UI ui = subscriber.target instanceof Component ? ((Component) subscriber.target).getUI() : null;

        if (ui != null) {
            ui.access(() -> subscriber.dispatchEvent(event));
        } else {
            subscriber.dispatchEvent(event);
        }
    }
}
