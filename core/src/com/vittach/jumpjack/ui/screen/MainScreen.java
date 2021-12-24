package com.vittach.jumpjack.ui.screen;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.engine.MainEngine;
import com.vittach.jumpjack.Preferences;

public class MainScreen implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferenceInstance = Preferences.getInstance();

    public void display(Viewport viewport) {
        engineInstance.startMenu.display(viewport);

        listenInput();
    }

    private void listenInput() {
        int pressedKey = engineInstance.startMenu.pressedKey;
        if (pressedKey < 0) return;

        preferenceInstance.inputListener.cleanProcesses();

        if (pressedKey == 1) {
            engineInstance.createWorldMenu.setUpListeners();
            engineInstance.currentScreen = MainEngine.Screen.WORLD_CONSTRUCT;
        } else if (pressedKey == 2) {
            engineInstance.loadAndSaveMenu.setUpListeners();
            engineInstance.currentScreen = MainEngine.Screen.LOAD_SAVE;
        }

        engineInstance.startMenu.pressedKey = -1;
    }
}
