package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.vittach.jumpjack.framework.ColorImpl;
import com.vittach.jumpjack.framework.MyPoint;
import com.vittach.jumpjack.framework.MyTimer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Control implements ProcessorInput {
    private boolean isStop;
    MyTimer joyTimer;
    private float coefficientWidth;
    private float coefficientHeight;
    private MyPoint[] startPositions = new MyPoint[2];

    private int we, he, ew, eh;
    private int xPos, yPos, offset = 0;
    private int ID = -1, joystickPosition;

    Control(int joyXY) {
        isStop = true;
        joystickPosition = joyXY;
        joyTimer = new MyTimer();
    }

    public boolean joy_states() {
        if (isStop) {
            return false;
        }
        else {
            return isStop = true;
        }
    }

    public boolean get_joyvis() {
        joyTimer.isActive();
        return joyTimer.visible;
    }

    MyPoint get_pos(int number) {
        return startPositions[number];
    }

    @Override
    public void setIDOffset(int set) {
        offset = set;
    }

    @Override
    public boolean
    mouseMoved(int screenx, int screeny) {
        return true;
    }

    @Override
    public boolean scrolled(int mnt) {
        return true;
    }

    @Override
    public boolean keyTyped(char ch) {
        return true;
    }

    @Override
    public boolean keyDown(int ikey) {
        return true;
    }

    @Override
    public boolean keyUp(int id_key) {
        return true;
    }

    @Override
    public boolean
    touchUp(int x, int y, int p, int btn) {
        Preference.usedID.remove(p + offset);
        if (!joyTimer.isActive() && ID >= 0) {
            ID = -1;
            isStop = false;
            joyTimer.start(2000);
            startPositions[1] = startPositions[0];
        }
        return true;
    }

    public void setJP() {
        ew = (Preference.width - Preference.screenWidth) / 2;
        coefficientWidth = (float) Preference.width / (float) Preference.screenWidth;//установка чувствительности
        eh = (Preference.height - Preference.screenHeight) / 2;
        coefficientHeight = (float) Preference.height / (float) Preference.screenHeight;//установка чувствительности
    }

    //печать грибковых джойстика
    void joyPrint(ColorImpl clr) {
        if (get_joyvis()) {
            int Sradius, Lradius;
            Gdx.gl.glEnable(3042);
            Gdx.gl.glBlendFunc(770, 771);
            ShapeRenderer shape = new ShapeRenderer();
            Sradius = (Lradius = Preference.screenWidth / 20) / 2;
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(clr.color());

            shape.circle(get_pos(0).getX() + we, Preference.screenHeight - get_pos(0).getY() + he, Lradius);
            shape.end();
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.circle(get_pos(1).getX() + we, Preference.screenHeight - get_pos(1).getY() + he, Sradius);
            shape.end();
            shape.dispose();//itrash
        }
    }


    //сдвиг по X
    public float analogX() {
        int hgh = Preference.screenHeight;
        return (!joyTimer.isActive() && get_joyvis()) ? (xPos - startPositions[0].getX()) / (hgh / 256f) : 0;
    }

    //сдвиг по Y
    public float analogY() {
        int hgh = Preference.screenHeight;
        return (!joyTimer.isActive() && get_joyvis()) ? (yPos - startPositions[0].getY()) / (hgh / 256f) : 0;
    }

    @Override
    public boolean
    touchDown(int x, int y, int numb, int b) {
        xPos = x -= ew;
        yPos = y -= eh;
        if (!Preference.usedID.contains(numb + offset))
            if ((x > 0 && x < Preference.screenWidth / 4 && y > Preference.screenHeight - Preference.screenHeight / 4
                    && y < Preference.screenHeight && joystickPosition == 0) ||
                    (joystickPosition == 1 && x > Preference.screenWidth - Preference.screenWidth / 4 && x < Preference.screenWidth
                            && y > Preference.screenHeight - Preference.screenHeight / 4 && y < Preference.screenHeight)) {
                //сделай видимым джойстик(do visible joy)
                if (!joyTimer.visible) {
                    joyTimer.visible = true;
                    ID = numb;
                    we = (int) ((x - x / coefficientWidth) * coefficientWidth);
                    //координаты для джойстика
                    startPositions[0] = new MyPoint(x, y);
                    startPositions[1] = new MyPoint(x, y);
                    y = Preference.screenHeight - y;
                    he = (int) ((y - y / coefficientHeight) * coefficientHeight);
                } else if (xPos >= startPositions[0].getX() - Preference.screenWidth / 20
                        && xPos <= startPositions[0].getX() + Preference.screenWidth / 20 &&
                        yPos >= startPositions[0].getY() - Preference.screenWidth / 20
                        && yPos <= startPositions[0].getY() + Preference.screenWidth / 20) {
                    startPositions[1] = new MyPoint(x, yPos);
                    joyTimer.stop();
                    ID = numb;
                }
                Preference.usedID.add(numb + offset);
            }
        return true;
    }

    @Override
    public boolean
    touchDragged(int x, int y, int numb) {
        xPos = x -= ew;
        yPos = y -= eh;
        if ((x > 0 && x < Preference.screenWidth / 4 && y > Preference.screenHeight - Preference.screenHeight / 4
                && y < Preference.screenHeight && joystickPosition == 0) ||
                (joystickPosition == 1 && x > Preference.screenWidth - Preference.screenWidth / 4 && x < Preference.screenWidth
                        && y > Preference.screenHeight - Preference.screenHeight / 4f && y < Preference.screenHeight))
            if (Preference.usedID.contains(numb + offset)) {
                //сделай видимым джойстик(do visible joy)
                if (!joyTimer.visible) {
                    joyTimer.visible = true;
                    ID = numb;
                    we = (int) ((x - x / coefficientWidth) * coefficientWidth);
                    //координаты для джойстика
                    startPositions[0] = new MyPoint(x, y);
                    startPositions[1] = new MyPoint(x, y);
                    y = Preference.screenHeight - y;
                    he = (int) ((y - y / coefficientHeight) * coefficientHeight);
                } else if (x >= startPositions[0].getX() - Preference.screenWidth / 20
                        && x <= startPositions[0].getX() + Preference.screenWidth / 20 &&
                        y >= startPositions[0].getY() - Preference.screenWidth / 20
                        && y <= startPositions[0].getY() + Preference.screenWidth / 20 && ID < 0)
                    ID = numb;//запишу ID пальца
            }
        if (numb == ID) {
            if (joyTimer.isActive())
                joyTimer.stop();
            //если палец на джойстике
            if ((xPos - startPositions[0].getX()) * (xPos - startPositions[0].getX())
                    + (yPos - startPositions[0].getY()) * (yPos - startPositions[0].getY()) <
                    Math.pow(Preference.screenWidth / 20, 2))
                startPositions[1] = new MyPoint(x, y);
            else {
                double numerat = (x - startPositions[0].getX());
                double denomin = Math.sqrt((xPos - startPositions[0].getX()) * (xPos - startPositions[0].getX()) +
                        (yPos - startPositions[0].getY()) * (yPos - startPositions[0].getY()));
                double angle = Math.acos(numerat / denomin);
                if (y < startPositions[0].getY()) angle = -angle;
                startPositions[1] = new MyPoint
                        (startPositions[0].getX() + Preference.screenWidth / 20 * Math.cos(angle),
                                startPositions[0].getY() + Preference.screenWidth / 20 * Math.sin(angle));
            }
        }
        return true;
    }
}