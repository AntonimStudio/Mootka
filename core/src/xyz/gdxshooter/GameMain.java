package xyz.gdxshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import xyz.gdxshooter.GameScreens.MenuScreen;
import xyz.gdxshooter.GameScreens.PlayScreen;

public class GameMain extends Game {
	public static final boolean DEBUG = false;

	public static final int SCREEN_WIDTH = 500;
	public static final int SCREEN_HEIGHT = 280;

	private Music menuTheme;
	public AssetManager assetManager;

	@Override
	public void create () {
		assetManager = new AssetManager();
		assetManager.load("Music/Theme/DemoMusic.mp3", Music.class);
		assetManager.load("Music/Theme/MenuTheme.mp3", Music.class);
		assetManager.load("Music/Sounds/MenuButtonSound.wav", Music.class);
		assetManager.load("Music/Sounds/PlayerHurtSound.wav", Music.class);
		// assetManager.load("Music/Sounds/CowHurtSound.wav", Music.class);
		assetManager.finishLoading();

		menuTheme = assetManager.get("Music/Theme/MenuTheme.mp3", Music.class);

		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}

	public Music getMenuTheme() {
		return menuTheme;
	}

}
