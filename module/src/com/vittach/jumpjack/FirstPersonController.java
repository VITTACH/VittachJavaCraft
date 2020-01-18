package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.PerspectiveCamera;

import java.util.HashSet;

class FirstPersonController implements ProcessorInput, HumanHealth {
    private final float MOVE_VELOCITY = 0.1f;
    private final float FAST_VELOCITY = 1.0f;
    private final Preference prefInst = Preference.getInstance();

    private int deviceId;
    private int idOffset = 0;
    private int health = 100;
    private final PerspectiveCamera camera;

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

    public int viewDistance = 64;

    public HashSet<Integer> pressedKeys;

    public FirstPersonController(int deviceId) {
        this.deviceId = deviceId;

        camera = new PerspectiveCamera(67, JJEngine.getInstance().renderWidth, JJEngine.getInstance().renderHeight);

        pressedKeys = new HashSet<Integer>();

        camera.position.set(0, 0, 0);
        camera.near = 0.1f;
        camera.update();
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
        Vector3 upVector = new Vector3().set(0, 1, 0);
        camera.rotate(upVector, -Gdx.input.getDeltaX() * horizontalSense);
        camera.update();
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return prefInst.usedInputIdMap.remove(pointer + idOffset);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        boolean screenCornersLeft = x > 0 && x < prefInst.screenWidth / 4f
                && y > prefInst.screenHeight - prefInst.screenHeight / 4.f
                && y < prefInst.screenHeight;

        boolean screenCornersRight = x > prefInst.screenWidth - prefInst.screenWidth / 4f
                && x < prefInst.screenWidth
                && y > prefInst.screenHeight - prefInst.screenHeight / 4.f
                && y < prefInst.screenHeight;

        if (!(screenCornersLeft || screenCornersRight) || deviceId == 0) {
            prefInst.usedInputIdMap.add(pointer + idOffset);

            if (button == Input.Buttons.LEFT) {
                deleteBlock();
            } else if (button == Input.Buttons.RIGHT) {
                detectBlock();
            }
        }

        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        if (prefInst.usedInputIdMap.contains(pointer + idOffset)) {
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

    public PerspectiveCamera getCamera() {
        controlCam();
        detectBlock();

        return camera;
    }

    private void controlCam() {
        Vector3 move = new Vector3();
        Vector3 normal = new Vector3();

        if (pressedKeys.contains(FLY)) {
            if (pressedKeys.contains(DOWN)) {
                move.add(normal.set(0, 1, 0).scl(-velocity));
            } else if (pressedKeys.contains(JUMP)) {
                move.add(normal.set(0, 1, 0).scl(velocity));
            }
        }

        if (pressedKeys.contains(RUN)) velocity = FAST_VELOCITY;

        if (pressedKeys.contains(LEFT)) {
            move.add(normal.set(camera.direction.x, 0, camera.direction.z)
                    .setLength(1).crs(0, 1, 0).scl(-velocity));
        } else if (pressedKeys.contains(RIGHT)) {
            move.add(normal.set(camera.direction.x, 0, camera.direction.z)
                    .setLength(1f).crs(0, 1, 0).scl(velocity));
        }

        if (pressedKeys.contains(FORWARD)) {
            move.add(normal.set(camera.direction.x, 0, camera.direction.z)
                    .setLength(1f).scl(velocity));
        } else if (pressedKeys.contains(BACK)) {
            move.add(normal.set(camera.direction.x, 0, camera.direction.z)
                    .setLength(1).scl(-velocity));
        }

        // движение перса по диагонали
        if ((pressedKeys.contains(RIGHT) || pressedKeys.contains(LEFT))
                && (pressedKeys.contains(BACK) || pressedKeys.contains(FORWARD))) {
            move.setLength(1).scl(velocity);
        }

        camera.translate(move);
        camera.update();
    }

    private void detectBlock() {
    }

    private void checkCollision() {
    }

    private void deleteBlock() {
    }
}