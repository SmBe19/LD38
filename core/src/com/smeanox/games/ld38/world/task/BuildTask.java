package com.smeanox.games.ld38.world.task;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOSoundEffect;
import com.smeanox.games.ld38.screen.GameScreen;
import com.smeanox.games.ld38.world.Dude;
import com.smeanox.games.ld38.world.Pair;
import com.smeanox.games.ld38.world.SpaceStation;
import com.smeanox.games.ld38.world.module.Module;

public class BuildTask extends Task {

	private Module buildModule;
	private float destX, destY;
	private float startedSound;
	private long soundID;

	public BuildTask(Module buildModule) {
		this.buildModule = buildModule;
	}

	private boolean calculateDestForDir(int dir) {
		Module neighbor = buildModule.getNeighbor(dir);
		if (neighbor != null && neighbor.canWalkThrough()) {
			Pair<Integer, Integer> portLocation = buildModule.getModuleLocation().getPortLocation(dir);
			destX = portLocation.first + .5f;
			destY = portLocation.second + .5f;
			destX += (MathUtils.random() * 2 - 1) * Consts.BUILD_POSITION_RANDOM_OFFSET;
			destY += (MathUtils.random() * 2 - 1) * Consts.BUILD_POSITION_RANDOM_OFFSET;
			return true;
		}
		destX = -100;
		destY = -100;
		return false;
	}

	private boolean findPossiblePath(){
		for(int i = 0; i < 4; i++) {
			if (calculateDestForDir(i)) {
				dude.setDest(destX, destY);
				if(dude.hasPath()){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void onUpdate(float delta) {
		if(!dude.hasPath()){
			if (isArrived(destX, destY)) {
				startedSound -= delta;
				if (startedSound < 0) {
					soundID = IOSoundEffect.building.play();
					startedSound = IOSoundEffect.building.duration;
				}
				buildModule.doBuildProrgess(delta);
				if (buildModule.isFinished()) {
					IOSoundEffect.building.sound.stop(soundID);
					finished = true;
				}
			} else {
				if(!findPossiblePath()){
					returnTask();
				}
			}
		}
	}

	@Override
	protected void onAccept() {
		findPossiblePath();
	}
}
