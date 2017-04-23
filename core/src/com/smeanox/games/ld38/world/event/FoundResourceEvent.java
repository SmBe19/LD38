package com.smeanox.games.ld38.world.event;

import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.Resource;
import com.smeanox.games.ld38.world.SpaceStation;

public class FoundResourceEvent extends Event {

	private String description;
	private Resource resource;
	private float amount;

	public FoundResourceEvent() {
		resource = Resource.values()[MathUtils.random(Resource.values().length - 1)];
		amount = MathUtils.random() * Consts.EVENT_MAX_RESOURCE / resource.deliveryCost;
		amount = Math.min(amount, SpaceStation.get().getResourceMax(resource) - SpaceStation.get().getResource(resource));
		if (amount > 0.01f) {
			description = "You found " + (int) amount + " " + resource.displayName + ".";
		} else {
			description = "You found some " + resource.displayName + " but you\ndidn't have enough space to store them.";
		}
		finished = true;
	}

	@Override
	public void startEvent() {
		if (amount > 0.01f) {
			SpaceStation.get().getResources().get(resource).value += amount;
		}
		finished = true;
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public String getDescription() {
		return description;
	}
}
