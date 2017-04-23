package com.smeanox.games.ld38.world;

public enum Resource {
	H2 ("H2", "H"),
	O2 ("O2", "O"),
	H2O ("H2O", "W"),
	Fe ("Fe", "F"),
	Si ("Si", "S"),
	Electricity ("Electricity", "E"),
	Food ("Food", "M"),
	Humans ("Humans", "D"),
	;

	public final String displayName, icon;

	Resource(String displayName, String icon) {
		this.displayName = displayName;
		this.icon = icon;
	}
}
