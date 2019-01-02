package com.vittach.jumpjack;

import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenu implements GameScreen {
    private JJEngine E;

    MainMenu(JJEngine E) {
        this.E = E;
    }

    public void
    render(Viewport view) {
        E.menu.Display(view);
        if (E.menu.DWNKey >= 0) {
            Preference.inputListener.clnProces();
            switch (E.menu.DWNKey) {
                //loadButton old world
                case 2:
                    E.screen = 4;
                    Preference.inputListener.addListener(E.explorer.backButton);
                    Preference.inputListener.addListener(E.explorer.loadButton);
                    Preference.inputListener.addListener(E.explorer.acceptButton);
                    Preference.inputListener.addListener(E.explorer);
                    E.explorer.updateDir();
                    E.explorer.getCurDir();
                    break;
                //create new world
                case 1:
                    E.screen = 3;
                    Preference.inputListener.addListener(E.news.create);
                    Preference.inputListener.addListener(E.news.goback);
                    Preference.inputListener.addListener(E.news);
                    break;
            }
            E.menu.DWNKey = -1;
        }
    }
}
