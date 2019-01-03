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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ZHARIKOV VITALIY at 23.02.2016
 */

public class WorldMap {
    private Model skyBoxModel;
    private ShaderProgram shader;
    private Environment environment;
    private Matrix4 transactionMatrix;
    private Map<Vector3, Mesh> meshsMap;
    private List<Mesh> meshes;
    private JJEngine engineInst = JJEngine.getInstance();

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

    int mapLengthX;
    int mapLengthZ;
    int mapLengthY;
    int mouseX;
    int mouseY;
    private int areaSize = 32; // 32 is MAX VALUE
    private Texture texture;

    public WorldMap() {
        mouseY = 3; // TODO WTF, magical number?
        blockSize = 0.5f;
        transactionMatrix = new Matrix4();

        texture = new Texture(Gdx.files.internal("3d/skyboxSprite.png"));

        shader = new ShaderProgram(Gdx.files.internal("glshs/vertex.glsl"), Gdx.files.internal("glshs/fragment.glsl"));
        meshsMap = new ConcurrentHashMap<Vector3, Mesh>();

        MeshBuilder builder = new MeshBuilder();
        VertexAttributes attributes = new VertexAttributes(
                new VertexAttribute(Usage.Position, 3, "a_Position"), new VertexAttribute(Usage.Normal, 3, "a_Normal"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_TexCoord")
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
        highlights = new ArrayList<ModelInstance>();
    }

    // построение мира
    private List<MeshObj> buildWorld(int startXArea, int startYArea, int startZArea) {
        pointLights.clear();
        List<MeshObj> meshObjects = new ArrayList<MeshObj>();
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
                    Mesh mesh = null;
                    switch (worldMap[x][y][z]) {
                        case 't': mesh = meshes.get(0); break;
                        case 'b': mesh = meshes.get(1); break;
                        case 'd': mesh = meshes.get(2); break;
                        case 'f':
                            mesh = meshes.get(3);
                            pointLights.add(new PointLight().set(1, 1, 1,
                                    -(x % areaSize) * blockSize, (y % areaSize) * blockSize, -(z % areaSize) * blockSize, 2.5f));
                            break;
                        case 'S': mesh = meshes.get(4); break;
                        case 's': mesh = meshes.get(5); break;
                        case 'k': mesh = meshes.get(6); break;
                        case 'D': mesh = meshes.get(7); break;
                        case 'B': mesh = meshes.get(8); break;
                        case 'L':
                            mesh = meshes.get(9);
                            pointLights.add(new PointLight().set(1, 1, 1,
                                    -(x % areaSize) * blockSize, (y % areaSize) * blockSize, -(z % areaSize) * blockSize, 2.0f));
                            break;
                        case 'G': mesh = meshes.get(10); break;
                        case 'c': mesh = meshes.get(11); break;
                        case 'R': mesh = meshes.get(12); break;
                        case 'r': mesh = meshes.get(13); break;
                        case 'y': mesh = meshes.get(14); break;
                        case 'W': mesh = meshes.get(15); break;
                        case 'w': mesh = meshes.get(16); break;
                        case 'M': mesh = meshes.get(17); break;
                        case 'A': mesh = meshes.get(18); break;
                        case 'N': mesh = meshes.get(19); break;
                    }
                    if (mesh != null) {
                        meshObjects.add(new MeshObj(mesh,
                                -(x % areaSize) * blockSize, (y % areaSize) * blockSize, -(z % areaSize) * blockSize));
                    }
                }
        return meshObjects;
    }

    private void updateEnvironment() {
        if (cameraPosition == null) {
            cameraPosition = engineInst.human.camera.position;
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

    // генерация мира (generate worldMapInst)
    public void createWorld(int sizeX, int sizeY, int sizeZ) {
        this.mapLengthX = sizeX;
        this.mapLengthZ = sizeZ;
        this.mapLengthY = sizeY;
        this.worldMap = new char[sizeX][sizeY][sizeZ];
        int height = 4;

        for (int x = 0; x < sizeX; x++)
            for (int y = 0; y < sizeY; y++)
                for (int z = 0; z < sizeZ; z++) {
                    if (worldMap[x][y][z] == Character.MIN_VALUE) {
                        worldMap[x][y][z] = 'o';
                    }

                    if (x == sizeX / 2 && z == sizeZ / 2 && y >= height + 1 && y < height + 3) {
                        continue; // do not set blocks on the human place
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
                            for (int i = 3 + new Random().nextInt(2); i < treeHeight; i += 2) {
                                if (x >= 1) worldMap[x - 1][y + i][z] = 'R';
                                if (x < sizeX - 1) worldMap[x + 1][y + i][z] = 'R';
                                if (z >= 1) worldMap[x][y + i][z - 1] = 'R';
                                if (z < sizeZ - 1) worldMap[x][y + i][z + 1] = 'R';
                                if (i >= treeHeight - 2) {
                                    continue;
                                }

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
                    } else if (y == height) {
                        switch (new Random().nextInt(2)) { // generate walk road
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

    public void build() {
        for (int xArea = 0; xArea < mapLengthX; xArea += areaSize)
            for (int yArea = 0; yArea < mapLengthY; yArea += areaSize)
                for (int zArea = 0; zArea < mapLengthZ; zArea += areaSize) {
                    Mesh mesh = createMesh(buildWorld(xArea, yArea, zArea));
                    if (mesh == null) continue;

                    updateMeshMap(xArea, yArea, zArea, mesh);
                }

        engineInst.human.setWorld(worldMap);
        updateEnvironment();
    }

    public void delBlock(int x, int y, int z) {
        worldMap[x][y][z] = 'o';
        updateWorld(x, y, z);
    }

    private void updateWorld(int x, int y, int z) {
        int startXArea = x / areaSize * areaSize;
        int startYArea = y / areaSize * areaSize;
        int startZArea = z / areaSize * areaSize;

        Mesh mesh = createMesh(buildWorld(startXArea, startYArea, startZArea));
        if (mesh == null) return;
        updateMeshMap(startXArea, startYArea, startZArea, mesh);
    }

    private void updateMeshMap(int xArea, int yArea, int zArea, Mesh merged) {
        float x = mapLengthX / 2 * blockSize - xArea * blockSize;
        float y = yArea * blockSize - blockSize * 2;
        float z = mapLengthZ / 2 * blockSize - zArea * blockSize;
        meshsMap.put(new Vector3(x, y, z), merged);
    }

    public void setBlock(int x, int y, int z) {
        int index = Math.abs(mouseY - 3) * 10 + mouseX + 1;
        if (index > 20) {
            index = 20;
        }
        float vel = engineInst.human.oldVelocity;
        if (x >= 0 && x < mapLengthX && y >= 0 && y < mapLengthY && z >= 0 && z < mapLengthZ &&
                !(cameraPosition.y > (y - 3) * blockSize + vel && cameraPosition.y < y * blockSize &&
                        cameraPosition.x >= -x * blockSize + mapLengthX / 2 * blockSize - blockSize + vel &&
                        cameraPosition.x < mapLengthX / 2 * blockSize - (x - 1) * blockSize - vel &&
                        cameraPosition.z >= -z * blockSize + mapLengthZ / 2 * blockSize - blockSize + vel &&
                        cameraPosition.z < mapLengthZ / 2 * blockSize - vel - (z - 1) * blockSize) &&
                worldMap[x][y][z] == 'o') {
            switch (index) {
                case 1: worldMap[x][y][z] = 't'; break;
                case 2: worldMap[x][y][z] = 'b'; break;
                case 3: worldMap[x][y][z] = 'd'; break;
                case 4: worldMap[x][y][z] = 'f'; break;
                case 5: worldMap[x][y][z] = 'S'; break;
                case 6: worldMap[x][y][z] = 's'; break;
                case 7: worldMap[x][y][z] = 'k'; break;
                case 8: worldMap[x][y][z] = 'D'; break;
                case 9: worldMap[x][y][z] = 'B'; break;
                case 10: worldMap[x][y][z] = 'L'; break;
                case 11: worldMap[x][y][z] = 'G'; break;
                case 12: worldMap[x][y][z] = 'c'; break;
                case 13: worldMap[x][y][z] = 'R'; break;
                case 14: worldMap[x][y][z] = 'r'; break;
                case 15: worldMap[x][y][z] = 'y'; break;
                case 16: worldMap[x][y][z] = 'W'; break;
                case 17: worldMap[x][y][z] = 'w'; break;
                case 18: worldMap[x][y][z] = 'M'; break;
                case 19: worldMap[x][y][z] = 'A'; break;
                case 20: worldMap[x][y][z] = 'N'; break;
            }

            updateWorld(x, y, z);
        }
    }

    private Mesh createMesh(List<MeshObj> meshObjects) {
        List<Mesh> meshes = new ArrayList<Mesh>();
        List<Matrix4> transactionList = new ArrayList<Matrix4>();
        for (MeshObj meshObject : meshObjects) {
            meshes.add(meshObject.getMesh());
            transactionList.add(new Matrix4().setToTranslation(meshObject.getPosition()));
        }
        return MeshCompress.mergeMeshes(meshes, transactionList);
    }

    public void setSkyBox() {
        // initialize skyBoxModelInst *.obj meshObjects from fileController
        skyBoxModel = new ObjLoader().loadModel(Gdx.files.internal("3d/skyBoxModel.obj"));
    }

    public void render(Viewport viewport) {
        // claimRegion();
        System.out.println("fps " + Gdx.graphics.getFramesPerSecond());

        viewport.apply();

        PerspectiveCamera perspectiveCam = engineInst.human.getCamera();

        cameraPosition = perspectiveCam.position;

        texture.bind();
        shader.begin();
        shader.setUniformMatrix("modelView", perspectiveCam.combined);
        shader.setUniformf("uCameraFar", perspectiveCam.far);
        shader.setUniformf("uLightPosition", perspectiveCam.position);

        for (Map.Entry<Vector3, Mesh> entries : meshsMap.entrySet()) {
            transactionMatrix.setToTranslation(entries.getKey());
            shader.setUniformMatrix("model", transactionMatrix);
            // shader.setUniformi("u_texture", 0);
            entries.getValue().render(shader, GL20.GL_TRIANGLES);
        }
        shader.end();

        modelBatch.begin(perspectiveCam);
        modelBatch.render(new ModelInstance(skyBoxModel, engineInst.human.camera.position));
        modelBatch.render(highlights);
        modelBatch.end();
    }

    public void dispose() {
        highlights.clear();
        modelBatch.dispose();
        meshsMap.clear();
    }

    void clearHighlights() {
        highlights.clear();
    }
}
