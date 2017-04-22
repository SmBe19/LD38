package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

@ModuleInformation(
		name = "Cross Module",
		width = 1,
		height = 1
)
public class CrossModule extends Module {

	static{
		Map<Resource, Float> buildCost = new HashMap<Resource, Float>();
		buildCost.put(Resource.Electricity, 5.f);
		ModuleFactory.putBuildCost(CrossModule.class, buildCost);
	}

	public CrossModule(ModuleLocation moduleLocation) {
		super(moduleLocation);
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleCross.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return IOAnimation.HullCross.texture();
	}
}
