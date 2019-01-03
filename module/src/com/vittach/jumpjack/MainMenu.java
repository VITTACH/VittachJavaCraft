package com.vittach.jumpjack;

import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenu implements GameScreen {
    public void render(Viewport view) {
        JJEngine.getInstance().startMenuInst.Display(view);

        if (JJEngine.getInstance().startMenuInst.pressedKey >= 0) {
            Preference.getInstance().inputListener.cleanProcesses();

            switch (JJEngine.getInstance().startMenuInst.pressedKey) {
                //loadButton old worldMapInst
                case 2:
                    JJEngine.getInstance().currentScreen = 4;
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().fileExplorer.backButton);
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().fileExplorer.loadButton);
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().fileExplorer.acceptButton);
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().fileExplorer);
                    JJEngine.getInstance().fileExplorer.updateDir();
                    JJEngine.getInstance().fileExplorer.getCurDir();
                    break;
                //createButton new worldMapInst
                case 1:
                    JJEngine.getInstance().currentScreen = 3;
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().worldCreator.createButton);
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().worldCreator.goBackButton);
                    Preference.getInstance().inputListener.addListener(JJEngine.getInstance().worldCreator);
                    break;
            }

            JJEngine.getInstance().startMenuInst.pressedKey = -1;
        }
    }
}
