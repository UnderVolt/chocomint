package io.undervolt.gui.menu;

import io.undervolt.api.animation.AnimationRender;
import io.undervolt.api.animation.AnimationTimings;

public class MenuAnimations extends AnimationRender {
    public MenuAnimations() {
        super(300);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        this.render();
    }
}
