package com.smeanox.games.ld38.world.task;

import com.smeanox.games.ld38.Consts;

public class WaitTask extends Task{

	private float time;

	public WaitTask(float time) {
		this.time = time;
	}

	@Override
	public void onUpdate(float delta) {
		time -= delta;
		if (time < 0) {
			finished = true;
		}
	}

	@Override
	protected void onAccept() {}
}
