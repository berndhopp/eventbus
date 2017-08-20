package org.vaadin.guice.bus;

/**
 * This class serves as a means to allow VaadinSession-scope communication between objects.
 * SessionEventBus is intended for events that are of 'VaadinSession-scope' interest, like updates
 * to data that is used by multiple {@link com.vaadin.ui.UI}'s of the same {@link
 * com.vaadin.server.VaadinSession}.
 *
 * <code> {@literal @}Inject private SessionEventBus sessionEventBus;
 *
 * ... sessionEventBus.post(new DataSetInSessionScopeChangedEvent()); ...
 *
 * </code> </pre>
 *
 * @author Bernd Hopp (bernd@vaadin.com)
 */
public interface SessionEventBus extends EventBus {
}
