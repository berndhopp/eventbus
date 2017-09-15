package org.vaadin.guice.bus;

import org.vaadin.guice.bus.api.GlobalEvent;

class GlobalMatcher extends BaseMatcher {
    public GlobalMatcher() {
        super(GlobalEvent.class);
    }
}
