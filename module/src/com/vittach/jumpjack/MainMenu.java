package com.vittach.jumpjack;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.engine.GameScreen;
import com.vittach.jumpjack.engine.Preference;

public class MainMenu implements GameScreen {
    private final MainEngine engineInstance = MainEngine.getInstance();
    private final com.vittach.jumpjack.engine.Preference prefInstance = Preference.getInstance();

    public void display(Viewport view) {
        engineInstance.startMenu.display(view);

        listenInput();
    }

    private void listenInput() {
        if (engineInstance.startMenu.pressedKey < 0) return;
        prefInstance.inputListener.cleanProcesses();
        switch (engineInstance.startMenu.pressedKey) {
            case 2:
                prefInstance.inputListener.addListener(engineInstance.fileExplorer.backButton);
                prefInstance.inputListener.addListener(engineInstance.fileExplorer.loadButton);
                prefInstance.inputListener.addListener(engineInstance.fileExplorer.sartButton);
                prefInstance.inputListener.addListener(engineInstance.fileExplorer);
                engineInstance.fileExplorer.updateDir();
                engineInstance.fileExplorer.getCurDir();

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
