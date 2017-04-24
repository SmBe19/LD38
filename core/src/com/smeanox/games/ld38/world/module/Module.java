package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.screen.Window;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Pair;
import com.smeanox.games.ld38.world.Resource;
import com.smeanox.games.ld38.world.SpaceStation;
import com.smeanox.games.ld38.world.event.Event;
import com.smeanox.games.ld38.world.task.BuildTask;

import java.util.Map;

public abstract class Module {
	protected ModuleLocation moduleLocation;
	protected Module[] neighbors = new Module[4];
	protected boolean[] allowNeighbors = new boolean[4];
	protected boolean finished, justFinished, hadEnoughResources, active;
	protected float buildProgress;

	public Module(ModuleLocation moduleLocation) {
		this.moduleLocation = moduleLocation;

		allowNeighbors[Consts.UP] = allowNeighbors[Consts.DOWN] = moduleLocation.getWidth() % 2 == 1;
		allowNeighbors[Consts.RIGHT] = allowNeighbors[Consts.LEFT] = moduleLocation.getHeight() % 2 == 1;

		finished = false;
		justFinished = false;
		hadEnoughResources = true;
		active = true;
	}

	public int rotateDirection(int direction){
		return (direction + 4 - getModuleLocation().getRotation()) % 4;
	}

	public boolean canAttachSolarPanel(){
		return true;
	}

	public float getBuildSpeed(){
		return Consts.BUILD_SPEED * getBuildSpeedMultiplier();
	}

	protected float getBuildSpeedMultiplier(){
		return 1.f;
	}

	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta){
		hadEnoughResources = true;
	}

	protected boolean tryUseResource(Map<Resource, GenericRapper<Float>> resources, float delta, Resource resource, float valuePerDay) {
		resources.get(resource).value -= valuePerDay * delta / Consts.DURATION_DAY;
		return resources.get(resource).value > 0;
	}

	public void adjustResourceMax(Map<Resource, GenericRapper<Float>> resources, boolean add){}

	public Window createWindow(float x, float y){
		return null;
	}

	public boolean isHadEnoughResources() {
		return hadEnoughResources;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean canWalkThrough(){
		return isFinished();
	}

	public boolean canRandomWalk() {
		return true;
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
				justFinished = true;
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

	public Module tryAddNeighbor(int direction, int rotation, ModuleType moduleType){
		if(!canAddNeighbor(direction)){
			return null;
		}
		Module module = ModuleFactory.createModule(moduleType, getModuleLocation().getRotX(), getModuleLocation().getRotY(), rotation);
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
		} else {
			return null;
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

	public Module addNeighbor(int direction, int rotation, ModuleType moduleType){
		Module module = tryAddNeighbor(direction, rotation, moduleType);
		if (module == null) {
			return null;
		}
		setNeighbor(direction, module);
		module.setNeighbor(ModuleLocation.flipDirection(direction), this);
		for(int i = 0; i < 4; i++){
			if(module.canAddNeighbor(i)){
				Pair<Integer, Integer> portLocation = module.getModuleLocation().getPortLocation(i);
				Module other = SpaceStation.get().getModule(portLocation.first, portLocation.second);
				if(other != null && other.canAddNeighbor(ModuleLocation.flipDirection(i))){
					Pair<Integer, Integer> otherPortLocation = other.getModuleLocation().getPortLocation(ModuleLocation.flipDirection(i));
					if(module.getModuleLocation().isPointInModule(otherPortLocation.first, otherPortLocation.second)) {
						module.setNeighbor(i, other);
						other.setNeighbor(ModuleLocation.flipDirection(i), module);
					}
				}
			}
		}
		SpaceStation.get().addModule(module);
		SpaceStation.get().addTask(new BuildTask(module));
		return module;
	}

	public Module tryAddSolar(int direction, int x, int y){
		if(canAddNeighbor(direction)){
			return null;
		}
		if (!canAttachSolarPanel()) {
			return null;
		}
		Module module = ModuleFactory.createModule(ModuleType.SolarModule, x, y, direction);
		if(direction == Consts.UP || direction == Consts.DOWN){
			if(x < getModuleLocation().getRotX() || getModuleLocation().getRotRight() - 1 < x){
				return null;
			}
			if (x == getModuleLocation().getRotRight() - 1){
				Module neighbor = getNeighbor(Consts.RIGHT);
				if(neighbor == null || !neighbor.canAttachSolarPanel() || neighbor.canAddNeighbor(direction)) {
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
		// there might be more than one neighbor so we don't set it
		// setNeighbor(direction, module);
		module.setNeighbor(ModuleLocation.flipDirection(direction), this);
		SpaceStation.get().addModule(module);
		SpaceStation.get().addTask(new BuildTask(module));
		return module;
	}

	public ModuleLocation getModuleLocation() {
		return moduleLocation;
	}

	public boolean isAffectedBySolarFlare(){
		return SpaceStation.get().isSolarFlare() && !Event.hasMagnetModule();
	}

	public boolean isWorking(){
		return isFinished() && isActive() && !isAffectedBySolarFlare();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isJustFinished() {
		return justFinished;
	}

	public void setJustFinished(boolean justFinished) {
		this.justFinished = justFinished;
	}

	public abstract TextureRegion getTextureInterior(float time);

	public abstract TextureRegion getTextureHull(float time);

	public void drawBackground(SpriteBatch batch, float time) {
		TextureRegion textureInterior = getTextureInterior(time);
		if (textureInterior != null) {
			Color oldColor = batch.getColor().cpy();
			if (!finished) {
				batch.setColor(1, 1, 1, Consts.BUILDING_ALPHA);
				TextureRegion buildTexture = new TextureRegion(textureInterior);
				buildTexture.setRegionWidth((int) (textureInterior.getRegionWidth() * buildProgress));
				batch.draw(buildTexture, getModuleLocation().getX(), getModuleLocation().getY(), .5f, .5f,
						buildTexture.getRegionWidth() / (float) Consts.SPRITE_SIZE,
						buildTexture.getRegionHeight() / Consts.SPRITE_SIZE,
						1, 1, getModuleLocation().getRotation() * 90);
			} else {
				if(!isWorking()){
					batch.setColor(Consts.INACTIVE_BRIGHTNESS, Consts.INACTIVE_BRIGHTNESS, Consts.INACTIVE_BRIGHTNESS, 1);
				}
				batch.draw(textureInterior, getModuleLocation().getX(), getModuleLocation().getY(), .5f, .5f,
						textureInterior.getRegionWidth() / Consts.SPRITE_SIZE,
						textureInterior.getRegionHeight() / Consts.SPRITE_SIZE,
						1, 1, getModuleLocation().getRotation() * 90);
			}
			batch.setColor(oldColor);
		}
	}

	public void drawForeground(SpriteBatch batch, float time){
		TextureRegion textureHull = getTextureHull(time);
		if (textureHull != null) {
			Color oldColor = batch.getColor().cpy();
			if (!isHadEnoughResources()) {
				batch.setColor(1, 0.5f, 0.5f, 1);
			}
			batch.draw(textureHull, getModuleLocation().getX(), getModuleLocation().getY(), .5f, .5f,
					textureHull.getRegionWidth() / Consts.SPRITE_SIZE,
					textureHull.getRegionHeight() / Consts.SPRITE_SIZE,
					1, 1, getModuleLocation().getRotation() * 90);
			batch.setColor(oldColor);
		}
	}
}
