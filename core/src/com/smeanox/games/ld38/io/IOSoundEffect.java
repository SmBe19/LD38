package com.smeanox.games.ld38.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public enum IOSoundEffect {
	building("music/LD38BuildingSound.ogg", 0.8f, 11),
	;

	public final Sound sound;
	public final float volume;
	public final float duration;

	IOSoundEffect(String file, float volume, float duration) {
		sound = Gdx.audio.newSound(Gdx.files.internal(file));
		this.volume = volume;
		this.duration = duration;
	}

	public long play(){
		return sound.play(volume);
	}
}
