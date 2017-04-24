package com.smeanox.games.ld38.world.event;


import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.ld38.world.SpaceStation;
import com.smeanox.games.ld38.world.module.MagnetModule;
import com.smeanox.games.ld38.world.module.Module;
import com.smeanox.games.ld38.world.module.RocketModule;

public abstract class Event {

	public static boolean hasRocketModule() {
		for (Module module : SpaceStation.get().getModules()) {
			if (module.isFinished() && module instanceof RocketModule) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasMagnetModule() {
		for (Module module : SpaceStation.get().getModules()) {
			if (module.isFinished() && module.isActive() && module instanceof MagnetModule) {
				return true;
			}
		}
		return false;
	}

	public static Event getRandomEvent(){
		if(!SpaceStation.get().isWorldWarStarted() && (SpaceStation.get().getDay() > 10 || hasRocketModule())){
			return new WorldWarEvent();
		}
		float randVal = MathUtils.random();
		if (randVal < 0.2f) {
			return new SolarFlareEvent();
		} else if (randVal < 0.4f) {
			return new LaunchFailureEvent();
		} else if (randVal < 0.6f) {
			return new EclipseEvent();
		} else if (randVal < 0.8f) {
			return new FoundResourceEvent();
		} else {
			return null;
		}
	}

	protected boolean finished;

	public abstract void startEvent();

	public abstract void update(float delta);

	public abstract String getDescription();

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
