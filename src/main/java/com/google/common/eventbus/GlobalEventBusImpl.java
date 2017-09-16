package com.google.common.eventbus;

import com.google.common.util.concurrent.MoreExecutors;

import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServiceInitListener;
import com.vaadin.server.VaadinSession;

import org.vaadin.guice.bus.GlobalEventBus;
import org.vaadin.guice.bus.api.GlobalEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * a global eventbus that dispatches to the correct ui and prevents memory leaks
 */
@SuppressWarnings("unused")
class GlobalEventBusImpl extends EventBus implements GlobalEventBus, VaadinServiceInitListener, SessionDestroyListener {

    private final Map<VaadinSession, Set<Object>> registeredObjectsBySession = new ConcurrentHashMap<>();

    GlobalEventBusImpl() {
        super("vaadin-global", MoreExecutors.directExecutor(), new VaadinGlobalDispatcher(), LoggingHandler.INSTANCE);
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
    public void post(GlobalEvent globalEvent) {
        super.post(globalEvent);
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

    static class EventWrapper{
        final GlobalEvent globalEvent;
        final Predicate<VaadinSession> sessionPredicate;
        final boolean cancelAfterDisposal;

        EventWrapper(GlobalEvent globalEvent, Predicate<VaadinSession> sessionPredicate, boolean cancelAfterDisposal) {
            this.globalEvent = globalEvent;
            this.sessionPredicate = sessionPredicate;
            this.cancelAfterDisposal = cancelAfterDisposal;
        }
    }
}
