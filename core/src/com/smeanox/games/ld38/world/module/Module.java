package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Pair;
import com.smeanox.games.ld38.world.Resource;
import com.smeanox.games.ld38.world.SpaceStation;

import java.util.Map;

public abstract class Module {
	protected ModuleLocation moduleLocation;
	protected Module[] neighbors = new Module[4];
	protected boolean[] allowNeighbors = new boolean[4];
	protected boolean finished, hadEnoughResources;
	protected float buildProgress;

	public Module(ModuleLocation moduleLocation) {
		this.moduleLocation = moduleLocation;

		allowNeighbors[Consts.UP] = allowNeighbors[Consts.DOWN] = moduleLocation.getWidth() % 2 == 1;
		allowNeighbors[Consts.RIGHT] = allowNeighbors[Consts.LEFT] = moduleLocation.getHeight() % 2 == 1;

		finished = false;
		hadEnoughResources = true;
	}

	private int rotateDirection(int direction){
		return (direction + 4 - getModuleLocation().getRotation()) % 4;
	}

	public boolean canAttachSolarPanel(){
		return true;
	}

	public float getBuildSpeed(){
		return Consts.BUILD_SPEED;
	}

	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta){
		hadEnoughResources = true;
	}

	protected boolean tryUseResource(Map<Resource, GenericRapper<Float>> resources, Resource resource, float valueUse) {
		resources.get(resource).value -= valueUse;
		return resources.get(resource).value > 0;
	}

	public void adjustResourceMax(Map<Resource, GenericRapper<Float>> resources, boolean add){}

	public boolean isHadEnoughResources() {
		return hadEnoughResources;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public float getBuildProgress() {
		return buildProgress;
	}

	public void setBuildProgress(float buildProgress) {
		this.buildProgress = buildProgress;
	}

	public void doBuildProrgess(float delta){
		if (!finished) {
			buildProgress += delta * getBuildSpeed();
			if (buildProgress >= 1.f) {
				finished = true;
			}
		}
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
		if(module.canAddNeighbor(ModuleLocation.flipDirection(direction))) {
			Pair<Integer, Integer> portLocation = getModuleLocation().getPortLocation(direction);
			switch (direction) {
				case Consts.UP:
					module.getModuleLocation().setRotX(portLocation.first - module.getModuleLocation().getRotWidth() / 2);
					module.getModuleLocation().setRotY(portLocation.second);
					break;
				case Consts.RIGHT:
					module.getModuleLocation().setRotX(portLocation.first);
					module.getModuleLocation().setRotY(portLocation.second - module.getModuleLocation().getRotHeight() / 2);
					break;
				case Consts.DOWN:
					module.getModuleLocation().setRotX(portLocation.first - module.getModuleLocation().getRotWidth() / 2);
					module.getModuleLocation().setRotY(portLocation.second + 1 - module.getModuleLocation().getRotHeight());
					break;
				case Consts.LEFT:
					module.getModuleLocation().setRotX(portLocation.first + 1 - module.getModuleLocation().getRotWidth());
					module.getModuleLocation().setRotY(portLocation.second - module.getModuleLocation().getRotHeight() / 2);
					break;
			}
		}
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
				return null;
			}
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
		SpaceStation.get().addModule(module);
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
		if(canAddNeighbor(direction)){
			return null;
		}
		if (!canAttachSolarPanel()) {
			return null;
		}
		Module module = ModuleFactory.createModule(SolarModule.class, x, y, direction);
		if(direction == Consts.UP || direction == Consts.DOWN){
			if(x < getModuleLocation().getRotX() || getModuleLocation().getRotRight() - 1 < x){
				return null;
			}
			if (x == getModuleLocation().getRotRight() - 1){
				Module neighbor = getNeighbor(Consts.RIGHT);
				if(neighbor == null || !neighbor.canAttachSolarPanel()) {
					return null;
				} else {
					if(direction == Consts.UP && neighbor.getModuleLocation().getRotTop() != getModuleLocation().getRotTop() ||
							direction == Consts.DOWN && neighbor.getModuleLocation().getRotY() != getModuleLocation().getRotY()){
						return null;
					}
				}
			}
		} else {
			if(y < getModuleLocation().getRotY() || getModuleLocation().getRotTop() - 1 < y){
				return null;
			}
			if (y == getModuleLocation().getRotTop() - 1){
				Module neighbor = getNeighbor(Consts.UP);
				if(neighbor == null || !neighbor.canAttachSolarPanel()) {
					return null;
				} else {
					if(direction == Consts.RIGHT && neighbor.getModuleLocation().getRotRight() != getModuleLocation().getRotRight() ||
							direction == Consts.LEFT && neighbor.getModuleLocation().getRotX() != getModuleLocation().getRotX()){
						return null;
					}
				}
			}
		}
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
		SpaceStation.get().addModule(module);
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
			if (!finished) {
				Color oldColor = batch.getColor().cpy();
				batch.setColor(1, 1, 1, Consts.BUILDING_ALPHA);
				TextureRegion buildTexture = new TextureRegion(textureInterior);
				buildTexture.setRegionWidth((int) (textureInterior.getRegionWidth() * buildProgress));
				batch.draw(buildTexture, getModuleLocation().getX(), getModuleLocation().getY(), .5f, .5f,
						buildTexture.getRegionWidth() / (float) Consts.SPRITE_SIZE,
						buildTexture.getRegionHeight() / Consts.SPRITE_SIZE,
						1, 1, getModuleLocation().getRotation() * 90);
				batch.setColor(oldColor);
			} else {
				batch.draw(textureInterior, getModuleLocation().getX(), getModuleLocation().getY(), .5f, .5f,
						textureInterior.getRegionWidth() / Consts.SPRITE_SIZE,
						textureInterior.getRegionHeight() / Consts.SPRITE_SIZE,
						1, 1, getModuleLocation().getRotation() * 90);
			}
		}
	}

	public void drawForeground(SpriteBatch batch, float time){
		TextureRegion textureHull = getTextureHull(time);
		if (textureHull != null) {
			Color oldColor = batch.getColor().cpy();
			batch.draw(textureHull, getModuleLocation().getX(), getModuleLocation().getY(), .5f, .5f,
					textureHull.getRegionWidth() / Consts.SPRITE_SIZE,
					textureHull.getRegionHeight() / Consts.SPRITE_SIZE,
					1, 1, getModuleLocation().getRotation() * 90);
		}
	}
}
