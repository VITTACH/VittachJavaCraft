package com.vittach.jumpjack.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vittach.jumpjack.engine.MainEngine;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config0 = new LwjglApplicationConfiguration();
        config0.width = 960;
        config0.height = 544;
        config0.title = "VitaCraft";
        new LwjglApplication(MainEngine.getInstance(MainEngine.Device.DESKTOP), config0);
    }
}
