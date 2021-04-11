package io.undervolt.gui;

import net.minecraft.client.gui.Gui;

import java.util.function.Consumer;

public class Clickable extends Gui {
    
    private Consumer<Clickable> consumer;
    private int x, y;
    
    public void draw() {}
    
}
