package com.vittach.jumpjack;

import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadSave implements GameScreen {
    public void
    render(Viewport view) {
        JJEngine.getInstance().fileExplorer.Display(view);
        if (JJEngine.getInstance().fileExplorer.pressedKey >= 0) {
            Preference.getInstance().inputListener.cleanProcesses();
            switch (JJEngine.getInstance().fileExplorer.pressedKey) {
                case 1:
                    JJEngine.getInstance().currentScreen = 0;

                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().human);
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().currentBlock);
                    JJEngine.getInstance().fileController.setFileName(JJEngine.getInstance().fileExplorer.getName());
                    JJEngine.getInstance().fileController.loadWorld();
                    break;
                case 2:
                    JJEngine.getInstance().currentScreen = 2;
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().startMenuInst.gameButton);
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().startMenuInst.loadButton);
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().startMenuInst.exitButton);
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().startMenuInst);
                    break;
            }
            JJEngine.getInstance().fileExplorer.pressedKey = -1;
        }
    }
}
