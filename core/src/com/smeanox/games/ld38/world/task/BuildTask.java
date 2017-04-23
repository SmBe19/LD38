package com.smeanox.games.ld38.world.task;

import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.Pair;
import com.smeanox.games.ld38.world.SpaceStation;
import com.smeanox.games.ld38.world.module.Module;

public class BuildTask extends Task {

	private Module buildModule;
	private float destX, destY;

	public BuildTask(Module buildModule) {
		this.buildModule = buildModule;

		for(int i = 0; i < 4; i++) {
			if (buildModule.getNeighbor(i) != null) {
				Pair<Integer, Integer> portLocation = buildModule.getModuleLocation().getPortLocation(i);
				destX = portLocation.first + .5f;
				destY = portLocation.second + .5f;
			}
		}
	}

	@Override
	public void onUpdate(float delta) {
		if(!dude.hasPath()){
			if ((dude.getX() - destX) * (dude.getX() - destX) + (dude.getY() - destY) * (dude.getY() - destY) < Consts.ARRIVAL_DISTANCE) {
				buildModule.doBuildProrgess(delta);
				if (buildModule.isFinished()) {
					finished = true;
				}
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
