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

public class MagnetModule extends Module {

	public MagnetModule(ModuleLocation moduleLocation) {
		super(moduleLocation);

		allowNeighbors[Consts.UP] = false;
		allowNeighbors[Consts.DOWN] = false;
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleMagnet.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return IOAnimation.HullWide33.texture();
	}

	@Override
	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta) {
		hadEnoughResources = tryUseResource(resources, delta, Resource.Electricity, 120);
	}

	@Override
	public Window createWindow(float x, float y) {
		if(!isAffectedBySolarFlare()) {
			return new ActivateWindow(x, y, this);
		} else {
			return null;
		}
	}
}
