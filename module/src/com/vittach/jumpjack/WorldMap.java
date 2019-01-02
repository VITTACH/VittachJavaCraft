package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.vittach.jumpjack.framework.MeshCompress;
import com.vittach.jumpjack.framework.MeshObj;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

/**
 * Created by ZHARIKOV VITALIY at 23.02.2016
 */

public class WorldMap {
    private Model skyBoxModel;
    private ShaderProgram shader;
    private Environment environment;
    private List<MeshObj> mergedMeshs;
    private List<MeshObj> meshObjects;
    private Matrix4 transactionMatrix;
    private List<Mesh> meshes;

    char[][][] worldMap;
    private Vector3 cameraPosition;
    private float blockSize;

    float redColor;
    float blueColor;
    float greenColor;

    private ModelBatch modelBatch;
    private Material highlightObj;
    private ArrayList<ModelInstance> highlights;

    private ArrayList<PointLight> pointLights;
    private ArrayList<ArrayIndex> newEntities;

    int mapLengthX;
    int mapLengthZ;
    int mapLengthY;
    int mouseX;
    int mouseY;
    private int areaSize = 32; // 32- MAX VALUE

    public WorldMap() {
        mouseY = 3; // TODO WTF magical number?
        blockSize = 0.5f;
        transactionMatrix = new Matrix4();

        shader = new ShaderProgram(Gdx.files.internal("glshs/vertex.glsl"), Gdx.files.internal("glshs/fragment.glsl"));
        meshObjects = new ArrayList<MeshObj>();
        mergedMeshs = new ArrayList<MeshObj>();

        MeshBuilder builder = new MeshBuilder();
        VertexAttributes attributes = new VertexAttributes(
                new VertexAttribute(Usage.Position, 3, "a_Position"), new VertexAttribute(Usage.Normal, 3, "a_Normal")
        );
        builder.begin(attributes, GL20.GL_TRIANGLES);
        builder.box(blockSize, blockSize, blockSize);
        Mesh mesh = builder.end();
        meshes = new ArrayList<Mesh>();
        for (int i = 0; i < 20; i++) {
            meshes.add(mesh);
        }

        environment = new Environment();
        modelBatch = new ModelBatch();

        highlightObj = new Material(ColorAttribute.createDiffuse(1.0f, 1.0f, 1f, 0.3f));
        highlightObj.set(new BlendingAttribute(770, 771));

        pointLights = new ArrayList<PointLight>();
        newEntities = new ArrayList<ArrayIndex>();
        highlights = new ArrayList<ModelInstance>();
    }

