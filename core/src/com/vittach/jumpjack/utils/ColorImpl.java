package com.vittach.jumpjack.utils;

import com.badlogic.gdx.graphics.Color;

public class ColorImpl {
    private Color color;

    public ColorImpl(float x, float y, float z, float a) {
        color = new Color(x, y, z, a);
    }

    public ColorImpl(float x, float y, float z) {
        this(x, y, z, 1);
    }

    public Color getColor() {
        return color;
    }
}
