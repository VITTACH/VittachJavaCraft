package com.vittach.jumpjack.ui.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.ui.GameScreen;
import com.vittach.jumpjack.Preferences;

public class GameStop implements GameScreen {
    private MainEngine engineInstance = MainEngine.getInstance();
    private Preferences prefInstance = Preferences.getInstance();

    public void display(Viewport view) {
        engineInstance.pauseMenu.display(view);

        listenInput();
    }

    private void listenInput() {
        if (engineInstance.pauseMenu.pressedKey <= 0) return;
        prefInstance.inputListener.cleanProcesses();
        switch (engineInstance.pauseMenu.pressedKey) {
            case Input.Keys.ESCAPE:
                prefInstance.inputListener.addListener(engineInstance.inventoryBtn);
                prefInstance.inputListener.addListener(engineInstance.fpController);

                engineInstance.currentScreen = 0;
                break;

            case 67:
                prefInstance.inputListener.addListener(engineInstance.startMenu.gameButton);
                prefInstance.inputListener.addListener(engineInstance.startMenu.loadButton);
                prefInstance.inputListener.addListener(engineInstance.startMenu.exitButton);
                prefInstance.inputListener.addListener(engineInstance.startMenu);

                engineInstance.currentScreen = 2;
                break;
        }

        engineInstance.pauseMenu.pressedKey = -1;
    }
}