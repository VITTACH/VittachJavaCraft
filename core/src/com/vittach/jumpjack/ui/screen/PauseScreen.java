package com.vittach.jumpjack.ui.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.engine.MainEngine;
import com.vittach.jumpjack.Preferences;

public class PauseScreen implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferenceInstance = Preferences.getInstance();

    public void display(Viewport viewport) {
        engineInstance.pauseMenu.display(viewport);

        listenInput();
    }

    private void listenInput() {
        int pressedKey = engineInstance.pauseMenu.pressedKey;
        if (pressedKey <= 0) return;

        preferenceInstance.inputListener.cleanProcesses();

        if (pressedKey == Input.Keys.ESCAPE) {
            engineInstance.gameScene.setUpListeners();
            engineInstance.currentScreen = MainEngine.Screen.GAME_PLAY;
        } else if (pressedKey == 2) {
            engineInstance.startMenu.setUpListeners();
            engineInstance.currentScreen = MainEngine.Screen.GAME_MAIN_SCREEN;
        }

        engineInstance.pauseMenu.pressedKey = -1;
    }
}
