package com.vittach.jumpjack;

import com.badlogic.gdx.math.Vector3;

import java.io.*;

public class LoadAndSave implements Serializable {

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

    LoadAndSave(WorldMap worldMapObj) {
        map = worldMapObj;
    }

    public void saveWorld() {
        try {
            worldMap = map.worldMap;
            upVector = JJEngine.human.camera.up;
            healthValue = JJEngine.human.healthValue;
            newPositionY = JJEngine.human.camNewPositionY;
            oldPositionY = JJEngine.human.camOldPositionY;
            red = map.redColor;
            blue = map.blueColor;
            green = map.greenColor;
            vectorHumanPosition = JJEngine.human.camera.position;
            vectorCamDirection = JJEngine.human.camera.direction;

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
            LoadAndSave world = (LoadAndSave) objInputStream.readObject();

            map.worldMap = world.worldMap;
            map.mapLengthX = map.worldMap.length;
            map.mapLengthY = map.worldMap[1].length;
            map.mapLengthZ = map.worldMap[1][1].length;
            JJEngine.human.healthValue = world.healthValue;
            JJEngine.human.camera.up.set(world.upVector);
            JJEngine.human.camOldPositionY = world.oldPositionY;
            JJEngine.human.camNewPositionY = world.newPositionY;
            map.redColor = world.red;
            map.blueColor = world.blue;
            map.greenColor = world.green;
            JJEngine.human.camera.direction.set(world.vectorCamDirection);
            JJEngine.human.camera.position.set(world.vectorHumanPosition);
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
