package com.vittach.jumpjack.ui.screen;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;

public class WorldConstructor implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferenceInstance = Preferences.getInstance();

    public void display(Viewport view) {
        engineInstance.worldCreator.display(view);

        listenInput();
    }

    private void listenInput() {
        if (engineInstance.worldCreator.pressedKey < 0) return;

        preferenceInstance.inputListener.cleanProcesses();

        switch (engineInstance.worldCreator.pressedKey) {
            case 1:
                engineInstance.mainScreen.genWorld();
                preferenceInstance.inputListener.addListener(engineInstance.boxBtn);
                preferenceInstance.inputListener.addListener(engineInstance.leftStick);
                preferenceInstance.inputListener.addListener(engineInstance.rightStick);
                preferenceInstance.inputListener.addListener(engineInstance.fpController);

                engineInstance.currentScreen = MainEngine.Screen.GAME_PLAY;
                break;
            case 2:
                preferenceInstance.inputListener.addListener(engineInstance.startMenu.gameButton);
                preferenceInstance.inputListener.addListener(engineInstance.startMenu.loadButton);
                preferenceInstance.inputListener.addListener(engineInstance.startMenu.moreButton);
                preferenceInstance.inputListener.addListener(engineInstance.startMenu);

                engineInstance.currentScreen = MainEngine.Screen.GAME_MAIN_SCREEN;
                break;
        }

        engineInstance.worldCreator.textField.setText(" ");
        engineInstance.worldCreator.pressedKey = -1;
    }
}
