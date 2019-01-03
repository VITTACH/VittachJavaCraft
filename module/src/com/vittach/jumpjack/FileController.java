package com.vittach.jumpjack;

import com.badlogic.gdx.math.Vector3;

import java.io.*;

public class FileController implements Serializable {

    private int healthValue;
    private char[][][] worldMap;
    private transient WorldMap map;
    private transient String fileName;

    private Vector3 upVector;
    private Vector3 vectorCamDirection;
    private Vector3 vectorHumanPosition;

    private float red;
    private float blue;
    private float green;

    private float newPositionY;
    private float oldPositionY;

    private transient JJEngine engineInst = JJEngine.getInstance();

    public FileController(WorldMap worldMapObj) {
        map = worldMapObj;
    }

    public void saveWorld() {
        try {
            worldMap = map.worldMap;
            upVector = engineInst.human.camera.up;
            healthValue = engineInst.human.getHealthValue();
            newPositionY = engineInst.human.camNewPositionY;
            oldPositionY = engineInst.human.camOldPositionY;
            red = map.redColor;
            blue = map.blueColor;
            green = map.greenColor;
            vectorHumanPosition = engineInst.human.camera.position;
            vectorCamDirection = engineInst.human.camera.direction;

            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName));
            outputStream.writeObject(this);

            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadWorld() {
        try {
            ObjectInputStream objInputStream = new ObjectInputStream(new FileInputStream(fileName));
            FileController world = (FileController) objInputStream.readObject();

            map.worldMap = world.worldMap;
            map.mapLengthX = map.worldMap.length;
            map.mapLengthY = map.worldMap[1].length;
            map.mapLengthZ = map.worldMap[1][1].length;
            engineInst.human.setHealthValue(world.healthValue);
            engineInst.human.camera.up.set(world.upVector);
            engineInst.human.camOldPositionY = world.oldPositionY;
            engineInst.human.camNewPositionY = world.newPositionY;
            map.redColor = world.red;
            map.blueColor = world.blue;
            map.greenColor = world.green;
            engineInst.human.camera.direction.set(world.vectorCamDirection);
            engineInst.human.camera.position.set(world.vectorHumanPosition);
            map.build();
            objInputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setFileName(String name) {
        fileName = name;
    }
}
