package org.vaadin.guice.bus;

import org.vaadin.guice.bus.api.ViewEvent;

class ViewMatcher extends BaseMatcher {
    public ViewMatcher() {
        super(ViewEvent.class);
    }
}
