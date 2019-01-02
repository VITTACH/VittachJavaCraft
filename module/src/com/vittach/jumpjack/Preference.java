package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.vittach.jumpjack.framework.PlayerMusic;
import com.vittach.jumpjack.framework.ImageHandler;

import java.util.HashSet;

public class Preference {
    static PlayerMusic player = new PlayerMusic();
    static HashSet<Integer> usedID = new HashSet<Integer>();
    static InputListener inputListener = new InputListener();

    static int screenWidth = Gdx.graphics.getWidth();
    static int screenHeight = Gdx.graphics.getHeight();

    public static int width, height;
    public static int windowWidth = 480;
    public static int windowHeight = 272;

    static ImageHandler screenWindow = new ImageHandler();

    static void setWidth(int width1, int width2) {
        screenWidth = width1;
        width = width2;
    }

    static void setHeight(int height1, int height2) {
        screenHeight = height1;
        height = height2;
    }
}
