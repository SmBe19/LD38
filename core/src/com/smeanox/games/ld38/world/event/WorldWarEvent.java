package com.smeanox.games.ld38.world.event;

import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.SpaceStation;

public class WorldWarEvent extends Event {

	@Override
	public void startEvent() {
		SpaceStation.get().setWorldWarStarted(true);
	}

	@Override
	public void update(float delta) {
		if (SpaceStation.get().getTimeOfDay() > Consts.DURATION_DAY * 0.95f) {
			finished = true;
		}
	}

	@Override
	public String getDescription() {
		return "Today there is a world war.";
	}
}
