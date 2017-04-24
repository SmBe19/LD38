package com.smeanox.games.ld38.io;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smeanox.games.ld38.Consts;

public enum IOFont {
	grusigPunktBdf(IOTexture.font.texture, 7, 11, "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz!?.,;\"'/+-*0123456789"),
	icons(IOTexture.icons.texture, 16, 16, "MOHEWFSD+-"),
	;

	private final Texture texture;
	private final int width, height;
	private final String chars;

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
			}
			ax += scaledWidth;
			if(text.charAt(i) == '\n'){
				ax = x;
				ay -= scaledHeight * Consts.LINE_SPACING;
			}
		}
	}

	public void draw(SpriteBatch batch, int x, int y, String text){
		draw(batch, x, y, Consts.SPRITE_SIZE, text);
	}

	public float width(int scale, String text) {
		float res = 0;
		float scaledWidth = width * scale / (float) Consts.SPRITE_SIZE;
		float ax = 0;
		for(int i = 0; i < text.length(); i++){
			int idx = chars.indexOf(text.charAt(i));
			ax += scaledWidth;
			if(text.charAt(i) == '\n'){
				res = Math.max(res, ax - scaledWidth);
				ax = 0;
			}
		}
		res = Math.max(res, ax);
		return res;
	}

	public float width(String text) {
		return width(Consts.SPRITE_SIZE, text);
	}

	public float height(int scale, String text){
		float scaledHeight = height * scale / (float) Consts.SPRITE_SIZE;
		int occ = 0;
		for(int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '\n') {
				occ++;
			}
		}
		return scaledHeight * Consts.LINE_SPACING * (occ + 1);
	}

	public float height(String text) {
		return height(Consts.SPRITE_SIZE, text);
	}
}
