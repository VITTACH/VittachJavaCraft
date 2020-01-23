package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;

import static com.badlogic.gdx.Gdx.graphics;

public class JJEngine extends ApplicationAdapter {
    public int currentScreen = 2;
    public float renderWidth = 480;
    public float renderHeight = 272;
    private int deviceId;

    public FirstPersonController controller;
    public MainGameLoop mainGameLoop;

    public PauseMenu pauseMenu;
    public InventoryButton inventoryBtn;
    public FileExplorer fileExplorer;
    public WorldCreator worldCreator;
    public StartMenu startMenu;

    private Viewport viewport;
    private OrthographicCamera orthographicCamera;

    private ArrayList<GameScreen> gameState;
    private static JJEngine engineInst;
    private Preference prefsInst;

    private JJEngine(int deviceId) {
        this.deviceId = deviceId;
    }

    public static JJEngine getInstance() {
        return getInstance(0);
    }

    public static synchronized JJEngine getInstance(int deviceId) {
        if (engineInst == null) {
            engineInst = new JJEngine(deviceId);
        }
        return engineInst;
    }

    @Override
    public void dispose() {
        pauseMenu.dispose();
        mainGameLoop.dispose();
        inventoryBtn.dispose();
        worldCreator.dispose();
        fileExplorer.dispose();
        startMenu.dispose();
    }

    @Override
    public void resize(int width, int height) {
        orthographicCamera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());

        viewport.update(width, height);
        prefsInst.setWidth(viewport.getScreenWidth(), width);
        prefsInst.setHeight(viewport.getScreenHeight(), height);
        orthographicCamera.update();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        gameState.get(currentScreen).display(viewport);
    }

    @Override
    public void create() {

        startMenu = new StartMenu();

        prefsInst = Preference.getInstance();

        prefsInst.listener.addListener(startMenu);
        prefsInst.listener.addListener(startMenu.gameButton);
        prefsInst.listener.addListener(startMenu.loadButton);
        prefsInst.listener.addListener(startMenu.exitButton);

        prefsInst.playerMusic.load("1.ogg");
        prefsInst.playerMusic.setLoop(true);
        prefsInst.playerMusic.setVolume(1f);
        prefsInst.playerMusic.play();

        gameState = new ArrayList<GameScreen>() {{
            add(new GamePlay());
            add(new GameStop());
            add(new MainMenu());
            add(new WorldConstructor());
            add(new LoadSave());
        }};

        controller = new FirstPersonController(deviceId);

        mainGameLoop = new MainGameLoop();
        fileExplorer = new FileExplorer();
        worldCreator = new WorldCreator();
        pauseMenu = new PauseMenu();

        inventoryBtn = new InventoryButton();
        inventoryBtn.setPosition(renderWidth - inventoryBtn.getWidth(), renderHeight - inventoryBtn.getHeight());

        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false, graphics.getWidth(), graphics.getHeight());
        orthographicCamera.update();

        viewport = new FitViewport(renderWidth, renderHeight, orthographicCamera);

        Gdx.input.setInputProcessor(prefsInst.listener);
    }
}
