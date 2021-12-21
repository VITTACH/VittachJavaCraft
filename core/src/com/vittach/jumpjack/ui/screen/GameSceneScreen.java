package com.vittach.jumpjack.ui.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;

public class GameSceneScreen implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferenceInstance = Preferences.getInstance();

    public void display(Viewport view) {
        engineInstance.fpController.handleInput();
        engineInstance.rightStick.handleInput();
        engineInstance.leftStick.handleInput();

        engineInstance.gameScene.display(view);
        engineInstance.rightStick.display(view);
        engineInstance.leftStick.display(view);
        engineInstance.boxBtn.display(view);

        listenInput();
    }

    private void listenInput() {
        if (!engineInstance.fpController.keySet.contains(Input.Keys.ESCAPE)) return;

        preferenceInstance.inputListener.cleanProcesses();

        preferenceInstance.inputListener.addListener(engineInstance.pauseMenu.saveButton);
        preferenceInstance.inputListener.addListener(engineInstance.pauseMenu.exitButton);
        preferenceInstance.inputListener.addListener(engineInstance.pauseMenu.loadButton);
        preferenceInstance.inputListener.addListener(engineInstance.pauseMenu.playButton);
        preferenceInstance.inputListener.addListener(engineInstance.pauseMenu);

        engineInstance.fpController.keySet.clear();
        engineInstance.currentScreen = MainEngine.Screen.GAME_STOP;
    }
}