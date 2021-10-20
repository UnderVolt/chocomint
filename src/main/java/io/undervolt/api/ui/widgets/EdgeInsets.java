package io.undervolt.api.ui.widgets;

public class EdgeInsets {

    protected int top = 0;
    protected int left = 0;
    protected int right = 0;
    protected int bottom = 0;

    public EdgeInsets(int top, int left, int right, int bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
    }

    public EdgeInsets top(int top) {
        this.top = top;
        return this;
    }

    public EdgeInsets left(int left) {
        this.left = left;
        return this;
    }

    public EdgeInsets right(int right) {
        this.right = right;
        return this;
    }

    public EdgeInsets bottom(int bottom) {
        this.bottom = bottom;
        return this;
    }

    public static EdgeInsets horizontal(int padding){
        return new EdgeInsets(0, padding, padding, 0);
    }

    public static EdgeInsets vertical(int padding){
        return new EdgeInsets(padding, 0, 0, padding);
    }

    public static EdgeInsets all(int padding){
        return new EdgeInsets(padding, padding, padding, padding);
    }
}
