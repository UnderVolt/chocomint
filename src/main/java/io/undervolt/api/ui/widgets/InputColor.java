package io.undervolt.api.ui.widgets;

import com.google.common.collect.Lists;
import io.undervolt.api.ui.Screen;
import io.undervolt.api.ui.widgets.controllers.ColorSelectController;
import io.undervolt.bridge.GameBridge;
import io.undervolt.utils.OverlayUtils;
import io.undervolt.utils.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class InputColor extends Drawable implements IgnoreOverflow {

    protected ColorSelectController controller;
    protected Color indicatorColor = new Colour(50);
    protected Color backgroundColor = Color.white;
    protected Color buttonDefaultColor = new Colour(0, 80);
    protected Color buttonHoverColor = new Colour(0, 140);
    protected Color separatorColor = new Colour(20, 50);

    private ArrayList<ICBtn> btns;
    private int currentH = -1;
    private int currentO = -1;
    private int currentS = -1;
    private int currentL = -1;
    private Color currentColor;
    private boolean alreadyInit = false;

    public InputColor setIndicatorColor(Color indicatorColor) {
        this.indicatorColor = indicatorColor;
        return this;
    }

    public InputColor setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public InputColor setButtonDefaultColor(Color buttonDefaultColor) {
        this.buttonDefaultColor = buttonDefaultColor;
        return this;
    }

    public InputColor setButtonHoverColor(Color buttonHoverColor) {
        this.buttonHoverColor = buttonHoverColor;
        return this;
    }

    public InputColor setSeparatorColor(Color separatorColor) {
        this.separatorColor = separatorColor;
        return this;
    }

    public InputColor(ColorSelectController controller) {
        this.controller = controller;
        this.width = 100;
        this.height = 129;
        this.btns  = Lists.newArrayList();
    }

    @Override
    public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {

        if(!this.alreadyInit){
            this.btns.add(new ICBtn("O", this.buttonDefaultColor, this.buttonHoverColor, (int) (x + this.width - 17),  (int) (y + this.height - 17), 15, 15, (c, mX, mY) -> {
                this.controller.toggleContainer();
                this.controller.setColor(this.currentColor);
            }));
            this.btns.add(new ICBtn("X", this.buttonDefaultColor, this.buttonHoverColor, (int) (x + this.width - 35),  (int) (y + this.height - 17), 15, 15, (c, mX, mY) -> {
                this.controller.toggleContainer();
            }));
            if(this.controller.drawRainbowButton()){
                this.btns.add(new ICBtn("R" , this.buttonDefaultColor, this.buttonHoverColor, (int) (x + this.width - 53),  (int) (y + this.height - 17), 15, 15, (c, mX, mY) -> {
                    this.controller.toggleRainbow();
                }));
            }
            this.alreadyInit = true;
        }

        if(this.controller.drawContainer()){
            float tmpWidth = 4;
            float tmpHeight = 68;
            float sliderX = ((x + (this.width / 2) - ((this.width * 0.5f) / 2)));
            float maxAlpha = 255;

            /* Set Defaults */

            if(this.controller.getColor() != null && this.currentColor != this.controller.getColor()){
                float[] hsb = new float[3];

                this.currentColor = this.controller.getColor();

                Color.RGBtoHSB(this.controller.getColor().getRed(), this.controller.getColor().getGreen(), this.controller.getColor().getBlue(), hsb);

                if(this.currentH == -1) this.currentH = (int) ((sliderX + 15) + (hsb[0] *  (this.width * 0.5f)));
                if(this.currentS == -1) this.currentS = (int) (x + (hsb[1] * this.width));
                if(this.currentL == -1) this.currentL = (int) (y + tmpHeight - (hsb[2] * tmpHeight));
                if(this.currentO == -1) this.currentO = (int) ((sliderX + 15) + (this.controller.getColor().getAlpha() / 255.0f) * (this.width * 0.5f));
            }else{
                if(this.currentH == -1) this.currentH = (int) (sliderX + 15);
                if(this.currentS == -1) this.currentS = (int) (x + this.width);
                if(this.currentL == -1) this.currentL = y;
                if(this.currentO == -1) this.currentO = (int) (sliderX + 15);
            }

            /* Vars & Background */
            this.currentColor = Colour.getHSBAColor(((this.width * 0.5f) - (sliderX + 15 - this.currentH)) / (this.width * 0.5f), (this.currentS - x) / this.width, (tmpHeight + y - this.currentL) / tmpHeight, (1 + ((sliderX + 15 - this.currentO) / (this.width * 0.5f))) * maxAlpha);
            GameBridge.getChocomint().getRenderUtils().drawRect(x, y, this.width, this.height, this.backgroundColor);

            /* Rainbow */
            if(this.controller.isRainbow()){
                float rv = ((System.currentTimeMillis()) % 1000) / 1000.0F;
                this.currentH = (int) (sliderX + 15 + ((this.width / 2) * rv));
                this.controller.setColor(this.currentColor);
            }

            /* Color Picker */

            for (float r = 0; r < this.width; r += tmpWidth){
                for (float r1 = 0; r1 < tmpHeight; r1 += tmpWidth) {
                    Color c = Color.getHSBColor(((this.width * 0.5f) - (sliderX + 15 - this.currentH)) / (this.width * 0.5f), (r / this.width), (r1 / tmpHeight));
                    GameBridge.getChocomint().getRenderUtils().drawRect(x + r, (y + (tmpHeight - r1)) - tmpWidth, tmpWidth, tmpWidth, c.getRGB());
                }
            }
            /* HUE Slider */

            for (int r = 0; r < (this.width * 0.5f); r++){
                GameBridge.getChocomint().getRenderUtils().drawRect( sliderX + 15 + r, y + tmpHeight + 12, 1, 5, Color.getHSBColor(r / (this.width * 0.5f),0.75F, 1.0F).getRGB());
            }

            /* Opacity Slider */

            GL11.glColor3f(1, 1, 1);
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("volt/ui/colorpicker/TransparencyBGH.png"));
            Gui.drawModalRectWithCustomSizedTexture(sliderX + 15, y + tmpHeight + 21, 0, 0, (int) (this.width * 0.5f), 5, (int) (this.width * 0.5f), 5);

            for (int r = 0; r < (this.width * 0.5f); r += 2){
                int alpha = (int) ((((this.width * 0.5f) - r) / (this.width * 0.5f)) * maxAlpha);
                GameBridge.getChocomint().getRenderUtils().drawRect( sliderX + 15 + r, y + tmpHeight + 21, 1, 5, new Color(this.currentColor.getRed(), this.currentColor.getBlue(), this.currentColor.getGreen(), alpha));
            }

            /* Color Preview */

            GameBridge.getChocomint().getRenderUtils().drawRect(x + 11, y + tmpHeight + 11, 20, 20, new Colour(0, 30));

            GL11.glColor3f(1, 1, 1);
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("volt/ui/colorpicker/TransparencyBG.png"));
            Gui.drawModalRectWithCustomSizedTexture(x + 10, y + tmpHeight + 10, 0, 0, 20, 20, 20, 20);

            GameBridge.getChocomint().getRenderUtils().drawRect(x + 10, y + tmpHeight + 10, 20, 20, this.currentColor.getRGB());

            /* Indicators */

            OverlayUtils.drawCircle(this.currentS, this.currentL, 2,  this.indicatorColor.getRGB());
            GL11.glPushMatrix();
            GL11.glTranslatef(0, -0.5f, 0);
            OverlayUtils.drawCircle(this.currentH, (int) (y + tmpHeight + 15), 3, new Colour(50).getRGB());
            OverlayUtils.drawCircle(this.currentO, (int) (y + tmpHeight + 24), 3, new Colour(50).getRGB());
            GL11.glPopMatrix();

            /* Color Handlers */

            if(mouseX >= sliderX + 15 && mouseY > y + tmpHeight + 12 && mouseX <= sliderX + 15 + (this.width * 0.5f) && mouseY < y + tmpHeight + 12 + 5 && Mouse.isButtonDown(0)){
                this.currentH = mouseX;
            }

            if(mouseX >= sliderX + 15 && mouseY > y + tmpHeight + 21 && mouseX <= sliderX + 15 + (this.width * 0.5f) && mouseY < y + tmpHeight + 21 + 5 && Mouse.isButtonDown(0)){
                this.currentO = mouseX;
            }

            if(mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + tmpHeight && Mouse.isButtonDown(0)){
                this.currentS = mouseX;
                this.currentL = mouseY;
            }

            /* Design */

            GameBridge.getChocomint().getRenderUtils().drawRect(x, y + this.height - 20, this.getWidth(), 0.5f, this.separatorColor);

            this.btns.forEach(bt -> bt.drawBtn(ui, mouseX, mouseY, deltaTime));
        }
        super.draw(ui, x, y, mouseX, mouseY, deltaTime);
    }

    @Override
    public void onPress(int x, int y, int button) {
        if(this.controller.drawContainer()) this.btns.forEach(bt -> bt.clickBtn(this, x, y));
    }

    public static class ICBtn{

        private String icon;
        private Color defaultColor;
        private Color hoverColor;
        private int x;
        private int y;
        private int w;
        private int h;
        private boolean hovered = false;
        private ActionListener listener;
        private Color currentColor;
        private ResourceLocation loc;

        public ICBtn(String icon, Color defaultColor, Color hoverColor, int x, int y, int w, int h, ActionListener listener) {
            this.icon = icon;
            this.defaultColor = defaultColor;
            this.hoverColor = hoverColor;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.currentColor = this.defaultColor;
            this.listener = listener;
            this.loc = null;
        }

        public ICBtn(ResourceLocation icon, Color defaultColor, Color hoverColor, int x, int y, int w, int h, ActionListener listener) {
            this.defaultColor = defaultColor;
            this.hoverColor = hoverColor;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.currentColor = this.defaultColor;
            this.listener = listener;
            this.loc = icon;
        }

        public void drawBtn(Screen ui, int mouseX, int mouseY, float deltaTime){
            this.hovered = mouseX > this.x && mouseY > this.y && mouseX < this.x + this.w && mouseY < this.y + this.h;

            this.currentColor = Colour.Lerp(this.defaultColor, this.hoverColor, deltaTime * 1);

            GameBridge.getChocomint().getRenderUtils().drawRect(this.x, this.y, this.w, this.h, this.hovered ? this.hoverColor : this.defaultColor);
            if(this.icon != null)
                ui.fontRendererObj.drawStringWithShadow(this.icon, this.x + (this.w * 0.5f) - (ui.fontRendererObj.getStringWidth(this.icon) * 0.5f), this.y + (this.h * 0.5f) - (ui.fontRendererObj.FONT_HEIGHT * 0.5f), -1);
            if(this.loc != null){
                Minecraft.getMinecraft().getTextureManager().bindTexture(this.loc);
                Gui.drawModalRectWithCustomSizedTexture(this.x + 4.5f, this.y + 4.5f, 0, 0, 9, 9, 9, 9);
            }

        }

        public void clickBtn(InputColor ic, int mouseX, int mouseY){
            if(this.hovered){
                this.listener.onClick(ic, mouseX, mouseY);
            }
        }

        public interface ActionListener{
            void onClick(InputColor c, int mouseX, int mouseY);
        }

    }
}
