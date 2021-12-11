package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.domain.Chunk;
import com.vittach.jumpjack.mesh.MeshCompress;
import com.vittach.jumpjack.mesh.MeshObj;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.badlogic.gdx.Gdx.graphics;

public class MainGameLoop {
    private final Mesh blockMesh;
    private final ShaderProgram shader;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Texture texture;
    private Vector3 camPosition;

    private final Map<Vector3, Mesh> meshMap = new HashMap<Vector3, Mesh>();
    private final Map<String, List<TextureRegion>> textureMap = new HashMap<String, List<TextureRegion>>();
    private final Map<Vector3, Chunk> chunkMap = new ConcurrentHashMap<Vector3, Chunk>();

    private final JJEngine engineInst = JJEngine.getInstance();
    private final int distance = engineInst.controller.viewDistance;
    private final int chunkSize = 16;
    private final int mapHeight = 96;

    public void dispose() {
        textureMap.clear();
        chunkMap.clear();
    }

    public MainGameLoop() {
        camPosition = new Vector3(0, 0, 0);
        shapeRenderer.setColor(0.2f, 0.3f, 0.43f, 1);
        setTextures(Gdx.files.internal("3d/blocksSprite.png"));

        shader = new ShaderProgram(Gdx.files.internal("glshs/vertex.glsl"), Gdx.files.internal("glshs/fragment.glsl"));
        ShaderProgram.pedantic = false;
        if (!shader.isCompiled()) {
            throw new GdxRuntimeException(shader.getLog());
        }

        VertexAttributes attributes = new VertexAttributes(
                new VertexAttribute(Usage.Position, 3, "a_Position"),
                new VertexAttribute(Usage.Normal, 3, "a_Normal"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_TexCoord")
        );

        float blockSize = 1.0f;
        MeshBuilder builder = new MeshBuilder();
        builder.begin(attributes, GL20.GL_TRIANGLES);
        builder.box(blockSize, blockSize, blockSize);
        blockMesh = builder.end();
    }

    public void genWorld() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (engineInst.currentScreen == 0) {
                    if (camPosition == null) continue;
                    perlinNoise((int) camPosition.x, (int) camPosition.z);
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

        for (int i = 5; i < 7; i++) {
            String symbol = i % 2 == 0 ? "a" : "b";
            List<TextureRegion> regions = new ArrayList<TextureRegion>();
            regions.add(imageRegions[i][0]);
            textureMap.put(symbol, regions);
        }
    }

    public void display(Viewport viewport) {
        System.out.println("FPS count: " + graphics.getFramesPerSecond());

        viewport.apply();
        PerspectiveCamera fpcCamera = engineInst.controller.getCamera();

        camPosition = fpcCamera.position;

        // background filled
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, graphics.getWidth(), graphics.getHeight());
        shapeRenderer.end();
        //

        texture.bind(0);

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        shader.begin();
        shader.setUniformMatrix("modelView", fpcCamera.combined);
        shader.setUniformf("uLightPosition", camPosition);
        shader.setUniform4fv("uFogColor", new float[]{0.8f, 0.9f, 1f, 1f}, 0, 4);
        shader.setUniformf("uFogNear", distance * 0.2f);
        shader.setUniformf("uFogFar", distance * 0.8f);

        Matrix4 model = new Matrix4();
        for (Map.Entry<Vector3, Chunk> chunkEntry : chunkMap.entrySet()) {
            Vector3 pos = chunkEntry.getKey();

            if (Math.abs(camPosition.x - pos.x) > distance
                    || Math.abs(camPosition.z - pos.z) > distance
                    || Math.abs(camPosition.y - pos.y) > distance) {
                continue;
            }

            List<MeshObj> blockObjs = chunkEntry.getValue().getMeshObjs();
            Mesh mergedMesh = meshMap.get(pos);
            if (mergedMesh == null) {
                mergedMesh = compressMesh(blockObjs);
                if (mergedMesh == null) continue;
                meshMap.put(pos, mergedMesh);
            }

            shader.setUniformMatrix("model", model.setToTranslation(pos));
            shader.setUniformi("uTexture", 0);
            mergedMesh.render(shader, GL20.GL_TRIANGLES);
        }
        shader.end();

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }

    private Mesh compressMesh(List<MeshObj> meshObjects) {
        List<Matrix4> matrix = new ArrayList<Matrix4>();
        for (MeshObj obj : meshObjects) {
            matrix.add(new Matrix4().setToTranslation(obj.getPosition()));
        }
        return MeshCompress.compressMeshes(meshObjects, textureMap, matrix, chunkSize);
    }

    private void perlinNoise(int cameraXPos, int cameraZPos) {
        Vector3 chunkPosition;
        String symbol;
        for (int y = 0; y < Math.max(mapHeight / chunkSize, 1); y++) {
            for (int x = (cameraXPos - distance) / chunkSize; x < (cameraXPos + distance) / chunkSize; x++) {
                for (int z = (cameraZPos - distance) / chunkSize; z < (cameraZPos + distance) / chunkSize; z++) {

                    chunkPosition = new Vector3(x * chunkSize, y * chunkSize, z * chunkSize);

                    if (chunkMap.keySet().contains(chunkPosition)) {
                        continue;
                    }

                    List<MeshObj> meshes = new ArrayList<MeshObj>();

                    for (int positionY = 0; positionY < Math.min(chunkSize, mapHeight); positionY++) {
                        for (int positionX = 0; positionX < chunkSize; positionX++) {
                            for (int positionZ = 0; positionZ < chunkSize; positionZ++) {
                                if (positionY == 0) {
                                    symbol = new Random().nextInt() % 2 == 0 ? "a" : "";
                                } else if (positionX % 6 == 0 && positionZ % 4 == 0) {
                                    symbol = new Random().nextInt() % 2 == 0 ? "b" : "";
                                } else {
                                    symbol = "";
                                }

                                Vector3 vector = new Vector3(positionX, positionY, positionZ);
                                meshes.add(new MeshObj(blockMesh, symbol, vector));
                            }
                        }
                    }

                    chunkMap.put(chunkPosition, new Chunk(meshes));
                }
            }
        }
    }
}
