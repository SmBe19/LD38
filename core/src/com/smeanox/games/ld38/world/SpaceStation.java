package com.smeanox.games.ld38.world;

import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.modules.MainModule;
import com.smeanox.games.ld38.world.modules.Module;
import com.smeanox.games.ld38.world.modules.ModuleFactory;

import java.util.HashSet;
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
	private Module mainModule;

	private SpaceStation() {
		singleton = this;
		modules = new HashSet<Module>();
		mainModule = ModuleFactory.createModule(MainModule.class, 0, 0);
		modules.add(mainModule);
		init();
	}

	private void init(){
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
	}

	public Set<Module> getModules() {
		return modules;
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
}
