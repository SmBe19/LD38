package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

public class RocketPlaceholderModule extends Module {

	public RocketPlaceholderModule(ModuleLocation moduleLocation) {
		super(moduleLocation);

		allowNeighbors[Consts.LEFT] = false;
		allowNeighbors[Consts.RIGHT] = false;
		allowNeighbors[Consts.UP] = false;
		allowNeighbors[Consts.DOWN] = false;
	}

	@Override
	public boolean canRandomWalk() {
		return false;
	}

	@Override
	public boolean canAttachSolarPanel() {
		return false;
	}

	@Override
	public boolean canWalkThrough() {
		return false;
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return null;
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return null;
	}
}
