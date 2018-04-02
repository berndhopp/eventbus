package org.vaadin.guice.bus;

import com.google.common.eventbus.EventBus;
import com.google.inject.Singleton;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.SessionDestroyEvent;
import com.vaadin.flow.server.SessionDestroyListener;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * a singleton eventbus that prevents memory leaks
 */
@Singleton
@SuppressWarnings("unused")
public class SingletonEventBus extends EventBus implements org.vaadin.guice.bus.EventBus, VaadinServiceInitListener, SessionDestroyListener {

    private final Map<VaadinSession, Set<Object>> registeredObjectsBySession = new ConcurrentHashMap<>();

    SingletonEventBus(){
    }

    @Override
    public void register(Object object) {
        checkNotNull(object);

        VaadinSession vaadinSession = checkNotNull(VaadinSession.getCurrent());

        registeredObjectsBySession
                .computeIfAbsent(vaadinSession, s -> new HashSet<>())
                .add(object);

        super.register(object);
    }

    @Override
    public void unregister(Object object) {
        checkNotNull(object);

        VaadinSession vaadinSession = checkNotNull(VaadinSession.getCurrent());

        Set<Object> objects = registeredObjectsBySession.get(vaadinSession);

        if (objects != null) {
            objects.remove(object);
        }

        super.unregister(object);
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        VaadinService.getCurrent().addSessionDestroyListener(this);
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent e) {
        Set<Object> registeredObjects = registeredObjectsBySession.remove(e.getSession());

        if (registeredObjects != null) {
            registeredObjects.forEach(super::unregister);
        }
    }
}
