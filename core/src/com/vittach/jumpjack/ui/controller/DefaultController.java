package com.vittach.jumpjack.ui.controller;

import com.vittach.jumpjack.Preferences;

public class DefaultController {

    private final Preferences preferenceInstance = Preferences.getInstance();

    private final float widthPart4 = preferenceInstance.screenWidth / 4f;
    private final float heightPart4 = preferenceInstance.screenHeight / 4f;

    protected int idOffset = 0;

    boolean isNewId;
    boolean isInLeftArea;
    boolean isInRightArea;

    public void checkTouchArea(int x, int y, int id) {
        isNewId = !preferenceInstance.inputIdMap.contains(id + idOffset);
        isInLeftArea = x > 0 && x < widthPart4 && y > preferenceInstance.screenHeight - heightPart4
            && y < preferenceInstance.screenHeight;
        isInRightArea = x > preferenceInstance.screenWidth - widthPart4 && x < preferenceInstance.screenWidth
            && y > preferenceInstance.screenHeight - heightPart4 && y < preferenceInstance.screenHeight;
    }
}
