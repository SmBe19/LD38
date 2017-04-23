package com.smeanox.games.ld38.world;

import com.badlogic.gdx.utils.Queue;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.module.MainModule;
import com.smeanox.games.ld38.world.module.Module;
import com.smeanox.games.ld38.world.module.ModuleFactory;
import com.smeanox.games.ld38.world.task.Task;
import com.smeanox.games.ld38.world.task.WalkTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpaceStation {

	private static SpaceStation singleton;

	public static SpaceStation get(){
		if (singleton == null) {
			singleton = new SpaceStation();
		}
		return singleton;
	}

	private Set<Module> modules;
	private Set<Dude> dudes;
	private Module mainModule;
	private Map<Resource, GenericRapper<Float>> resources;
	private Map<Resource, GenericRapper<Float>> resourceMax;
	private Queue<Task> taskQueue;
	private float time;

	private SpaceStation() {
		singleton = this;
		modules = new HashSet<Module>();
		dudes = new HashSet<Dude>();
		resources = new HashMap<Resource, GenericRapper<Float>>();
		resourceMax = new HashMap<Resource, GenericRapper<Float>>();
		taskQueue = new Queue<Task>();
		init();
	}

	private void init(){
		time = 0;

		modules.clear();
		dudes.clear();
		resources.clear();
		resourceMax.clear();

		for (Resource resource : Resource.values()) {
			resources.put(resource, new GenericRapper<Float>(0.f));
			resourceMax.put(resource, new GenericRapper<Float>(0.f));
		}
		resources.get(Resource.H2).value = 0.f;
		resources.get(Resource.O2).value = 360.f;
		resources.get(Resource.H2O).value = 300.f;
		resources.get(Resource.Fe).value = 600.f;
		resources.get(Resource.Si).value = 100.f;
		resources.get(Resource.Electricity).value = 300.f;
		resources.get(Resource.Food).value = 160.f;

		mainModule = ModuleFactory.createModule(MainModule.class, 0, 0);
		mainModule.setBuildProgress(1);
		mainModule.setFinished(true);
		mainModule.setJustFinished(true);
		addModule(mainModule);

		addDude(Dude.getRandomDude(1.5f, 1.5f));
		addDude(Dude.getRandomDude(1.5f, 0.5f));
	}

	public void addModule(Module module) {
		modules.add(module);
	}

	public void removeModule(Module module) {
		modules.remove(module);
		module.adjustResourceMax(resourceMax, false);
	}

	public void addDude(Dude dude) {
		dudes.add(dude);
		resources.get(Resource.Humans).value += 1.f;
	}

	public void removeDude(Dude dude) {
		dudes.remove(dude);
	}

	public void addTask(Task task) {
		taskQueue.addLast(task);
	}

	public Task popTask(){
		if (taskQueue.size == 0) {
			return null;
		}
		return taskQueue.removeFirst();
	}

	public Task peekTask(){
		if (taskQueue.size == 0) {
			return null;
		}
		return taskQueue.first();
	}

	public float getResource(Resource resource) {
		return resources.get(resource).value;
	}

	public float getResourceMax(Resource resource) {
		return resourceMax.get(resource).value;
	}

	public boolean buyModule(Class<? extends Module> clazz, boolean tryOnly) {
		if (!tryOnly) {
			if (!buyModule(clazz, true)) {
				return false;
			}
		}
		Map<Resource, Float> moduleBuildCost = ModuleFactory.getModuleBuildCost(clazz);
		for (Resource resource : moduleBuildCost.keySet()) {
			if (moduleBuildCost.get(resource) > resources.get(resource).value) {
				return false;
			}
			if (!tryOnly) {
				resources.get(resource).value -= moduleBuildCost.get(resource);
			}
		}
		return true;
	}

	public Set<Module> getModules() {
		return modules;
	}

	public Set<Dude> getDudes() {
		return dudes;
	}

	public Module getMainModule() {
		return mainModule;
	}

	public Module getModule(int x, int y){
		for(Module module : modules){
			if (module.getModuleLocation().isPointInModule(x, y)) {
				return module;
			}
		}
		return null;
	}

	public float getTime() {
		return time;
	}

	public float getTimeOfDay(){
		return time % Consts.DURATION_DAY;
	}

	public void update(float delta){
		time += delta;

		for (Module module : modules) {
			if (module.isJustFinished()) {
				module.adjustResourceMax(resourceMax, true);
				module.setJustFinished(false);
			}
			if(module.isActive()) {
				module.doInputOutputProcessing(resources, delta);
			}
		}

		// clamp resources
		for (Resource resource : Resource.values()) {
			if (resource == Resource.Humans) {
				continue;
			}
			if (resources.get(resource).value > resourceMax.get(resource).value) {
				resources.get(resource).value = resourceMax.get(resource).value;
			}
			if (resources.get(resource).value < 0) {
				resources.get(resource).value = 0.f;
			}
		}

		for (Dude dude : dudes) {
			dude.doInputOutputProcessing(resources, delta);
			dude.update(delta);
		}
	}
}
