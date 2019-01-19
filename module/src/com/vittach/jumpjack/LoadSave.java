package com.vittach.jumpjack;

import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadSave implements GameScreen {
    private JJEngine engineInst = JJEngine.getInstance();
    private Preference prefInst = Preference.getInstance();

    public void display(Viewport view) {
        engineInst.fileExplorer.display(view);

        if (engineInst.fileExplorer.pressedKey >= 0) {
            prefInst.listener.cleanProcesses();
            switch (engineInst.fileExplorer.pressedKey) {
                case 1:
                    prefInst.listener.addListener(engineInst.controller);
                    prefInst.listener.addListener(engineInst.inventoryButton);

                    engineInst.currentScreen = 0;
                    break;

                case 2:
                    prefInst.listener.addListener(engineInst.startMenu.gameButton);
                    prefInst.listener.addListener(engineInst.startMenu.loadButton);
                    prefInst.listener.addListener(engineInst.startMenu.exitButton);
                    prefInst.listener.addListener(engineInst.startMenu);

                    engineInst.currentScreen = 2;
                    break;
            }

            engineInst.fileExplorer.pressedKey = -1;
        }
    }
}
