package com.vittach.jumpjack.ui.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;

public class GameStop implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferencesInstance = Preferences.getInstance();

    public void display(Viewport view) {
        engineInstance.pauseMenu.display(view);

        listenInput();
    }

    private void listenInput() {
        if (engineInstance.pauseMenu.pressedKey <= 0) return;

        preferencesInstance.inputListener.cleanProcesses();

        switch (engineInstance.pauseMenu.pressedKey) {
            case Input.Keys.ESCAPE:
                preferencesInstance.inputListener.addListener(engineInstance.boxBtn);
                preferencesInstance.inputListener.addListener(engineInstance.fpController);

                engineInstance.currentScreen = MainEngine.Screen.GAME_PLAY;
                break;

            case 67:
                preferencesInstance.inputListener.addListener(engineInstance.startMenu.gameButton);
                preferencesInstance.inputListener.addListener(engineInstance.startMenu.loadButton);
                preferencesInstance.inputListener.addListener(engineInstance.startMenu.moreButton);
                preferencesInstance.inputListener.addListener(engineInstance.startMenu);

                engineInstance.currentScreen = MainEngine.Screen.GAME_MAIN_SCREEN;
                break;
        }

        engineInstance.pauseMenu.pressedKey = -1;
    }
}
