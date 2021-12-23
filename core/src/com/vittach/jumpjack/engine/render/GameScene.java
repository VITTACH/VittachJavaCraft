package com.vittach.jumpjack.engine.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;
import com.vittach.jumpjack.engine.render.domain.Chunk;
import com.vittach.jumpjack.engine.render.domain.MeshObj;
import com.vittach.jumpjack.engine.render.domain.ModelInstanceObj;
import com.vittach.jumpjack.engine.render.light.DirectionalLight;
import com.vittach.jumpjack.engine.render.light.Light;
import com.vittach.jumpjack.engine.render.shader.SimpleTextureShader;
import com.vittach.jumpjack.engine.render.utils.MeshCompressor;
import com.vittach.jumpjack.ui.buttons.BoxButton;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.badlogic.gdx.Gdx.graphics;

public class GameScene {
    private final BoxButton boxBtn;

    private final Mesh cubeMesh;
    private final ModelBatch modelBatch;
    private final ShaderProgram shaderProgram;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Texture texture;
    private Vector3 camPosition;

    private final Map<Vector3, ModelInstanceObj> modelInstanceObjMap = new HashMap<Vector3, ModelInstanceObj>();
    private final Map<String, List<TextureRegion>> textureMap = new HashMap<String, List<TextureRegion>>();
    private final Map<Vector3, Chunk> cubeChunkMap = new ConcurrentHashMap<Vector3, Chunk>();

    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferenceInstance = Preferences.getInstance();

    private final int distance = engineInstance.fpController.viewDistance;
    private final int chunkSize = 16;
    private final int mapHeight = 48;

    private final Light sunLight = new DirectionalLight(this, new Vector3(-10, 10, 10), new Vector3(-10, 0, 0));

    private final ModelBuilder modelBuilder = new ModelBuilder();
    private Matrix4 chunkTrans = new Matrix4();


    public void dispose() {
        textureMap.clear();
        modelInstanceObjMap.clear();
        cubeChunkMap.clear();
        boxBtn.dispose();
    }

    public void setUpListeners() {
        preferenceInstance.inputListener.addListener(boxBtn);
        preferenceInstance.inputListener.addListener(engineInstance.fpController);
        preferenceInstance.inputListener.addListener(engineInstance.leftStick);
        preferenceInstance.inputListener.addListener(engineInstance.rightStick);
    }

    public GameScene() {
        shapeRenderer.setColor(0.2f, 0.3f, 0.43f, 1);
        camPosition = new Vector3(0, 0, 0);
        setTextures(Gdx.files.internal("sprites/cubesSprite.png"));

        shaderProgram = setupShader("scene");
        modelBatch = new ModelBatch(new DefaultShaderProvider() {
            @Override
            protected Shader createShader(final Renderable renderable) {
                return new SimpleTextureShader(renderable, shaderProgram);
            }
        });

        VertexAttributes attributes = new VertexAttributes(
            new VertexAttribute(Usage.Position, 3, "a_position"),
            new VertexAttribute(Usage.Normal, 3, "a_normal"),
            new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0")
        );

        MeshBuilder cubeBuilder = new MeshBuilder();
        cubeBuilder.begin(attributes, GL20.GL_TRIANGLES);
        cubeBuilder.box(1.0f, 1.0f, 1.0f);
        cubeMesh = cubeBuilder.end();

        boxBtn = new BoxButton();
        boxBtn.setPosition(engineInstance.renderWidth - boxBtn.getWidth(), engineInstance.renderHeight - boxBtn.getHeight());
    }

