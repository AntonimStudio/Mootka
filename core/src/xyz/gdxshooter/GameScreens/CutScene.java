package xyz.gdxshooter.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

import xyz.gdxshooter.GameMain;

public class CutScene implements Screen {
    private GameMain game;
    private VideoPlayer videoPlayer;
    private SpriteBatch batch;
    private Texture videoFrame;

    public CutScene(GameMain game) {
        this.game = game;
        batch = new SpriteBatch();
        videoPlayer = VideoPlayerCreator.createVideoPlayer();
    }

    @Override
    public void show() {
        try {
            videoPlayer.play(Gdx.files.internal("Videos/CutScene.webm"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        videoPlayer.update();
        batch.begin();
        videoFrame = videoPlayer.getTexture();
        if (videoFrame != null)
            batch.draw(videoFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        if (videoPlayer.getCurrentTimestamp() > 13300) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        videoPlayer.pause();
    }

    @Override
    public void resume() {
        videoPlayer.resume();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        videoPlayer.dispose();
    }
}
