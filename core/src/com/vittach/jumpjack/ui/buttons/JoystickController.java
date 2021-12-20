package com.vittach.jumpjack.ui.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;
import com.vittach.jumpjack.engine.controller.ProcessorInput;
import com.vittach.jumpjack.framework.ColorImpl;
import com.vittach.jumpjack.framework.MyPoint;
import com.vittach.jumpjack.framework.MyTimer;
import com.vittach.jumpjack.framework.TimerListener;

public class JoystickController implements ProcessorInput {
    public enum Stick {
        LEFT, RIGHT
    }

    private float scaleX, scaleY;
    private final MyPoint[] points = new MyPoint[2];
    private boolean isTouchUpped = true;
    private boolean isSetUpped = false;

    private final Stick stick;
    private int offsetX, offsetY;
    private int mouseX, mouseY, idOffset = 0;
    private int touchId = -1;

    private int smallRadius, largeRadius;

    private final ColorImpl whiteColor = new ColorImpl(1, 1, 1, 0.5f);

    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferenceInstance = Preferences.getInstance();

    private final MyTimer timer;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public JoystickController(Stick stick) {
        this.stick = stick;
        timer = new MyTimer();
    }

    @Override
    public void setIDOffset(int idOffset) {
        this.idOffset = idOffset;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean keyDown(int keyId) {
        return true;
    }

    @Override
    public boolean keyUp(int keyId) {
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int id, int button) {
        preferenceInstance.inputIdMap.remove(id + idOffset);
        if (!isTimerRunning() && touchId >= 0) {
            touchId = -1;
            isTouchUpped = false;
            timer.start(2000);
            points[1] = points[0];
        }
        return true;
    }

    @Override
    public boolean touchDown(int x, int y, int id, int b) {
        mouseX = x -= offsetX;
        mouseY = y -= offsetY;

        handleTouchDown(x, y, id, false);
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int id) {
        mouseX = x -= offsetX;
        mouseY = y -= offsetY;

        handleTouchDown(x, y, id, true);

        if (id == touchId) {
            if (isTimerRunning()) {
                timer.reset();
            }

            double square = Math.pow((mouseX - points[0].getX()), 2) + Math.pow((mouseY - points[0].getY()), 2);

            if (square < Math.pow(largeRadius, 2)) {
                points[1] = new MyPoint(x, y);
            } else {
                double angle = Math.acos((x - points[0].getX()) / Math.sqrt(square));
                if (y < points[0].getY()) {
                    angle = -angle;
                }
                points[1] = new MyPoint(
                        points[0].getX() + largeRadius * Math.cos(angle),
                        points[0].getY() + largeRadius * Math.sin(angle)
                );
            }
        }
        return true;
    }

    public void display(Viewport viewport) {
        if (isSetUpped) {
            Gdx.gl.glEnable(3042);
            Gdx.gl.glBlendFunc(770, 771);

            viewport.apply();
            shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            float x = points[0].getX() * scaleX;
            float y = engineInstance.renderHeight - points[0].getY() * scaleY;
            shapeRenderer.setColor(whiteColor.getColor());
            shapeRenderer.circle(x, y, largeRadius / 2f);
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            x = points[1].getX() * scaleX;
            y = engineInstance.renderHeight - points[1].getY() * scaleY;
            shapeRenderer.circle(x, y, smallRadius);
            shapeRenderer.end();
        }
    }

    public void handleInput() {
        switch (stick) {
            case LEFT:
                if (isTouchUpped()) {
                    engineInstance.fpController.onRight(false);
                    engineInstance.fpController.onForward(false);
                    engineInstance.fpController.onBack(false);
                    engineInstance.fpController.onLeft(false);
                } else if (!isTimerRunning() && isSetUpped) {
                    engineInstance.fpController.onRight(engineInstance.leftStick.distanceX() > 5);
                    engineInstance.fpController.onBack(engineInstance.leftStick.distanceY() > 5);
                    engineInstance.fpController.onLeft(engineInstance.leftStick.distanceX() < -5);
                    engineInstance.fpController.onForward(engineInstance.leftStick.distanceY() < -5);
                }
                break;

            case RIGHT:
                if (isTouchUpped()) {
                    engineInstance.fpController.onDown(false);
                    engineInstance.fpController.onJump(false);
                    engineInstance.fpController.onFly(false);
                    engineInstance.fpController.onRun(false);
                } else if (!isTimerRunning() && isSetUpped) {
                    engineInstance.fpController.onDown(engineInstance.rightStick.distanceX() > 5);
                    engineInstance.fpController.onFly(engineInstance.rightStick.distanceY() > 5);
                    engineInstance.fpController.onRun(engineInstance.rightStick.distanceY() < -5);
                    engineInstance.fpController.onJump(engineInstance.rightStick.distanceX() < -5);
                }
                break;
        }
    }

    private boolean isTouchUpped() {
        if (isTouchUpped) {
            return false;
        } else {
            return isTouchUpped = true;
        }
    }

    private boolean isTimerRunning() {
        return timer.isActive(new TimerListener() {
            @Override
            public void onTimerStopped() {
                isSetUpped = false;
            }
        });
    }

    private void handleTouchDown(int x, int y, int id, boolean isDragged) {
        float widthHalf4 = preferenceInstance.screenWidth / 4f;
        float heightHalf4 = preferenceInstance.screenHeight / 4f;
        boolean isInLeftStickArea = x > 0 && x < widthHalf4 && y > preferenceInstance.screenHeight - widthHalf4 && y < preferenceInstance.screenHeight;
        boolean isInRightStickArea = x > preferenceInstance.screenWidth - widthHalf4 && x < preferenceInstance.screenWidth && y > preferenceInstance.screenHeight - heightHalf4 && y < preferenceInstance.screenHeight;
        boolean isNewId = !preferenceInstance.inputIdMap.contains(id + idOffset);
        if (isNewId && ((stick == Stick.LEFT && isInLeftStickArea) || (stick == Stick.RIGHT && isInRightStickArea))) {
            if (!isSetUpped) {
                isSetUpped = true;
                touchId = id;
                points[0] = new MyPoint(x, y);
                points[1] = new MyPoint(x, y);
            } else if (mouseX >= points[0].getX() - largeRadius
                    && mouseX <= points[0].getX() + largeRadius
                    && mouseY >= points[0].getY() - largeRadius
                    && mouseY <= points[0].getY() + largeRadius
            ) {
                if (!isDragged) {
                    points[1] = new MyPoint(x, mouseY);
                    timer.reset();
                }
                touchId = id;
            }
            preferenceInstance.inputIdMap.add(id + idOffset);
        }
    }

    public void setJoystickPosition() {
        scaleX = engineInstance.renderWidth / (float) preferenceInstance.screenWidth;
        scaleY = engineInstance.renderHeight / (float) preferenceInstance.screenHeight;

        offsetX = (preferenceInstance.displayWidth - preferenceInstance.screenWidth) / 2;
        offsetY = (preferenceInstance.displayHeight - preferenceInstance.screenHeight) / 2;

        smallRadius = (largeRadius = preferenceInstance.screenWidth / 20) / 2;
    }

    private float distanceX() {
        if (!isTimerRunning() && isSetUpped) {
            return (mouseX - points[0].getX()) / (preferenceInstance.screenHeight / 256f);
        } else return 0f;
    }

    private float distanceY() {
        if (!isTimerRunning() && isSetUpped) {
            return (mouseY - points[0].getY()) / (preferenceInstance.screenHeight / 256f);
        } else return 0f;
    }
}