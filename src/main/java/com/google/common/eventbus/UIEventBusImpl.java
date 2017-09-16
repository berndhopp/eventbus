package com.google.common.eventbus;

import com.google.common.util.concurrent.MoreExecutors;

import org.vaadin.guice.bus.UIEventBus;
import org.vaadin.guice.bus.api.UIEvent;

final class UIEventBusImpl extends EventBus implements UIEventBus {

    UIEventBusImpl(){
        super("vaadin-ui", MoreExecutors.directExecutor(), Dispatcher.immediate(), LoggingHandler.INSTANCE);
    }

    @Override
    public void post(UIEvent uiEvent) {
        super.post(uiEvent);
    }
}
