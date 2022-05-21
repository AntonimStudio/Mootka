package xyz.gdxshooter.Hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import xyz.gdxshooter.Characters.Bullet;
import xyz.gdxshooter.Characters.Character;
import xyz.gdxshooter.GameMain;
import xyz.gdxshooter.GameScreens.LevelsScreen;
import xyz.gdxshooter.GameScreens.MenuScreen;
import xyz.gdxshooter.GameScreens.PlayScreen;
import xyz.gdxshooter.GameScreens.Level;
import xyz.gdxshooter.Characters.Player;

public class Hud {
    GameMain game;
    PlayScreen playScreen;

    public Stage stage;
    private Viewport viewport;
    private Integer worldTimer;
    private float timeCount;
    private static Integer score;

    private Label coordLabel;
    private Label velocityLabel;
    private Label accelLabel;
    private Label scoreLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;

    private Texture myTextureMenuButton;
    private TextureRegion myTextureRegionMenu;
    private TextureRegionDrawable myTexRegionDrawableMenu;
    private ImageButton menubutton;
    private ImageButton firebutton;
    private Texture myTextureFireButton;
    private TextureRegion myTextureRegionFire;
    private TextureRegionDrawable myTexRegionDrawableFire;

    private ImageButton jumpbutton;
    private Texture myTextureJumpButton;
    private TextureRegion myTextureRegionJump;
    private TextureRegionDrawable myTexRegionDrawableJump;

    private ImageButton restartButton;
    private ImageButton youWinButton;

    private WalkingController walkingController;
    private int controllerPointer;
    private Music firebuttonSound;
    private Music theme;
    private Music loseSound;
    private Music winSound;

    private Array<Image> hpImages;
    private Image currentHpImage;
    private int lastFrameHp;

    private boolean isPlayerInactive;

