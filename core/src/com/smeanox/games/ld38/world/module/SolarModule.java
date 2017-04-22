package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

@ModuleInformation(
		name = "Solar Panel",
		width = 4,
		height = 2
)
public class SolarModule extends Module {

	static{
		Map<Resource, Float> buildCost = new HashMap<Resource, Float>();
		buildCost.put(Resource.Electricity, 5.f);
		ModuleFactory.putBuildCost(SolarModule.class, buildCost);
	}

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

	@Override
	public boolean canAttachSolarPanel() {
		return false;
	}

	@Override
	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta) {
	}
}
