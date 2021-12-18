package com.vittach.jumpjack.ui.screen.menu;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.ui.screen.GameScreen;
import com.vittach.jumpjack.Preferences;

public class MainMenu implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences prefInstance = Preferences.getInstance();

    public void display(Viewport view) {
        engineInstance.startMenu.display(view);

        listenInput();
    }

    private void listenInput() {
        if (engineInstance.startMenu.pressedKey < 0) return;
        prefInstance.inputListener.cleanProcesses();
        switch (engineInstance.startMenu.pressedKey) {
            case 2:
                prefInstance.inputListener.addListener(engineInstance.fileMenu.backButton);
                prefInstance.inputListener.addListener(engineInstance.fileMenu.loadButton);
                prefInstance.inputListener.addListener(engineInstance.fileMenu.sartButton);
                prefInstance.inputListener.addListener(engineInstance.fileMenu);
                engineInstance.fileMenu.updateDir();
                engineInstance.fileMenu.getCurDir();

                engineInstance.currentScreen = 4;
                break;

            case 1:
                prefInstance.inputListener.addListener(engineInstance.worldCreator.sartButton);
                prefInstance.inputListener.addListener(engineInstance.worldCreator.backButton);
                prefInstance.inputListener.addListener(engineInstance.worldCreator);

                engineInstance.currentScreen = 3;
                break;
        }

        engineInstance.startMenu.pressedKey = -1;
    }
}
