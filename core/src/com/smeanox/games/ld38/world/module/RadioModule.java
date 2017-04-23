package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

@ModuleInformation(
		name = "Radio",
		width = 3,
		height = 1
)
public class RadioModule extends Module {

	static{
		Map<Resource, Float> buildCost = new HashMap<Resource, Float>();
		buildCost.put(Resource.Fe, 300.f);
		buildCost.put(Resource.Si, 150.f);
		ModuleFactory.putBuildCost(RadioModule.class, buildCost);
	}

	public RadioModule(ModuleLocation moduleLocation) {
		super(moduleLocation);

		allowNeighbors[Consts.UP] = false;
		allowNeighbors[Consts.DOWN] = false;
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleEmpty3.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return IOAnimation.HullDefault3.texture();
	}
}
