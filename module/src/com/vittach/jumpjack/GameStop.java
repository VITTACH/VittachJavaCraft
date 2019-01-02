package com.vittach.jumpjack;

import com.badlogic.gdx.utils.viewport.Viewport;

public class GameStop implements GameScreen {
    private JJEngine E;
    long time_fall = 0;

    GameStop(JJEngine E) {
        this.E = E;
    }

    public void render
            (Viewport view) {
        if (time_fall == 0)
            time_fall = System.currentTimeMillis();
        E.selectBoxs.Display(view);
        if (E.selectBoxs.DWNKey > 0) {
            switch (E.selectBoxs.DWNKey) {
                case 131:
                    Preference.inputListener.clnProces();
                    Preference.inputListener.addListener(E.currentBlock);
                    Preference.inputListener.addListener(E.human);
                    //if game was running android
                    if (E.deviceId == 1) {
                        Preference.inputListener.addListener(E.leftStick);
                        Preference.inputListener.addListener(E.rightStick);
                    }
                    JJEngine.human.fallingTimer.set_Start(
                            E.human.fallingTimer.getMe_Start() + System.currentTimeMillis() - time_fall);
                    E.human.selectedBlockY = E.human.selectedBlockZ = -1;
                    E.human.builderTimer.visible = false;
                    E.human.builderTimer.stop();
                    E.human.selectedBlockX = -1;
                    time_fall = 0;
                    E.screen = 0;
                    break;
                case 67:
                    E.human.isFalling = false;
                    Preference.inputListener.clnProces();
                    Preference.inputListener.addListener(E.menu.game);
                    Preference.inputListener.addListener(E.menu.load);
                    Preference.inputListener.addListener(E.menu.exit);
                    Preference.inputListener.addListener(E.menu);
                    E.human.fallingTimer.stop();
                    E.screen = 2;
                    break;
            }
            E.selectBoxs.DWNKey = -1;
        }
    }
}
