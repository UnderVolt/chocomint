package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Row extends Drawable {

    protected List<Drawable> children;
    protected AxisAlignment mainAxis = AxisAlignment.START;
    protected AxisAlignment crossAxis = AxisAlignment.START;

    public Row(Drawable... childs){
        this.children = Arrays.asList(childs);
        this.children.forEach(c -> c.parent = this);
    }

    public Row mainAxisAlign(AxisAlignment mainAxis) {
        this.mainAxis = mainAxis;
        return this;
    }

    public Row crossAxisAlign(AxisAlignment crossAxis) {
        this.crossAxis = crossAxis;
        return this;
    }

    @Override
    public void load() {
        this.children.forEach(Drawable::load);
    }

    @Override
    public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        int width = 0;
        int childWidth = 0;
        this.height = (int) this.children.stream().sorted(Comparator.comparing(Drawable::getWidth)).collect(Collectors.toList()).get(0).getHeight();
        for (Drawable child : this.children) {
            childWidth = (int) (childWidth + child.width);
        }

        this.width = childWidth;
        //this.height = this.parent.height;

        for (Drawable child : this.children) {
            if(mainAxis.equals(AxisAlignment.SPACE_BETWEEN)){
                int baswW = (int) (this.parent.width / this.children.size());

                System.out.println(this.parent.height * crossAxis.getYModifier());

                child.draw(ui, (int) ((x + width) + (baswW * 0.5) - (child.width * 0.5)), (int) (y + (this.parent.height * crossAxis.getYModifier()) - (child.getHeight() * crossAxis.getYModifier())), mouseX, mouseY, deltaTime);
                width += baswW;
            }else{
                child.draw(ui, (int) ((x + width) + (this.parent.width * mainAxis.getXModifier()) - (childWidth * mainAxis.getXModifier())), (int) (y + (this.parent.height * crossAxis.getYModifier()) - (child.getHeight() * crossAxis.getYModifier())), mouseX, mouseY, deltaTime);
                width = (int) (width + child.width);
            }
        }
        super.draw(ui, x, y, mouseX, mouseY, deltaTime);
    }

    @Override
    public void onPress(int x, int y, int button) {
        this.children.forEach(w -> w.onPress(x, y, button));
    }

    @Override
    public void onRelease(int x, int y, int button) {
        this.children.forEach(w -> w.onRelease(x, y, button));
    }

    @Override
    public void onDrag(int x, int y, int button, long timeSinceLastClick) {
        this.children.forEach(w -> w.onDrag(x, y, button, timeSinceLastClick));
    }
}
