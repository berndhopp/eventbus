package org.vaadin.guice.bus;

import com.google.common.eventbus.EventBus;

import com.vaadin.guice.annotation.UIScope;

import org.vaadin.guice.bus.api.UIEvent;

final class UIEventBusImpl extends EventBus implements UIEventBus {

    @Override
    public void post(UIEvent uiEvent) {
        super.post(uiEvent);
    }
}
