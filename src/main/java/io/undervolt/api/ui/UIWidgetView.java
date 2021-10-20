package io.undervolt.api.ui;

import io.undervolt.api.ui.widgets.Drawable;
import java.io.IOException;

public abstract class UIWidgetView extends Screen {

    public void initView(){}
    public abstract Drawable build();
    private Drawable baseInstance;

    @Override
    public void load() {
        this.baseInstance = this.build();
        this.baseInstance.init();
    }

    public void updateVars(WidgetState ws){
        ws.onRun();
        this.baseInstance = this.build();
        this.baseInstance.init();
    }

    @Override
    public void update(int mouseX, int mouseY, float deltaTime) {
        if(this.minecraft.theWorld == null) this.drawDefaultBackground();
        this.baseInstance.draw(this, 0, 0, mouseX, mouseY, deltaTime);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.baseInstance.onPress(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.baseInstance.onRelease(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        this.baseInstance.onDrag(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    public interface WidgetState{
        void onRun();
    }
}
