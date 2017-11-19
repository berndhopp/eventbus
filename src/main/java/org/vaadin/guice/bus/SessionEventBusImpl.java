package org.vaadin.guice.bus;

import com.google.common.eventbus.EventBus;
import org.vaadin.guice.bus.api.SessionEvent;

final class SessionEventBusImpl extends EventBus implements SessionEventBus {

    @Override
    public void post(SessionEvent sessionEvent) {
        super.post(sessionEvent);
    }
}
