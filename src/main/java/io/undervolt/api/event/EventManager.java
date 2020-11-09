package io.undervolt.api.event;


import io.undervolt.api.event.event.Cancellable;
import io.undervolt.api.event.event.Event;;
import io.undervolt.api.event.handler.EventHandler;
import io.undervolt.api.event.handler.EventPriority;
import io.undervolt.api.event.handler.Listener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventManager {

	protected List<RegistredHandler> handlers;

	public EventManager() {
		handlers = new ArrayList<>();
	}

	public void registerEvents(Listener l){
		for(Method m : l.getClass().getDeclaredMethods()){
			EventHandler h = m.getAnnotation(EventHandler.class);
			if(h == null)continue;
			handlers.add(new RegistredHandler(l,m));
		}
		
	}

	public void unregisterEvents(Listener l){
		if(this.handlers.contains(l)){
			this.handlers.remove(l);
		}
	}

	public boolean callEvent(Event e){
		boolean cancellable = (e instanceof Cancellable);
		boolean canceled = cancellable && ((Cancellable)e).isCancelled();
		for(int i = 0; i < EventPriority.values().length; i++){
			for(int index = 0; index < handlers.size(); index++){
				RegistredHandler r = handlers.get(index);
				if(r.getPriority().equals(EventPriority.values()[i])){
					r.callEvent(e);
				}
			}
		}
		return (cancellable ? canceled : false);
	}

}
