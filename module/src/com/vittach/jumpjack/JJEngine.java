package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class JJEngine extends ApplicationAdapter {
    static Human human;
    static int deviceId = 1;
    static WorldMap world;
    static LoadAndSave fileLoader;
    static Control leftStick;
    static Control rightStick;

    ShapeRenderer cursor;
    CurrentBlock currentBlock;
    DeathWin deathWin;
    Explorer explorer;
    public Viewport viewport;
    NewCreate news;
    StartMenu menu;
    HealthBar healthBar;
    SelectBoxs selectBoxs;
    private OrthographicCamera orthoCamera;
    int screen = 2;
    private ArrayList<GameScreen> screenList;

    public JJEngine(int deviceId) {
        JJEngine.deviceId = deviceId;
    }

    @Override
    public void create() {
        currentBlock = new CurrentBlock();
        explorer = new Explorer();
        deathWin = new DeathWin();
        world = new WorldMap();
        news = new NewCreate();
        menu = new StartMenu();
        Preference.inputListener.addListener(menu);
        selectBoxs = new SelectBoxs();

        human = new Human(deviceId);
        healthBar = new HealthBar(human);

        world.setSkyBox();
        fileLoader = new LoadAndSave(world);

        Preference.player.load("1.ogg");
        Preference.player.setLoop(true);
        Preference.player.setVolume(1f);
        Preference.player.play();

        screenList = new ArrayList<GameScreen>();
        screenList.add(new GamePlay(this));
        screenList.add(new GameStop(this));
        screenList.add(new MainMenu(this));
        screenList.add(new NewWorld(this));
        screenList.add(new LoadSave(this));

        cursor = new ShapeRenderer();

        currentBlock.setPosition(Preference.windowWidth - currentBlock.background.getWidth() - 5,
                Preference.windowHeight - currentBlock.background.getHeight() - 5);

        healthBar.setPosition(5, Preference.windowHeight - healthBar.background.getHeight() - 5);

        orthoCamera = new OrthographicCamera();
        orthoCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewport = new FitViewport(Preference.windowWidth, Preference.windowHeight, orthoCamera);
        Preference.inputListener.addListener(menu.game);
        Preference.inputListener.addListener(menu.load);
        Preference.inputListener.addListener(menu.exit);

        Gdx.input.setInputProcessor(Preference.inputListener);
    }

    @Override
    public void resize(int width, int height) {
        if (deviceId == 1) {
            leftStick.setJP();
        }

        orthoCamera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());

        viewport.update(width, height);
        Preference.setWidth(viewport.getScreenWidth(), width);
        Preference.setHeight(viewport.getScreenHeight(), height);
        cursor.setProjectionMatrix(orthoCamera.combined);
    }

    @Override
    public void render() {
        orthoCamera.update();
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        screenList.get(screen).render(viewport);
    }

    @Override
    public void dispose() {
        currentBlock.dispose();
        deathWin.dispose();
        healthBar.dispose();
        selectBoxs.dispose();
        news.dispose();
        menu.dispose();
        explorer.dispose();
        world.dispose();
    }
}
