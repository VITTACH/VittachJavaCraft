package com.vittach.jumpjack;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.engine.GameScreen;
import com.vittach.jumpjack.engine.Preference;

public class GamePlay implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final com.vittach.jumpjack.engine.Preference prefInstance = Preference.getInstance();

    public void display(Viewport view) {
        engineInstance.mainScreen.display(view);
        engineInstance.inventoryBtn.display(view);

        listenInput();
    }

    private void listenInput() {
        if (!engineInstance.fpController.pressedKeys.contains(Input.Keys.ESCAPE)) {
            return;
        }

        prefInstance.inputListener.cleanProcesses();

        prefInstance.inputListener.addListener(engineInstance.pauseMenu.saveButton);
        prefInstance.inputListener.addListener(engineInstance.pauseMenu.exitButton);
        prefInstance.inputListener.addListener(engineInstance.pauseMenu.loadButton);
        prefInstance.inputListener.addListener(engineInstance.pauseMenu.playButton);
        prefInstance.inputListener.addListener(engineInstance.pauseMenu);

        engineInstance.fpController.pressedKeys.clear();
        engineInstance.currentScreen = 1;
    }
}
