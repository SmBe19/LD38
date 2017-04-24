package com.smeanox.games.ld38.world.event;

import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.MessageManager;
import com.smeanox.games.ld38.world.SpaceStation;

public class EclipseEvent extends Event {

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
		return "Would you look at that? An eclipse in space. But this does mean that that our solar panels won’t be working for a while.";
	}

	@Override
	public MessageManager.Message getMessage() {
		return new MessageManager.Message("narration/LD38RadioVoiceOver093.ogg", getDescription());
	}
}
