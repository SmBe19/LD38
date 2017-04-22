package com.smeanox.games.ld38.world.modules;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.io.IOAnimation;

public class SolarModule extends Module {
	public SolarModule(ModuleLocation moduleLocation) {
		super(moduleLocation);
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return null;
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return IOAnimation.SolarPanel.texture();
	}
}
