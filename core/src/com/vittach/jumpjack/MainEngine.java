package com.vittach.jumpjack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.ui.screen.WorldConstructor;
import com.vittach.jumpjack.ui.buttons.BoxButton;
import com.vittach.jumpjack.ui.screen.GameScreen;
import com.vittach.jumpjack.engine.controller.FirstPersonController;
import com.vittach.jumpjack.engine.render.MainScreen;
import com.vittach.jumpjack.ui.screen.GamePlay;
import com.vittach.jumpjack.ui.screen.GameStop;
import com.vittach.jumpjack.ui.screen.LoadSave;
import com.vittach.jumpjack.ui.screen.WorldCreator;
import com.vittach.jumpjack.ui.screen.menu.FileMenu;
import com.vittach.jumpjack.ui.screen.menu.MainMenu;
import com.vittach.jumpjack.ui.screen.menu.PauseMenu;
import com.vittach.jumpjack.ui.screen.menu.StartMenu;

import java.util.ArrayList;

import static com.badlogic.gdx.Gdx.graphics;

public class MainEngine extends ApplicationAdapter {
    enum Screen {
        GAME_STOP, GAME_PLAY, GAME_MAIN_SCREEN
    }

    public int currentScreen = 2;
    public int renderWidth = 480;
    public int renderHeight = 272;
    private int deviceId;

    public FirstPersonController fpController;
    public MainScreen mainScreen;

    public PauseMenu pauseMenu;
    public BoxButton inventoryBtn;
    public FileMenu fileMenu;
    public com.vittach.jumpjack.ui.screen.WorldCreator worldCreator;
    public StartMenu startMenu;

    private Viewport viewport;
    private OrthographicCamera orthographicCamera;

    private ArrayList<GameScreen> gameState;
    private static MainEngine engineInstance;
    private Preferences prefsInstance;

    private MainEngine(int deviceId) {
        this.deviceId = deviceId;
    }

    public static MainEngine getInstance() {
        return getInstance(0);
    }

    public static synchronized MainEngine getInstance(int deviceId) {
        if (engineInstance == null) {
            engineInstance = new MainEngine(deviceId);
        }
        return engineInstance;
    }

    @Override
    public void dispose() {
        pauseMenu.dispose();
        mainScreen.dispose();
        inventoryBtn.dispose();
        worldCreator.dispose();
        fileMenu.dispose();
        startMenu.dispose();
    }

    @Override
    public void resize(int width, int height) {
        orthographicCamera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());

        viewport.update(width, height);
        prefsInstance.setWidth(viewport.getScreenWidth(), width);
        prefsInstance.setHeight(viewport.getScreenHeight(), height);
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

        prefsInstance = Preferences.getInstance();

        prefsInstance.inputListener.addListener(startMenu);
        prefsInstance.inputListener.addListener(startMenu.gameButton);
        prefsInstance.inputListener.addListener(startMenu.loadButton);
        prefsInstance.inputListener.addListener(startMenu.exitButton);

        prefsInstance.playerMusic.load("1.ogg");
        prefsInstance.playerMusic.setLoop(true);
        prefsInstance.playerMusic.setVolume(1f);
        prefsInstance.playerMusic.play();

        gameState = new ArrayList<GameScreen>() {{
            add(new GamePlay());
            add(new GameStop());
            add(new MainMenu());
            add(new WorldConstructor());
            add(new LoadSave());
        }};

        fpController = new FirstPersonController(deviceId);

        mainScreen = new MainScreen();
        fileMenu = new FileMenu();
        worldCreator = new WorldCreator();
        pauseMenu = new PauseMenu();

        inventoryBtn = new BoxButton();
        inventoryBtn.setPosition(renderWidth - inventoryBtn.getWidth(), renderHeight - inventoryBtn.getHeight());

        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false, graphics.getWidth(), graphics.getHeight());
        orthographicCamera.update();

        viewport = new FitViewport(renderWidth, renderHeight, orthographicCamera);

        Gdx.input.setInputProcessor(prefsInstance.inputListener);
    }
}
