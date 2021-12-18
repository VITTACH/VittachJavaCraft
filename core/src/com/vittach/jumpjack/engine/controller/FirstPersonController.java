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
    private final Preferences prefInstance = Preferences.getInstance();

    private int deviceId;
    private int idOffset = 0;
    private int health = 100;
    private final PerspectiveCamera fpCamera;

    private int FLY = Input.Keys.F;
    private int LEFT = Input.Keys.A;
    private int RIGHT = Input.Keys.D;
    private int FORWARD = Input.Keys.W;
    private int BACK = Input.Keys.S;
    private int DOWN = Input.Keys.SPACE;
    private int JUMP = Input.Keys.ALT_LEFT;
    private int RUN = Input.Keys.SHIFT_LEFT;

    private float velocity = MOVE_VELOCITY;
    private float cameraNewDelta;

    public int viewDistance = 96;

    public HashSet<Integer> pressedKeys;

    public FirstPersonController(int deviceId) {
        this.deviceId = deviceId;

        fpCamera = new PerspectiveCamera(67, MainEngine.getInstance().renderWidth, MainEngine.getInstance().renderHeight);

        pressedKeys = new HashSet<Integer>();

        float[] position = new float[]{0, 0, 0};

        fpCamera.near = 0.1f;
        fpCamera.far = viewDistance * 2f;
        fpCamera.position.set(position);
        fpCamera.update();
    }

    @Override
    public void setIDOffset(int idOffset) {
        this.idOffset = idOffset;
    }

    @Override
    public boolean scrolled(int amount) {
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
        Vector3 right = new Vector3().set(fpCamera.direction).crs(fpCamera.up);

        if (cameraNewDelta + cameraDelta < 90 && cameraNewDelta + cameraDelta > -90) {
            fpCamera.rotate(right, cameraDelta);
            cameraNewDelta += cameraDelta;
        } else {
            if (cameraNewDelta + cameraDelta >= 90.0f) {
                fpCamera.rotate(right, 89.0f - cameraNewDelta);
                cameraNewDelta = 89f;
            } else {
                fpCamera.rotate(right, -90.f - cameraNewDelta);
                cameraNewDelta = -90;
            }
        }

        float horizontalSense = 1;
        Vector3 up = new Vector3().set(0, 1, 0);
        fpCamera.rotate(up, -Gdx.input.getDeltaX() * horizontalSense);
        fpCamera.update();
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return prefInstance.usedInputIdMap.remove(pointer + idOffset);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        boolean screenCornersLeft = x > 0 && x < prefInstance.screenWidth / 4f
                && y > prefInstance.screenHeight - prefInstance.screenHeight / 4.f
                && y < prefInstance.screenHeight;

        boolean screenCornersRight = x > prefInstance.screenWidth - prefInstance.screenWidth / 4f
                && x < prefInstance.screenWidth
                && y > prefInstance.screenHeight - prefInstance.screenHeight / 4.f
                && y < prefInstance.screenHeight;

        if (!(screenCornersLeft || screenCornersRight) || deviceId == 0) {
            prefInstance.usedInputIdMap.add(pointer + idOffset);

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
        if (prefInstance.usedInputIdMap.contains(pointer + idOffset)) {
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
        moveCamera();
        detectCube();

        return fpCamera;
    }

    private void detectCube() {
    }

    private void checkCollision() {
    }

    private void deleteCube() {
    }

    private void moveCamera() {
        Vector3 move = new Vector3();
        Vector3 normal = new Vector3();

        if (pressedKeys.contains(FLY)) {
            if (pressedKeys.contains(DOWN)) move.add(normal.set(0, 1, 0).scl(-velocity));
            else if (pressedKeys.contains(JUMP)) move.add(normal.set(0, 1, 0).scl(velocity));
        }

        if (pressedKeys.contains(FORWARD)) {
            move.add(normal.set(fpCamera.direction.x, 0, fpCamera.direction.z).setLength2(1).scl(velocity));
        } else if (pressedKeys.contains(BACK)) {
            move.add(normal.set(fpCamera.direction.x, 0, fpCamera.direction.z).setLength2(1).scl(-velocity));
        }

        if (pressedKeys.contains(RUN)) velocity = FAST_VELOCITY;

        if (pressedKeys.contains(LEFT)) {
            move.add(normal.set(fpCamera.direction.x, 0, fpCamera.direction.z)
                    .setLength2(1)
                    .crs(0, 1, 0)
                    .scl(-velocity)
            );
        } else if (pressedKeys.contains(RIGHT)) {
            move.add(normal.set(fpCamera.direction.x, 0, fpCamera.direction.z)
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

        fpCamera.translate(move);
        fpCamera.update();
    }
}