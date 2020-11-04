package io.undervolt.client;

import net.minecraft.client.Minecraft;

public class Client {

    private final Minecraft mc;

    public Client(final Minecraft mc) {
        this.mc = mc;
    }

    public void startClient() {
        System.out.println("Iniciado cliente.");
    }

}
