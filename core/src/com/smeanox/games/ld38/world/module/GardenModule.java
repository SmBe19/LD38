package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

public class GardenModule extends Module {

	public GardenModule(ModuleLocation moduleLocation) {
		super(moduleLocation);

		allowNeighbors[Consts.UP] = false;
		allowNeighbors[Consts.DOWN] = false;
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleFlowers.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return IOAnimation.HullDefault3.texture();
	}

	@Override
	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta) {
		hadEnoughResources = tryUseResource(resources, delta, Resource.H2O, 120) &&
				tryUseResource(resources, delta, Resource.Electricity, 45) &&
				tryUseResource(resources, delta, Resource.O2, -135) &&
				tryUseResource(resources, delta, Resource.Food, -72);
	}
}
