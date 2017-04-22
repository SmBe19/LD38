package com.smeanox.games.ld38.world.modules;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Pair;
import com.smeanox.games.ld38.world.SpaceStation;

public abstract class Module {
	private ModuleLocation moduleLocation;
	private Module[] neighbors = new Module[4];
	private boolean[] allowNeighbors = new boolean[4];

	public Module(ModuleLocation moduleLocation) {
		this.moduleLocation = moduleLocation;

		allowNeighbors[Consts.UP] = allowNeighbors[Consts.DOWN] = moduleLocation.getWidth() % 2 == 1;
		allowNeighbors[Consts.RIGHT] = allowNeighbors[Consts.LEFT] = moduleLocation.getHeight() % 2 == 1;
	}

	private int rotateDirection(int direction){
		return (direction + getModuleLocation().getRotation()) % 4;
	}

	public Module getNeighbor(int direction){
		return neighbors[rotateDirection(direction)];
	}

	public void setNeighbor(int direction, Module module){
		neighbors[rotateDirection(direction)] = module;
	}

	public boolean canAddNeighbor(int direction){
		return allowNeighbors[rotateDirection(direction)] && neighbors[rotateDirection(direction)] == null;
	}

	public Module tryAddNeighbor(int direction, int rotation, Class<? extends Module> clazz){
		if(!canAddNeighbor(direction)){
			return null;
		}
		Module module = ModuleFactory.createModule(clazz, getModuleLocation().getRotX(), getModuleLocation().getRotY(), rotation);
		boolean success = false;
		if(module.canAddNeighbor(ModuleLocation.flipDirection(direction))) {
			Pair<Integer, Integer> portLocation = getModuleLocation().getPortLocation(direction);
			switch (direction) {
				case Consts.UP:
					module.getModuleLocation().setRotX(portLocation.first - module.getModuleLocation().getRotWidth() / 2);
					module.getModuleLocation().setRotY(portLocation.second);
					success = true;
					break;
				case Consts.RIGHT:
					module.getModuleLocation().setRotX(portLocation.first);
					module.getModuleLocation().setRotY(portLocation.second - module.getModuleLocation().getRotHeight() / 2);
					success = true;
					break;
				case Consts.DOWN:
					module.getModuleLocation().setRotX(portLocation.first - module.getModuleLocation().getRotWidth() / 2);
					module.getModuleLocation().setRotY(portLocation.second + 1 - module.getModuleLocation().getRotHeight());
					success = true;
					break;
				case Consts.LEFT:
					module.getModuleLocation().setRotX(portLocation.first + 1 - module.getModuleLocation().getRotWidth());
					module.getModuleLocation().setRotY(portLocation.second - module.getModuleLocation().getRotHeight() / 2);
					success = true;
					break;
			}
		}
		if(success) {
			for (final Module amodule : SpaceStation.get().getModules()) {
				final GenericRapper<Boolean> eminem = new GenericRapper<Boolean>(true);
				module.getModuleLocation().foreach(new ModuleLocation.ForeachRunnable() {
					@Override
					public void run(int x, int y) {
						if (amodule.getModuleLocation().isPointInModule(x, y)) {
							eminem.value = false;
						}
					}
				});
				if (!eminem.value) {
					success = false;
					break;
				}
			}
		}
		if(!success){
			return null;
		}
		return module;
	}

	public Module addNeighbor(int direction, int rotation, Class<? extends Module> clazz){
		Module module = tryAddNeighbor(direction, rotation, clazz);
		if (module == null) {
			return null;
		}
		setNeighbor(direction, module);
		module.setNeighbor(ModuleLocation.flipDirection(direction), this);
		SpaceStation.get().getModules().add(module);
		for(int i = 0; i < 4; i++){
			if(module.canAddNeighbor(i)){
				Pair<Integer, Integer> portLocation = module.getModuleLocation().getPortLocation(i);
				Module other = SpaceStation.get().getModule(portLocation.first, portLocation.second);
				if(other != null && other.canAddNeighbor(ModuleLocation.flipDirection(i))){
					module.setNeighbor(i, other);
					other.setNeighbor(ModuleLocation.flipDirection(i), module);
				}
			}
		}
		return module;
	}

	public Module tryAddSolar(int direction, int x, int y){
		// todo remove false
		if(canAddNeighbor(direction) && false){
			return null;
		}
		// todo check if in range
		Module module = ModuleFactory.createModule(SolarModule.class, x, y, direction);
		switch (direction) {
			case Consts.UP:
				module.getModuleLocation().setRotX(x);
				module.getModuleLocation().setRotY(getModuleLocation().getRotY() + getModuleLocation().getRotHeight());
				break;
			case Consts.RIGHT:
				module.getModuleLocation().setRotX(getModuleLocation().getRotX() + getModuleLocation().getRotWidth());
				module.getModuleLocation().setRotY(y);
				break;
			case Consts.DOWN:
				module.getModuleLocation().setRotX(x);
				module.getModuleLocation().setRotY(getModuleLocation().getRotY() - module.getModuleLocation().getRotHeight());
				break;
			case Consts.LEFT:
				module.getModuleLocation().setRotX(getModuleLocation().getRotX() - module.getModuleLocation().getRotWidth());
				module.getModuleLocation().setRotY(y);
				break;
		}
		for(final Module amodule : SpaceStation.get().getModules()){
			final GenericRapper<Boolean> eminem = new GenericRapper<Boolean>(true);
			module.getModuleLocation().foreach(new ModuleLocation.ForeachRunnable() {
				@Override
				public void run(int x, int y) {
					if (amodule.getModuleLocation().isPointInModule(x, y)) {
						eminem.value = false;
					}
				}
			});
			if (!eminem.value) {
				return null;
			}
		}
		return module;
	}

	public Module addSolar(int direction, int x, int y){
		Module module = tryAddSolar(direction, x, y);
		if (module == null) {
			return null;
		}
		SpaceStation.get().getModules().add(module);
		return module;
	}

	public ModuleLocation getModuleLocation() {
		return moduleLocation;
	}

	public abstract TextureRegion getTextureInterior(float time);

	public abstract TextureRegion getTextureHull(float time);

	public void drawBackground(SpriteBatch batch, float time) {
		TextureRegion textureInterior = getTextureInterior(time);
		if (textureInterior != null) {
			batch.draw(textureInterior, getModuleLocation().getX(), getModuleLocation().getY(), .5f, .5f,
					textureInterior.getRegionHeight() / Consts.SPRITE_SIZE,
					textureInterior.getRegionWidth() / Consts.SPRITE_SIZE,
					1, 1, getModuleLocation().getRotation() * 90 - 90, false);
		}
	}

	public void drawForeground(SpriteBatch batch, float time){
		TextureRegion textureHull = getTextureHull(time);
		if (textureHull != null) {
			batch.draw(textureHull, getModuleLocation().getX(), getModuleLocation().getY(), .5f, .5f,
					textureHull.getRegionHeight() / Consts.SPRITE_SIZE,
					textureHull.getRegionWidth() / Consts.SPRITE_SIZE,
					1, 1, getModuleLocation().getRotation() * 90 - 90, false);
		}
	}
}
