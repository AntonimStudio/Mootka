package xyz.gdxshooter.GameScreens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import xyz.gdxshooter.GameMain;


public class LevelsScreen implements Screen {
    private GameMain game;

    private Stage stage;

    private ImageButton level1button;
    private ImageButton level2button;
    private ImageButton level3button;
    private ImageButton level4button;
    private ImageButton level5button;
    private ImageButton level6button;
    private Texture myTextureLevelButton;
    private TextureRegion myTextureRegionLevel;
    private TextureRegionDrawable myTexRegionDrawableLevel;
    private ImageButton exitbutton;
    private Texture myTextureExitButton;
    private TextureRegion myTextureRegionExit;
    private Image background;
    private TextureRegionDrawable myTexRegionDrawableExit;

    public LevelsScreen(final GameMain game) {
        this.game = game;
        stage = new Stage(new FitViewport(GameMain.SCREEN_WIDTH, GameMain.SCREEN_HEIGHT));
        background = new Image(new Texture(Gdx.files.internal("Menu/background.png")));

        float levelButtonWidth = GameMain.SCREEN_WIDTH / 12f;
        float levelButtonHeight = levelButtonWidth;
        myTextureLevelButton = new Texture(Gdx.files.internal("Levels/LevelButton1.png"));
        myTextureRegionLevel = new TextureRegion(myTextureLevelButton);
        myTexRegionDrawableLevel = new TextureRegionDrawable(myTextureRegionLevel);
        myTexRegionDrawableLevel.setMinSize(levelButtonWidth, levelButtonHeight);
        level1button = new ImageButton(myTexRegionDrawableLevel, new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Levels/LevelButton1Pressed.png")))));
        level1button.setPosition(GameMain.SCREEN_WIDTH - 10f*levelButtonWidth,
                GameMain.SCREEN_HEIGHT - 4f*levelButtonHeight);
        level1button.setScale(levelButtonWidth,levelButtonHeight);

        myTextureLevelButton = new Texture(Gdx.files.internal("Levels/LevelButton2.png"));
        myTextureRegionLevel = new TextureRegion(myTextureLevelButton);
        myTexRegionDrawableLevel = new TextureRegionDrawable(myTextureRegionLevel);
        myTexRegionDrawableLevel.setMinSize(levelButtonWidth, levelButtonHeight);
        level2button = new ImageButton(myTexRegionDrawableLevel, new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Levels/LevelButton2Pressed.png")))));
        level2button.setPosition(GameMain.SCREEN_WIDTH - 8.5f*levelButtonWidth,
                GameMain.SCREEN_HEIGHT - 4f*levelButtonHeight);
        level2button.setScale(levelButtonWidth,levelButtonHeight);

        myTextureLevelButton = new Texture(Gdx.files.internal("Levels/LevelButton3.png"));
        myTextureRegionLevel = new TextureRegion(myTextureLevelButton);
        myTexRegionDrawableLevel = new TextureRegionDrawable(myTextureRegionLevel);
        myTexRegionDrawableLevel.setMinSize(levelButtonWidth, levelButtonHeight);
        level3button = new ImageButton(myTexRegionDrawableLevel, new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Levels/LevelButton3Pressed.png")))));
        level3button.setPosition(GameMain.SCREEN_WIDTH - 7f*levelButtonWidth,
                GameMain.SCREEN_HEIGHT - 4f*levelButtonHeight);
        level3button.setScale(levelButtonWidth,levelButtonHeight);

        myTextureLevelButton = new Texture(Gdx.files.internal("Levels/LevelButton4.png"));
        myTextureRegionLevel = new TextureRegion(myTextureLevelButton);
        myTexRegionDrawableLevel = new TextureRegionDrawable(myTextureRegionLevel);
        myTexRegionDrawableLevel.setMinSize(levelButtonWidth, levelButtonHeight);
        level4button = new ImageButton(myTexRegionDrawableLevel, new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Levels/LevelButton4Pressed.png")))));
        level4button.setPosition(GameMain.SCREEN_WIDTH - 5.5f*levelButtonWidth,
                GameMain.SCREEN_HEIGHT - 4f*levelButtonHeight);
        level4button.setScale(levelButtonWidth,levelButtonHeight);

        myTextureLevelButton = new Texture(Gdx.files.internal("Levels/LevelButton5.png"));
        myTextureRegionLevel = new TextureRegion(myTextureLevelButton);
        myTexRegionDrawableLevel = new TextureRegionDrawable(myTextureRegionLevel);
        myTexRegionDrawableLevel.setMinSize(levelButtonWidth, levelButtonHeight);
        level5button = new ImageButton(myTexRegionDrawableLevel, new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Levels/LevelButton5Pressed.png")))));
        level5button.setPosition(GameMain.SCREEN_WIDTH - 4f*levelButtonWidth,
                GameMain.SCREEN_HEIGHT - 4f*levelButtonHeight);
        level5button.setScale(levelButtonWidth,levelButtonHeight);

        myTextureLevelButton = new Texture(Gdx.files.internal("Levels/LockedLevel.png"));
        myTextureRegionLevel = new TextureRegion(myTextureLevelButton);
        myTexRegionDrawableLevel = new TextureRegionDrawable(myTextureRegionLevel);
        myTexRegionDrawableLevel.setMinSize(levelButtonWidth, levelButtonHeight);
        level6button = new ImageButton(myTexRegionDrawableLevel, new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Levels/LockedLevelPressed.png")))));
        level6button.setPosition(GameMain.SCREEN_WIDTH - 2.5f*levelButtonWidth,
                GameMain.SCREEN_HEIGHT - 4f*levelButtonHeight);
        level6button.setScale(levelButtonWidth,levelButtonHeight);

        myTextureExitButton = new Texture(Gdx.files.internal("Levels/BackButton.png"));
        myTextureRegionExit = new TextureRegion(myTextureExitButton);
        myTexRegionDrawableExit = new TextureRegionDrawable(myTextureRegionExit);
        myTexRegionDrawableExit.setMinSize(levelButtonWidth, levelButtonHeight);
        exitbutton = new ImageButton(myTexRegionDrawableExit, new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Levels/BackButtonPressed.png")))));
        exitbutton.setPosition(GameMain.SCREEN_WIDTH - 1.25f*levelButtonWidth,
                0.6f*levelButtonHeight);
        exitbutton.setScale(levelButtonWidth,levelButtonHeight);

        AssetManager manager = new AssetManager();
        manager.load("Music/Sounds/LevelsButtonSound.wav", Music.class);
        manager.finishLoading();
        final Music buttonsound = manager.get("Music/Sounds/LevelsButtonSound.wav", Music.class);

        manager.load("Music/Sounds/LockedLevelButtonSound.wav", Music.class);
        manager.finishLoading();
        final Music lockedbuttonsound = manager.get("Music/Sounds/LockedLevelButtonSound.wav", Music.class);

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonsound.setVolume(0.5f);
                buttonsound.play();
                game.getMenuTheme().stop();
                game.setScreen(new PlayScreen(game, 1));
            }
        };
        level1button.addCaptureListener(changeListener);

        ChangeListener changeListener2 = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonsound.setVolume(0.5f);
                buttonsound.play();
                game.getMenuTheme().stop();
                game.setScreen(new PlayScreen(game, 2));
            }
        };
        level2button.addCaptureListener(changeListener2);

        ChangeListener changeListener3 = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonsound.setVolume(0.5f);
                buttonsound.play();
                game.getMenuTheme().stop();
                game.setScreen(new PlayScreen(game, 3));
            }
        };
        level3button.addCaptureListener(changeListener3);

        ChangeListener changeListener4 = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonsound.setVolume(0.5f);
                buttonsound.play();
            }
        };
        level4button.addCaptureListener(changeListener4);

        ChangeListener changeListener5 = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonsound.setVolume(0.5f);
                buttonsound.play();
            }
        };
        level5button.addCaptureListener(changeListener5);

        ChangeListener changeListenerLock = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                lockedbuttonsound.setVolume(0.5f);
                lockedbuttonsound.play();
            }
        };
        level6button.addCaptureListener(changeListenerLock);

        ChangeListener changeListener1 = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonsound.setVolume(0.5f);
                buttonsound.play();
                game.setScreen(new MenuScreen(game));
            }
        };
        exitbutton.addCaptureListener(changeListener1);

        background.setScale(0.8f,0.8f);
        stage.addActor(background);
        stage.addActor(exitbutton);

        stage.addActor(level1button);
        stage.addActor(level2button);
        stage.addActor(level3button);
        stage.addActor(level4button);
        stage.addActor(level5button);
        stage.addActor(level6button);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
    }
}