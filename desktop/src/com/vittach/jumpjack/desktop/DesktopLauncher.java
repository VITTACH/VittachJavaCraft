package com.vittach.jumpjack.desktop;

import com.vittach.jumpjack.MainEngine;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config0 = new LwjglApplicationConfiguration();
        config0.width = 960;
        config0.height = 544;
        config0.title = "Crafter3d";
        new LwjglApplication(MainEngine.getInstance(0), config0);
    }
}
