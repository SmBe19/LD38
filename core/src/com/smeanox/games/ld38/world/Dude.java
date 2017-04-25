package com.smeanox.games.ld38.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.io.IOTexture;
import com.smeanox.games.ld38.screen.GameScreen;
import com.smeanox.games.ld38.world.module.Module;
import com.smeanox.games.ld38.world.module.RocketModule;
import com.smeanox.games.ld38.world.module.RocketPlaceholderModule;
import com.smeanox.games.ld38.world.task.Task;
import com.smeanox.games.ld38.world.task.WaitTask;
import com.smeanox.games.ld38.world.task.WalkTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dude {

	private IOAnimation textureIdle, textureWalk;
	private float animationOffset, damageMultiplier;
	private float x, y, hp, destX, destY;
	private boolean visible;
	private boolean hadEnoughResources, flipped;
	private Task currentTask;
	private Queue<Pair<Float, Float>> currentPath;

	public Dude(IOAnimation textureIdle, IOAnimation textureWalk, float x, float y) {
		this.textureIdle = textureIdle;
		this.textureWalk = textureWalk;
		this.x = x;
		this.y = y;
		this.hp = 1;
		this.destX = x;
		this.destY = y;
		visible = true;
		hadEnoughResources = true;
		currentTask = null;
		currentPath = null;
		this.animationOffset = MathUtils.random() * textureWalk.animation.getAnimationDuration();
		this.damageMultiplier = MathUtils.random() + 1.f;
		flipped = false;
	}

	public static Dude getRandomDude(float x, float y) {
		int tex = MathUtils.random(7);
		switch (tex) {
			case 0:
				return new Dude(IOAnimation.Char1MIdle, IOAnimation.Char1MMove, x, y);
			case 1:
				return new Dude(IOAnimation.Char2MIdle, IOAnimation.Char2MMove, x, y);
			case 2:
				return new Dude(IOAnimation.Char3MIdle, IOAnimation.Char3MMove, x, y);
			case 3:
				return new Dude(IOAnimation.Char4MIdle, IOAnimation.Char4MMove, x, y);
			case 4:
				return new Dude(IOAnimation.Char1FIdle, IOAnimation.Char1FMove, x, y);
			case 5:
				return new Dude(IOAnimation.Char2FIdle, IOAnimation.Char2FMove, x, y);
			case 6:
				return new Dude(IOAnimation.Char3FIdle, IOAnimation.Char3FMove, x, y);
			case 7:
				return new Dude(IOAnimation.Char4FIdle, IOAnimation.Char4FMove, x, y);
		}
		throw new RuntimeException("Boom!!!");
	}

	protected boolean tryUseResource(Map<Resource, GenericRapper<Float>> resources, float delta, Resource resource, float valuePerDay, float hpLose) {
		resources.get(resource).value -= valuePerDay * delta / Consts.DURATION_DAY;
		if (resources.get(resource).value <= 0) {
			hp -= hpLose * delta * damageMultiplier;
		}
		return resources.get(resource).value > 0;
	}

	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta){
		hadEnoughResources = tryUseResource(resources, delta, Resource.O2, 90, Consts.HEALTH_LOSE_O2) &
				tryUseResource(resources, delta, Resource.H2O, 60, Consts.HEALTH_LOSE_H2O) &
				tryUseResource(resources, delta, Resource.Food, 36, Consts.HEALTH_LOSE_Food);

		if (hadEnoughResources) {
			hp = Math.min(1, hp + Consts.HEALTH_REGENERATE * delta);
		}
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}

	public float getDestX() {
		return destX;
	}

	public float getDestY() {
		return destY;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
		this.currentTask.accept(this);
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public void removeTask(){
		currentTask = null;
	}

	public boolean hasPath(){
		return currentPath != null;
	}

	public void setDest(float destX, float destY) {
		this.destX = destX;
		this.destY = destY;

		Queue<Module> q = new Queue<Module>();
		Map<Module, Module> par = new HashMap<Module, Module>();
		Module start = SpaceStation.get().getModule(x, y);
		q.addLast(start);
		par.put(start, start);
		while (q.size > 0) {
			Module current = q.removeFirst();
			for(int i = 0; i < 4; i++) {
				Module neighbor = current.getNeighbor(i);
				if (neighbor != null && neighbor.canWalkThrough() && !par.containsKey(neighbor)) {
					q.addLast(neighbor);
					par.put(neighbor, current);
				}
			}
		}
		Module destModule = SpaceStation.get().getModule(destX, destY);
		if (destModule == null) {
			currentPath = null;
			return;
		}
		if (par.get(destModule) == null) {
			currentPath = null;
			return;
		}
		// path is backwards
		List<Module> path = new ArrayList<Module>();
		path.add(destModule);
		while (destModule != start && destModule != null) {
			destModule = par.get(destModule);
			path.add(destModule);
		}
		if (destModule == null) {
			currentPath = null;
			throw new RuntimeException("dndrtnd");
			// return;
		}
		destModule = SpaceStation.get().getModule(destX, destY);
		currentPath = new Queue<Pair<Float, Float>>();
		for(int i = path.size()-1; i > 0; i--) {
			currentPath.addLast(path.get(i).getModuleLocation().getCenter());
			for(int j = 0; j < 4; j++){
				if (path.get(i - 1) == path.get(i).getNeighbor(j)) {
					Pair<Integer, Integer> portLocation = path.get(i).getModuleLocation().getPortLocation(j);
					currentPath.addLast(new Pair<Float, Float>(portLocation.first + .5f, portLocation.second + .5f));
					break;
				}
			}
		}
		currentPath.addLast(destModule.getModuleLocation().getCenter());
		currentPath.addLast(new Pair<Float, Float>(destX, destY));

		if (currentPath.size >= 2) {
			Pair<Float, Float> first = currentPath.removeFirst();
			Pair<Float, Float> second = currentPath.removeFirst();
			currentPath.addFirst(second);
			if (new Vector2(first.first - x, first.second - y).crs(second.first - first.first, second.second - first.second) > 0.01f) {
				currentPath.addFirst(first);
			}
		}
	}

	public boolean isHadEnoughResources() {
		return hadEnoughResources;
	}

	protected void handleTasks(float delta) {
		if (currentTask != null) {
			currentTask.update(delta);
			if (currentTask != null && currentTask.isFinished()) {
				currentTask = null;
			}
		}
		if (currentTask != null && currentTask.isIdleTask() && SpaceStation.get().peekTask() != null) {
			currentTask = null;
		}
		if (currentTask == null) {
			currentTask = SpaceStation.get().popTask();
			if (currentTask == null) {
				currentTask = getIdleTask();
			}
			if (currentTask != null) {
				currentTask.accept(this);
			}
		}
	}

	protected Task getIdleTask(){
		int idleTask = MathUtils.random(1);
		if(idleTask == 0) {
			Task task = new WaitTask(MathUtils.random(Consts.MAX_DUDE_WAIT_TIME));
			task.setIdleTask(true);
			return task;
		} else if (idleTask == 1) {
			Module module = null, currentModule = SpaceStation.get().getModule(x, y);
			List<Module> possibleModules = new ArrayList<Module>();
			for (Module amodule : SpaceStation.get().getModules()) {
				if (amodule == currentModule || !amodule.canRandomWalk()) {
					continue;
				}
				possibleModules.add(amodule);
			}
			if(possibleModules.size() > 0) {
				int val = MathUtils.random(possibleModules.size() - 1);
				for (Module amodule : possibleModules) {
					if (val == 0) {
						module = amodule;
						break;
					}
					val--;
				}
				if (module == null) {
					throw new RuntimeException("Booooom!!!");
				}
				if (!module.canWalkThrough()) {
					Task task = new WaitTask(MathUtils.random(Consts.MAX_DUDE_WAIT_TIME));
					task.setIdleTask(true);
					return task;
				}
				Task task = new WalkTask(module.getModuleLocation().getCenter().first, module.getModuleLocation().getCenter().second);
				task.setIdleTask(true);
				return task;
			} else {
				Task task = new WaitTask(MathUtils.random(Consts.MAX_DUDE_WAIT_TIME));
				task.setIdleTask(true);
				return task;
			}
		} else {
			throw new RuntimeException("Blub");
		}
	}

	public void update(float delta) {
		if (hp < 0) {
			if (currentTask != null && !currentTask.isIdleTask()) {
				SpaceStation.get().addTask(currentTask);
			}
			SpaceStation.get().removeDude(this);
		}
		handleTasks(delta);
		if (currentPath != null && currentPath.size > 0) {
			float desX = currentPath.first().first, desY = currentPath.first().second;
			Vector2 dir = new Vector2(desX - x, desY - y);
			if (dir.len() < Consts.ARRIVAL_DISTANCE) {
				currentPath.removeFirst();
			} else {
				dir.nor().scl(Consts.WALK_SPEED * delta);
				x += dir.x;
				y += dir.y;
				flipped = dir.x < 0;
			}
		}
		if ((destX - x) * (destX - x) + (destY - y) * (destY - y) < Consts.ARRIVAL_DISTANCE) {
			currentPath = null;
		}
	}

	public void draw(SpriteBatch batch, float time) {
		if (!visible) {
			return;
		}
		batch.draw((hasPath() ? textureWalk.keyFrame(time + animationOffset) : textureIdle.keyFrame(time + animationOffset)), x - (flipped ? -1 : 1) * .5f, y - .5f, (flipped ? -1 : 1), 1);
	}

	public void drawUI(SpriteBatch batch, float time){
		if (!visible) {
			return;
		}
		Color oldColor = batch.getColor().cpy();
		Color color = Color.RED.cpy().lerp(Color.GREEN, hp * 2 - 1);
		batch.setColor(color);
		batch.draw(IOTexture.pixel, x - hp / 2, y + .5f, hp, .1f);
		batch.setColor(oldColor);
	}
}
