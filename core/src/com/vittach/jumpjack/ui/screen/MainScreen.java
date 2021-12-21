package com.vittach.jumpjack.ui.screen;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;

public class MainScreen implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferenceInstance = Preferences.getInstance();

    public void display(Viewport view) {
        engineInstance.startMenu.display(view);

        listenInput();
    }

    private void listenInput() {
        if (engineInstance.startMenu.pressedKey < 0) return;
        preferenceInstance.inputListener.cleanProcesses();
        switch (engineInstance.startMenu.pressedKey) {
            case 2:
                preferenceInstance.inputListener.addListener(engineInstance.loadAndSaveMenu.backButton);
                preferenceInstance.inputListener.addListener(engineInstance.loadAndSaveMenu.loadButton);
                preferenceInstance.inputListener.addListener(engineInstance.loadAndSaveMenu.startButton);
                preferenceInstance.inputListener.addListener(engineInstance.loadAndSaveMenu);
                engineInstance.loadAndSaveMenu.updateDir();
                engineInstance.loadAndSaveMenu.getCurDir();

                engineInstance.currentScreen = MainEngine.Screen.LOAD_SAVE;
                break;

            case 1:
                preferenceInstance.inputListener.addListener(engineInstance.createWorldMenu.sartButton);
                preferenceInstance.inputListener.addListener(engineInstance.createWorldMenu.backButton);
                preferenceInstance.inputListener.addListener(engineInstance.createWorldMenu);

                engineInstance.currentScreen = MainEngine.Screen.WORLD_CONSTRUCT;
                break;
        }

        engineInstance.startMenu.pressedKey = -1;
    }
}
