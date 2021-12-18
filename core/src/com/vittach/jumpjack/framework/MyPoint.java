package com.vittach.jumpjack.framework;

public class MyPoint {
    private int width, height;

    public MyPoint(int xPosition, int yPosition) {
        width = xPosition;
        height = yPosition;
    }

    public MyPoint(double xPosition, double yPosition) {
        this((int) xPosition, (int) yPosition);
    }

    public int getX() {
        return width;
    }

    public int getY() {
        return height;
    }
}
