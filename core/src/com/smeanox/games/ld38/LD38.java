package com.smeanox.games.ld38;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smeanox.games.ld38.screen.GameScreen;

public class LD38 extends Game {
	public static LD38 me;

	private GameScreen gameScreen;

	public LD38() {
		if(me == null){
			me = this;
		}
	}

	@Override
	public void create() {
		showGameScreen();
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
		gameScreen.dispose();
	}
}
