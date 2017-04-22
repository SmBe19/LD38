package com.smeanox.games.ld38.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public enum IOTexture {
	station("img/station.png"),
	dude("img/dude.png"),
	font("img/font.png"),
	;

	public Texture texture;

	IOTexture(String file) {
		texture = new Texture(Gdx.files.internal(file), true);
	}
}
