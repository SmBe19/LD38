package com.smeanox.games.ld38.io;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum IOTexture {
	station("img/station.png"),
	dude("img/dude.png"),
	charMale("img/CharMale.png"),
	charFemale("img/CharFemale.png"),
	font("img/font.png"),
	icons("img/icons.png"),
	ui("img/ui.png"),
	bg("img/bg.png"),
	map("img/map.png"),
	clouds("img/clouds.png"),
	;

	public static TextureRegion pixel = new TextureRegion(IOTexture.ui.texture, 17, 0, 1, 1);

	public final Texture texture;

	IOTexture(String file) {
		texture = new Texture(Gdx.files.internal(file), Gdx.app.getType() != Application.ApplicationType.WebGL);
	}
}
