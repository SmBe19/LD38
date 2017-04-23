package com.smeanox.games.ld38.world;

public enum Resource {
	H2 ("Hydrogen", "H", 10),
	O2 ("Oxygen", "O", 1),
	H2O ("Water", "W", 1),
	Fe ("Iron", "F", 1),
	Si ("Silicon", "S", 3),
	Electricity ("Electricity", "E", 0.5f),
	Food ("Carrots", "M", 2),
	Humans ("Dudes", "D", 200),
	;

	public final String displayName, icon;
	public final float deliveryCost;

	Resource(String displayName, String icon, float deliveryCost) {
		this.displayName = displayName;
		this.icon = icon;
		this.deliveryCost = deliveryCost;
	}
}
