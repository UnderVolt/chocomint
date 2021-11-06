package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;
import io.undervolt.api.ui.widgets.controllers.AnimationController;

public class AnimationBuilder extends Drawable{


    protected AnimationController controller;
    protected Builder builder;

    public AnimationBuilder(AnimationController controller, Builder builder) {
        this.controller = controller;
        this.builder = builder;
    }

    @Override
    public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        builder.onRun(this.controller.forward()).draw(ui, x, y, mouseX, mouseY, deltaTime);
        super.draw(ui, x, y, mouseX, mouseY, deltaTime);
    }


    public interface Builder{
        Drawable onRun(double timeFraction);
    }
}


