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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import xyz.gdxshooter.GameMain;


public class MenuScreen implements Screen {
    private GameMain game;

    private Stage stage;

    private Image logo;
    private ImageButton playbutton;
    private Texture myTexturePlayButton;
    private TextureRegion myTextureRegionPlay;
    private TextureRegionDrawable myTexRegionDrawablePlay;
    private ImageButton exitbutton;
    private Texture myTextureExitButton;
    private TextureRegion myTextureRegionExit;
    private Image background;
    private TextureRegionDrawable myTexRegionDrawableExit;


    public MenuScreen(final GameMain game) {
        this.game = game;
        background = new Image(new Texture(Gdx.files.internal("Menu/background.png")));
        stage = new Stage(new FitViewport(GameMain.SCREEN_WIDTH, GameMain.SCREEN_HEIGHT));
        logo = new Image(new Texture(Gdx.files.internal("Menu/logo.png")));

        float playButtonWidth = GameMain.SCREEN_WIDTH / 3f;
        float playButtonHeight = 0.5f*playButtonWidth;
        myTexturePlayButton = new Texture(Gdx.files.internal("Menu/PlayButton.png"));
        myTextureRegionPlay = new TextureRegion(myTexturePlayButton);
        myTexRegionDrawablePlay = new TextureRegionDrawable(myTextureRegionPlay);
        myTexRegionDrawablePlay.setMinSize(playButtonWidth, playButtonHeight);
        playbutton = new ImageButton(myTexRegionDrawablePlay, new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Menu/PlayButtonPressed.png")))));
        playbutton.setPosition(GameMain.SCREEN_WIDTH - 1.25f*playButtonWidth,
                GameMain.SCREEN_HEIGHT - 1.40f*playButtonHeight);
        playbutton.setScale(playButtonWidth,playButtonHeight);

        float exitButtonWidth = GameMain.SCREEN_WIDTH / 3f;
        float exitButtonHeight = 0.5f*exitButtonWidth;
        myTextureExitButton = new Texture(Gdx.files.internal("Menu/ExitButton.png"));
        myTextureRegionExit = new TextureRegion(myTextureExitButton);
        myTexRegionDrawableExit = new TextureRegionDrawable(myTextureRegionExit);
        myTexRegionDrawableExit.setMinSize(exitButtonWidth, exitButtonHeight);
        exitbutton = new ImageButton(myTexRegionDrawableExit, new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Menu/ExitButtonPressed.png")))));
        exitbutton.setPosition(GameMain.SCREEN_WIDTH - 1.25f*exitButtonWidth,
                0.6f*exitButtonHeight);
        exitbutton.setScale(exitButtonWidth,exitButtonHeight);

        game.getMenuTheme().play();

        final Music music = game.assetManager.get("Music/Sounds/MenuButtonSound.wav", Music.class);

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                music.setVolume(0.5f);
                music.play();
                game.setScreen(new LevelsScreen(game));
            }

        };
        playbutton.addCaptureListener(changeListener);

        ClickListener exitListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                music.setVolume(0.5f);
                music.play();
                game.dispose(); // reference to GameClass
                Gdx.app.exit();
                super.clicked(event, x, y);
            }
        };
        exitbutton.addCaptureListener(exitListener);

        logo.setScale(1.75f,1.75f);
        logo.setPosition(10, 0.5f*GameMain.SCREEN_HEIGHT- 0.85f*logo.getHeight());
        background.setScale(0.6f,0.6f);
        stage.addActor(background);
        stage.addActor(exitbutton);
        stage.addActor(playbutton);

        stage.addActor(logo);

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
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {}
}

