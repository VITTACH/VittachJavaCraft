package com.vittach.jumpjack;

import com.badlogic.gdx.utils.viewport.Viewport;

public class NewWorld implements GameScreen {
    private JJEngine engineInst = JJEngine.getInstance();
    private Preference prefInst = Preference.getInstance();

    public void render(Viewport view) {
        engineInst.worldCreator.display(view);

        if (engineInst.worldCreator.pressedKey >= 0) {
            prefInst.inputListener.cleanProcesses();

            switch (engineInst.worldCreator.pressedKey) {
                case 1: // createButton new worldMapInst
                    engineInst.currentScreen = 0;
                    engineInst.human.setHealthValue(100);
                    engineInst.human.deathWinState = 0;
                    engineInst.fileController.setFileName(engineInst.worldCreator.getName());

                    engineInst.worldMapInst.createWorld(64, 32, 64);
                    engineInst.human.camera.position.set(0, 2f, 0f);
                    engineInst.fileController.saveWorld();

                    prefInst.inputListener.addListener(engineInst.human);
                    prefInst.inputListener.addListener(engineInst.currentBlock);
                    break;

                case 2: // go to backButton screenWindow
                    engineInst.currentScreen = 2;
                    prefInst.inputListener.addListener(engineInst.startMenuInst.gameButton);
                    prefInst.inputListener.addListener(engineInst.startMenuInst.loadButton);
                    prefInst.inputListener.addListener(engineInst.startMenuInst.exitButton);
                    prefInst.inputListener.addListener(engineInst.startMenuInst);
                    break;
            }

            engineInst.worldCreator.textField.setCursorPosition(2);
            engineInst.worldCreator.textField.setText(" ");
            engineInst.worldCreator.pressedKey = -1;
        }
    }
}
