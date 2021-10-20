package io.undervolt.api.ui.widgets;
import io.undervolt.api.ui.UIView;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Column extends IWidget {

    protected List<IWidget> children;
    protected AxisAlignment mainAxis = AxisAlignment.START;
    protected AxisAlignment crossAxis = AxisAlignment.START;

    public Column(IWidget... childs){
        this.children = Arrays.asList(childs);
        this.children.forEach(c -> c.parent = this);
    }

    public Column mainAxisAlign(AxisAlignment mainAxis) {
        this.mainAxis = mainAxis;
        return this;
    }

    public Column crossAxisAlign(AxisAlignment crossAxis) {
        this.crossAxis = crossAxis;
        return this;
    }

    @Override
    public void init() {
        this.children.forEach(c -> {
            c.parent = this;
            c.init();
        });
    }

    @Override
    public void draw(UIView ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        int height = 0;
        int childHeight = 0;
        int width = (int) this.children.stream().sorted(Comparator.comparing(IWidget::getWidth)).collect(Collectors.toList()).get(0).getWidth();
        for (IWidget child : this.children) {
            childHeight = (int) (childHeight + child.height);
        }

        this.width = this.parent.width;
        this.height = childHeight;

        for (IWidget child : this.children) {
            if(mainAxis.equals(AxisAlignment.SPACE_BETWEEN)){
                child.draw(ui, (int) (this.parent.width * crossAxis.getXModifier() - (x + width * crossAxis.getXModifier())), (int) (y + height + (child.height * 0.5f)), mouseX, mouseY, deltaTime);
                height += this.parent.height / this.children.size() - 1;

            }else{
                child.draw(ui, (int) ((x + width * crossAxis.getXModifier())), (int) ((int) ((y + height) + this.parent.height * mainAxis.getYModifier()) - (childHeight * mainAxis.getYModifier())), mouseX, mouseY, deltaTime);
                height = (int) (height + child.height);
            }
        }
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
