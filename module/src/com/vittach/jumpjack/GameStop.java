package com.vittach.jumpjack;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameStop implements GameScreen {
    private JJEngine engineInst = JJEngine.getInstance();
    private Preference prefInst = Preference.getInstance();

    public void display(Viewport view) {
        engineInst.pauseMenu.display(view);

        listenInput();
    }

    private void listenInput() {
        if (engineInst.pauseMenu.pressedKey > 0) {
            prefInst.listener.cleanProcesses();
            switch (engineInst.pauseMenu.pressedKey) {
                case Input.Keys.ESCAPE:
                    prefInst.listener.addListener(engineInst.inventoryBtn);
                    prefInst.listener.addListener(engineInst.controller);

                    engineInst.currentScreen = 0;
                    break;

                case 67:
                    prefInst.listener.addListener(engineInst.startMenu.gameButton);
                    prefInst.listener.addListener(engineInst.startMenu.loadButton);
                    prefInst.listener.addListener(engineInst.startMenu.exitButton);
                    prefInst.listener.addListener(engineInst.startMenu);

                    engineInst.currentScreen = 2;
                    break;
            }

            engineInst.pauseMenu.pressedKey = -1;
        }
    }
}
