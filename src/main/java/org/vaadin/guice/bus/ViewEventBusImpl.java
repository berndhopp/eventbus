package org.vaadin.guice.bus;

import com.google.common.eventbus.EventBus;

import org.vaadin.guice.bus.api.ViewEvent;

final class ViewEventBusImpl extends EventBus implements ViewEventBus {

    @Override
    public void post(ViewEvent viewEvent) {
        super.post(viewEvent);
    }
}
