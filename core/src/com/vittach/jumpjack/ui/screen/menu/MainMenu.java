package com.vittach.jumpjack.ui.screen.menu;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;
import com.vittach.jumpjack.ui.screen.GameScreen;

public class MainMenu implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferencesInstance = Preferences.getInstance();

    public void display(Viewport view) {
        engineInstance.startMenu.display(view);

        listenInput();
    }

    private void listenInput() {
        if (engineInstance.startMenu.pressedKey < 0) return;
        preferencesInstance.inputListener.cleanProcesses();
        switch (engineInstance.startMenu.pressedKey) {
            case 2:
                preferencesInstance.inputListener.addListener(engineInstance.fileMenu.backButton);
                preferencesInstance.inputListener.addListener(engineInstance.fileMenu.loadButton);
                preferencesInstance.inputListener.addListener(engineInstance.fileMenu.startButton);
                preferencesInstance.inputListener.addListener(engineInstance.fileMenu);
                engineInstance.fileMenu.updateDir();
                engineInstance.fileMenu.getCurDir();

                engineInstance.currentScreen = MainEngine.Screen.LOAD_SAVE;
                break;

            case 1:
                preferencesInstance.inputListener.addListener(engineInstance.worldCreator.sartButton);
                preferencesInstance.inputListener.addListener(engineInstance.worldCreator.backButton);
                preferencesInstance.inputListener.addListener(engineInstance.worldCreator);

                engineInstance.currentScreen = MainEngine.Screen.WORLD_CONSTRUCT;
                break;
        }

        engineInstance.startMenu.pressedKey = -1;
    }
}
