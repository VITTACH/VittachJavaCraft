package com.vittach.jumpjack.engine.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;
import com.vittach.jumpjack.framework.TouchPoint;

import java.util.HashSet;

public class FirstPersonController implements ProcessorInput {
    private static final int FLY = Input.Keys.F;
    private static final int LEFT = Input.Keys.A;
    private static final int RIGHT = Input.Keys.D;
    private static final int FORWARD = Input.Keys.W;
    private static final int BACK = Input.Keys.S;
    private static final int DOWN = Input.Keys.SPACE;
    private static final int JUMP = Input.Keys.ALT_LEFT;
    private static final int RUN = Input.Keys.SHIFT_LEFT;
    private static final int ESCAPE = Input.Keys.ESCAPE;

    private final float MOVE_VELOCITY = 0.1f;
    private final float FAST_VELOCITY = 1.0f;
    private final Preferences preferenceInstance = Preferences.getInstance();

    private TouchPoint touchPoint;

    private MainEngine.Device device;
    private int offsetX, offsetY;
    private int idOffset = 0;

    private final PerspectiveCamera camera;

    private float velocity = MOVE_VELOCITY;
    private float cameraNewDelta;

    private int health = 100;
    public int viewDistance = 64;

    public HashSet<Integer> keySet;

    public FirstPersonController(MainEngine.Device device) {
        this.device = device;

        MainEngine engineInstance = MainEngine.getInstance();
        camera = new PerspectiveCamera(67, engineInstance.renderWidth, engineInstance.renderHeight);

        keySet = new HashSet<Integer>();

        float[] position = new float[]{0, 0, 0};

        camera.near = 0.1f;
        camera.far = viewDistance * 2f;
        camera.position.set(position);
        camera.update();
    }

