package com.vittach.jumpjack.ui.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;

public class GameSceneScreen implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferenceInstance = Preferences.getInstance();

    public void display(Viewport viewport) {
        engineInstance.fpController.handleInput();
        engineInstance.rightStick.handleInput();
        engineInstance.leftStick.handleInput();

        engineInstance.gameScene.display(viewport);
        engineInstance.rightStick.display(viewport);
        engineInstance.leftStick.display(viewport);

        listenInput();
    }

    private void listenInput() {
        if (!engineInstance.fpController.keySet.contains(Input.Keys.ESCAPE)) {
            return;
        }

        preferenceInstance.inputListener.cleanProcesses();

        engineInstance.pauseMenu.setUpListeners();
        engineInstance.currentScreen = MainEngine.Screen.GAME_STOP;

        engineInstance.fpController.keySet.clear();
    }
}