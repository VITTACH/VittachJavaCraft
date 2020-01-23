package com.vittach.jumpjack;

import com.badlogic.gdx.utils.viewport.Viewport;

public class WorldConstructor implements GameScreen {
    private JJEngine engineInst = JJEngine.getInstance();
    private Preference prefInst = Preference.getInstance();

    public void display(Viewport view) {
        engineInst.worldCreator.display(view);

        if (engineInst.worldCreator.pressedKey >= 0) {
            prefInst.listener.cleanProcesses();
            switch (engineInst.worldCreator.pressedKey) {
                case 1:
                    engineInst.mainGameLoop.genWorld();

                    prefInst.listener.addListener(engineInst.inventoryBtn);
                    prefInst.listener.addListener(engineInst.controller);

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

            engineInst.worldCreator.textField.setText(" ");
            engineInst.worldCreator.pressedKey = -1;
        }
    }
}
