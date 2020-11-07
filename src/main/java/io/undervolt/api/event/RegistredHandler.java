package io.undervolt.api.event;

import io.undervolt.api.event.event.Cancellable;
import io.undervolt.api.event.event.Event;;
import io.undervolt.api.event.handler.EventHandler;
import io.undervolt.api.event.handler.EventPriority;
import io.undervolt.api.event.handler.Listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RegistredHandler {

	protected Listener listener;
	protected EventPriority pr;
	protected Method m;

	public RegistredHandler(Listener l, Method m) {
		this.listener = l;
		this.m = m;
		EventHandler h = m.getAnnotation(EventHandler.class);

		if (h == null)return;

		if (m.getParameterTypes().length != 1)return;

		pr = h.priority();
	}

	public void callEvent(Event e){
		if (!m.getParameterTypes()[0].getSimpleName().equals(e.getClass().getSimpleName()))return;

		if(e instanceof Cancellable){
			EventHandler h = m.getAnnotation(EventHandler.class);
			if(((Cancellable)e).isCancelled() && !h.ignoreCanceled())return;
		}
		try {
			m.invoke(listener, e);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Error while trying to invoke event " + e.getClass().getSimpleName());
			e1.printStackTrace();
		}
	}

	public Listener getListener() {
		return listener;
	}

	public EventPriority getPriority() {
		return pr;
	}

}
