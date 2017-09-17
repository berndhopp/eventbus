package org.vaadin.guice.bus;

import com.google.common.eventbus.CancellableEventDispatcher;
import com.google.common.eventbus.ExtendableEventBus;
import com.google.common.util.concurrent.MoreExecutors;

import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServiceInitListener;
import com.vaadin.server.VaadinSession;

import org.vaadin.guice.bus.api.GlobalEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * a global eventbus that prevents memory leaks
 */
@SuppressWarnings("unused")
class GlobalEventBusImpl extends ExtendableEventBus implements GlobalEventBus, VaadinServiceInitListener, SessionDestroyListener {

    GlobalEventBusImpl(){
        super("vaadin-global-eventbus", MoreExecutors.directExecutor(), new CancellableEventDispatcher());
    }

    private final Map<VaadinSession, Set<Object>> registeredObjectsBySession = new ConcurrentHashMap<>();

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
}
