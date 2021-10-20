package io.undervolt.api.ui.widgets;

public enum Alignment {
    TOP_LEFT(0, 0),
    TOP_CENTER(0.5f, 0),
    TOP_RIGHT(1, 0),
    MIDDLE_LEFT(0, 0.5f),
    CENTER(0.5f, 0.5f),
    MIDDLE_RIGHT(1, 0.5f),
    BOTTOM_LEFT(0, 1),
    BOTTOM_CENTER(0.5f, 1),
    BOTTOM_RIGHT(1, 1);

    float xModifier = 0;
    float yModifier = 0;

    Alignment(float xModifier, float yModifier) {
        this.xModifier = xModifier;
        this.yModifier = yModifier;
    }
}
