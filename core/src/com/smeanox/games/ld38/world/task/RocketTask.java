package com.smeanox.games.ld38.world.task;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.Dude;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Pair;
import com.smeanox.games.ld38.world.Resource;
import com.smeanox.games.ld38.world.SpaceStation;
import com.smeanox.games.ld38.world.module.ModuleLocation;
import com.smeanox.games.ld38.world.module.RocketModule;

import java.util.HashMap;
import java.util.Map;

public class RocketTask extends Task {

	private RocketModule rocketModule;
	private float destX, destY;
	private boolean flying;
	private float remainingTime;

	private static Map<Resource, Float> taskCost, taskReturn;

	public static Map<Resource, Float> getCost(){
		if (taskCost == null) {
			taskCost = new HashMap<Resource, Float>();
			taskCost.put(Resource.H2, 10.f);
			taskCost.put(Resource.O2, 80.f);
			taskCost.put(Resource.Food, 12.f);
			taskCost.put(Resource.Electricity, 120.f);
		}
		return taskCost;
	}

	private static Map<Resource, Float> getReturn(){
		if (taskReturn == null) {
			taskReturn = new HashMap<Resource, Float>();
			taskReturn.put(Resource.H2O, 630.f);
			taskReturn.put(Resource.Fe, 300.f);
			taskReturn.put(Resource.Si, 100.f);
		}
		return taskReturn;
	}

	public RocketTask(RocketModule rocketModule) {
		this.rocketModule = rocketModule;
		int direction = rocketModule.antiRotateDirection(Consts.LEFT);
		Pair<Integer, Integer> portLocation = rocketModule.getNeighbor(direction).getModuleLocation().getPortLocation(ModuleLocation.flipDirection(direction));
		destX = portLocation.first + .5f;
		destY = portLocation.second + .5f;
		flying = false;
		remainingTime = MathUtils.random(Consts.ROCKET_MIN_TIME, Consts.ROCKET_MAX_TIME);
	}

	public void removeResources() {
		Map<Resource, GenericRapper<Float>> resources = SpaceStation.get().getResources();
		Map<Resource, Float> cost = getCost();
		for (Resource resource : Resource.values()) {
			if (cost.containsKey(resource)) {
				resources.get(resource).value -= cost.get(resource);
			}
		}
	}

	public void addResources() {
		Map<Resource, GenericRapper<Float>> resources = SpaceStation.get().getResources();
		Map<Resource, Float> aReturn = new HashMap<Resource, Float>(getReturn());
		StringBuilder sb = new StringBuilder();
		sb.append("You mined an asteroid with\n");
		for (Resource resource : Resource.values()) {
			if (aReturn.containsKey(resource)) {
				aReturn.put(resource, aReturn.get(resource) * MathUtils.random(Consts.ROCKET_MIN_RETURN, Consts.ROCKET_MAX_RETURN));
				resources.get(resource).value += aReturn.get(resource);
				sb.append(aReturn.get(resource).intValue());
				sb.append(" ");
				sb.append(resource.displayName);
				sb.append(",\n");
			}
		}
		SpaceStation.get().clampResources();
		sb.replace(sb.length - 2, sb.length, ".");
		SpaceStation.get().addMessage(sb.toString());
	}

	@Override
	protected void onUpdate(float delta) {
		if (flying) {
			remainingTime -= delta;
			if (remainingTime < 0) {
				addResources();
				finished = true;
				dude.setVisible(true);
			}
		} else if (!dude.hasPath()) {
			if (isArrived(destX, destY)) {
				flying = true;
				dude.setVisible(false);
				rocketModule.setRocketFlight(SpaceStation.get().getTime(), SpaceStation.get().getTime() + remainingTime);
			} else {
				dude.setDest(destX, destY);
				if (!dude.hasPath()) {
					returnTask();
				}
			}
		}
	}

	@Override
	protected void onAccept() {
		dude.setDest(destX, destY);
	}
}
