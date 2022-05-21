package xyz.gdxshooter.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import xyz.gdxshooter.Characters.Bullet;
import xyz.gdxshooter.Characters.Character;
import xyz.gdxshooter.Characters.Cow;
import xyz.gdxshooter.Characters.CowType;
import xyz.gdxshooter.GameMain;
import xyz.gdxshooter.Hud.Hud;
import xyz.gdxshooter.Characters.Player;

public class PlayScreen implements Screen {

    private GameMain game;

    private int screenWidth;
    private int screenHeight;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private float cameraAttachProportion;
    private ShapeRenderer shapeRenderer;
    private float runTime;
    private float cameraShift;

    private Hud hud;
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Texture background;
    private Texture whiteCowTexture;
    private Texture whiteCowTextureDead;
    private Texture whiteCowTextureRun1;
    private Texture whiteCowTextureRun2;
    private Animation<Texture> whiteCowMovingAnim;

    private Texture brownCowTexture;
    private Texture brownCowTextureDead;
    private Texture brownCowTextureRun1;
    private Texture brownCowTextureRun2;
    private Animation<Texture> brownCowMovingAnim;

    private Texture blackCowTexture;
    private Texture blackCowTextureDead;
    private Texture blackCowTextureRun1;
    private Texture blackCowTextureRun2;
    private Animation<Texture> blackCowMovingAnim;

    private Texture bulletTexture;

    private Texture playerTexture;
    private Texture playerTextureRun1;
    private Texture playerTextureRun2;

    private Level level;
    private Player player;
    private Array<Character> allNPC;
    private Array<Cow> cows;
    private Array<Bullet> bullets;
    private int levelIdx;

    private Music cowHurt;

    private int i;

