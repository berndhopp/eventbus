package org.vaadin.guice.bus;

import com.google.common.eventbus.EventBus;

import com.vaadin.guice.annotation.UIScope;

/**
 * This class serves as a means to allow UI-scope communication between objects. UIEventBus is
 * intended for events that are of 'UI-scope' interest, like updates to data that is used by only
 * the current UI.
 *
 * <code> {@literal @}Inject private UIEventBus uiIEventBus;
 *
 * ... uiIEventBus.post(new DataSetInUIScopeChangedEvent()); ...
 *
 * </code> </pre>
 *
 * @author Bernd Hopp (bernd@vaadin.com)
 */
@UIScope
public final class UIEventBus extends EventBus {
    UIEventBus() {
    }
}
