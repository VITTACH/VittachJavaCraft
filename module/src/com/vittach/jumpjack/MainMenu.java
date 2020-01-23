package com.vittach.jumpjack;

import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenu implements GameScreen {
    private JJEngine engineInst = JJEngine.getInstance();
    private Preference prefInst = Preference.getInstance();

    public void display(Viewport view) {
        engineInst.startMenu.display(view);

        listenInput();
    }

    private void listenInput() {
        if (engineInst.startMenu.pressedKey >= 0) {
            prefInst.listener.cleanProcesses();
            switch (engineInst.startMenu.pressedKey) {
                case 2:
                    prefInst.listener.addListener(engineInst.fileExplorer.backButton);
                    prefInst.listener.addListener(engineInst.fileExplorer.loadButton);
                    prefInst.listener.addListener(engineInst.fileExplorer.sartButton);
                    prefInst.listener.addListener(engineInst.fileExplorer);
                    engineInst.fileExplorer.updateDir();
                    engineInst.fileExplorer.getCurDir();

                    engineInst.currentScreen = 4;
                    break;

                case 1:
                    prefInst.listener.addListener(engineInst.worldCreator.sartButton);
                    prefInst.listener.addListener(engineInst.worldCreator.backButton);
                    prefInst.listener.addListener(engineInst.worldCreator);

                    engineInst.currentScreen = 3;
                    break;
            }

            engineInst.startMenu.pressedKey = -1;
        }
    }
}
