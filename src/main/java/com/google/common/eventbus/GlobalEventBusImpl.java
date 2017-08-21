package com.google.common.eventbus;

import com.google.common.util.concurrent.MoreExecutors;

import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;

import org.vaadin.guice.bus.GlobalEventBus;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * a global eventbus that dispatches to the correct ui and prevents
 * memory leaks
 */
@SuppressWarnings("unused")
class GlobalEventBusImpl extends EventBus implements GlobalEventBus, SessionDestroyListener {

    private final Map<VaadinSession, Set<Object>> registeredObjectsBySession = new ConcurrentHashMap<>();

    GlobalEventBusImpl() {
        super("default", MoreExecutors.directExecutor(), new UIDispatcher(), LoggingHandler.INSTANCE);
        VaadinService.getCurrent().addSessionDestroyListener(this);
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
    public void sessionDestroy(SessionDestroyEvent e) {
        Set<Object> registeredObjects = registeredObjectsBySession.remove(e.getSession());

        if (registeredObjects != null) {
            registeredObjects.forEach(super::unregister);
        }
    }
}
