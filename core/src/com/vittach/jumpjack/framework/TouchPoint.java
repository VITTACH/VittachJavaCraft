package com.vittach.jumpjack.framework;

public class TouchPoint {
    private int x, y;

    public TouchPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void updatePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
