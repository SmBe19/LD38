package com.smeanox.games.ld38.world.module;

import com.smeanox.games.ld38.world.Resource;

import java.util.HashMap;
import java.util.Map;

public enum ModuleType {
	EmptyModule("Connector", "E-", 3, 1, true, add(Resource.Fe, 50)),
	CrossModule("X Connector", "E-", 1, 1, true, add(Resource.Fe, 25)),
	HydrolysisModule("Hydrolysis", "W-\nE-\nH+\nO+", 2, 1, true, add(Resource.Fe, 200, add(Resource.Si, 100))),
	GardenModule("Garden", "W-\nE-\nO+\nM+", 3, 1, true, add(Resource.H2O, 100, add(Resource.Fe, 150))),
	SolarModule("Solar Panel", "E+", 4, 2, true, add(Resource.Fe, 50, add(Resource.Si, 25))),
	SleepingModule("Sleep Module", "E-", 2, 3, true, add(Resource.Fe, 200)),
	StorageModule("Storage", "E-", 2, 3, true, add(Resource.Fe, 150)),
	RadioModule("Radio", "", 2, 3, true, add(Resource.Fe, 300, add(Resource.Si, 150))),
	RocketModule("Rockets", "E-", 3, 3, true, add(Resource.H2O, 100, add(Resource.Fe, 600, add(Resource.Si, 200)))),
	MagnetModule("Magnet", "E-", 3, 3, true, add(Resource.Fe, 600, add(Resource.Si, 200))),
	CryogenicModule("Cryogenic", "", 3, 3, true, add(Resource.H2, 120, add(Resource.H2O, 600, add(Resource.Food, 300, add(Resource.Fe, 1200, add(Resource.Si, 800)))))),
	MainModule("Command Module", "E-", 3, 3, false, nothing()),
	RocketPlaceholder("Rocket Placeholder", "", 3, 5, false, nothing()),
	;

	public final String displayName;
	public final String tooltip;
	public final int width ,height;
	public final boolean canBuild;
	public final Map<Resource, Float> buildCost;

	ModuleType(String displayName, String tooltip, int width, int height, boolean canBuild, Map<Resource, Float> buildCost) {
		this.displayName = displayName;
		this.tooltip = tooltip;
		this.width = width;
		this.height = height;
		this.canBuild = canBuild;
		this.buildCost = buildCost;
	}

	private static Map<Resource, Float> nothing(){
		return new HashMap<Resource, Float>();
	}

	private static Map<Resource, Float> add(Resource resource, float amount) {
		return add(resource, amount, nothing());
	}

	private static Map<Resource, Float> add(Resource resource, float amount, Map<Resource, Float> m) {
		m.put(resource, amount);
		return m;
	}
}
