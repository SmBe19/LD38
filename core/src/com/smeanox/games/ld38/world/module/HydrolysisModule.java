package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.screen.ActivateWindow;
import com.smeanox.games.ld38.screen.Window;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

@ModuleInformation(
		name = "Hydrolysis",
		width = 2,
		height = 1
)
public class HydrolysisModule extends Module {

	static{
		Map<Resource, Float> buildCost = new HashMap<Resource, Float>();
		buildCost.put(Resource.Fe, 200.f);
		buildCost.put(Resource.Si, 100.f);
		ModuleFactory.putBuildCost(HydrolysisModule.class, buildCost);
	}

	public HydrolysisModule(ModuleLocation moduleLocation) {
		super(moduleLocation);

		allowNeighbors[Consts.UP] = false;
		allowNeighbors[Consts.DOWN] = false;
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleHydrolysis.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return IOAnimation.HullDefault2.texture();
	}

	@Override
	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta) {
		hadEnoughResources = tryUseResource(resources, delta, Resource.H2O, 180) &&
				tryUseResource(resources, delta, Resource.Electricity, 180) &&
				tryUseResource(resources, delta, Resource.H2, -20) &&
				tryUseResource(resources, delta, Resource.O2, -160);
	}

	@Override
	public Window createWindow(float x, float y) {
		return new ActivateWindow(x, y, this);
	}
}
