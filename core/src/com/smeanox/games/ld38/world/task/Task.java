package com.smeanox.games.ld38.world.task;

import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.Dude;
import com.smeanox.games.ld38.world.SpaceStation;

public abstract class Task {

	protected Dude dude;
	protected boolean accepted;
	protected boolean finished;
	protected boolean idleTask;

	public final void update(float delta){
		if (finished || !accepted) {
			return;
		}
		onUpdate(delta);
	}

	protected abstract void onUpdate(float delta);

	protected abstract void onAccept();

	protected void returnTask(){
		System.out.println("return task");
		dude.removeTask();
		setAccepted(false);
		if(!isIdleTask()) {
			SpaceStation.get().addTask(this);
		}
	}

	public boolean isArrived(float destX, float destY) {
		return (dude.getX() - destX) * (dude.getX() - destX) + (dude.getY() - destY) * (dude.getY() - destY) < Consts.ARRIVAL_DISTANCE;
	}

	public Dude getDude() {
		return dude;
	}

	public final void accept(Dude dude) {
		if(accepted){
			throw new RuntimeException("Task already accepted");
		}
		finished = false;
		accepted = true;
		this.dude = dude;
		onAccept();
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
		if(!accepted){
			dude = null;
		}
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isIdleTask() {
		return idleTask;
	}

	public void setIdleTask(boolean idleTask) {
		this.idleTask = idleTask;
	}
}
