package org.vaadin.guice.bus;

import org.vaadin.guice.bus.api.UIEvent;

class UIMatcher extends BaseMatcher {
    public UIMatcher() {
        super(UIEvent.class);
    }
}
