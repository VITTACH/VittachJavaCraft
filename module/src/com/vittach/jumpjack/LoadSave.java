package com.vittach.jumpjack;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.engine.GameScreen;
import com.vittach.jumpjack.engine.Preference;

public class LoadSave implements GameScreen {
    private MainEngine engineInstance = MainEngine.getInstance();
    private com.vittach.jumpjack.engine.Preference prefInstance = Preference.getInstance();

    public void display(Viewport view) {
        engineInstance.fileExplorer.display(view);

        listenInput();
    }

    private void listenInput() {
        if (engineInstance.fileExplorer.pressedKey < 0) return;
        prefInstance.inputListener.cleanProcesses();
        switch (engineInstance.fileExplorer.pressedKey) {
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

        engineInstance.fileExplorer.pressedKey = -1;
    }
}
