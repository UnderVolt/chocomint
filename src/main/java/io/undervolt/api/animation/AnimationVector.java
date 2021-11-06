package io.undervolt.api.animation;

public class AnimationVector {

    private double x, y;

    public AnimationVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public AnimationVector setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public AnimationVector setY(double y) {
        this.y = y;
        return this;
    }

}
