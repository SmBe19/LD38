package com.smeanox.games.ld38.world.module;

import com.smeanox.games.ld38.Consts;

public class ModuleFactory {

	public static ModuleLocation createModuleLocation(ModuleType moduleType, int x, int y, int rotation) {
		return new ModuleLocation(x, y, moduleType.width, moduleType.height, rotation);
	}

	public static Module createModule(ModuleType moduleType, int x, int y, int rotation) {
		ModuleLocation moduleLocation = createModuleLocation(moduleType, x, y, rotation);
		switch (moduleType) {
			case EmptyModule:
				return new EmptyModule(moduleLocation);
			case CrossModule:
				return new CrossModule(moduleLocation);
			case HydrolysisModule:
				return new HydrolysisModule(moduleLocation);
			case GardenModule:
				return new GardenModule(moduleLocation);
			case SolarModule:
				return new SolarModule(moduleLocation);
			case SleepingModule:
				return new SleepingModule(moduleLocation);
			case StorageModule:
				return new StorageModule(moduleLocation);
			case RadioModule:
				return new RadioModule(moduleLocation);
			case RocketModule:
				return new RocketModule(moduleLocation);
			case MagnetModule:
				return new MagnetModule(moduleLocation);
			case CryogenicModule:
				return new CryogenicModule(moduleLocation);
			case MainModule:
				return new MainModule(moduleLocation);
			case RocketPlaceholder:
				return new RocketPlaceholderModule(moduleLocation);
		}
		throw new RuntimeException("Unknown module type");
	}

	public static Module createModule(ModuleType moduleType, int x, int y) {
		return createModule(moduleType, x, y, Consts.RIGHT);
	}
}
