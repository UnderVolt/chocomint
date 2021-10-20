package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.UIView;

import java.awt.*;

public class Text extends IWidget{

    protected String text;
    protected TextStyle style;

    public Text(String text) {
        this.text = text;
        this.style = new TextStyle();
    }

    @Override
    public void draw(UIView ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        this.width = ui.fontRendererObj.getStringWidth(this.text);
        this.height = ui.fontRendererObj.FONT_HEIGHT;
        switch (this.style.align){
            case LEFT:
                ui.fontRendererObj.drawString(this.text, x, y, this.style.textColor.getRGB(), this.style.dropShadow);
                break;
            case RIGHT:
                ui.fontRendererObj.setBidiFlag(true);
                ui.fontRendererObj.drawString(this.text, x, y, this.style.textColor.getRGB(), this.style.dropShadow);
                break;
            case CENTER:
                this.width = ui.fontRendererObj.getStringWidth(this.text);
                ui.fontRendererObj.drawString(this.text, x - (this.width * 0.5f), y, this.style.textColor.getRGB(), this.style.dropShadow);
                break;
        }

    }

    public Text style(TextStyle style) {
        this.style = style;
        return this;
    }

    public static class TextStyle{
        protected boolean dropShadow = true;
        protected Color textColor = Color.white;
        protected TextAlign align = TextAlign.LEFT;

        public TextStyle setDropShadow(boolean dropShadow) {
            this.dropShadow = dropShadow;
            return this;
        }

        public TextStyle setTextColor(Color textColor) {
            this.textColor = textColor;
            return this;
        }

        public TextStyle setAlign(TextAlign align) {
            this.align = align;
            return this;
        }
    }

    public enum TextAlign{
        LEFT, CENTER, RIGHT
    }

}
