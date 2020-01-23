package com.vittach.jumpjack;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GamePlay implements GameScreen {
    private JJEngine engineInst = JJEngine.getInstance();
    private Preference prefInst = Preference.getInstance();

    public void display(Viewport view) {
        engineInst.mainGameLoop.display(view);
        engineInst.inventoryBtn.display(view);

        listenInput();
    }

    private void listenInput() {
        if (engineInst.controller.pressedKeys.contains(Input.Keys.ESCAPE)) {
            prefInst.listener.cleanProcesses();

            prefInst.listener.addListener(engineInst.pauseMenu.saveButton);
            prefInst.listener.addListener(engineInst.pauseMenu.exitButton);
            prefInst.listener.addListener(engineInst.pauseMenu.loadButton);
            prefInst.listener.addListener(engineInst.pauseMenu.playButton);
            prefInst.listener.addListener(engineInst.pauseMenu);

            engineInst.controller.pressedKeys.clear();
            engineInst.currentScreen = 1;
        }
    }
}
