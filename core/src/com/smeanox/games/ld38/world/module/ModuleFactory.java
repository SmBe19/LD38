package com.smeanox.games.ld38.world.module;

import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleFactory {

	public static final List<Class<? extends Module>> moduleClasses = new ArrayList<Class<? extends Module>>();
	static{
		moduleClasses.add(MainModule.class);
		moduleClasses.add(CrossModule.class);
		moduleClasses.add(SolarModule.class);
	}

	public static ModuleLocation createModuleLocation(Class<? extends Module> clazz) {
		return createModuleLocation(clazz, 0, 0, 0);
	}

	public static ModuleLocation createModuleLocation(Class<? extends Module> clazz, int x, int y, int rotation) {
		return new ModuleLocation(x, y,
				clazz.getAnnotation(ModuleInformation.class).width(),
				clazz.getAnnotation(ModuleInformation.class).height(), rotation);
	}

	static Map<Class<? extends Module>, Map<Resource, Float>> buildCostCache = new HashMap<Class<? extends Module>, Map<Resource, Float>>();

	public static void putBuildCost(Class<? extends Module> clazz, Map<Resource, Float> cost){
		buildCostCache.put(clazz, cost);
	}

	public static String getModuleName(Class<? extends Module> clazz){
		return clazz.getAnnotation(ModuleInformation.class).name();
	}

	private static boolean tryLoadClass(Class<? extends Module> clazz) {
		try {
			Class.forName(clazz.getCanonicalName());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Map<Resource, Float> getModuleBuildCost(Class<? extends Module> clazz) {
		if(!buildCostCache.containsKey(clazz)) {
			if(!tryLoadClass(clazz) || !buildCostCache.containsKey(clazz)) {
				throw new RuntimeException("Unknown module type: " + clazz);
			}
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
