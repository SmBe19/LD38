package com.smeanox.games.ld38.world.modules;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.GenericRapper;
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

	public Module addNeighbor(int direction, int rotation, Class<? extends Module> clazz){
		if(!canAddNeighbor(direction)){
			return null;
		}
		Module module = ModuleFactory.createModule(clazz, getModuleLocation().getRotX(), getModuleLocation().getRotY(), rotation);
		boolean success = false;
		switch (direction){
			case Consts.UP:
				if(!canAddNeighbor(Consts.DOWN)){
					break;
				}
				module.getModuleLocation().setRotX(getModuleLocation().getRotX() + getModuleLocation().getRotWidth()/2 - module.getModuleLocation().getRotWidth()/2);
				module.getModuleLocation().setRotY(getModuleLocation().getRotY() + getModuleLocation().getRotHeight());
				success = true;
				break;
			case Consts.RIGHT:
				if(!canAddNeighbor(Consts.LEFT)){
					break;
				}
				module.getModuleLocation().setRotX(getModuleLocation().getRotX() + getModuleLocation().getRotWidth());
				module.getModuleLocation().setRotY(getModuleLocation().getRotY() + getModuleLocation().getRotHeight()/2 - module.getModuleLocation().getRotHeight()/2);
				success = true;
				break;
			case Consts.DOWN:
				if(!canAddNeighbor(Consts.UP)){
					break;
				}
				module.getModuleLocation().setRotX(getModuleLocation().getRotX() + getModuleLocation().getRotWidth()/2 - module.getModuleLocation().getRotWidth()/2);
				module.getModuleLocation().setRotY(getModuleLocation().getRotY() - module.getModuleLocation().getRotHeight());
				success = true;
				break;
			case Consts.LEFT:
				if(!canAddNeighbor(Consts.RIGHT)){
					break;
				}
				module.getModuleLocation().setRotX(getModuleLocation().getRotX() - module.getModuleLocation().getRotWidth());
				module.getModuleLocation().setRotY(getModuleLocation().getRotY() + getModuleLocation().getRotHeight()/2 - module.getModuleLocation().getRotHeight()/2);
				success = true;
				break;
		}
		for(final Module amodule : SpaceStation.get().getModules()){
			if (amodule == module) {
				continue;
			}
			final GenericRapper<Boolean> eminem = new GenericRapper<Boolean>(true);
			module.getModuleLocation().foreach(new ModuleLocation.ForeachRunnable() {
				@Override
				public void run(int x, int y) {
					if(amodule.getModuleLocation().isPointInModule(x, y)){
						eminem.value = false;
					}
				}
			});
			if(!eminem.value){
				success = false;
				break;
			}
		}
		if(!success){
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

	public void draw(final SpriteBatch batch, float time){
		TextureRegion textureInterior = getTextureInterior(time);
		if (textureInterior != null) {
			batch.draw(textureInterior, getModuleLocation().getX(), getModuleLocation().getY(), .5f, .5f,
					textureInterior.getRegionHeight() / Consts.SPRITE_SIZE,
					textureInterior.getRegionWidth() / Consts.SPRITE_SIZE,
					1, 1, getModuleLocation().getRotation() * 90 - 90, true);
		}
		TextureRegion textureHull = getTextureHull(time);
		if (textureHull != null) {
			batch.draw(textureHull, getModuleLocation().getX(), getModuleLocation().getY(), .5f, .5f,
					textureHull.getRegionHeight() / Consts.SPRITE_SIZE,
					textureHull.getRegionWidth() / Consts.SPRITE_SIZE,
					1, 1, getModuleLocation().getRotation() * 90 - 90, true);
		}
	}
}
