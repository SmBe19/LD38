package com.smeanox.games.ld38.world.event;

import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.SpaceStation;

public class LaunchFailureEvent extends Event {

	@Override
	public void startEvent() {
	}

	@Override
	public void update(float delta) {
		if (SpaceStation.get().getTimeOfDay() > Consts.DURATION_DAY * 0.95f) {
			finished = true;
		}
	}

	@Override
	public String getDescription() {
		return "I just received the message that there’s been a failure in the launch of today’s supply rocket. There won’t be another delivery until tomorrow. We’ll have to see how we manage.";
	}
}
