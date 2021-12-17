package com.vittach.jumpjack.ui.screen;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.ui.GameScreen;
import com.vittach.jumpjack.Preferences;

public class LoadSave implements GameScreen {
    private MainEngine engineInstance = MainEngine.getInstance();
    private Preferences prefInstance = Preferences.getInstance();

    public void display(Viewport view) {
        engineInstance.fileMenu.display(view);

        listenInput();
    }

    private void listenInput() {
        if (engineInstance.fileMenu.pressedKey < 0) return;
        prefInstance.inputListener.cleanProcesses();
        switch (engineInstance.fileMenu.pressedKey) {
            case 1:
                prefInstance.inputListener.addListener(engineInstance.fpController);
                prefInstance.inputListener.addListener(engineInstance.inventoryBtn);

                engineInstance.currentScreen = 0;
                break;

            case 2:
                prefInstance.inputListener.addListener(engineInstance.startMenu.gameButton);
                prefInstance.inputListener.addListener(engineInstance.startMenu.loadButton);
                prefInstance.inputListener.addListener(engineInstance.startMenu.exitButton);
                prefInstance.inputListener.addListener(engineInstance.startMenu);

                engineInstance.currentScreen = 2;
                break;
        }

        engineInstance.fileMenu.pressedKey = -1;
    }
}
