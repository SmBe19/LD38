package com.smeanox.games.ld38.world.modules;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.io.IOAnimation;

public class MainModule extends Module {
	public MainModule(ModuleLocation moduleLocation) {
		super(moduleLocation);
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleEmpty.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return IOAnimation.HullDefault.texture();
	}
}
