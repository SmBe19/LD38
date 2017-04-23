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
	HullWide23(IOTexture.station, 0, 9, 2, 3),
	HullWide33(IOTexture.station, 6, 7, 3, 3),
	ModuleEmpty3(IOTexture.station, 5, 0, 3, 1),
	ModuleHydrolysis(IOTexture.station, 2, 2, 2, 1),
	ModuleFlowers(IOTexture.station, 5, 1, 3, 1),
	ModuleMain(IOTexture.station, 3, 6, 3, 3),
	SolarPanel(IOTexture.station, 0, 4, 4, 2),
	ModuleCross(IOTexture.station, 4, 0, 1, 1),
	ModuleSleeping(IOTexture.station, 2, 9, 2, 3),
	ModuleRockets(IOTexture.station, 0, 12, 3, 3),
	ModuleRadio(IOTexture.station, 8, 0, 2, 3),
	ModuleStorage(IOTexture.station, 4, 9, 2, 3),
	ModuleCryogenic(IOTexture.station, 9, 7, 3, 3),
	ModuleMagnet(IOTexture.station, 12, 7, 3, 3),

	Rocket(IOTexture.station, 11, 0, 2, 5),

	Char1MIdle(IOTexture.charMale, 1.f, 0, 0, 2),
	Char1MMove(IOTexture.charMale, 0.2f, 0, 1, 7),
	Char2MIdle(IOTexture.charMale, 1.f, 0, 2, 2),
	Char2MMove(IOTexture.charMale, 0.2f, 0, 3, 7),
	Char3MIdle(IOTexture.charMale, 1.f, 0, 4, 2),
	Char3MMove(IOTexture.charMale, 0.2f, 0, 5, 7),
	Char4MIdle(IOTexture.charMale, 1.f, 0, 6, 2),
	Char4MMove(IOTexture.charMale, 0.2f, 0, 7, 7),
	Char1FIdle(IOTexture.charFemale, 1.f, 0, 0, 2),
	Char1FMove(IOTexture.charFemale, 0.2f, 0, 1, 7),
	Char2FIdle(IOTexture.charFemale, 1.f, 0, 2, 2),
	Char2FMove(IOTexture.charFemale, 0.2f, 0, 3, 7),
	Char3FIdle(IOTexture.charFemale, 1.f, 0, 4, 2),
	Char3FMove(IOTexture.charFemale, 0.2f, 0, 5, 7),
	Char4FIdle(IOTexture.charFemale, 1.f, 0, 6, 2),
	Char4FMove(IOTexture.charFemale, 0.2f, 0, 7, 7),
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
