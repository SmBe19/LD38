package com.smeanox.games.ld38.world;

import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.world.module.MainModule;
import com.smeanox.games.ld38.world.module.Module;
import com.smeanox.games.ld38.world.module.ModuleFactory;

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

	private SpaceStation() {
		singleton = this;
		modules = new HashSet<Module>();
		dudes = new HashSet<Dude>();
		resources = new HashMap<Resource, GenericRapper<Float>>();
		resourceMax = new HashMap<Resource, GenericRapper<Float>>();
		init();
	}

	private void init(){
		for (Resource resource : Resource.values()) {
			resources.put(resource, new GenericRapper<Float>(0.f));
			resourceMax.put(resource, new GenericRapper<Float>(0.f));
		}

		mainModule = ModuleFactory.createModule(MainModule.class, 0, 0);
		mainModule.setBuildProgress(1);
		mainModule.setFinished(true);
		addModule(mainModule);

		if(mainModule
				.addNeighbor(Consts.RIGHT, Consts.RIGHT, MainModule.class)
				.addNeighbor(Consts.RIGHT, Consts.RIGHT, MainModule.class)
				.addNeighbor(Consts.UP, Consts.UP, MainModule.class)
				.addNeighbor(Consts.UP, Consts.UP, MainModule.class)
				.addNeighbor(Consts.LEFT, Consts.LEFT, MainModule.class)
				.addNeighbor(Consts.LEFT, Consts.LEFT, MainModule.class)
				.addNeighbor(Consts.DOWN, Consts.DOWN, MainModule.class)
				.addNeighbor(Consts.DOWN, Consts.DOWN, MainModule.class)
				!= null){
			throw new RuntimeException("boom");
		}
		if(mainModule.addSolar(Consts.DOWN, mainModule.getModuleLocation().getRotX() + 1, mainModule.getModuleLocation().getY() - 1) == null){
			throw new RuntimeException("boom2");
		}

		dudes.add(new Dude(IOAnimation.Dude1));
	}

	public void addModule(Module module) {
		modules.add(module);
		module.adjustResourceMax(resourceMax, true);
	}

	public void removeModule(Module module) {
		modules.remove(module);
		module.adjustResourceMax(resourceMax, false);
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

	public void update(float delta){
		for (Module module : modules) {
			module.doInputOutputProcessing(resources, delta);
			module.doBuildProrgess(delta);
		}

		// clamp resources
		for (Resource resource : Resource.values()) {
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
