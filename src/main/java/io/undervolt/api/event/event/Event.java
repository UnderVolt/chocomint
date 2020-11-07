package io.undervolt.api.event.event;

import net.minecraft.client.Minecraft;

public abstract class Event {
	public final Minecraft mc = Minecraft.getMinecraft();
}