    // построение мира
    void buildWorld(int startXArea, int startYArea, int startZArea) {
        pointLights.clear();
        meshObjects.clear();
        newEntities.clear();
        for (int x = startXArea; x < startXArea + areaSize; x++)
            for (int y = startYArea; y < startYArea + areaSize; y++)
                for (int z = startZArea; z < startZArea + areaSize; z++) {
                    if (x >= 1)
                        if (worldMap[x - 1][y][z] != 'o')
                            if (x < mapLengthX - 1)
                                if (worldMap[x + 1][y][z] != 'o')
                                    if (y == 0)
                                        if (worldMap[x][y + 1][z] != 'o')
                                            if (z >= 1)
                                                if (worldMap[x][y][z - 1] != 'o')
                                                    if (z < mapLengthZ - 1)
                                                        if (worldMap[x][y][z + 1] != 'o')
                                                            continue;
                    if (x >= 1)
                        if (worldMap[x - 1][y][z] != 'o' && worldMap[x - 1][y][z] != 'A' &&
                                worldMap[x - 1][y][z] != 'N' && worldMap[x - 1][y][z] != 'L')
                            if (x < mapLengthX - 1)
                                if (worldMap[x + 1][y][z] != 'o' && worldMap[x + 1][y][z] != 'A' &&
                                        worldMap[x + 1][y][z] != 'N' && worldMap[x + 1][y][z] != 'L')
                                    if (y >= 1)
                                        if (worldMap[x][y - 1][z] != 'o')
                                            if (y < mapLengthY - 1)
                                                if (worldMap[x][y + 1][z] != 'o' && worldMap[x][y + 1][z] != 'A' &&
                                                        worldMap[x][y + 1][z] != 'N' && worldMap[x][y + 1][z] != 'L')
                                                    if (z >= 1)
                                                        if (worldMap[x][y][z - 1] != 'o' && worldMap[x][y][z - 1] != 'A' &&
                                                                worldMap[x][y][z - 1] != 'N' && worldMap[x][y][z - 1] != 'L')
                                                            if (z < mapLengthZ - 1)
                                                                if (worldMap[x][y][z + 1] != 'o' && worldMap[x][y][z + 1] != 'A' &&
                                                                        worldMap[x][y][z + 1] != 'N' && worldMap[x][y][z + 1] != 'L')
                                                                    continue;
                    switch (worldMap[x][y][z]) {
                        case 't':
                            meshObjects.add(new MeshObj(meshes.get(0), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'b':
                            meshObjects.add(new MeshObj(meshes.get(1), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'd':
                            meshObjects.add(new MeshObj(meshes.get(2), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'f':
                            meshObjects.add(new MeshObj(meshes.get(3), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            pointLights.add(new PointLight().set(1, 1, 1, -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize, 2.5f));
                            break;
                        case 'S':
                            meshObjects.add(new MeshObj(meshes.get(4), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 's':
                            meshObjects.add(new MeshObj(meshes.get(5), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'k':
                            meshObjects.add(new MeshObj(meshes.get(6), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'D':
                            meshObjects.add(new MeshObj(meshes.get(7), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'B':
                            meshObjects.add(new MeshObj(meshes.get(8), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'L':
                            meshObjects.add(new MeshObj(meshes.get(9), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            pointLights.add(new PointLight().set(1, 1, 1, -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize, 2.0f));
                            break;
                        case 'G':
                            meshObjects.add(new MeshObj(meshes.get(10), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'c':
                            meshObjects.add(new MeshObj(meshes.get(11), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'R':
                            meshObjects.add(new MeshObj(meshes.get(12), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'r':
                            meshObjects.add(new MeshObj(meshes.get(13), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'y':
                            meshObjects.add(new MeshObj(meshes.get(14), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'W':
                            meshObjects.add(new MeshObj(meshes.get(15), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'w':
                            meshObjects.add(new MeshObj(meshes.get(16), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'M':
                            meshObjects.add(new MeshObj(meshes.get(17), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'A':
                            meshObjects.add(new MeshObj(meshes.get(18), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                        case 'N':
                            meshObjects.add(new MeshObj(meshes.get(19), -x * blockSize + mapLengthX / 2 * blockSize, y *
                                    blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                            break;
                    }
                    if (worldMap[x][y][z] != 'o') newEntities.add(new ArrayIndex(x, y, z));
                }
    }

    private void updateEnvironment() {
        if (cameraPosition == null) {
            cameraPosition = JJEngine.human.camera.position;
        }

        environment.clear();
        environment.add(new DirectionalLight().set(redColor, greenColor, blueColor, 0, -1, 0));
        environment.add(new DirectionalLight().set(redColor, greenColor, blueColor, 0, 1f, 0));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, redColor - 0.6f, greenColor - 0.6f, blueColor - 0.6f, 1.f));

        for (PointLight light : pointLights) {
            if (light.position.x > cameraPosition.x - 5 && light.position.x < cameraPosition.x + 1
                    && light.position.z > cameraPosition.z - 5 && light.position.z < cameraPosition.z + 1) {
                environment.add(light);
            }
        }
    }

    //выбор границы блока для подсветки при взгляде
    void selectBlock(int margin, int x, int y, int z) {
        switch (margin) {
            case 0://выбор верхней границы
                highlights.add(new ModelInstance(new ModelBuilder().createRect(blockSize / 2, blockSize / 2, -blockSize / 2,
                        -blockSize / 2, blockSize / 2, -blockSize / 2, -blockSize / 2, blockSize / 2, blockSize / 2, blockSize / 2,
                        blockSize / 2, blockSize / 2, 0, 1, 0, highlightObj, Usage.Position | Usage.Normal),
                        -x * blockSize + mapLengthX / 2 * blockSize, y * blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                break;
            case 1://выбор на нижней границе
                highlights.add(new ModelInstance(new ModelBuilder().createRect(blockSize / 2, -blockSize / 2, blockSize / 2,
                        -blockSize / 2, -blockSize / 2, blockSize / 2, -blockSize / 2, -blockSize / 2, -blockSize / 2, blockSize / 2,
                        -blockSize / 2, -blockSize / 2, 0, 1, 0, highlightObj, Usage.Position | Usage.Normal),
                        -x * blockSize + mapLengthX / 2 * blockSize, y * blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                break;
            case 2://выбор передней границы
                highlights.add(new ModelInstance(new ModelBuilder().createRect(blockSize / 2, blockSize / 2, blockSize / 2,
                        -blockSize / 2, blockSize / 2, blockSize / 2, -blockSize / 2, -blockSize / 2, blockSize / 2, blockSize / 2,
                        -blockSize / 2, blockSize / 2, 0, 1, 0f, highlightObj, Usage.Position | Usage.Normal),
                        -x * blockSize + mapLengthX / 2 * blockSize, y * blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                break;
            case 3://выбор на задней границе
                highlights.add(new ModelInstance(new ModelBuilder().createRect(blockSize / 2, -blockSize / 2, -blockSize / 2,
                        -blockSize / 2, -blockSize / 2, -blockSize / 2, -blockSize / 2, blockSize / 2, -blockSize / 2, blockSize / 2,
                        blockSize / 2, -blockSize / 2, 0, 1, 0f, highlightObj, Usage.Position | Usage.Normal),
                        -x * blockSize + mapLengthX / 2 * blockSize, y * blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                break;
            case 4://выбор на левой границе
                highlights.add(new ModelInstance(new ModelBuilder().createRect(-blockSize / 2, blockSize / 2, -blockSize / 2,
                        -blockSize / 2, -blockSize / 2, -blockSize / 2, -blockSize / 2, -blockSize / 2, blockSize / 2, -blockSize / 2,
                        blockSize / 2, blockSize / 2, 0, 1, 0f, highlightObj, Usage.Position | Usage.Normal),
                        -x * blockSize + mapLengthX / 2 * blockSize, y * blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                break;
            case 5://выбор на правой границе
                highlights.add(new ModelInstance(new ModelBuilder().createRect(blockSize / 2, blockSize / 2, blockSize / 2,
                        blockSize / 2, -blockSize / 2, blockSize / 2, blockSize / 2, -blockSize / 2, -blockSize / 2, blockSize / 2,
                        blockSize / 2, -blockSize / 2, 0, 1, 0f, highlightObj, Usage.Position | Usage.Normal),
                        -x * blockSize + mapLengthX / 2 * blockSize, y * blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                break;
            case 6://выбор у верхней границы
                highlights.add(new ModelInstance(new ModelBuilder().createRect(blockSize / 2, -blockSize / 2.1f, -blockSize / 2,
                        -blockSize / 2, -blockSize / 2.1f, -blockSize / 2, -blockSize / 2, -blockSize / 2.1f, blockSize / 2, blockSize / 2,
                        -blockSize / 2.1f, blockSize / 2, 0, 1, 0, highlightObj, Usage.Position | Usage.Normal),
                        -x * blockSize + mapLengthX / 2 * blockSize, y * blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
                break;
        }
    }

    // генерация мира (generate world)
    public void generateWorld(int sizeX, int sizeY, int sizeZ) {
        this.mapLengthX = sizeX;
        this.mapLengthZ = sizeZ;
        this.mapLengthY = sizeY;
        this.worldMap = new char[sizeX][sizeY][sizeZ];
        int height = 2 + new Random().nextInt(3);

        for (int x = 0; x < sizeX; x++)
            for (int y = 0; y < sizeY; y++)
                for (int z = 0; z < sizeZ; z++) {
                    if (worldMap[x][y][z] == Character.MIN_VALUE) {
                        worldMap[x][y][z] = 'o';
                    }
                    if (x == sizeX / 2 && z == sizeZ / 2 && y > height && y < height + 3)
                        continue; // do not set blocks on the human place
                    int randomValue = new Random().nextInt(2);
                    if (y == height + 2 && new Random().nextInt() % 2 == 1) {
                        switch (randomValue) {
                            case 0: // generate wooden wall
                                int i;
                                for (i = 1; worldMap[x][y - i][z] == 'o'; i++)
                                    worldMap[x][y - i][z] = 'D';
                                if (i > 1) worldMap[x][y][z] = 'B';
                                break;
                            case 1: // generate green walls
                                for (i = 1; worldMap[x][y - i][z] == 'o'; i++)
                                    worldMap[x][y - i][z] = 'r';
                                if (i > 1) worldMap[x][y][z] = 'R';
                                break;
                        }
                    } else if (y == height + 1 && (new Random().nextInt()) % 100 == 1) {
                        // lava generate on the map
                        if (new Random().nextInt() % 2 == 0)
                            for (int i = 0; i < 25; i++) {
                                int genX = new Random().nextInt(6) - 2;
                                int genZ = new Random().nextInt(6) - 2;
                                if (x + genX > 0 && x + genX < sizeX && z + genZ > 0 && z + genZ < sizeZ) {
                                    worldMap[x + genX][y][z + genZ] = 'L';
                                }
                            }
                        else worldMap[x][y][z] = 'A';
                    } else if (y == height + 1 && (new Random().nextInt()) % 100 == 0) {
                        int treeHeight = 6 + new Random().nextInt(7);
                        for (int i = 0; i < treeHeight; i += 1) {
                            worldMap[x][y + i][z] = 'd';
                        }
                        if (treeHeight < 10) { // generate tree
                            worldMap[x][y + treeHeight][z] = 'r';
                            for (int i = treeHeight - 1; i > treeHeight - 4; i--) {
                                if (x >= 1) worldMap[x - 1][y + i][z] = 'R';
                                if (x < sizeX - 1) worldMap[x + 1][y + i][z] = 'R';
                                if (z >= 1) worldMap[x][y + i][z - 1] = 'R';
                                if (z < sizeZ - 1) worldMap[x][y + i][z + 1] = 'R';
                            }
                        } else { // generate really big tree
                            worldMap[x][y + treeHeight][z] = 'R';
                            int bias = new Random().nextInt(2);
                            for (int i = 3 + bias; i < treeHeight; i += 2) {
                                if (x >= 1) worldMap[x - 1][y + i][z] = 'R';
                                if (x < sizeX - 1) worldMap[x + 1][y + i][z] = 'R';
                                if (z >= 1) worldMap[x][y + i][z - 1] = 'R';
                                if (z < sizeZ - 1) worldMap[x][y + i][z + 1] = 'R';
                                if (i < treeHeight - 2) {
                                    if (x < sizeX - 2) worldMap[x + 2][y + i][z] = 'R';
                                    if (z < sizeZ - 2) worldMap[x][y + i][z + 2] = 'R';
                                    if (x >= 2) worldMap[x - 2][y + i][z] = 'R';
                                    if (z >= 2) worldMap[x][y + i][z - 2] = 'R';
                                    if (z < sizeZ - 1 && x < sizeX - 1) worldMap[x + 1][y + i][z + 1] = 'R';
                                    if (x < sizeX - 1 && z > 0) worldMap[x + 1][y + i][z - 1] = 'R';
                                    if (z < sizeZ - 1 && x > 0) worldMap[x - 1][y + i][z + 1] = 'R';
                                    if (x > 0 && z > 0) worldMap[x - 1][y + i][z - 1] = 'R';
                                }
                            }
                        }
                    } else if (y == height) {
                        switch (randomValue) { // generate walk road
                            case 0:
                                worldMap[x][y][z] = new Random().nextInt() % 2 == 1? 'b': 'd';
                                break;
                            case 1:
                                worldMap[x][y][z] = 't';
                                break;
                        }
                    } else if (y < height) {
                        worldMap[x][y][z] = 'G';
                    }
                }

        // generate finishing block on the map
        worldMap[new Random().nextInt(sizeX)][height + 1][new Random().nextInt(sizeZ)] = 'N';
        build();
    }

    void build() {
        for (int xArea = 0; xArea < mapLengthX; xArea += areaSize)
            for (int yArea = 0; yArea < mapLengthY; yArea += areaSize)
                for (int zArea = 0; zArea < mapLengthZ; zArea += areaSize) {
                    buildWorld(xArea, yArea, zArea);

                    Mesh meshObj = createMesh();
                    if (meshObj == null) continue;

                    float x = -xArea / areaSize + xArea / areaSize * 2 * blockSize;
                    float y = yArea / areaSize - yArea / areaSize * 2 * blockSize;
                    float z = -zArea / areaSize + zArea / areaSize * 2 * blockSize;
                    mergedMeshs.add(new MeshObj(meshObj, x, y, z));
                }

        JJEngine.human.setWorld(worldMap);
        updateEnvironment();
    }

    void delBlock(int x, int y, int z) {
        worldMap[x][y][z] = 'o';
        int size = newEntities.size();
        for (int i = 0; i < size; i++) {
            if (newEntities.get(i).x == x && newEntities.get(i).y == y && newEntities.get(i).z == z) {
                newEntities.remove(i);
                meshObjects.remove(i);
                createMesh();
                break;
            }
        }
    }

    void setBlock(int x, int y, int z) {
        int index = Math.abs(mouseY - 3) * 10 + mouseX + 1;
        if (index > 20) {
            index = 20;
        }
        float vel = JJEngine.human.oldVelocity;
        if (x >= 0 && x < mapLengthX && y >= 0 && y < mapLengthY && z >= 0 && z < mapLengthZ &&
                !(cameraPosition.y > (y - 3) * blockSize + vel && cameraPosition.y < y * blockSize &&
                        cameraPosition.x >= -x * blockSize + mapLengthX / 2 * blockSize - blockSize + vel &&
                        cameraPosition.x < mapLengthX / 2 * blockSize - (x - 1) * blockSize - vel &&
                        cameraPosition.z >= -z * blockSize + mapLengthZ / 2 * blockSize - blockSize + vel &&
                        cameraPosition.z < mapLengthZ / 2 * blockSize - vel - (z - 1) * blockSize) &&
                worldMap[x][y][z] == 'o') {
            meshObjects.add(new MeshObj(meshes.get(index), -x * blockSize + mapLengthX / 2 * blockSize,
                    y * blockSize - blockSize * 2, -z * blockSize + mapLengthZ / 2 * blockSize));
            newEntities.add(new ArrayIndex(x, y, z));
            switch (index) {
                case 1:
                    worldMap[x][y][z] = 't';
                    break;
                case 2:
                    worldMap[x][y][z] = 'b';
                    break;
                case 3:
                    worldMap[x][y][z] = 'd';
                    break;
                case 4:
                    worldMap[x][y][z] = 'f';
                    break;
                case 5:
                    worldMap[x][y][z] = 'S';
                    break;
                case 6:
                    worldMap[x][y][z] = 's';
                    break;
                case 7:
                    worldMap[x][y][z] = 'k';
                    break;
                case 8:
                    worldMap[x][y][z] = 'D';
                    break;
                case 9:
                    worldMap[x][y][z] = 'B';
                    break;
                case 10:
                    worldMap[x][y][z] = 'L';
                    break;
                case 11:
                    worldMap[x][y][z] = 'G';
                    break;
                case 12:
                    worldMap[x][y][z] = 'c';
                    break;
                case 13:
                    worldMap[x][y][z] = 'R';
                    break;
                case 14:
                    worldMap[x][y][z] = 'r';
                    break;
                case 15:
                    worldMap[x][y][z] = 'y';
                    break;
                case 16:
                    worldMap[x][y][z] = 'W';
                    break;
                case 17:
                    worldMap[x][y][z] = 'w';
                    break;
                case 18:
                    worldMap[x][y][z] = 'M';
                    break;
                case 19:
                    worldMap[x][y][z] = 'A';
                    break;
                case 20:
                    worldMap[x][y][z] = 'N';
                    break;
            }
            createMesh();
        }
    }

    private Mesh createMesh() {
        List<Mesh> meshes = new ArrayList<Mesh>();
        List<Matrix4> transactionList = new ArrayList<Matrix4>();
        for (MeshObj meshObject : meshObjects) {
            meshes.add(meshObject.getMesh());
            transactionList.add(new Matrix4().setToTranslation(meshObject.getPosition()));
        }
        return MeshCompress.mergeMeshes(meshes, transactionList);
    }

    public void setSkyBox() {
        // initialize skyBoxModelInst *.obj meshObjects from fileLoader
        skyBoxModel = new ObjLoader().loadModel(Gdx.files.internal("3d/skyBoxModel.obj"));
    }

    public void render(Viewport viewport) {
        // claimRegion();
        System.out.println("fps " + Gdx.graphics.getFramesPerSecond());

        cameraPosition = JJEngine.human.camera.position;

        viewport.apply();

        PerspectiveCamera perspectiveCams = JJEngine.human.getCamera();

        shader.begin();
        shader.setUniformMatrix("modelView", perspectiveCams.combined);
        shader.setUniformf("uCameraFar", perspectiveCams.far);
        shader.setUniformf("uLightPosition", perspectiveCams.position);
        for (MeshObj obj : mergedMeshs) {
            transactionMatrix.setToTranslation(obj.getPosition());
            shader.setUniformMatrix("model", transactionMatrix);
            obj.getMesh().render(shader, GL20.GL_TRIANGLES);
        }
        shader.end();

        modelBatch.begin(perspectiveCams);
        modelBatch.render(new ModelInstance(skyBoxModel, JJEngine.human.camera.position));
        modelBatch.render(highlights);
        modelBatch.end();
    }

    public void dispose() {
        highlights.clear();
        newEntities.clear();
        modelBatch.dispose();
        meshObjects.clear();
    }

    void clearHighlights() {
        highlights.clear();
    }
}
