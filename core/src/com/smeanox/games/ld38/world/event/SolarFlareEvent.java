package com.smeanox.games.ld38.world.event;

import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.MessageManager;
import com.smeanox.games.ld38.world.Resource;
import com.smeanox.games.ld38.world.SpaceStation;

public class SolarFlareEvent extends Event {

	private float time;

	public SolarFlareEvent() {
		this.time = MathUtils.random(Consts.EVENT_SOLARFLARE_DURATION);
	}

	@Override
	public void startEvent() {
		if(SpaceStation.get().isWorldWarStarted() && !hasMagnetModule()) {
			SpaceStation.get().getResources().get(Resource.Electricity).value *= 0.5f;
		} else {
			time = 0;
		}
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
		if(SpaceStation.get().isWorldWarStarted()){
			return "Our sensors indicate that the sun is heavily active today. The nuclear bombs of the war have changed the Earth's magnetic field so much that it won't protect us. We need to set up our own magnets.";
		} else {
			return "Scientists have predicted increased solar activity today. This means any electrical device not protected by a magnet will work very unreliably. Luckily, the earth's magnetic field protects us from these effects.";
		}
	}

	@Override
	public MessageManager.Message getMessage() {
		String audio = SpaceStation.get().isWorldWarStarted() ? "narration/LD38RadioVoiceOver096shit.ogg" : "narration/LD38RadioVoiceOver095.ogg";
		return new MessageManager.Message(audio, getDescription());
	}
}
