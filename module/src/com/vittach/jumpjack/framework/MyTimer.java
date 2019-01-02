package com.vittach.jumpjack.framework;

import java.io.Serializable;

public class MyTimer implements Serializable {
    public boolean visible = false;
    private long delay = 0;
    private long start, currentTime;

    public void start(long dela) {
        delay = dela;
        start = System.currentTimeMillis();
    }

    public boolean isActive() {
        if (delay > 0) {
            if ((currentTime = System.currentTimeMillis() - start) < delay) {
                return true;
            } else {
                visible = false;
                stop();
            }
        }
        return false;
    }

    public void set_Start(long start) {
        this.start = start;
    }

    public float getCurTime() {
        return (delay == 0) ? 0 : currentTime / 1000f;
    }

    public long getMe_Start() {
        return start;
    }

    public void stop() {
        delay = 0;
    }
}
