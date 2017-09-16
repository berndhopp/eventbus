package com.google.common.eventbus;

import com.google.common.util.concurrent.MoreExecutors;

import org.vaadin.guice.bus.SessionEventBus;
import org.vaadin.guice.bus.api.SessionEvent;

final class SessionEventBusImpl extends EventBus implements SessionEventBus {

    SessionEventBusImpl(){
        super("vaadin-session", MoreExecutors.directExecutor(), Dispatcher.immediate(), LoggingHandler.INSTANCE);
    }

    @Override
    public void post(SessionEvent sessionEvent) {
        super.post(sessionEvent);
    }
}
