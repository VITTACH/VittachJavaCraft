package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.vittach.jumpjack.framework.PlayerMusic;

import java.util.HashSet;

public class Preference {
    public PlayerMusic playerMusic = new PlayerMusic();
    public InputListener listener = new InputListener();
    public HashSet<Integer> usedInputIdMap = new HashSet<Integer>();

    public int screenWidth = Gdx.graphics.getWidth();
    public int screenHeight = Gdx.graphics.getHeight();

    public int displayWidth;
    public int displayHeight;

    void setWidth(int screenWidth, int displayWidth) {
        this.screenWidth = screenWidth;
        this.displayWidth = displayWidth;
    }

    void setHeight(int screenHeight, int displayHeight) {
        this.screenHeight = screenHeight;
        this.displayHeight = displayHeight;
    }

    private Preference() { }

    private static Preference instance;

    public synchronized static Preference getInstance() {
        if (instance == null) {
            instance = new Preference();
        }
        return instance;
    }
}
