package com.vittach.jumpjack.ui.screen;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.Preferences;
import com.vittach.jumpjack.engine.MainEngine;

public class LoadAndSaveScreen implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferenceInstance = Preferences.getInstance();

    public void display(Viewport viewport) {
        engineInstance.loadAndSaveMenu.display(viewport);

        listenInput();
    }

    private void listenInput() {
        int pressedKey = engineInstance.loadAndSaveMenu.pressedKey;
        if (pressedKey < 0) return;

        preferenceInstance.inputListener.cleanProcesses();

        if (pressedKey == 1) {
            engineInstance.gameScene.setUpListeners();
            engineInstance.currentScreen = MainEngine.Screen.GAME_PLAY;
        } else if (pressedKey == 2) {
            engineInstance.startMenu.setUpListeners();
            engineInstance.currentScreen = MainEngine.Screen.GAME_MAIN_SCREEN;
        }

        engineInstance.loadAndSaveMenu.pressedKey = -1;
    }
}
