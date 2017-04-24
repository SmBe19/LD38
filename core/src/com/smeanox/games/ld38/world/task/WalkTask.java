package com.smeanox.games.ld38.world.task;

import com.smeanox.games.ld38.Consts;

public class WalkTask extends Task{

	private float destX, destY;

	public WalkTask(float destX, float destY) {
		this.destX = destX;
		this.destY = destY;
	}

	@Override
	public void onUpdate(float delta) {
		if(!dude.hasPath()){
			if (isArrived(destX, destY)) {
				finished = true;
			} else {
				dude.setDest(destX, destY);
			}
		}
	}

	@Override
	protected void onAccept() {
		dude.setDest(destX, destY);
	}
}
