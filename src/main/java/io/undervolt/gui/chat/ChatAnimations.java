package io.undervolt.gui.chat;

import io.undervolt.api.animation.AnimationRender;
import io.undervolt.api.animation.AnimationTimings;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ChatAnimations extends AnimationRender {
    public ChatAnimations() {
        super(250, AnimationTimings.QUAD);
    }

    public void draw(float chatHeight, float width, float height){
        this.render();

    }
}
