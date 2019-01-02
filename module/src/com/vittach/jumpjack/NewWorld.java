package com.vittach.jumpjack;

import com.badlogic.gdx.utils.viewport.Viewport;

public class NewWorld implements GameScreen {
    private JJEngine engine;

    NewWorld(JJEngine engine) { this.engine = engine; }

    public void render(Viewport view) {
        engine.news.Display(view);
        if (engine.news.PressedKeyCode >= 0) {
            Preference.inputListener.clnProces();
            switch (engine.news.PressedKeyCode) {
                // create new world
                case 1:
                    engine.screen = 0;
                    engine.human.healthValue = 100;
                    engine.human.deathWinState = 0;
                    this.engine.fileLoader.setFileName(engine.news.getName());

                    engine.world.generateWorld(128, 128, 128);
                    JJEngine.human.camera.position.set(0, 2, 0);
                    engine.fileLoader.saveWorld();

                    if (engine.deviceId == 1) {
                        Preference.inputListener.addListener(engine.leftStick);
                        Preference.inputListener.addListener(engine.rightStick);
                    }
                    Preference.inputListener.addListener(engine.human);
                    Preference.inputListener.addListener(engine.currentBlock);
                    break;

                // go to backButton screenWindow
                case 2:
                    engine.screen = 2;
                    Preference.inputListener.addListener(engine.menu.game);
                    Preference.inputListener.addListener(engine.menu.load);
                    Preference.inputListener.addListener(engine.menu.exit);
                    Preference.inputListener.addListener(engine.menu);
                    break;
            }
            engine.news.textField.setText(" ");
            engine.news.textField.setCursorPosition(2);
            engine.news.PressedKeyCode = -1;
        }
    }
}
