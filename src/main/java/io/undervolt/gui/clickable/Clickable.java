package io.undervolt.gui.clickable;

import io.undervolt.gui.FloatingLabel;
import net.minecraft.client.gui.Gui;

import java.util.function.Consumer;

public class Clickable extends Gui {
    public int x, y, width, height;
    public Consumer consumer;

    public Clickable(int x, int y, int width, int height, Consumer consumer) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.consumer = consumer;
    }

    public void draw(int mouseX, int mouseY) {}
    public void click(int mouseX, int mouseY, int mouseButton) {
        if(mouseX > x && mouseY > y && mouseX < x + width && mouseY < y + height) {
            consumer.accept(0);
        }
    }

    public Clickable appendLabel(FloatingLabel floatingLabel, int mouseX, int mouseY) {
        if(mouseX > x && mouseY > y && mouseX < x + width && mouseY < y + height) {
            floatingLabel.drawLabel(mouseX, mouseY);
        }
        return this;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
