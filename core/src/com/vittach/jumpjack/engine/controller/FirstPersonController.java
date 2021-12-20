package com.vittach.jumpjack.engine.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;

import java.util.HashSet;

public class FirstPersonController implements ProcessorInput {
    private final float MOVE_VELOCITY = 0.1f;
    private final float FAST_VELOCITY = 1.0f;
    private final Preferences preferencesInstance = Preferences.getInstance();

    private int deviceId;
    private int idOffset = 0;
    private int health = 100;
    private final PerspectiveCamera camera;

    private static final int FLY = Input.Keys.F;
    private static final int LEFT = Input.Keys.A;
    private static final int RIGHT = Input.Keys.D;
    private static final int FORWARD = Input.Keys.W;
    private static final int BACK = Input.Keys.S;
    private static final int DOWN = Input.Keys.SPACE;
    private static final int JUMP = Input.Keys.ALT_LEFT;
    private static final int RUN = Input.Keys.SHIFT_LEFT;

    private static final int ESCAPE = Input.Keys.ESCAPE;

    private float velocity = MOVE_VELOCITY;
    private float cameraNewDelta;

    public int viewDistance = 64;

    public HashSet<Integer> pressedKeys;

    public FirstPersonController(int deviceId) {
        this.deviceId = deviceId;

        camera = new PerspectiveCamera(67, MainEngine.getInstance().renderWidth, MainEngine.getInstance().renderHeight);

        pressedKeys = new HashSet<Integer>();

        float[] position = new float[]{0, 0, 0};

        camera.near = 0.1f;
        camera.far = viewDistance * 2f;
        camera.position.set(position);
        camera.update();
    }

    @Override
    public void setIDOffset(int idOffset) {
        this.idOffset = idOffset;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean keyDown(int code) {
        if (code == FLY && pressedKeys.contains(FLY)) {
            pressedKeys.remove(code);
            return true;
        }

        return pressedKeys.add(code);
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
        return pressedKeys.remove(code);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        float verticalSense = 1;
        float cameraDelta = -Gdx.input.getDeltaY() * verticalSense;
        Vector3 right = new Vector3().set(camera.direction).crs(camera.up);

        if (cameraNewDelta + cameraDelta < 90 && cameraNewDelta + cameraDelta > -90) {
            camera.rotate(right, cameraDelta);
            cameraNewDelta += cameraDelta;
        } else {
            if (cameraNewDelta + cameraDelta >= 90.0f) {
                camera.rotate(right, 89.0f - cameraNewDelta);
                cameraNewDelta = 89f;
            } else {
                camera.rotate(right, -90.f - cameraNewDelta);
                cameraNewDelta = -90;
            }
        }

        float horizontalSense = 1;
        Vector3 up = new Vector3().set(0, 1, 0);
        camera.rotate(up, -Gdx.input.getDeltaX() * horizontalSense);
        camera.update();
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return preferencesInstance.inputIdMap.remove(pointer + idOffset);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        boolean screenCornersLeft = x > 0 && x < preferencesInstance.screenWidth / 4f
                && y > preferencesInstance.screenHeight - preferencesInstance.screenHeight / 4.f
                && y < preferencesInstance.screenHeight;

        boolean screenCornersRight = x > preferencesInstance.screenWidth - preferencesInstance.screenWidth / 4f
                && x < preferencesInstance.screenWidth
                && y > preferencesInstance.screenHeight - preferencesInstance.screenHeight / 4.f
                && y < preferencesInstance.screenHeight;

        if (!(screenCornersLeft || screenCornersRight) || deviceId == 0) {
            preferencesInstance.inputIdMap.add(pointer + idOffset);

            if (button == Input.Buttons.LEFT) {
                deleteCube();
            } else if (button == Input.Buttons.RIGHT) {
                detectCube();
            }
        }

        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        if (preferencesInstance.inputIdMap.contains(pointer + idOffset)) {
            mouseMoved(x, y);
        }
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
        Vector3 normal = new Vector3();

        if (pressedKeys.contains(FLY)) {
            if (pressedKeys.contains(DOWN)) move.add(normal.set(0, 1, 0).scl(-velocity));
            else if (pressedKeys.contains(JUMP)) move.add(normal.set(0, 1, 0).scl(velocity));
        }

        if (pressedKeys.contains(FORWARD)) {
            move.add(normal.set(camera.direction.x, 0, camera.direction.z).setLength2(1).scl(velocity));
        } else if (pressedKeys.contains(BACK)) {
            move.add(normal.set(camera.direction.x, 0, camera.direction.z).setLength2(1).scl(-velocity));
        }

        if (pressedKeys.contains(RUN)) velocity = FAST_VELOCITY;

        if (pressedKeys.contains(LEFT)) {
            move.add(normal.set(camera.direction.x, 0, camera.direction.z)
                    .setLength2(1)
                    .crs(0, 1, 0)
                    .scl(-velocity)
            );
        } else if (pressedKeys.contains(RIGHT)) {
            move.add(normal.set(camera.direction.x, 0, camera.direction.z)
                    .setLength2(1)
                    .crs(0, 1, 0)
                    .scl(velocity)
            );
        }

        // движение перса по диагонали
        if ((pressedKeys.contains(RIGHT) || pressedKeys.contains(LEFT))
                && (pressedKeys.contains(BACK) || pressedKeys.contains(FORWARD))) {
            move.setLength2(1).scl(velocity);
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

    private void detectCube() {
    }

    private void checkCollision() {
    }
}