package com.vittach.jumpjack;

import com.badlogic.gdx.utils.viewport.Viewport;

public class GameStop implements GameScreen {
    public void render(Viewport view) {
        JJEngine.getInstance().blockSelector.display(view);
        if (JJEngine.getInstance().blockSelector.pressedKey > 0) {
            switch (JJEngine.getInstance().blockSelector.pressedKey) {
                case 131:
                    Preference.getInstance().inputListener.cleanProcesses();
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().currentBlock);
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().human);

                    JJEngine.getInstance().human.selectedBlockY = JJEngine.getInstance().human.selectedBlockZ = -1;
                    JJEngine.getInstance().human.builderTimer.visible = false;
                    JJEngine.getInstance().human.builderTimer.stop();
                    JJEngine.getInstance().human.selectedBlockX = -1;
                    JJEngine.getInstance().currentScreen = 0;
                    break;
                case 67:
                    JJEngine.getInstance().human.isFalling = false;
                    Preference.getInstance().inputListener.cleanProcesses();
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().startMenuInst.gameButton);
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().startMenuInst.loadButton);
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().startMenuInst.exitButton);
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().startMenuInst);
                    JJEngine.getInstance().human.fallingTimer.stop();
                    JJEngine.getInstance().currentScreen = 2;
                    break;
            }
            JJEngine.getInstance().blockSelector.pressedKey = -1;
        }
    }
}
