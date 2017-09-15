package org.vaadin.guice.bus;

import org.vaadin.guice.bus.api.SessionEvent;

class SessionMatcher extends BaseMatcher {
    public SessionMatcher() {
        super(SessionEvent.class);
    }
}