    public PlayScreen(GameMain game, int levelIdx) {
        this.game = game;
        this.levelIdx = levelIdx;
        screenWidth = GameMain.SCREEN_WIDTH;
        screenHeight = GameMain.SCREEN_HEIGHT;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
        cameraAttachProportion = 0.4f;

        cowHurt = game.assetManager.get("Music/Sounds/PlayerHurtSound.wav", Music.class);
        // cowHurt = game.assetManager.get("Music/Sounds/CowHurtSound.wav", Music.class);

        playerTexture = new Texture(Gdx.files.internal("Player/player.png"));
        playerTextureRun1 = new Texture(Gdx.files.internal("Player/AnimationRun1.png"));
        playerTextureRun2 = new Texture(Gdx.files.internal("Player/AnimationRun2.png"));
        Animation<Texture> playerMovingAnim = new Animation<>(
                0.17f,
                new Array<>(new Texture[] {playerTextureRun1, playerTextureRun2}),
                Animation.PlayMode.LOOP);

        bulletTexture = new Texture(Gdx.files.internal("Player/Bullet.png"));
        Music playerHurt = game.assetManager.get("Music/Sounds/PlayerHurtSound.wav");
        player = new Player(new Vector2(30, 150),
                            22,
                            40,
                            3,
                            playerTexture,
                            playerTexture,
                            playerMovingAnim,
                            bulletTexture,
                            playerHurt);

        hud = new Hud(game, batch, this, player);

        allNPC = new Array<>();
        cows = new Array<>();
        bullets = new Array<>();

        whiteCowTexture = new Texture(Gdx.files.internal("Enemy/WhiteCow/WhiteCow.png"));
        whiteCowTextureDead = new Texture(Gdx.files.internal("Enemy/WhiteCow/WhiteCowDead.png"));
        whiteCowTextureRun1 = new Texture(Gdx.files.internal("Enemy/WhiteCow/WhiteCowRun1.png"));
        whiteCowTextureRun2 = new Texture(Gdx.files.internal("Enemy/WhiteCow/WhiteCowRun2.png"));
        whiteCowMovingAnim = new Animation<>(
                0.25f,
                new Array<>(new Texture[] {whiteCowTextureRun1, whiteCowTextureRun2}),
                Animation.PlayMode.LOOP);

        brownCowTexture = new Texture(Gdx.files.internal("Enemy/BrownCow/BrownCow.png"));
        brownCowTextureDead = new Texture(Gdx.files.internal("Enemy/BrownCow/BrownCowDead.png"));
        brownCowTextureRun1 = new Texture(Gdx.files.internal("Enemy/BrownCow/BrownCowRun1.png"));
        brownCowTextureRun2 = new Texture(Gdx.files.internal("Enemy/BrownCow/BrownCowRun2.png"));
        brownCowMovingAnim = new Animation<>(
                0.25f,
                new Array<>(new Texture[] {brownCowTextureRun1, brownCowTextureRun2}),
                Animation.PlayMode.LOOP);

        blackCowTexture = new Texture(Gdx.files.internal("Enemy/BlackBigCow/BlackBigCow.png"));
        blackCowTextureDead = new Texture(Gdx.files.internal("Enemy/BlackBigCow/BlackBigCowDead.png"));
        blackCowTextureRun1 = new Texture(Gdx.files.internal("Enemy/BlackBigCow/BlackBigCowRun1.png"));
        blackCowTextureRun2 = new Texture(Gdx.files.internal("Enemy/BlackBigCow/BlackBigCowRun2.png"));
        blackCowMovingAnim = new Animation<>(
                0.34f,
                new Array<>(new Texture[] {blackCowTextureRun1, blackCowTextureRun2}),
                Animation.PlayMode.LOOP);

        maploader = new TmxMapLoader();

        if (levelIdx == 1) {
            background = new Texture(Gdx.files.internal("Level1/background.png"));
            map = maploader.load("Level1/Map.tmx");

            Vector2 pos1 = new Vector2(300, 150);
            Array<Float> routePointsX1 = new Array<>(new Float[]{70f, 700f});
            spawnCow(pos1, routePointsX1, CowType.WHITE);

            Vector2 pos2 = new Vector2(800, 150);
            Array<Float> routePointsX2 = new Array<>(new Float[]{20f, 300f, 100f, 800f});
            spawnCow(pos2, routePointsX2, CowType.BROWN);
        }
        else if (levelIdx == 2) {
            background = new Texture(Gdx.files.internal("Level2/background.png"));
            map = maploader.load("Level2/Map.tmx");

            for (int i = 1; i <= 12; i++) {
                if (i == 1) {
                    Vector2 pos = new Vector2(384, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{100f, 624f});
                    spawnCow(pos, routePointsX, CowType.WHITE);
                } else if (i == 3) {
                    Vector2 pos = new Vector2(816, 170);
                    Array<Float> routePointsX = new Array<>(new Float[]{20f, 624f, 100f, 480f});
                    spawnCow(pos, routePointsX, CowType.WHITE);
                } else if (i == 4) {
                    Vector2 pos = new Vector2(2040, 80);
                    Array<Float> routePointsX = new Array<>(new Float[]{2040f, 2300f});
                    spawnCow(pos, routePointsX, CowType.BLACK);
                } else if (i == 5) {
                    Vector2 pos = new Vector2(2136, 216);
                    Array<Float> routePointsX = new Array<>(new Float[]{2000f, 2136f, 1704f, 2136f});
                    spawnCow(pos, routePointsX, CowType.WHITE);
                } else if (i == 6) {
                    Vector2 pos = new Vector2(1272, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{1272f, 1500f, 1272f, 1400f});
                    spawnCow(pos, routePointsX, CowType.BROWN);
                } else if (i == 7) {

                    Vector2 pos = new Vector2(984, 170);
                    Array<Float> routePointsX = new Array<>(new Float[]{600f,1000f});
                    spawnCow(pos, routePointsX, CowType.BROWN);
                } else if (i == 8) {
                    Vector2 pos = new Vector2(1272, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{1600f, 1200f, 1700f, 1272f});
                    spawnCow(pos, routePointsX, CowType.WHITE);
                } /*else if (i == 9) {
                    Vector2 pos = new Vector2(2200, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{1800f, 2330f});
                    spawnCow(pos, routePointsX, CowType.BROWN);
                } else if (i == 10) {
                    Vector2 pos = new Vector2(2000, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{1800f, 2330f, 2150f, 2300f, 1900f, 2000f});
                    spawnCow(pos, routePointsX, CowType.WHITE);
                } else if (i == 11) {
                    Vector2 pos = new Vector2(1800, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{2000f, 2330f});
                    spawnCow(pos, routePointsX, CowType.BROWN);
                } else if (i == 12) {
                    Vector2 pos = new Vector2(2100, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{1900f, 2330f, 2000f});
                    spawnCow(pos, routePointsX, CowType.WHITE);
                }*/


            }
        }

        else if (levelIdx == 3) {
            background = new Texture(Gdx.files.internal("Level3/WinterBackground.png"));
            map = maploader.load("Level3/Level3.tmx");

            for (int i = 1; i <= 12; i++) {
                if (i == 1) {
                    Vector2 pos = new Vector2(360, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{100f, 728f});
                    spawnCow(pos, routePointsX, CowType.BROWN);
                } else if (i == 3) {
                    Vector2 pos = new Vector2(728, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{20f, 728f, 100f, 480f});
                    spawnCow(pos, routePointsX, CowType.WHITE);
                } else if (i == 4) {
                    Vector2 pos = new Vector2(936, 96);
                    Array<Float> routePointsX = new Array<>(new Float[]{650f, 1000f});
                    spawnCow(pos, routePointsX, CowType.BLACK);
                } else if (i == 5) {
                    Vector2 pos = new Vector2(650, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{1080f, 600f, 800f, 100f});
                    spawnCow(pos, routePointsX, CowType.WHITE);
                } else if (i == 6) {
                    Vector2 pos = new Vector2(1176, 72);
                    Array<Float> routePointsX = new Array<>(new Float[]{1176f, 1500f, 1176f, 1350f});
                    spawnCow(pos, routePointsX, CowType.BLACK);
                } else if (i == 7) {
                    Vector2 pos = new Vector2(1656, 170);
                    Array<Float> routePointsX = new Array<>(new Float[]{1872f,1600f});
                    spawnCow(pos, routePointsX, CowType.BROWN);
                } else if (i == 8) {
                    Vector2 pos = new Vector2(1872, 72);
                    Array<Float> routePointsX = new Array<>(new Float[]{1860f, 2350f, 2000f, 2370f});
                    spawnCow(pos, routePointsX, CowType.WHITE);
                } else if (i == 9) {
                    Vector2 pos = new Vector2(2200, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{1860f, 2330f});
                    spawnCow(pos, routePointsX, CowType.BROWN);
                } else if (i == 10) {
                    Vector2 pos = new Vector2(2000, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{1860f, 2330f, 2150f, 2300f, 1900f, 2000f});
                    spawnCow(pos, routePointsX, CowType.WHITE);
                } else if (i == 11) {
                    Vector2 pos = new Vector2(1880, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{1900f, 2330f});
                    spawnCow(pos, routePointsX, CowType.BLACK);
                } else if (i == 12) {
                    Vector2 pos = new Vector2(2100, 150);
                    Array<Float> routePointsX = new Array<>(new Float[]{1900f, 2330f, 2000f});
                    spawnCow(pos, routePointsX, CowType.WHITE);
                }
            }
        }

        else if (levelIdx == 4) {

        }

        else if (levelIdx == 5) {

        }
        level = new Level(map);
        mapRenderer =  new OrthogonalTiledMapRenderer(map);

        runTime = 0;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1/30f);

