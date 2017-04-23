package com.smeanox.games.ld38.io;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.smeanox.games.ld38.Consts;

public enum IOAnimation {
	HullDefault3(IOTexture.station, 0, 0, 3, 1),
	HullDefault2(IOTexture.station, 0, 2, 2, 1),
	HullMain(IOTexture.station, 0, 6, 3, 3),
	HullCross(IOTexture.station, 3, 0, 1, 1),
	ModuleEmpty3(IOTexture.station, 5, 0, 3, 1),
	ModuleHydrolysis(IOTexture.station, 2, 2, 2, 1),
	ModuleFlowers(IOTexture.station, 5, 1, 3, 1),
	ModuleMain(IOTexture.station, 3, 6, 3, 3),
	SolarPanel(IOTexture.station, 0, 4, 4, 2),
	ModuleCross(IOTexture.station, 4, 0, 1, 1),
	Dude1(IOTexture.dude, 1.f, 0, 0, 3),
	;

	public final Animation<TextureRegion> animation;

	IOAnimation(IOTexture texture, float duration, int startx, int starty, int tiles, int width, int height){
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int i = 0; i < tiles; i++){
			frames.add(new TextureRegion(texture.texture, (startx + i * width) * Consts.SPRITE_SIZE, starty * Consts.SPRITE_SIZE, width * Consts.SPRITE_SIZE, height * Consts.SPRITE_SIZE));
		}
		animation = new Animation<TextureRegion>(duration, frames, Animation.PlayMode.LOOP);
	}

	IOAnimation(IOTexture texture, float duration, int startx, int starty, int tiles) {
		this(texture, duration, startx, starty, tiles, 1, 1);
	}

	IOAnimation(IOTexture texture, int startx, int starty, int width, int height){
		this(texture, 1.f, startx, starty, 1, width, height);
	}

	IOAnimation(IOTexture texture, int startx, int starty){
		this(texture, 1.f, startx, starty, 1);
	}

	public TextureRegion keyFrame(float time){
		return animation.getKeyFrame(time);
	}

	public TextureRegion texture(){
		return keyFrame(0);
	}
}