    public void genWorld() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (engineInstance.currentScreen == MainEngine.Screen.GAME_PLAY) {
                    if (camPosition == null) continue;
                    generateWorld((int) camPosition.x, (int) camPosition.z);
                }
            }
        }).start();
    }

    private void setTextures(FileHandle path) {
        texture = new Texture(path);
        TextureRegion[][] imageRegions = TextureRegion.split(texture, texture.getWidth() / 8, texture.getHeight() / 8);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, 16, 16);
        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                String symbol;
                if (i == 5 && j == 0) symbol = "b";
                else if (i == 6 && j == 0) symbol = "a";
                else if (i == 3 && j == 1) symbol = "c";
                else continue;
                List<TextureRegion> regionList = new ArrayList<TextureRegion>();
                regionList.add(imageRegions[i][0]);
                textureMap.put(symbol, regionList);
            }
        }
    }

    public void display(Viewport viewport) {
        System.out.println("FPS counter: " + graphics.getFramesPerSecond());

        // background filled
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, graphics.getWidth(), graphics.getHeight());
        shapeRenderer.end();

        render(cubeChunkMap, modelInstanceObjMap, textureMap);

        boxBtn.display(viewport);
    }

    public ShaderProgram setupShader(final String prefix) {
        ShaderProgram.pedantic = false;
        final ShaderProgram shaderProgram = new ShaderProgram(
            Gdx.files.internal("shaders/" + prefix + "_v.glsl"),
            Gdx.files.internal("shaders/" + prefix + "_f.glsl")
        );

        if (!shaderProgram.isCompiled()) {
            System.err.println("Error with shader " + prefix + ": " + shaderProgram.getLog());
            System.exit(1);
        } else {
            Gdx.app.log("init", "Shader " + prefix + " compilled " + shaderProgram.getLog());
        }
        return shaderProgram;
    }

    private Mesh getCompressedMesh(List<MeshObj> meshObjects, Map<String, List<TextureRegion>> textureMap) {
        List<Matrix4> matrix = new ArrayList<Matrix4>();
        for (MeshObj meshObj : meshObjects) {
            matrix.add(new Matrix4().setToTranslation(meshObj.getPosition()));
        }
        return MeshCompressor.compressMeshes(meshObjects, textureMap, matrix, chunkSize);
    }

    private void generateWorld(int cameraXPos, int cameraZPos) {
        Vector3 chunkPosition;
        for (int y = 0; y < Math.max(mapHeight / chunkSize, 1); y++) {
            for (int x = (cameraXPos - distance) / chunkSize; x < (cameraXPos + distance) / chunkSize; x++) {
                for (int z = (cameraZPos - distance) / chunkSize; z < (cameraZPos + distance) / chunkSize; z++) {
                    chunkPosition = new Vector3(x * chunkSize, y * chunkSize, z * chunkSize);
                    if (cubeChunkMap.keySet().contains(chunkPosition)) continue;

                    List<MeshObj> cubeMeshes = new ArrayList<MeshObj>();
                    generateChunk(cubeMeshes, cubeMesh);

                    cubeChunkMap.put(chunkPosition, new Chunk(cubeMeshes));
                }
            }
        }
    }

    private void generateChunk(List<MeshObj> meshList, Mesh mesh) {
        for (int positionY = 0; positionY < Math.min(chunkSize, mapHeight); positionY++) {
            for (int positionX = 0; positionX < chunkSize; positionX++) {
                for (int positionZ = 0; positionZ < chunkSize; positionZ++) {
                    String symbol = "";

                    if (positionY == 0 && new Random().nextInt() % 2 == 0) symbol = "a";
                    if (positionX % 6 == 0 && positionZ % 4 == 0 && new Random().nextInt() % 2 == 0) symbol = "b";

                    Vector3 meshPosition = new Vector3(positionX, positionY, positionZ);
                    meshList.add(new MeshObj(mesh, symbol, meshPosition));
                }
            }
        }
    }

    private void render(
        Map<Vector3, Chunk> chunkMap,
        Map<Vector3, ModelInstanceObj> modelInstanceObjMap,
        Map<String, List<TextureRegion>> textureMap
    ) {
        PerspectiveCamera fpCamera = engineInstance.fpController.getFpCamera();
        camPosition = fpCamera.position;

        for (Map.Entry<Vector3, Chunk> chunkEntry : chunkMap.entrySet()) {
            Vector3 position = chunkEntry.getKey();
            chunkTrans = chunkTrans.setToTranslation(position);

            if (Math.abs(camPosition.x - position.x) > distance
                || Math.abs(camPosition.z - position.z) > distance
                || Math.abs(camPosition.y - position.y) > distance) {
                continue;
            }

            List<MeshObj> nonCompressedMeshObjs = chunkEntry.getValue().getMeshObjs();
            ModelInstanceObj instanceObj = modelInstanceObjMap.get(position);
            if (instanceObj == null) {
                Mesh compressedMesh = getCompressedMesh(nonCompressedMeshObjs, textureMap);
                if (compressedMesh == null) continue;
                ModelInstance modelInstance = meshToModelInst(compressedMesh);

                sunLight.render(modelInstance, chunkTrans);

                instanceObj = new ModelInstanceObj(modelInstance, sunLight.getDepthMap());
                modelInstanceObjMap.put(position, instanceObj);
            }

            applyShaders(fpCamera, instanceObj.getDepthMap());

            modelBatch.begin(fpCamera);
            modelBatch.render(instanceObj.getModelInstance());
            modelBatch.end();
        }
    }

    private ModelInstance meshToModelInst(Mesh compressedMesh) {
        modelBuilder.begin();
        modelBuilder.part("", compressedMesh, GL20.GL_TRIANGLES, new Material());
        return new ModelInstance(modelBuilder.end());
    }

    private void applyShaders(PerspectiveCamera fpCamera, Texture depthMap) {
        shaderProgram.begin();

        texture.bind(1);
        shaderProgram.setUniformi("u_texture", 1);

        shaderProgram.setUniformf("u_lightPosition", sunLight.getCamera().position);
        shaderProgram.setUniformMatrix("u_lightTrans", sunLight.getCamera().combined);

        shaderProgram.setUniform4fv("u_fogColor", new float[]{0.8f, 0.9f, 1f, 1f}, 0, 4);
        shaderProgram.setUniformf("u_fogFar", distance * 0.8f);
        shaderProgram.setUniformf("u_fogNear", distance * 0.2f);

        shaderProgram.setUniformMatrix("u_chunkTrans", chunkTrans);

        depthMap.bind(2);
        shaderProgram.setUniformi("u_depthMap", 2);

        shaderProgram.setUniformf("u_cameraFar", sunLight.getCamera().far);
        shaderProgram.setUniformf("u_cameraPosition", fpCamera.position);
    }
}