    @Override
    public void setIdOffset(int idOffset) {
        this.idOffset = idOffset;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean keyDown(int code) {
        if (code == FLY && keySet.contains(FLY)) {
            keySet.remove(code);
            return true;
        }

        return keySet.add(code);
    }

    @Override
    public boolean keyTyped(char code) {
        return false;
    }

    @Override
    public boolean keyUp(int code) {
        if (code == RUN) {
            velocity = MOVE_VELOCITY;
        } else if (code == FLY) {
            return true;
        }
        return keySet.remove(code);
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        handleTouchDragged(Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int id, int button) {
        preferenceInstance.inputIdMap.remove(id + idOffset);
        return true;
    }

    @Override
    public boolean touchDown(int x, int y, int id, int button) {
        x -= offsetX;
        y -= offsetY;

        if (touchPoint == null) {
            touchPoint = new TouchPoint(x, y);
        }
        handleTouchDown(x, y, id, button);
        touchPoint.updatePoint(x, y);
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int id) {
        if (!preferenceInstance.inputIdMap.contains(id + idOffset)) return true;

        x -= offsetX;
        y -= offsetY;

        handleTouchDragged(x - touchPoint.getX(), y - touchPoint.getY());
        touchPoint.updatePoint(x, y);
        return true;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public PerspectiveCamera getFpCamera() {
        return camera;
    }

    public void deleteCube() {
    }

    public void handleInput() {
        Vector3 move = new Vector3();
        Vector3 up = new Vector3();

        if (keySet.contains(LEFT)) {
            move.add(up.set(camera.direction.x, 0, camera.direction.z).setLength2(1).crs(0, 1, 0).scl(-velocity));
        } else if (keySet.contains(RIGHT)) {
            move.add(up.set(camera.direction.x, 0, camera.direction.z).setLength2(1).crs(0, 1, 0).scl(velocity));
        }

        if (keySet.contains(RUN)) {
            velocity = FAST_VELOCITY;
        }

        if (keySet.contains(FLY)) {
            if (keySet.contains(DOWN)) {
                move.add(up.set(0, 1, 0).scl(-velocity));
            } else if (keySet.contains(JUMP)) {
                move.add(up.set(0, 1, 0).scl(velocity));
            }
        }

        // движение перса по диагонали
        if ((keySet.contains(RIGHT) || keySet.contains(LEFT)) && (keySet.contains(BACK) || keySet.contains(FORWARD))) {
            move.setLength2(1).scl(velocity);
        }

        if (keySet.contains(FORWARD)) {
            move.add(up.set(camera.direction.x, 0, camera.direction.z).setLength2(1).scl(velocity));
        } else if (keySet.contains(BACK)) {
            move.add(up.set(camera.direction.x, 0, camera.direction.z).setLength2(1).scl(-velocity));
        }

        camera.translate(move);
        camera.update();
    }

    public void onEscape(Boolean isPressed) {
        if (isPressed) keyDown(ESCAPE);
        else keyUp(ESCAPE);
    }

    public void onLeft(Boolean isPressed) {
        if (isPressed) keyDown(LEFT);
        else keyUp(LEFT);
    }

    public void onRight(Boolean isPressed) {
        if (isPressed) keyDown(RIGHT);
        else keyUp(RIGHT);
    }

    public void onForward(Boolean isPressed) {
        if (isPressed) keyDown(FORWARD);
        else keyUp(FORWARD);
    }

    public void onBack(Boolean isPressed) {
        if (isPressed) keyDown(BACK);
        else keyUp(BACK);
    }

    public void onDown(Boolean isPressed) {
        if (isPressed) keyDown(DOWN);
        else keyUp(DOWN);
    }

    public void onJump(Boolean isPressed) {
        if (isPressed) keyDown(JUMP);
        else keyUp(JUMP);
    }

    public void onFly(Boolean isPressed) {
        if (isPressed) keyDown(FLY);
        else keyUp(FLY);
    }

    public void onRun(Boolean isPressed) {
        if (isPressed) keyDown(RUN);
        else keyUp(RUN);
    }

    public void updateAspectRatio() {
        offsetX = (preferenceInstance.displayWidth - preferenceInstance.screenWidth) / 2;
        offsetY = (preferenceInstance.displayHeight - preferenceInstance.screenHeight) / 2;
    }

    private void handleTouchDown(int x, int y, int id, int button) {
        float widthPart4 = preferenceInstance.screenWidth / 4f;
        float heightPart4 = preferenceInstance.screenHeight / 4f;

        boolean isNewId = !preferenceInstance.inputIdMap.contains(id + idOffset);
        boolean isInLeftArea = x > 0 && x < widthPart4 && y > preferenceInstance.screenHeight - heightPart4
            && y < preferenceInstance.screenHeight;
        boolean isInRightArea = x > preferenceInstance.screenWidth - widthPart4 && x < preferenceInstance.screenWidth
            && y > preferenceInstance.screenHeight - heightPart4 && y < preferenceInstance.screenHeight;

        if (!isNewId || isInLeftArea || isInRightArea) return;
        preferenceInstance.inputIdMap.add(id + idOffset);

        if (button == Input.Buttons.LEFT) {
            deleteCube();
        } else if (button == Input.Buttons.RIGHT) {
            detectCube();
        }
    }

    private void handleTouchDragged(int x, int y) {
        float verticalSense = 1;
        float delta = -y * verticalSense;
        Vector3 right = new Vector3().set(camera.direction).crs(camera.up);

        if (cameraNewDelta + delta < 90 && cameraNewDelta + delta > -90) {
            camera.rotate(right, delta);
            cameraNewDelta += delta;
        } else {
            if (cameraNewDelta + delta >= 90.0f) {
                camera.rotate(right, 89.0f - cameraNewDelta);
                cameraNewDelta = 89f;
            } else {
                camera.rotate(right, -90.f - cameraNewDelta);
                cameraNewDelta = -90;
            }
        }

        camera.rotate(new Vector3().set(0, 1, 0), -x);
        camera.update();
    }

    private void detectCube() {
    }

    private void checkCollision() {
    }
}