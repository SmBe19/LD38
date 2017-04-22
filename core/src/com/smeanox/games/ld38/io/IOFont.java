package com.smeanox.games.ld38.io;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smeanox.games.ld38.Consts;

public enum IOFont {
	grusigPunktBdf(IOTexture.font.texture, 7, 11, "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz"),
	;

	private Texture texture;
	private int width, height;
	private String chars;

	IOFont(Texture texture, int width, int height, String chars) {
		this.texture = texture;
		this.width = width;
		this.height = height;
		this.chars = chars;
	}

	public void draw(SpriteBatch batch, int x, int y, int scale, String text){
		float scaledWidth = width * scale / (float) Consts.SPRITE_SIZE;
		float scaledHeight = height * scale / (float) Consts.SPRITE_SIZE;
		float ax = x, ay = y;
		for(int i = 0; i < text.length(); i++){
			int idx = chars.indexOf(text.charAt(i));
			if(idx >= 0) {
				batch.draw(texture, ax, ay, scaledWidth, scaledHeight,
						idx * width, 0, width, height, false, false);
			} else {
				if(text.charAt(i) == '\n'){
					ax = x;
					ay += height * Consts.LINE_SPACING;
				}
			}
			ax += scaledWidth;
		}
	}

	public void draw(SpriteBatch batch, int x, int y, String text){
		draw(batch, x, y, 1, text);
	}
}