        runTime += delta;

        /* обработка ввода игрока и обновление худа */
        hud.render(delta, player, allNPC, bullets, level);
        for (Character character : allNPC)
            if (!character.isDead() && character instanceof Cow)
                ((Cow) character).control();

        /* обновление игрового мира */
        // update positions of player and characters
        player.updatePosition(delta);
        for (i = 0; i < allNPC.size; i++) {
            allNPC.get(i).updatePosition(delta);
            if (allNPC.get(i).getPositionY() < - 2 * GameMain.SCREEN_HEIGHT)
                allNPC.removeIndex(i);
        }
        for (i = 0; i < cows.size; i++) {
            if (cows.get(i).getPositionY() < -2 * GameMain.SCREEN_HEIGHT)
                cows.removeIndex(i);
        }

        // обработка коллизий с уровнем
        level.updateCollisions(delta, player, cows.isEmpty());
        for (Character character : allNPC)
            level.updateCollisions(delta, character, cows.isEmpty());

        // обработка попадания пуль во врагов, столкновения с врагами
        updateFight(delta);

        /* отрисов-очка */
        // Если игрок не в начале и не в конце уровня
        cameraShift = GameMain.SCREEN_WIDTH * cameraAttachProportion;
        if (player.getPositionX() > cameraShift
                && player.getPositionX() + 7 < level.getWidth() - GameMain.SCREEN_WIDTH + cameraShift
                && !player.isDead())
        {
            camera.position.set(player.getPositionX() - cameraShift + GameMain.SCREEN_WIDTH / 2f,
                                GameMain.SCREEN_HEIGHT / 2f,
                                0);
        }

