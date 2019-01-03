package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;

public class JJEngine extends ApplicationAdapter {
    public int deviceId;
    public int currentScreen = 2;
    
    public FirstPersonController human;
    public WorldMap worldMapInst;
    public FileController fileController;

    public BlockSelector blockSelector;
    public CurrentBlock currentBlock;
    public FileExplorer fileExplorer;
    public WorldCreator worldCreator;
    public StartMenu startMenuInst;
    
    private Preference prefInst;

    public float renderWidth = 480;
    public float renderHeight = 272;

    private Viewport viewport;
    private OrthographicCamera orthographicCamera;

    private ArrayList<GameScreen> screens;

    private static JJEngine instance;

    private JJEngine(int deviceId) {
        this.deviceId = deviceId;
    }

    public static JJEngine getInstance() {
        return getInstance(0);
    }

    public static synchronized JJEngine getInstance(int deviceId) {
        if (instance == null) {
            instance = new JJEngine(deviceId);
        }
        return instance;
    }

    @Override
    public void create() {
        currentBlock = new CurrentBlock();
        fileExplorer = new FileExplorer();
        worldCreator = new WorldCreator();
        startMenuInst = new StartMenu();
        worldMapInst = new WorldMap();
        blockSelector = new BlockSelector();

        prefInst = Preference.getInstance();
        prefInst.inputListener.addListener(startMenuInst);

        human = new FirstPersonController(deviceId);

        worldMapInst.setSkyBox();
        fileController = new FileController(worldMapInst);

        prefInst.player.load("1.ogg");
        prefInst.player.setLoop(true);
        prefInst.player.setVolume(1f);
        prefInst.player.play();

        screens = new ArrayList<GameScreen>();
        screens.add(new GamePlay());
        screens.add(new GameStop());
        screens.add(new MainMenu());
        screens.add(new NewWorld());
        screens.add(new LoadSave());

        currentBlock.setPosition(renderWidth - currentBlock.background.getWidth(),
                renderHeight - currentBlock.background.getHeight());

        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewport = new FitViewport(renderWidth, renderHeight, orthographicCamera);
        prefInst.inputListener.addListener(startMenuInst.gameButton);
        prefInst.inputListener.addListener(startMenuInst.loadButton);
        prefInst.inputListener.addListener(startMenuInst.exitButton);

        Gdx.input.setInputProcessor(prefInst.inputListener);
    }

    @Override
    public void resize(int width, int height) {
        orthographicCamera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());

        viewport.update(width, height);
        prefInst.setWidth(viewport.getScreenWidth(), width);
        prefInst.setHeight(viewport.getScreenHeight(), height);
    }

    @Override
    public void render() {
        orthographicCamera.update();

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        screens.get(currentScreen).render(viewport);
    }

    @Override
    public void dispose() {
        currentBlock.dispose();
        blockSelector.dispose();
        worldMapInst.dispose();
        worldCreator.dispose();
        startMenuInst.dispose();
        fileExplorer.dispose();
    }
}
