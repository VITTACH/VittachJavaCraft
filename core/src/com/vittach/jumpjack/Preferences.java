package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.vittach.jumpjack.ui.InputListener;
import com.vittach.jumpjack.utils.PlayerMusic;

import java.util.HashSet;

public class Preferences {
    public PlayerMusic playerMusic = new PlayerMusic();
    public InputListener inputListener = new InputListener();
    public HashSet<Integer> inputIdMap = new HashSet<Integer>();

    public int screenWidth = Gdx.graphics.getWidth();
    public int screenHeight = Gdx.graphics.getHeight();

    public int displayWidth;
    public int displayHeight;

    public void setWidth(int screenWidth, int displayWidth) {
        this.screenWidth = screenWidth;
        this.displayWidth = displayWidth;
    }

    public void setHeight(int screenHeight, int displayHeight) {
        this.screenHeight = screenHeight;
        this.displayHeight = displayHeight;
    }

    private Preferences() {
    }

    private static Preferences instance;

    public synchronized static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }
}
