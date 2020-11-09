package io.undervolt.api.event.event;

public interface Cancellable {
	public boolean isCancelled();

	public void setCancelled(boolean cancelled);
}
