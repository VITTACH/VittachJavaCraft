package com.vittach.jumpjack.ui.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;
import com.vittach.jumpjack.engine.controller.ProcessorInput;
import com.vittach.jumpjack.framework.ColorImpl;
import com.vittach.jumpjack.framework.MyTimer;
import com.vittach.jumpjack.framework.TimerListener;
import com.vittach.jumpjack.framework.TouchPoint;

public class JoystickController implements ProcessorInput {
    public enum Stick {
        LEFT, RIGHT
    }

    private final TouchPoint[] touchPoints = new TouchPoint[2];
    private boolean hasTouchUpped = true;
    private boolean hasFirstTouch = false;

    private final Stick stick;
    private int offsetX, offsetY;
    private int touchX, touchY, idOffset = 0;
    private int touchId = -1;

    private final ColorImpl whiteColor = new ColorImpl(1, 1, 1, 0.5f);

    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferenceInstance = Preferences.getInstance();

    private final MyTimer joystickTimer;

    private final int largeRadius = engineInstance.renderHeight / 5;
    private final int smallRadius = largeRadius / 2;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public JoystickController(Stick stick) {
        this.stick = stick;
        joystickTimer = new MyTimer();
    }

    @Override
    public void setIdOffset(int idOffset) {
        this.idOffset = idOffset;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
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
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int id, int button) {
        preferenceInstance.inputIdMap.remove(id + idOffset);
        if (!isTimerActive() && touchId >= 0) {
            touchId = -1;
            hasTouchUpped = false;
            joystickTimer.start(2000);
            touchPoints[1] = touchPoints[0];
        }
        return true;
    }

    @Override
    public boolean touchDown(int x, int y, int id, int b) {
        touchX = x -= offsetX;
        touchY = y -= offsetY;

        handleTouchDown(x, y, id, false);
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int id) {
        if (!preferenceInstance.inputIdMap.contains(id + idOffset)) return true;

        touchX = x -= offsetX;
        touchY = y -= offsetY;

        handleTouchDown(x, y, id, true);

        if (id != touchId) return true;

        double radiusSquare = Math.pow((x - touchPoints[0].getX()), 2) + Math.pow((y - touchPoints[0].getY()), 2);
        double angle = Math.acos((x - touchPoints[0].getX()) / Math.sqrt(radiusSquare));

        if (isTimerActive()) joystickTimer.finish();

        if (radiusSquare >= Math.pow(largeRadius, 2)) {
            if (y < touchPoints[0].getY()) {
                angle = -angle;
            }
            x = (int) (touchPoints[0].getX() + largeRadius * Math.cos(angle));
            y = (int) (touchPoints[0].getY() + largeRadius * Math.sin(angle));
        }
        touchPoints[1] = new TouchPoint(x, y);

        return true;
    }

    public void display(Viewport viewport) {
        if (!hasFirstTouch) return;

        float scaleX = engineInstance.renderWidth / (float) preferenceInstance.screenWidth;
        float scaleY = engineInstance.renderHeight / (float) preferenceInstance.screenHeight;

        Gdx.gl.glEnable(3042);
        Gdx.gl.glBlendFunc(770, 771);

        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        float x = touchPoints[0].getX() * scaleX;
        float y = engineInstance.renderHeight - touchPoints[0].getY() * scaleY;
        shapeRenderer.setColor(whiteColor.getColor());
        shapeRenderer.circle(x, y, largeRadius / 2f);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        x = touchPoints[1].getX() * scaleX;
        y = engineInstance.renderHeight - touchPoints[1].getY() * scaleY;
        shapeRenderer.circle(x, y, smallRadius);
        shapeRenderer.end();
    }

    public void handleInput() {
        switch (stick) {
            case LEFT:
                if (isHasTouchUpped()) {
                    engineInstance.fpController.onRight(false);
                    engineInstance.fpController.onForward(false);
                    engineInstance.fpController.onBack(false);
                    engineInstance.fpController.onLeft(false);
                } else if (!isTimerActive() && hasFirstTouch) {
                    engineInstance.fpController.onRight(engineInstance.leftStick.distanceX() > 5);
                    engineInstance.fpController.onBack(engineInstance.leftStick.distanceY() > 5);
                    engineInstance.fpController.onLeft(engineInstance.leftStick.distanceX() < -5);
                    engineInstance.fpController.onForward(engineInstance.leftStick.distanceY() < -5);
                }
                break;

            case RIGHT:
                if (isHasTouchUpped()) {
                    engineInstance.fpController.onDown(false);
                    engineInstance.fpController.onJump(false);
                    engineInstance.fpController.onFly(false);
                    engineInstance.fpController.onRun(false);
                } else if (!isTimerActive() && hasFirstTouch) {
                    engineInstance.fpController.onDown(engineInstance.rightStick.distanceX() > 5);
                    engineInstance.fpController.onFly(engineInstance.rightStick.distanceY() > 5);
                    engineInstance.fpController.onRun(engineInstance.rightStick.distanceY() < -5);
                    engineInstance.fpController.onJump(engineInstance.rightStick.distanceX() < -5);
                }
                break;
        }
    }

    public void updateAspectRatio() {
        offsetX = (preferenceInstance.displayWidth - preferenceInstance.screenWidth) / 2;
        offsetY = (preferenceInstance.displayHeight - preferenceInstance.screenHeight) / 2;
    }

    private boolean isHasTouchUpped() {
        if (hasTouchUpped) {
            return false;
        } else {
            return hasTouchUpped = true;
        }
    }

    private boolean isTimerActive() {
        return joystickTimer.isActive(new TimerListener() {
            @Override
            public void onTimerStopped() {
                hasFirstTouch = false;
            }
        });
    }

    private void handleTouchDown(int x, int y, int id, boolean isDragged) {
        float widthPart4 = preferenceInstance.screenWidth / 4f;
        float heightPart4 = preferenceInstance.screenHeight / 4f;

        boolean isNewId = !preferenceInstance.inputIdMap.contains(id + idOffset);
        boolean isInLeftArea = x > 0 && x < widthPart4 && y > preferenceInstance.screenHeight - heightPart4
            && y < preferenceInstance.screenHeight;
        boolean isInRightArea = x > preferenceInstance.screenWidth - widthPart4 && x < preferenceInstance.screenWidth
            && y > preferenceInstance.screenHeight - heightPart4 && y < preferenceInstance.screenHeight;

        if (!isNewId || !((stick == Stick.LEFT && isInLeftArea) || (stick == Stick.RIGHT && isInRightArea))) return;
        preferenceInstance.inputIdMap.add(id + idOffset);

        if (!hasFirstTouch) {
            hasFirstTouch = true;
            touchId = id;
            touchPoints[0] = new TouchPoint(x, y);
            touchPoints[1] = touchPoints[0];
        } else if (x >= touchPoints[0].getX() - largeRadius
            && x <= touchPoints[0].getX() + largeRadius
            && y >= touchPoints[0].getY() - largeRadius
            && y <= touchPoints[0].getY() + largeRadius
        ) {
            if (!isDragged) {
                touchPoints[1] = new TouchPoint(x, touchY);
                joystickTimer.finish();
            }
            touchId = id;
        }
    }

    private float distanceX() {
        if (!isTimerActive() && hasFirstTouch) {
            return (touchX - touchPoints[0].getX()) / (preferenceInstance.screenHeight / 256f);
        } else return 0;
    }

    private float distanceY() {
        if (!isTimerActive() && hasFirstTouch) {
            return (touchY - touchPoints[0].getY()) / (preferenceInstance.screenHeight / 256f);
        } else return 0;
    }
}