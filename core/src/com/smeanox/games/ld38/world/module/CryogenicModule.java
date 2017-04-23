package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

@ModuleInformation(
		name = "Cryogenic",
		width = 3,
		height = 3
)
public class CryogenicModule extends Module {

	static{
		Map<Resource, Float> buildCost = new HashMap<Resource, Float>();
		buildCost.put(Resource.H2, 120.f);
		buildCost.put(Resource.H2O, 600.f);
		buildCost.put(Resource.H2O, 300.f);
		buildCost.put(Resource.Fe, 1200.f);
		buildCost.put(Resource.Si, 800.f);
		ModuleFactory.putBuildCost(CryogenicModule.class, buildCost);
	}

	public CryogenicModule(ModuleLocation moduleLocation) {
		super(moduleLocation);

		allowNeighbors[Consts.UP] = false;
		allowNeighbors[Consts.DOWN] = false;
	}

	@Override
	protected float getBuildSpeedMultiplier() {
		return 0.1f;
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleCryogenic.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return IOAnimation.HullWide33.texture();
	}
}
