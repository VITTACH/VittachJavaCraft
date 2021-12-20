package com.vittach.jumpjack.ui.screen;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;

public class LoadSave implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferencesInstance = Preferences.getInstance();

    public void display(Viewport view) {
        engineInstance.fileMenu.display(view);

        listenInput();
    }

    private void listenInput() {
        if (engineInstance.fileMenu.pressedKey < 0) return;

        preferencesInstance.inputListener.cleanProcesses();

        switch (engineInstance.fileMenu.pressedKey) {
            case 1:
                preferencesInstance.inputListener.addListener(engineInstance.fpController);
                preferencesInstance.inputListener.addListener(engineInstance.boxBtn);

                engineInstance.currentScreen = MainEngine.Screen.GAME_PLAY;
                break;

            case 2:
                preferencesInstance.inputListener.addListener(engineInstance.startMenu.gameButton);
                preferencesInstance.inputListener.addListener(engineInstance.startMenu.loadButton);
                preferencesInstance.inputListener.addListener(engineInstance.startMenu.moreButton);
                preferencesInstance.inputListener.addListener(engineInstance.startMenu);

                engineInstance.currentScreen = MainEngine.Screen.GAME_MAIN_SCREEN;
                break;
        }

        engineInstance.fileMenu.pressedKey = -1;
    }
}
