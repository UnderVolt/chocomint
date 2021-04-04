package io.undervolt.api.animation;

import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public abstract class AnimationScreen extends GuiScreen {

    private List<AnimationRender> animationsQueue = new ArrayList<>();

    @Override
    public void initGui() {
        this.animationsQueue.forEach(AnimationRender::init);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.animationsQueue.forEach(AnimationRender::render);
    }

    public void addAnimation(AnimationRender render){
        animationsQueue.add(render);
    }
}
