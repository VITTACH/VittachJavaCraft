package com.vittach.jumpjack;

import com.vittach.jumpjack.framework.ColorImpl;
import com.vittach.jumpjack.framework.ImageHandler;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HealthBar extends ScreenButton {
    private final ValueOfHealth valueOfHealthInst;
    private int oldHealthValue;

    private ImageHandler leftUpPiece;
    private ImageHandler rightUpPiece;
    private ImageHandler leftDownPiece;
    private ImageHandler rightDownPiece;

    HealthBar(ValueOfHealth valueOfHealthInst) {
        this.valueOfHealthInst = valueOfHealthInst;
        leftUpPiece = new ImageHandler();
        rightUpPiece = new ImageHandler();
        leftDownPiece = new ImageHandler();
        rightDownPiece = new ImageHandler();

        color = new ColorImpl(1f, 0.84f, 0, 1f);

        font.load("jumpjack.ttf");
        font.setPixelSizes(6);

        background.load("healthBar/healthBox.png");
        leftUpPiece.load("healthBar/leftUpPiece.png");
        rightUpPiece.load("healthBar/rightUpPiece.png");
        leftDownPiece.load("healthBar/leftDownPiece.png");
        rightDownPiece.load("healthBar/rightDownPiece.png");
    }

    private void chgHealth() {
        int healthValue = valueOfHealthInst.getHealthValue();

        if (healthValue != oldHealthValue) {
            textMessage = String.valueOf(healthValue);
            backgroundHandler.blit(background);
            oldHealthValue = healthValue;
            if (healthValue > 99) {
                font.setPixelSizes(6);
                textX = 15;
                textY = 27;
            } else if (healthValue >= 10 && healthValue < 100) {
                font.setPixelSizes(8);
                textX = 18;
                textY = 29;
            } else {
                font.setPixelSizes(8);
                textX = 22;
                textY = 29;
            }

            if (healthValue > 75) {
                backgroundHandler.blit(4, 4, leftDownPiece);
                backgroundHandler.blit(27, 4, rightDownPiece);
                backgroundHandler.blit(4, 26, leftUpPiece);
                backgroundHandler.blit(27, 26, rightUpPiece);
            }
            if (healthValue > 50 && healthValue <= 75) {
                backgroundHandler.blit(4, 4, leftDownPiece);
                backgroundHandler.blit(27, 4, rightDownPiece);
                backgroundHandler.blit(27, 26, rightUpPiece);
            }
            if (healthValue > 25 && healthValue <= 50) {
                backgroundHandler.blit(27, 4, rightDownPiece);
                backgroundHandler.blit(27, 26, rightUpPiece);
            }
            if (healthValue > 0 && healthValue <= 25)
                backgroundHandler.blit(27, 26, rightUpPiece);
        }
    }

    public void display(Viewport viewport) {
        chgHealth();
        MyDisplay(viewport);
    }

    public void dispose() {
        leftUpPiece.dispose();
        rightUpPiece.dispose();
        leftDownPiece.dispose();
        rightDownPiece.dispose();
        super.dispose();
    }
}
