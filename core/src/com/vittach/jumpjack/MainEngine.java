package com.vittach.jumpjack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.engine.controller.FirstPersonController;
import com.vittach.jumpjack.engine.render.GameScene;
import com.vittach.jumpjack.ui.buttons.JoystickController;
import com.vittach.jumpjack.ui.screen.*;
import com.vittach.jumpjack.ui.screen.menu.CreateWorldMenu;
import com.vittach.jumpjack.ui.screen.menu.LoadAndSaveMenu;
import com.vittach.jumpjack.ui.screen.menu.PauseMenu;
import com.vittach.jumpjack.ui.screen.menu.StartMenu;

import java.util.HashMap;

public class MainEngine extends ApplicationAdapter {
    public enum Screen {
        GAME_PLAY, GAME_STOP, GAME_MAIN_SCREEN, WORLD_CONSTRUCT, LOAD_SAVE
    }

    public enum Device {
        ANDROID, DESKTOP
    }

    private static MainEngine engineInstance;

    public Screen currentScreen = Screen.GAME_MAIN_SCREEN;
    public int renderWidth = 480;
    public int renderHeight = 272;

    private final Device device;

    public FirstPersonController fpController;
    public JoystickController leftStick;
    public JoystickController rightStick;

    public GameScene gameScene;
    public PauseMenu pauseMenu;
    public LoadAndSaveMenu loadAndSaveMenu;
    public CreateWorldMenu createWorldMenu;
    public StartMenu startMenu;

    private HashMap<Screen, GameScreen> gameScreenMap;
    private Preferences preferenceInstance;

    private Viewport viewport;
    private OrthographicCamera camera;

    private MainEngine(Device device) {
        this.device = device;
    }

    public static MainEngine getInstance() {
        return engineInstance;
    }

    public static synchronized MainEngine getInstance(Device device) {
        if (engineInstance == null) {
            engineInstance = new MainEngine(device);
        }
        return engineInstance;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

        camera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());

        preferenceInstance.setWidth(viewport.getScreenWidth(), width);
        preferenceInstance.setHeight(viewport.getScreenHeight(), height);

        fpController.updateAspectRatio();
        leftStick.updateAspectRatio();
        rightStick.updateAspectRatio();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        viewport.apply();
        gameScreenMap.get(currentScreen).display(viewport);
    }

    @Override
    public void create() {
        preferenceInstance = Preferences.getInstance();

        camera = new OrthographicCamera();
        viewport = new FitViewport(renderWidth, renderHeight, camera);

        gameScreenMap = new HashMap<Screen, GameScreen>() {{
            put(Screen.GAME_PLAY, new GameSceneScreen());
            put(Screen.GAME_STOP, new PauseScreen());
            put(Screen.GAME_MAIN_SCREEN, new MainScreen());
            put(Screen.WORLD_CONSTRUCT, new CreateWorldScreen());
            put(Screen.LOAD_SAVE, new LoadAndSaveScreen());
        }};

        fpController = new FirstPersonController(device);
        leftStick = new JoystickController(JoystickController.Stick.LEFT);
        rightStick = new JoystickController(JoystickController.Stick.RIGHT);

        startMenu = new StartMenu();
        loadAndSaveMenu = new LoadAndSaveMenu();
        gameScene = new GameScene();
        createWorldMenu = new CreateWorldMenu();
        pauseMenu = new PauseMenu();

        preferenceInstance.playerMusic.load("1.ogg");
        preferenceInstance.playerMusic.setLoop(true);
        preferenceInstance.playerMusic.setVolume(1f);
        preferenceInstance.playerMusic.play();

        Gdx.input.setInputProcessor(preferenceInstance.inputListener);
        startMenu.setUpListeners();
    }

    @Override
    public void dispose() {
        pauseMenu.dispose();
        gameScene.dispose();
        createWorldMenu.dispose();
        loadAndSaveMenu.dispose();
        startMenu.dispose();
    }
}
