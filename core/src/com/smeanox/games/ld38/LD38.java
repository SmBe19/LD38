package com.smeanox.games.ld38;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smeanox.games.ld38.screen.GameScreen;
import com.smeanox.games.ld38.screen.MenuScreen;

public class LD38 extends Game {
	public static LD38 me;

	private GameScreen gameScreen;
	private MenuScreen menuScreen;

	public LD38() {
		if(me == null){
			me = this;
		}
	}

	@Override
	public void create() {
		if(Consts.DEBUG) {
			showGameScreen();
		} else {
			showMenuScreen();
		}
	}

	public void showMenuScreen(){
		if (menuScreen == null) {
			menuScreen = new MenuScreen();
		}
		setScreen(menuScreen);
	}

	public void showGameScreen(){
		if (gameScreen == null) {
			gameScreen = new GameScreen();
		}
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (gameScreen != null) {
			gameScreen.dispose();
		}
		if (menuScreen != null) {
			menuScreen.dispose();
		}
	}
}
