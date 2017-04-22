package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

@ModuleInformation(
		name = "Main Module",
		width = 3,
		height = 1
)
public class MainModule extends Module {

	static{
		Map<Resource, Float> buildCost = new HashMap<Resource, Float>();
		buildCost.put(Resource.Electricity, 5.f);
		ModuleFactory.putBuildCost(MainModule.class, buildCost);
	}

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