        Gdx.gl.glClearColor(0, 0, 0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        camera.update();

        // уровень
        batch.begin();
        batch.draw(background, -10, -30);
        batch.end();
        mapRenderer.setView(camera);
        mapRenderer.render();

        // все персонажи
        batch.begin();
        player.draw(batch, runTime);
        for (Character character: allNPC)
            character.draw(batch, runTime);
        batch.end();

        // хитбоксы
        if (GameMain.DEBUG) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            Gdx.gl.glLineWidth(3);

            shapeRenderer.rect(player.getHitbox().x, player.getHitbox().y,
                    player.getHitbox().width, player.getHitbox().height);

            for (Character character : allNPC)
                if (!(character instanceof Bullet))
                    shapeRenderer.rect(character.getHitbox().x, character.getHitbox().y,
                        character.getHitbox().width, character.getHitbox().height);

            shapeRenderer.end();
        }

        // худ
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    private void updateFight(float delta) {
        for (Cow cow : cows) {
            if (cow.isDead())
                continue;

            // столкновение коровы с пулями
            for (Bullet bullet : bullets) {
                if (bullet.isDead())
                    continue;

                if (Intersector.overlaps(bullet.getHitbox(), cow.getHitbox())) {
                    bullet.takeDamage(delta, 1);
                    cow.takeDamage(delta, 1);

                    if (bullet.isDead())
                        bullet.setVelocityX(0);
                    if (cow.isDead())
                        cow.setVelocityX(0);
                }
            }

            // столкновение коровы с игроком
            if (Intersector.overlaps(player.getHitbox(), cow.getHitbox()) && !player.isDead()) {

                if (cow.getType() == CowType.BLACK) {
                    player.takeDamage(delta, 2);
                }
                else {
                    player.takeDamage(delta, 1);
                }
            }
        }
    }

    private void spawnCow(Vector2 pos, Array<Float> routePointsX, CowType cowType) {
        Cow cow;
        if (cowType == CowType.WHITE) {
            cow = new Cow(pos, 61, 42, 70, 2, cowType,
                    whiteCowTexture, whiteCowTextureDead, whiteCowMovingAnim, routePointsX, cowHurt);
        }
        else if (cowType == CowType.BROWN) {
            cow = new Cow(pos, 61, 42, 46, 4, cowType,
                    brownCowTexture, brownCowTextureDead, brownCowMovingAnim, routePointsX, cowHurt);
        }
        else {
            cow = new Cow(pos, 73, 49, 30, 15, cowType,
                    blackCowTexture, blackCowTextureDead, blackCowMovingAnim, routePointsX, cowHurt);
        }
        allNPC.add(cow);
        cows.add(cow);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        background.dispose();

        whiteCowTexture.dispose();
        whiteCowTextureDead.dispose();
        whiteCowTextureRun1.dispose();
        whiteCowTextureRun2.dispose();

        brownCowTexture.dispose();
        brownCowTextureDead.dispose();
        brownCowTextureRun1.dispose();
        brownCowTextureRun2.dispose();

        playerTexture.dispose();
        playerTextureRun1.dispose();
        playerTextureRun1.dispose();

        bulletTexture.dispose();
    }

    public int getLevelIdx() {
        return levelIdx;
    }
}
