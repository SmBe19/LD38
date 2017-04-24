package com.smeanox.games.ld38;

public class Consts {
	public static final int DESIGN_WIDTH = 800;
	public static final int DESIGN_HEIGHT = 480;
	public static final String GAME_NAME = "Faster Than Mike";
	public static final int UP = 1, RIGHT = 0, DOWN = 3, LEFT = 2;
	public static final int SPRITE_SIZE = 32;
	public static final float CAMERA_SPEED = 7.5f;
	public static final int BUILD_SEARCH_DIST = 2;
	public static final float LINE_SPACING = 1.5f;
	public static final float BUILD_PREVIEW_ALPHA = 0.5f;
	public static final float BUILDING_ALPHA = 0.5f;
	public static final float INACTIVE_BRIGHTNESS = 0.5f;
	public static final float MAX_DUDE_WAIT_TIME = 10.f;
	public static final float ROCKET_ANIMATION_FADESPEED = 4f;
	public static final float ROCKET_ANIMATION_OFFSET = 5f;
	public static final float ROCKET_ANIMATION_DURATION = 5.f;
	public static final float ROCKET_DOCKED_DURATION = 2.f;
	public static final float ROCKET_DOCKED_POSITION_X = 1.5f;
	public static final float ROCKET_DOCKED_POSITION_Y = 4.25f;
	public static final float SMALLROCKET_ANIMATION_FADESPEED = 4f;
	public static final float SMALLROCKET_ANIMATION_OFFSET = 5f;
	public static final float SMALLROCKET_ANIMATION_DURATION = 5.f;
	public static final float NEW_DUDE_POSITION_X = 1.5f;
	public static final float NEW_DUDE_POSITION_Y = 1.5f;
	public static final float BUILD_POSITION_RANDOM_OFFSET = 0.2f;
	public static final float STAR_SPEED = 4;

	public static final float BUILD_SPEED = 0.1f;
	public static final float WALK_SPEED = 2.5f;
	public static final float DURATION_DAY = 25; // 180
	public static final float DELIVERY_TIME = DURATION_DAY / 3.f;
	public static final float ENDOFORDER_TIME = DELIVERY_TIME - ROCKET_ANIMATION_DURATION - ROCKET_DOCKED_DURATION;
	public static final float DURATION_NIGHT = DURATION_DAY / 3.f;
	public static final float DELIVERY_LIMIT = 1200;
	public static final float HEALTH_LOSE_O2 = 6.f/DURATION_DAY;
	public static final float HEALTH_LOSE_H2O = 3.f/DURATION_DAY;
	public static final float HEALTH_LOSE_Food = 1.f/DURATION_DAY;
	public static final float HEALTH_REGENERATE = 1.f/DURATION_DAY;
	public static final float EVENT_SOLARFLARE_DURATION = DURATION_DAY / 2.f;
	public static final float EVENT_MAX_RESOURCE = 400;
	public static final float ROCKET_MIN_TIME = DURATION_DAY * 0.5f;
	public static final float ROCKET_MAX_TIME = DURATION_DAY * 1.f;
	public static final float ROCKET_MIN_RETURN = 0.5f;
	public static final float ROCKET_MAX_RETURN = 2f;

	public static final float ARRIVAL_DISTANCE = 0.05f;
}
