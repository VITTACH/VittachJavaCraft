package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.files.FileHandle;
import com.vittach.jumpjack.framework.Chunk;
import com.vittach.jumpjack.framework.MeshCompress;
import com.vittach.jumpjack.framework.MeshObj;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.badlogic.gdx.Gdx.graphics;

public class MainGameLoop {
    private final Mesh blockMesh;
    private final ShaderProgram shader;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Vector3 camPosition;

    private final Map<Vector3, Mesh> meshMap = new HashMap<Vector3, Mesh>();
    private final Map<String, Texture> textureMap = new HashMap<String, Texture>();
    private final Map<Vector3, Chunk> chunkMap = new ConcurrentHashMap<Vector3, Chunk>();

    private final JJEngine engineInst = JJEngine.getInstance();
    private final int distance = engineInst.controller.viewDistance;
    private final int chunkSize = 32;
    private final int mapHeight = 32;

    public void dispose() {
        textureMap.clear();
        chunkMap.clear();
    }

    public MainGameLoop() {
        camPosition = new Vector3(0, 0, 0);
        shapeRenderer.setColor(0.4f, 0.69f, 0.9f, 1);
        setTextures(Gdx.files.internal("3d/blocksSprite.png"));
        shader = new ShaderProgram(Gdx.files.internal("glshs/vertex.glsl"), Gdx.files.internal("glshs/fragment.glsl"));

        MeshBuilder builder = new MeshBuilder();
        VertexAttributes attributes = new VertexAttributes(
                new VertexAttribute(Usage.Position, 3, "a_Position"), new VertexAttribute(Usage.Normal, 3, "a_Normal"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_TexCoord")
        );

        builder.begin(attributes, GL20.GL_TRIANGLES);
        float blockSize = 1f;
        builder.box(blockSize, blockSize, blockSize);
        blockMesh = builder.end();
    }

    private void setTextures(FileHandle path) {
        Texture texture = new Texture(path);
        TextureRegion tmp[][] = TextureRegion.split(texture, texture.getWidth() / 8, texture.getHeight() / 8);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, 16, 16);
        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        for (int i = 6; i < 8; i++) {
            String symbol = i % 2 == 0 ? "a" : "b";

            FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA4444, 16, 16, true);
            buffer.begin();
            batch.begin();
            batch.draw(tmp[i][0], 0, 0);
            batch.end();
            buffer.end();

            textureMap.put(symbol, buffer.getColorBufferTexture());
        }

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

    private void perlinNoise(int cameraXPos, int cameraZPos) {
        Vector3 newPosition;
        String symbol;
        for (int y = 0; y < mapHeight / chunkSize; y++) {
            for (int x = (cameraXPos - distance) / chunkSize; x < (cameraXPos + distance) / chunkSize; x++) {
                for (int z = (cameraZPos - distance) / chunkSize; z < (cameraZPos + distance) / chunkSize; z++) {

                    newPosition = new Vector3(x * chunkSize, y * chunkSize, z * chunkSize);

                    if (chunkMap.keySet().contains(newPosition)) {
                        continue;
                    }

                    List<MeshObj> meshes = new ArrayList<MeshObj>();

                    for (int positionY = 0; positionY < chunkSize; positionY++) {
                        if (positionY > 0) continue;
                        for (int positionX = 0; positionX < chunkSize; positionX++) {
                            for (int positionZ = 0; positionZ < chunkSize; positionZ++) {
                                symbol = new Random().nextInt() % 2 == 0 ? "a" : "b";

                                Vector3 position = new Vector3(positionX, positionY, positionZ);
                                meshes.add(new MeshObj(blockMesh, symbol, position));
                            }
                        }
                    }

                    chunkMap.put(newPosition, new Chunk(meshes));
                }
            }
        }
    }

    private Mesh compressMesh(List<MeshObj> meshObjects) {
        List<Mesh> meshes = new ArrayList<Mesh>();
        List<Matrix4> transactionList = new ArrayList<Matrix4>();
        for (MeshObj meshObject : meshObjects) {
            meshes.add(meshObject.getMesh());
            transactionList.add(new Matrix4().setToTranslation(meshObject.getPosition()));
        }
        return MeshCompress.mergeMeshes(meshes, transactionList);
    }

    public void display(Viewport viewport) {
        System.out.println("fps count " + graphics.getFramesPerSecond());

        viewport.apply();
        PerspectiveCamera fpcCamera = engineInst.controller.getCamera();

        camPosition = fpcCamera.position;

        // background filled
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, graphics.getWidth(), graphics.getHeight());
        shapeRenderer.end();
        //

        for (Map.Entry<String, Texture> texture : textureMap.entrySet()) {
            texture.getValue().bind(texture.getKey().hashCode());
        }

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        shader.begin();
        shader.setUniformMatrix("modelView", fpcCamera.combined);
        Matrix4 model = new Matrix4();
        for (Map.Entry<Vector3, Chunk> chunkEntry : chunkMap.entrySet()) {
            Vector3 pos = chunkEntry.getKey();
            if (Math.abs((int) camPosition.x - pos.x) > distance
                    || Math.abs((int) camPosition.z - pos.z) > distance) {
                continue;
            }

            List<MeshObj> blockObjs = chunkEntry.getValue().getMeshObjs();
            Mesh mergedMesh = meshMap.get(pos);
            if (mergedMesh == null) {
                mergedMesh = compressMesh(blockObjs);
                meshMap.put(pos, mergedMesh);
            }

            shader.setUniformMatrix("model", model.setToTranslation(pos));
            shader.setUniformi("u_texture", "a".hashCode());
            mergedMesh.render(shader, GL20.GL_TRIANGLES);
        }
        shader.end();

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }
}
