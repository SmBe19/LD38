package com.smeanox.games.ld38.world.modules;

import com.smeanox.games.ld38.Consts;

public class ModuleFactory {

	public static ModuleLocation createModuleLocation(Class<? extends Module> clazz){
		return createModuleLocation(clazz, 0, 0, 0);
	}

	public static ModuleLocation createModuleLocation(Class<? extends Module> clazz, int x, int y, int rotation) {
		if (clazz == MainModule.class) {
			return new ModuleLocation(x, y, 3, 1, rotation);
		}
		throw new RuntimeException("Unknown module type");
	}

	public static Module createModule(Class<? extends Module> clazz, int x, int y, int rotation){
		try {
			return clazz.getDeclaredConstructor(ModuleLocation.class).newInstance(createModuleLocation(clazz, x, y, rotation));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unknown module type", e);
		}
	}

	public static Module createModule(Class<? extends Module> clazz, int x, int y){
		return createModule(clazz, x, y, Consts.RIGHT);
	}
}
