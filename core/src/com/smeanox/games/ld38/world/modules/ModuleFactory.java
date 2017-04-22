package com.smeanox.games.ld38.world.modules;

import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Ressource;

import java.util.HashMap;
import java.util.Map;

public class ModuleFactory {

	public static ModuleLocation createModuleLocation(Class<? extends Module> clazz) {
		return createModuleLocation(clazz, 0, 0, 0);
	}

	public static ModuleLocation createModuleLocation(Class<? extends Module> clazz, int x, int y, int rotation) {
		if (clazz == MainModule.class) {
			return new ModuleLocation(x, y, 3, 1, rotation);
		} else if (clazz == SolarModule.class) {
			return new ModuleLocation(x, y, 4, 2, rotation);
		}
		throw new RuntimeException("Unknown module type");
	}

	static Map<Class<? extends Module>, Map<Ressource, Float>> buildCostCache = new HashMap<Class<? extends Module>, Map<Ressource, Float>>();

	public static Map<Ressource, Float> getModuleBuildCost(Class<? extends Module> clazz) {
		if(!buildCostCache.containsKey(clazz)) {
			Map<Ressource, Float> res = new HashMap<Ressource, Float>();
			if (clazz == MainModule.class) {
				res.put(Ressource.H2, 5.f);
			} else if (clazz == SolarModule.class) {
				res.put(Ressource.O2, 5.f);
			} else {
				throw new RuntimeException("Unknown module type");
			}
			buildCostCache.put(clazz, res);
		}
		return buildCostCache.get(clazz);
	}

	public static Module createModule(Class<? extends Module> clazz, int x, int y, int rotation) {
		try {
			return clazz.getDeclaredConstructor(ModuleLocation.class).newInstance(createModuleLocation(clazz, x, y, rotation));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unknown module type", e);
		}
	}

	public static Module createModule(Class<? extends Module> clazz, int x, int y) {
		return createModule(clazz, x, y, Consts.RIGHT);
	}
}
