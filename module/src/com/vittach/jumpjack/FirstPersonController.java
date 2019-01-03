package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.vittach.jumpjack.framework.MyTimer;
import com.badlogic.gdx.graphics.PerspectiveCamera;

import java.util.HashSet;

/**
 * Created by ZHARIKOV VITALIY at 22.02.2016.
 */

class FirstPersonController implements ProcessorInput, ValueOfHealth {
    PerspectiveCamera camera;
    private Vector3 humanVector;
    private Vector3 humanMoveVector;
    private int xMapLength;
    private int yMapLength;
    private int zMapLength;

    public float camOldPositionY;
    public float camNewPositionY;

    HashSet<Integer> pressedKey;
    private char[][][] worldMap;
    MyTimer builderTimer;
    MyTimer fallingTimer;
    private MyTimer jumpingTimer;
    private float depth = 0.02f;
    private float b_size = 0.5f;
    private int offset = 0;
    private boolean stayingOnGround;
    private float humanMoveVelocity;
    private int startX;
    private int startY;
    private int startZ;
    private int endX;
    private int endY;
    private int endZ;
    private float humanFallVelocity;
    private float oldFallVelocity;
    float oldVelocity;
    boolean isFalling = false;
    private int step;
    int selectedBlockX = -1, selectedBlockY = -1, selectedBlockZ = -1;

    int deathWinState;
    private int healthValue;
    private int deviceId;
    private int FORWARD = Input.Keys.W;
    private int BACK = Input.Keys.S;
    private int LEFT = Input.Keys.A;
    private int RGHT = Input.Keys.D;
    int FLY = Input.Keys.F;
    private int SIT_DOWN = Input.Keys.SPACE;
    private int RUN = Input.Keys.SHIFT_LEFT;
    private int JUMP = Input.Keys.ALT_LEFT;

    // показатель здоровья персонажа
    public int getHealthValue() {
        return healthValue;
    }

    public void setHealthValue(int value) {
        healthValue = value;
    }

    public void teleportation() {
        if (camera.position.y < -10) {
            camera.position.set(0, 3, 0);
            fallingTimer.start(10000);
        }
    }

    void setWorld(char[][][] worldMap) {
        this.worldMap = worldMap;
        xMapLength = worldMap.length;
        yMapLength = worldMap[1].length;
        zMapLength = worldMap[1][1].length;
    }

