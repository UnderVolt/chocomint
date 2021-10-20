package io.undervolt.api.ui.widgets;

public enum AxisAlignment {
    START(0, 0),
    END(1, 1),
    SPACE_BETWEEN(0, 0),
    CENTER(0.5f, 0.5f);

    private float xModifier;
    private float yModifier;

    AxisAlignment(float x, float y) {
        xModifier = x;
        yModifier = y;
    }

    public float getXModifier() {
        return xModifier;
    }

    public float getYModifier() {
        return yModifier;
    }
}