    public Hud(GameMain game, SpriteBatch batch, PlayScreen playScreen, Player player) {
        this.game = game;
        this.playScreen = playScreen;

        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(GameMain.SCREEN_WIDTH, GameMain.SCREEN_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);


        coordLabel = new Label("X: Y: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        velocityLabel = new Label("Vx: Vy: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        accelLabel = new Label("Ax: Ay: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("MOODUCK", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        if (GameMain.DEBUG)
            table.add(marioLabel).expandX().padTop(10);
        table.add().expandX().padTop(10);
        table.add().expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(coordLabel).expandX().padTop(10);

        table.row();
        if (GameMain.DEBUG) {
            table.add(scoreLabel).expandX();
            table.add(levelLabel).expandX();
        }
        table.add().expandX();
        table.add().expandX();
        table.add(velocityLabel).expandX();
        table.add(accelLabel).expandX();

        AssetManager manager = game.assetManager;
        manager.load("Music/Sounds/FireButtonSound.wav", Music.class);
        manager.finishLoading();
        firebuttonSound = manager.get("Music/Sounds/FireButtonSound.wav", Music.class);
        firebuttonSound.setVolume(0.15f);

        manager.load("Music/Theme/DemoMusic.mp3", Music.class);
        manager.finishLoading();
        theme = manager.get("Music/Theme/DemoMusic.mp3", Music.class);

        manager.load("Music/Sounds/LoseSound.mp3", Music.class);
        manager.finishLoading();
        loseSound = manager.get("Music/Sounds/LoseSound.mp3", Music.class);

        manager.load("Music/Sounds/WinSound.mp3", Music.class);
        manager.finishLoading();
        winSound = manager.get("Music/Sounds/WinSound.mp3", Music.class);

        theme.setVolume(0.5f);
        theme.play();
        theme.setLooping(true);

        float menuButtonWidth = GameMain.SCREEN_WIDTH / 16f;
        myTextureMenuButton = new Texture(Gdx.files.internal("Hud/MenuButton.png"));
        myTextureRegionMenu = new TextureRegion(myTextureMenuButton);
        myTexRegionDrawableMenu = new TextureRegionDrawable(myTextureRegionMenu);
        myTexRegionDrawableMenu.setMinSize(menuButtonWidth, menuButtonWidth);
        menubutton = new ImageButton(myTexRegionDrawableMenu, new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Hud/MenuButtonPressed.png")))));
        menubutton.setPosition(GameMain.SCREEN_WIDTH - 1.25f*menuButtonWidth,
                               GameMain.SCREEN_HEIGHT - 1.25f* menuButtonWidth);
        menubutton.setScale(menuButtonWidth, menuButtonWidth);


        float fireButtonWidth = GameMain.SCREEN_WIDTH / 7f;
        myTextureFireButton = new Texture(Gdx.files.internal("Hud/FireButton.png"));
        myTextureRegionFire = new TextureRegion(myTextureFireButton);
        myTexRegionDrawableFire = new TextureRegionDrawable(myTextureRegionFire);
        myTexRegionDrawableFire.setMinSize(fireButtonWidth, fireButtonWidth);
        firebutton = new ImageButton(myTexRegionDrawableFire, new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Hud/FireButtonPressed.png")))));
        firebutton.setPosition(GameMain.SCREEN_WIDTH - 1.25f*fireButtonWidth,
                GameMain.SCREEN_HEIGHT - 3.75f* fireButtonWidth);
        firebutton.setScale(fireButtonWidth, fireButtonWidth);
        firebutton.setSize(fireButtonWidth, fireButtonWidth);

        float jumpButtonWidth = GameMain.SCREEN_WIDTH / 16f;
        myTextureJumpButton = new Texture(Gdx.files.internal("Hud/JumpButton.png"));
        myTextureRegionJump = new TextureRegion(myTextureJumpButton);
        myTexRegionDrawableJump = new TextureRegionDrawable(myTextureRegionJump);
        myTexRegionDrawableJump.setMinSize(jumpButtonWidth, jumpButtonWidth);
        jumpbutton = new ImageButton(myTexRegionDrawableJump, new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Hud/JumpButtonPressed.png")))));
        jumpbutton.setPosition(GameMain.SCREEN_WIDTH - 1.25f * jumpButtonWidth,
                GameMain.SCREEN_HEIGHT - 6 * jumpButtonWidth);
        jumpbutton.setScale(jumpButtonWidth, jumpButtonWidth);

        float restartButtonSideLen = GameMain.SCREEN_WIDTH / 6f;
        restartButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(
                Gdx.files.internal("Hud/YouLose.png")))), new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Hud/YouLosePressed.png")))));
        restartButton.setPosition(GameMain.SCREEN_WIDTH / 2f - restartButtonSideLen / 2f,
                GameMain.SCREEN_HEIGHT / 2f - restartButtonSideLen / 2f);
        restartButton.setScale(restartButtonSideLen, restartButtonSideLen);

        float youWinButtonSideLen = GameMain.SCREEN_WIDTH / 5f;
        youWinButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(
                Gdx.files.internal("Hud/YouWin.png")))), new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("Hud/YouWinPressed.png")))));
        youWinButton.setPosition(GameMain.SCREEN_WIDTH / 2f - youWinButtonSideLen / 2f,
                GameMain.SCREEN_HEIGHT / 2f - youWinButtonSideLen / 2f);
        youWinButton.setScale(youWinButtonSideLen, youWinButtonSideLen);

        Texture controllerTexture = new Texture(Gdx.files.internal("Hud/Controller.png"));
        controllerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        TextureRegion controllerFrameActive = new TextureRegion(controllerTexture, 0, 0, WalkingController.frameWidth, WalkingController.frameHeight);
        TextureRegion controllerCircleActive = new TextureRegion(controllerTexture, WalkingController.frameWidth, 0, WalkingController.circleWidth, WalkingController.circleHeight);
        TextureRegion controllerFrameInactive = new TextureRegion(controllerTexture, 0,  WalkingController.frameHeight, WalkingController.frameWidth, WalkingController.frameHeight);
        TextureRegion controllerCircleInactive = new TextureRegion(controllerTexture, WalkingController.frameWidth, WalkingController.frameHeight,
                WalkingController.circleWidth, WalkingController.circleHeight);
        Skin controllerSkin = new Skin();

        controllerSkin.add("frameActive", controllerFrameActive);
        controllerSkin.add("circleActive", controllerCircleActive);
        controllerSkin.add("frameInactive", controllerFrameInactive);
        controllerSkin.add("circleInactive", controllerCircleInactive);

        this.walkingController = new WalkingController(this.viewport, controllerSkin);

        lastFrameHp = 0;
        Image hp3 = new Image(new Texture(Gdx.files.internal("Hud/HealthPoints3.png")));
        Image hp2 = new Image(new Texture(Gdx.files.internal("Hud/HealthPoints2.png")));
        Image hp1 = new Image(new Texture(Gdx.files.internal("Hud/HealthPoints1.png")));
        Image hp0 = new Image(new Texture(Gdx.files.internal("Hud/HealthPoints0.png")));
        hpImages = new Array<>(new Image[] {hp0, hp1, hp2, hp3});

        for (Image hpImage : hpImages) {
            hpImage.setScale(1.75f,1.75f);
            hpImage.setPosition(10, GameMain.SCREEN_HEIGHT - 2.25f * hpImage.getHeight());
        }

        currentHpImage = hpImages.get(player.getHp());
        lastFrameHp = player.getHp();

        isPlayerInactive = false;

        stage.addActor(walkingController);

        stage.addActor(firebutton);
        stage.addActor(jumpbutton);
        stage.addActor(currentHpImage);
        stage.addActor(menubutton);
        if (GameMain.DEBUG)
            stage.addActor(table);

        stage.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                if (x <= GameMain.SCREEN_WIDTH / 2f) {
                    walkingController.setCenterPosition(x, y);
                    walkingController.makeActive();
                    controllerPointer = pointer;

                    if (!walkingController.isTouched())
                        walkingController.fire(event);
                }
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                if (pointer == controllerPointer)
                {
                    walkingController.makeInactive();
                    walkingController.resetPosition();
                }
            }
        });
    }

    public void render(float delta, Player player, Array<Character> otherChars, Array<Bullet> bullets, Level level) {
        Gdx.input.setInputProcessor(stage);

        if (lastFrameHp != player.getHp()) {
            currentHpImage.setDrawable(hpImages.get(player.getHp()).getDrawable());
            lastFrameHp = player.getHp();
        }

        if (GameMain.DEBUG) {
            coordLabel.setText("X: " + player.getPositionX() + " Y: " + player.getPositionY());
            velocityLabel.setText("Vx: " + player.getVelocityX() + " Vy: " + player.getVelocityY());
            accelLabel.setText("Ax: " + player.getAccelX() + " Ay: " + player.getAccelY());
        }

        if (menubutton.isPressed()) {
            theme.stop();
            game.getMenuTheme().play();
            game.setScreen(new LevelsScreen(game));
        }

        if (!player.isDead() && !level.isCompleted()) {
            if (walkingController.isTouched())
                player.controlWithJoystick(walkingController.getKnobPercentX(), walkingController.getKnobPercentY());
            else {
                player.setVelocityX(0);
                player.setControlled(false);
            }

            if (jumpbutton.isPressed())
                player.jump();

            if (firebutton.isPressed()) {
                Bullet bullet = player.shoot(delta);
                if (bullet != null) {
                    firebuttonSound.play();
                    otherChars.add(bullet);
                    bullets.add(bullet);
                }
            }
        }
        else {
            if (!isPlayerInactive) {
                walkingController.remove();
                firebutton.remove();
                jumpbutton.remove();
                if (player.isDead()) {
                    theme.stop();
                    loseSound.play();
                    stage.addActor(restartButton);
                }
                else if (level.isCompleted()) {
                    theme.stop();
                    winSound.play();
                    stage.addActor(youWinButton);
                }
                isPlayerInactive = true;
            }
            else {
                if (restartButton.isPressed()) {
                    theme.stop();
                    if (loseSound.isPlaying())
                        loseSound.stop();
                    int levelIdx = playScreen.getLevelIdx();
                    playScreen.dispose();
                    game.setScreen(new PlayScreen(game, levelIdx));
                }
                if (youWinButton.isPressed()) {
                    if (winSound.isPlaying())
                        winSound.stop();
                    int levelIdx = playScreen.getLevelIdx();
                    playScreen.dispose();

                    if (levelIdx == 1)
                        game.setScreen(new PlayScreen(game, 2));
                    else if (levelIdx == 2)
                        game.setScreen(new PlayScreen(game, 3));
                    else if (levelIdx == 3)
                        game.setScreen(new MenuScreen(game));
                }
            }
        }

    }

}