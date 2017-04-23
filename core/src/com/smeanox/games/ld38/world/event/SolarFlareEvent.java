package com.smeanox.games.ld38.world.event;

import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.Resource;
import com.smeanox.games.ld38.world.SpaceStation;

public class SolarFlareEvent extends Event {

	float time;

	public SolarFlareEvent() {
		this.time = MathUtils.random(Consts.EVENT_SOLARFLARE_DURATION);
	}

	@Override
	public void startEvent() {
		SpaceStation.get().getResources().get(Resource.Electricity).value *= 0.5f;
	}

	@Override
	public void update(float delta) {
		time -= delta;
		if (time < 0) {
			finished = true;
		}
	}

	@Override
	public String getDescription() {
		return "There was a solar flare and your\nequipment doesn't work at the moment.";
	}
}