    @Override
    public void setIDOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public boolean keyDown(int key_D) {
        if (key_D == FLY)
            if (pressedKey.contains(FLY)) {
                pressedKey.remove(key_D);
                return true;
            }
        return pressedKey.add(key_D);
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean keyTyped(char ichar) {
        return false;
    }

    @Override
    public boolean keyUp(int key_U) {
        if (key_U == JUMP) jumpingTimer.stop();
        if (key_U == RUN) humanMoveVelocity = 0.08f;
        if (key_U == FLY) return true;
        return pressedKey.remove(key_U);
    }

    public PerspectiveCamera getCamera() {
        if (deathWinState == 0) {
            controlHuman();

            camera.translate(humanMoveVector);

            detectRegion(false);

            detectCollision();

            detectRegion(true);

            detectBlock(false);

            if (builderTimer.visible) {
                deleteBlock();
            }

            teleportation();
            camera.update();
        }

        return camera;
    }

    @Override
    public boolean touchUp(int x, int y, int p, int d) {
        if (builderTimer.isActive()) {
            // на случай, если управление будет сенсорным
            if (deviceId == 1) detectBlock(true);
            builderTimer.stop();
        }
        builderTimer.visible = false;
        selectedBlockX = selectedBlockY = selectedBlockZ = -1;
        return Preference.getInstance().usedID.remove(p + offset);
    }

    private void detectRegion(boolean collision) {
        humanVector = new Vector3();

        //территория взаимодействия персонажа
        startX = (int) ((-camera.position.x + xMapLength / 2 * b_size) / b_size);
        startZ = (int) ((-camera.position.z + zMapLength / 2 * b_size) / b_size);
        startY = (int) ((camera.position.y + b_size * 2) / b_size);

        if (collision) {
            endX = startX + 5;
            startX = startX - 3;
            if (startX < 0) startX = 0;
            else if (endX > xMapLength) {
                endX = xMapLength;
            }
            endZ = startZ + 5;
            startZ = startZ - 3;
            if (startZ < 0) startZ = 0;
            else if (endZ > zMapLength) {
                endZ = zMapLength;
            }
            endY = startY + 4;
            startY = startY - 4;
            if (startY < 0) startY = 0;
            else if (endY > yMapLength) {
                endY = yMapLength;
            }
        } else {
            endX = startX + 2;
            startX = startX - 2;
            if (startX < 0) startX = 0;
            else if (endX > xMapLength) {
                endX = xMapLength;
            }
            endZ = startZ + 2;
            startZ = startZ - 2;
            if (startZ < 0) startZ = 0;
            else if (endZ > zMapLength) {
                endZ = zMapLength;
            }
            endY = startY + 2;
            startY = startY - 1;
            if (startY < 0) startY = 0;
            else if (endY > yMapLength) {
                endY = yMapLength;
            }
        }
    }

    @Override
    public boolean mouseMoved(int screen_x, int screen_y) {
        float verticalSense = 1;
        camOldPositionY = -Gdx.input.getDeltaY() * verticalSense;
        Vector3 worldUpVector = new Vector3().set(0, 1, 0);
        Vector3 worldRightVector = new Vector3().set(camera.direction).crs(camera.up);

        if (camNewPositionY + camOldPositionY < 90.f && camNewPositionY + camOldPositionY > -90f) {
            camera.rotate(worldRightVector, camOldPositionY);
            camNewPositionY += camOldPositionY;
        } else {
            if (camNewPositionY + camOldPositionY >= 90) {
                camera.rotate(worldRightVector, 89 - camNewPositionY);
                camNewPositionY = 89;
            } else {
                camera.rotate(worldRightVector,-90 - camNewPositionY);
                camNewPositionY = -90;
            }
        }
        float horizontalSense = 1;
        camera.rotate(worldUpVector, -Gdx.input.getDeltaX() * horizontalSense);
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int id) {
        if (Preference.getInstance().usedID.contains(id + offset)) mouseMoved(x, y);
        return true;
    }

    @Override
    public boolean touchDown(int x, int y, int touchId, int button) {
        if (!((x > 0 && x < Preference.getInstance().screenWidth / 4
                        && y > Preference.getInstance().screenHeight - Preference.getInstance().screenHeight / 4
                        && y < Preference.getInstance().screenHeight)
                || (x > Preference.getInstance().screenWidth - Preference.getInstance().screenWidth / 4f
                        && x < Preference.getInstance().screenWidth
                        && y > Preference.getInstance().screenHeight - Preference.getInstance().screenHeight / 4
                        && y < Preference.getInstance().screenHeight)
                ) || deviceId == 0) {

            Preference.getInstance().usedID.add(touchId + offset);
            if (button == Input.Buttons.LEFT) {
                deleteBlock();
            }
            if (button == Input.Buttons.RIGHT) {
                detectBlock(true);
            }
        }
        return true;
    }

    FirstPersonController(int deviceId) {
        this.deviceId = deviceId;
        fallingTimer = new MyTimer();
        jumpingTimer = new MyTimer();
        builderTimer = new MyTimer();

        camera = new PerspectiveCamera(67, JJEngine.getInstance().renderWidth, JJEngine.getInstance().renderHeight);

        pressedKey = new HashSet<Integer>();
        camera.lookAt(0, 0, 0);
        camera.position.set(0, 3, 0);
        camera.near = 0.1f;
        camera.update();
        humanFallVelocity = 0.30f;
        oldFallVelocity = humanFallVelocity;
        humanMoveVelocity = 0.08f;
        oldVelocity = humanMoveVelocity;
        step = (int) (4f * b_size / depth);
    }

    //возвращае символьный идентификатор блока по координате
    private char getBlockId(int x, int y, int z) {
        if (x < 0f || x > xMapLength || y < 0f || y > yMapLength || z < 0 || z > zMapLength) return 'o';
        return worldMap[x][y][z];
    }

    //метод управления протагонистом
    private void controlHuman() {
        humanVector = new Vector3();
        humanMoveVector = new Vector3();

        if (pressedKey.contains(RUN)) humanMoveVelocity = 0.2f;

        if (pressedKey.contains(FLY)) {
            if (isFalling) isFalling = false;
            // ручное спускание персонажа
            if (pressedKey.contains(SIT_DOWN))
                humanMoveVector.add(humanVector.set(0, 1f, 0).scl(-oldVelocity));
            if (pressedKey.contains(JUMP))
                humanMoveVector.add(humanVector.set(0, 1f, 0f).scl(oldVelocity));
        }

        // движение персонажа влево
        if (pressedKey.contains(LEFT)) {
            humanMoveVector.add(humanVector.set(camera.direction.x, 0f, camera.direction.z).setLength(1)
                    .crs(0, 1, 0).scl(-humanMoveVelocity));
        }
        // движение персонажа вправо
        if (pressedKey.contains(RGHT)) {
            humanMoveVector.add(humanVector.set(camera.direction.x, 0f, camera.direction.z).setLength(1)
                    .crs(0, 1, 0).scl(humanMoveVelocity));
        }

        // движение персонажа вперед
        if (pressedKey.contains(FORWARD)) {
            humanMoveVector.add(humanVector.set(camera.direction.x, 0, camera.direction.z).setLength(1f)
                    .scl(humanMoveVelocity));
        }

        // движение персонажа назад
        if (pressedKey.contains(BACK)) {
            humanMoveVector.add(humanVector.set(camera.direction.x, 0, camera.direction.z).setLength(1f)
                    .scl(-humanMoveVelocity));
        }

        // движение перса по диагонали
        if ((pressedKey.contains(RGHT) || pressedKey.contains(LEFT))
                && (pressedKey.contains(BACK) || pressedKey.contains(FORWARD))) {
            humanMoveVector.setLength(1).scl(humanMoveVelocity);
        }

        // прыжок персонажа на верх
        if (!pressedKey.contains(FLY)) {
            if (jumpingTimer.isActive()) humanMoveVector.add(humanVector.set(0, 1, 0).scl(oldVelocity));
            else if (!stayingOnGround) {
                if (!isFalling) {
                    fallingTimer.start(10000);
                    isFalling = true;
                    oldFallVelocity = 0;
                }
                if (fallingTimer.isActive())
                    oldFallVelocity = (1.f / 4.f) * fallingTimer.getCurTime();
                if (oldFallVelocity >= humanFallVelocity || !fallingTimer.isActive())
                    oldFallVelocity = humanFallVelocity;
                humanMoveVector.add(humanVector.set(0, 1, 0).scl(-oldFallVelocity));
            } else {
                if (isFalling) {
                    isFalling = false;
                }
                if (pressedKey.contains(JUMP)) {
                    jumpingTimer.start(1000);
                }
            }
        }
    }

    // определение блока, на который смотрит персонаж
    private void detectBlock(boolean collision) {
        Vector3 camPosition = camera.position;
        JJEngine.getInstance().worldMapInst.clearHighlights();
        java_goto:
        for (int i = 0; i < step; i++) {
            humanVector.set(camera.direction.x, camera.direction.y, camera.direction.z).setLength(1f).scl((i + 1) * depth);
            for (int x = startX; x < endX; x++)
                for (int y = startY; y < endY; y++)
                    for (int z = startZ; z < endZ; z++) {
                        if (getBlockId(x, y, z) != 'o') {
                            //столкновение с front стенкой
                            if (camPosition.z + humanVector.z <= -z * b_size + zMapLength / 2 * b_size + b_size / 2f
                                    && camPosition.x + humanVector.x > -x * b_size + xMapLength / 2 * b_size - b_size / 2
                                    && -x * b_size + b_size / 2 + xMapLength / 2 * b_size >= humanVector.x + camPosition.x
                                    && camPosition.z + humanVector.z >= -z * b_size + zMapLength / 2 * b_size + b_size / 2 - depth
                                    && camPosition.y + humanVector.y > (y - 3) * b_size + b_size / 2
                                    && camPosition.y + humanVector.y < y * b_size - 3 * b_size / 2) {
                                if (getBlockId(x, y, z) != 'N' && getBlockId(x, y, z) != 'A' && getBlockId(x, y, z) != 'L') {
                                    if (collision) JJEngine.getInstance().worldMapInst.setBlock(x, y, z - 1);
                                    else JJEngine.getInstance().worldMapInst.selectBlock(2, x, y, z);
                                    return;
                                } else continue java_goto;
                            }
                            //столкновение с backButton стенкой
                            if (camPosition.z + humanVector.z >= -z * b_size + zMapLength / 2 * b_size - b_size / 2f
                                    && camPosition.x + humanVector.x > -x * b_size + xMapLength / 2 * b_size - b_size / 2
                                    && -x * b_size + b_size / 2 + xMapLength / 2 * b_size >= humanVector.x + camPosition.x
                                    && camPosition.z + humanVector.z <= -z * b_size + zMapLength / 2 * b_size - b_size / 2 + depth
                                    && camPosition.y + humanVector.y > (y - 3) * b_size + b_size / 2
                                    && camPosition.y + humanVector.y < y * b_size - 3 * b_size / 2) {
                                if (getBlockId(x, y, z) != 'N' && getBlockId(x, y, z) != 'A' && getBlockId(x, y, z) != 'L') {
                                    if (collision) JJEngine.getInstance().worldMapInst.setBlock(x, y, z + 1);
                                    else JJEngine.getInstance().worldMapInst.selectBlock(3, x, y, z);
                                    return;
                                } else continue java_goto;
                            }
                            //столкновение с left стенкой
                            if (camPosition.x + humanVector.x >= -x * b_size + xMapLength / 2 * b_size - b_size / 2f
                                    && camPosition.z + humanVector.z > zMapLength / 2 * b_size - z * b_size - b_size / 2f
                                    && camPosition.z + humanVector.z <= -z * b_size + zMapLength / 2 * b_size + b_size / 2
                                    && camPosition.x + humanVector.x <= -x * b_size + xMapLength / 2 * b_size - b_size / 2 + depth
                                    && camPosition.y + humanVector.y > (y - 3) * b_size + b_size / 2
                                    && y * b_size - 1.5 * b_size > humanVector.y + camPosition.y) {
                                if (getBlockId(x, y, z) != 'N' && getBlockId(x, y, z) != 'A' && getBlockId(x, y, z) != 'L') {
                                    if (collision) JJEngine.getInstance().worldMapInst.setBlock(x + 1, y, z);
                                    else JJEngine.getInstance().worldMapInst.selectBlock(4, x, y, z);
                                    return;
                                } else continue java_goto;
                            }
                            //столкновение с mouseRightVector стенкой
                            if (camPosition.x + humanVector.x <= -x * b_size + xMapLength / 2 * b_size + b_size / 2f
                                    && camPosition.z + humanVector.z > zMapLength / 2 * b_size - z * b_size - b_size / 2f
                                    && camPosition.z + humanVector.z <= -z * b_size + zMapLength / 2 * b_size + b_size / 2
                                    && camPosition.x + humanVector.x >= -x * b_size + xMapLength / 2 * b_size + b_size / 2 - depth
                                    && camPosition.y + humanVector.y > (y - 3) * b_size + b_size / 2
                                    && y * b_size - 1.5 * b_size > humanVector.y + camPosition.y) {
                                if (getBlockId(x, y, z) != 'N' && getBlockId(x, y, z) != 'A' && getBlockId(x, y, z) != 'L') {
                                    if (collision) JJEngine.getInstance().worldMapInst.setBlock(x - 1, y, z);
                                    else JJEngine.getInstance().worldMapInst.selectBlock(5, x, y, z);
                                    return;
                                } else continue java_goto;
                            }
                            //столкновение с верхней стенкой
                            if (camPosition.y + humanVector.y > (y - 3) * b_size + b_size / 2
                                    && camPosition.y + humanVector.y < y * b_size + depth - b_size / 0.4
                                    && camPosition.x + humanVector.x >= -x * b_size + xMapLength / 2f * b_size - b_size / 2
                                    && camPosition.x + humanVector.x < b_size / 2f - x * b_size + xMapLength / 2f * b_size
                                    && humanVector.z + camPosition.z - b_size / 2 >= zMapLength / 2 * b_size - z * b_size - b_size
                                    && camPosition.z + humanVector.z - b_size / 2.0f < zMapLength / 2.0 * b_size - z * b_size) {
                                if (!collision) {
                                    JJEngine.getInstance().worldMapInst.selectBlock(1, x, y, z);
                                    JJEngine.getInstance().worldMapInst.selectBlock(6, x, y, z);
                                } else if (getBlockId(x, y, z) != 'N' && getBlockId(x, y, z) != 'A' && getBlockId(x, y, z) != 'L')
                                    JJEngine.getInstance().worldMapInst.setBlock(x, y - 1, z);
                                return;
                            }
                            //столкновение с нижней стенкой
                            if (camPosition.y + humanVector.y < (y - 1) * b_size - b_size / 2
                                    && camPosition.y + humanVector.y > y * b_size - depth - b_size / 0.6
                                    && camPosition.x + humanVector.x >= -x * b_size + xMapLength / 2f * b_size - b_size / 2
                                    && camPosition.x + humanVector.x < b_size / 2f - x * b_size + xMapLength / 2f * b_size
                                    && humanVector.z + camPosition.z - b_size / 2 >= zMapLength / 2 * b_size - z * b_size - b_size
                                    && camPosition.z + humanVector.z - b_size / 2.0f < zMapLength / 2.0 * b_size - z * b_size) {
                                if (getBlockId(x, y, z) != 'N' && getBlockId(x, y, z) != 'A' && getBlockId(x, y, z) != 'L') {
                                    if (collision) JJEngine.getInstance().worldMapInst.setBlock(x, y + 1, z);
                                    else JJEngine.getInstance().worldMapInst.selectBlock(0, x, y, z);
                                    return;
                                } else continue java_goto;
                            }
                        }
                    }
        }
    }

    // метод, определяющий взаимодейтсвие игрока с элентами на карте
    private void detectCollision() {
        Vector3 camPosition = camera.position;
        stayingOnGround = false;
        for (int x = startX; x < endX; x++)
            for (int y = startY; y < endY; y++)
                for (int z = startZ; z < endZ; z++)
                    if (getBlockId(x, y, z) != 'o' && getBlockId(x, y, z) != 'L')
                        //условие прибывания на блоках
                        if (camPosition.x < -(x - 1) * b_size + xMapLength / 2 * b_size - humanMoveVelocity &&
                                camPosition.x >= -x * b_size + xMapLength / 2 * b_size - b_size + humanMoveVelocity &&
                                camPosition.z >= -z * b_size + zMapLength / 2 * b_size - b_size + humanMoveVelocity &&
                                camPosition.z < -(z - 1) * b_size + zMapLength / 2 * b_size - humanMoveVelocity) {
                            if (getBlockId(x, y, z) != 'N' && getBlockId(x, y, z) != 'A') {
                                if (camPosition.y < y * b_size && camPosition.y >= y * b_size - humanFallVelocity) {
                                    camPosition.set(camPosition.x, y * b_size, camPosition.z);
                                    stayingOnGround = true;
                                }
                            } else if (camPosition.y < (y - 1) * b_size &&
                                    camPosition.y >= (y - 1) * b_size - humanFallVelocity) {
                                camPosition.set(camPosition.x, (y - 1) * b_size, camPosition.z);
                                stayingOnGround = true;
                            }
                            if (stayingOnGround) {
                                x = xMapLength;
                                y = yMapLength;
                                break;
                            }
                        }
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                for (int z = startZ; z < endZ; z++) {
                    if (getBlockId(x, y, z) != 'o' && getBlockId(x, y, z) != 'L') {
                        //столкновение головой сверху
                        if (camPosition.y > (y - 3) * b_size &&
                                camPosition.y <= (y - 3) * b_size + humanMoveVelocity &&
                                camPosition.x >= -x * b_size + xMapLength / 2 * b_size - b_size + humanMoveVelocity &&
                                camPosition.z >= -z * b_size + zMapLength / 2 * b_size - b_size + humanMoveVelocity &&
                                camPosition.x < -(x - 1) * b_size + xMapLength / 2 * b_size - humanMoveVelocity &&
                                camPosition.z < -(z - 1) * b_size + zMapLength / 2 * b_size - humanMoveVelocity) {
                            camPosition.set(camPosition.x, (y - 3) * b_size, camPosition.z);
                            jumpingTimer.stop();
                        }
                        //условие столкновения с блоками
                        if ((camPosition.y > (y - 3) * b_size + humanMoveVelocity && camPosition.y < y * b_size &&
                                getBlockId(x, y, z) != 'N' && getBlockId(x, y, z) != 'A') ||
                                (camPosition.y > (y - 3) * b_size + humanMoveVelocity && camPosition.y < (y - 1) *
                                        b_size && (getBlockId(x, y, z) == 'N' || getBlockId(x, y, z) == 'A'))) {
                            //столкновение с backButton стеной
                            if (camPosition.z >= -z * b_size + zMapLength / 2 * b_size - b_size &&
                                    camPosition.z <= -z * b_size + zMapLength / 2 * b_size - b_size + humanMoveVelocity &&
                                    camPosition.x >= -x * b_size + xMapLength / 2 * b_size - b_size + humanMoveVelocity / 2 &&
                                    camPosition.x < -(x - 1) * b_size + xMapLength / 2 * b_size - humanMoveVelocity / 2)
                                camPosition.set(camPosition.x, camPosition.y, -z * b_size + zMapLength / 2 * b_size - b_size);
                            //столкновеие с front стеной
                            if (camPosition.z <= -z * b_size + zMapLength / 2 * b_size + b_size &&
                                    camPosition.z >= -z * b_size + zMapLength / 2 * b_size + b_size - humanMoveVelocity &&
                                    camPosition.x >= -x * b_size + xMapLength / 2 * b_size - b_size + humanMoveVelocity / 2 &&
                                    camPosition.x < -(x - 1) * b_size + xMapLength / 2 * b_size - humanMoveVelocity / 2)
                                camPosition.set(camPosition.x, camPosition.y, -z * b_size + zMapLength / 2 * b_size + b_size);
                            //столкновение с left стеной
                            if (camPosition.x >= -x * b_size + xMapLength / 2 * b_size - b_size &&
                                    camPosition.x <= -x * b_size + xMapLength / 2 * b_size - b_size + humanMoveVelocity &&
                                    camPosition.z >= -z * b_size + zMapLength / 2 * b_size - b_size + humanMoveVelocity / 2 &&
                                    camPosition.z < -(z - 1) * b_size + zMapLength / 2 * b_size - humanMoveVelocity / 2)
                                camPosition.set(-x * b_size + xMapLength / 2 * b_size - b_size, camPosition.y, camPosition.z);
                            //столкновение с mouseRightVector стеной
                            if (camPosition.x <= -x * b_size + xMapLength / 2 * b_size + b_size &&
                                    camPosition.x >= -x * b_size + xMapLength / 2 * b_size + b_size - humanMoveVelocity &&
                                    camPosition.z >= -z * b_size + zMapLength / 2 * b_size - b_size + humanMoveVelocity / 2 &&
                                    camPosition.z < -(z - 1) * b_size + zMapLength / 2 * b_size - humanMoveVelocity / 2)
                                camPosition.set(-x * b_size + xMapLength / 2 * b_size + b_size, camPosition.y, camPosition.z);
                        }
                    }
                    //попадание на специальные блоки
                    if (camPosition.y <= (y - 1) * b_size && camPosition.y > (y - 1) * b_size - humanMoveVelocity &&
                            camPosition.x >= -x * b_size + xMapLength / 2 * b_size - b_size + humanMoveVelocity &&
                            camPosition.z >= -z * b_size + zMapLength / 2 * b_size - b_size + humanMoveVelocity &&
                            camPosition.x < -(x - 1) * b_size + xMapLength / 2 * b_size - humanMoveVelocity &&
                            camPosition.z < -(z - 1) * b_size + zMapLength / 2 * b_size - humanMoveVelocity) {
                        if (getBlockId(x, y, z) == 'N') deathWinState = 2;
                        if (getBlockId(x, y, z) == 'A') healthValue = 100;
                    }
                }
            }
        }
    }

    //метод, удляющий с карты указанный, направлением взгляда, блок
    private void deleteBlock() {
        Vector3 cameraPosition = camera.position;
        int i;
        for (i = 0; i < step; i++) {
            humanVector.set(camera.direction.x, camera.direction.y, camera.direction.z).scl((i + 1) * depth);
            for (int x = startX; x < endX; x++)
                for (int y = startY; y < endY; y++)
                    for (int z = startZ; z < endZ; z++)
                        if (getBlockId(x, y, z) != 'o')
                            if (cameraPosition.x + humanVector.x >= -x * b_size + xMapLength / 2 * b_size - b_size / 2
                                    && cameraPosition.x + humanVector.x < -x * b_size + b_size / 2 + xMapLength / 2 * b_size
                                    && cameraPosition.z + humanVector.z >= -z * b_size + zMapLength / 2 * b_size - b_size / 1.9
                                    && cameraPosition.z + humanVector.z < -z * b_size + b_size / 1.9 + zMapLength / 2 * b_size
                                    && ((y - 3) * b_size + b_size / 1.9 < humanVector.y + cameraPosition.y
                                    && cameraPosition.y + humanVector.y < (y - 1) * b_size - b_size / 1.9f
                                    && getBlockId(x, y, z) != 'N'
                                    && getBlockId(x, y, z) != 'A'
                                    && getBlockId(x, y, z) != 'L'
                                    || cameraPosition.y + humanVector.y > (y - 3) * b_size + b_size / 2.f
                                    && cameraPosition.y + humanVector.y < y * b_size + depth - b_size / 0.4)) {

                                // one block of worldMapInst selected
                                if (!builderTimer.isActive()) {
                                    if (!(selectedBlockX == x && selectedBlockY == y && selectedBlockZ == z)) {
                                        selectedBlockX = x;
                                        selectedBlockY = y;
                                        selectedBlockZ = z;
                                        builderTimer.visible = true;
                                        builderTimer.start(1000);
                                    }

                                    if (!builderTimer.visible) {
                                        JJEngine.getInstance().worldMapInst.delBlock(x, y, z);
                                    }
                                } else {
                                    if (!(selectedBlockX == x && selectedBlockY == y && selectedBlockZ == z)) {
                                        selectedBlockX = x;
                                        selectedBlockY = y;
                                        selectedBlockZ = z;
                                        builderTimer.start(1000);
                                    }
                                }

                                x = xMapLength;
                                y = yMapLength;
                                i = step + 1;
                                break;
                            }
        }
        if (i == step) {
            selectedBlockX = selectedBlockY = selectedBlockZ = -1;
            builderTimer.visible = false;
            builderTimer.stop();
        }
    }
}