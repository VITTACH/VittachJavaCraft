package com.vittach.jumpjack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.ui.buttons.JoystickController;
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

import java.util.HashMap;

import static com.badlogic.gdx.Gdx.graphics;

public class MainEngine extends ApplicationAdapter {
    public enum Screen {
        GAME_PLAY, GAME_STOP, GAME_MAIN_SCREEN, WORLD_CONSTRUCT, LOAD_SAVE
    }

    private static MainEngine engineInstance;

    public Screen currentScreen = Screen.GAME_MAIN_SCREEN;
    public int renderWidth = 480;
    public int renderHeight = 272;

    public FirstPersonController fpController;
    public MainScreen mainScreen;

    public JoystickController leftStick;
    public JoystickController rightStick;

    public PauseMenu pauseMenu;
    public BoxButton boxBtn;
    public FileMenu fileMenu;
    public WorldCreator worldCreator;
    public StartMenu startMenu;

    private final int deviceId;

    private HashMap<Screen, GameScreen> gameScreenMap;
    private Preferences preferenceInstance;

    private Viewport viewport;
    private OrthographicCamera orthographicCamera;

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
        boxBtn.dispose();
        worldCreator.dispose();
        fileMenu.dispose();
        startMenu.dispose();
    }

    @Override
    public void resize(int width, int height) {
        orthographicCamera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());

        viewport.update(width, height);
        preferenceInstance.setWidth(viewport.getScreenWidth(), width);
        preferenceInstance.setHeight(viewport.getScreenHeight(), height);
        orthographicCamera.update();

        leftStick.setJoystickPosition();
        rightStick.setJoystickPosition();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        gameScreenMap.get(currentScreen).display(viewport);
    }

    @Override
    public void create() {
        startMenu = new StartMenu();

        preferenceInstance = Preferences.getInstance();

        preferenceInstance.inputListener.addListener(startMenu);
        preferenceInstance.inputListener.addListener(startMenu.gameButton);
        preferenceInstance.inputListener.addListener(startMenu.loadButton);
        preferenceInstance.inputListener.addListener(startMenu.moreButton);

        preferenceInstance.playerMusic.load("1.ogg");
        preferenceInstance.playerMusic.setLoop(true);
        preferenceInstance.playerMusic.setVolume(1f);
        preferenceInstance.playerMusic.play();

        gameScreenMap = new HashMap<Screen, GameScreen>() {{
            put(Screen.GAME_PLAY, new GamePlay());
            put(Screen.GAME_STOP, new GameStop());
            put(Screen.GAME_MAIN_SCREEN, new MainMenu());
            put(Screen.WORLD_CONSTRUCT, new WorldConstructor());
            put(Screen.LOAD_SAVE, new LoadSave());
        }};

        fpController = new FirstPersonController(deviceId);

        leftStick = new JoystickController(JoystickController.Stick.LEFT);
        rightStick = new JoystickController(JoystickController.Stick.RIGHT);

        fileMenu = new FileMenu();
        mainScreen = new MainScreen();
        worldCreator = new WorldCreator();
        pauseMenu = new PauseMenu();

        boxBtn = new BoxButton();
        boxBtn.setPosition(renderWidth - boxBtn.getWidth(), renderHeight - boxBtn.getHeight());

        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false, graphics.getWidth(), graphics.getHeight());
        orthographicCamera.update();

        viewport = new FitViewport(renderWidth, renderHeight, orthographicCamera);

        Gdx.input.setInputProcessor(preferenceInstance.inputListener);
    }
}
