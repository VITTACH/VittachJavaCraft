package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.vittach.jumpjack.framework.PlayerMusic;
import com.vittach.jumpjack.framework.ImageHandler;

import java.util.HashSet;

public class Preference {
    public int displayWidth, displayHeight;

    public PlayerMusic player = new PlayerMusic();
    public ImageHandler screenWindow = new ImageHandler();
    public HashSet<Integer> usedID = new HashSet<Integer>();
    public InputListener inputListener = new InputListener();

    public int screenWidth = Gdx.graphics.getWidth();
    public int screenHeight = Gdx.graphics.getHeight();

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
