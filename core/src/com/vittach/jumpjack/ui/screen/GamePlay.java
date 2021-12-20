package com.vittach.jumpjack.ui.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;

public class GamePlay implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferencesInstance = Preferences.getInstance();

    public void display(Viewport view) {
        engineInstance.fpController.handleInput();
        engineInstance.rightStick.handleInput();
        engineInstance.leftStick.handleInput();

        engineInstance.mainScreen.display(view);
        engineInstance.rightStick.display(view);
        engineInstance.leftStick.display(view);
        engineInstance.boxBtn.display(view);

        listenInput();
    }

    private void listenInput() {
        if (!engineInstance.fpController.pressedKeys.contains(Input.Keys.ESCAPE)) return;

        preferencesInstance.inputListener.cleanProcesses();

        preferencesInstance.inputListener.addListener(engineInstance.pauseMenu.saveButton);
        preferencesInstance.inputListener.addListener(engineInstance.pauseMenu.exitButton);
        preferencesInstance.inputListener.addListener(engineInstance.pauseMenu.loadButton);
        preferencesInstance.inputListener.addListener(engineInstance.pauseMenu.playButton);
        preferencesInstance.inputListener.addListener(engineInstance.pauseMenu);

        engineInstance.fpController.pressedKeys.clear();
        engineInstance.currentScreen = MainEngine.Screen.GAME_STOP;
    }
}