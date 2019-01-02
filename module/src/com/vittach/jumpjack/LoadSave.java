package com.vittach.jumpjack;

import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadSave implements GameScreen {
    private JJEngine E;

    LoadSave(JJEngine E) {
        this.E = E;
    }

    public void
    render(Viewport view) {
        E.explorer.Display(view);
        if (E.explorer.pressedKey >= 0) {
            Preference.inputListener.clnProces();
            switch (E.explorer.pressedKey) {
                case 1:
                    E.screen = 0;
                    if (E.deviceId == 1) {//running android
                        Preference.inputListener.addListener(E.leftStick);
                        Preference.inputListener.addListener(E.rightStick);
                    }
                    Preference.inputListener.addListener(E.human);
                    Preference.inputListener.addListener(E.currentBlock);
                    E.fileLoader.setFileName(E.explorer.getName());
                    E.fileLoader.loadWorld();
                    break;
                case 2:
                    E.screen = 2;
                    Preference.inputListener.addListener(E.menu.game);
                    Preference.inputListener.addListener(E.menu.load);
                    Preference.inputListener.addListener(E.menu.exit);
                    Preference.inputListener.addListener(E.menu);
                    break;
            }
            E.explorer.pressedKey = -1;
        }
    }
}
