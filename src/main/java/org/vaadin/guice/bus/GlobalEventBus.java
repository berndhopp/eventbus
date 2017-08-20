package org.vaadin.guice.bus;

import com.vaadin.server.VaadinSession;

/**
 * This class serves as a means to allow application-scope communication between objects.
 * GlobalEventBus is intended for events that are of 'global' interest, like updates to data that is
 * used by multiple UIs simultaneously. It is singleton-scoped and will release any subscribers once
 * their {@link VaadinSession} is ended in order to prevent memory leaks.
 *
 * <code> {@literal @}Inject private GlobalEventBus globalEventBus;
 *
 * ... globalEventBus.post(new DataSetOfGlobalInterestChangedEvent()); ...
 *
 * </code> </pre>
 *
 * @author Bernd Hopp (bernd@vaadin.com)
 */
public interface GlobalEventBus extends org.vaadin.guice.bus.EventBus {
}

