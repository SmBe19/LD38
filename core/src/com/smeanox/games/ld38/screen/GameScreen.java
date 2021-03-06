package com.smeanox.games.ld38.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.LD38;
import com.smeanox.games.ld38.world.MessageManager;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.io.IOFont;
import com.smeanox.games.ld38.io.IOTexture;
import com.smeanox.games.ld38.world.Dude;
import com.smeanox.games.ld38.world.Pair;
import com.smeanox.games.ld38.world.Resource;
import com.smeanox.games.ld38.world.SpaceStation;
import com.smeanox.games.ld38.world.module.Module;
import com.smeanox.games.ld38.world.module.ModuleLocation;
import com.smeanox.games.ld38.world.module.ModuleType;
import com.smeanox.games.ld38.world.task.WalkTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class GameScreen implements Screen {

	private SpriteBatch batch;
	private Camera camera, uiCamera;
	private float time, scale, timeScale;
	private float wWidth, wHeight;
	private Window buildWindow, buildInfoWindow, recourceInfoWindow, timeWindow, moduleWindow, messageWindow, gameOverWindow;
	private boolean paused;
	private boolean muted;
	private Music backgroundMusic, ambientMusic;
	private float musicPause;

	private ShaderProgram earthShader, bitAlphaShader;

	private List<ModuleType> buildModuleTypes;
	private ModuleType buildType, hoverBuildType;
	private Module buildModule, buildNeighbor;
	private int buildX, buildY, buildDirection, buildRotation;
	private Dude currentDude;
	private boolean[] wasDown = new boolean[2];
	private boolean isDrag;
	private float dragDownX, dragDownY;
	private Vector3 dragCamPos;

	private static List<Pair<Color, Pair<Float, Float>>> debugPoints = new ArrayList<Pair<Color, Pair<Float, Float>>>();

	public GameScreen() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();
		paused = false;
		muted = false;
		time = 0;
		timeScale = 1;
		dragCamPos = new Vector3();

		ShaderProgram.pedantic = false;
		earthShader = new ShaderProgram(Gdx.files.internal("shader/earth.vert"), Gdx.files.internal("shader/earth.frag"));
		bitAlphaShader = new ShaderProgram(Gdx.files.internal("shader/bitAlpha.vert"), Gdx.files.internal("shader/bitAlpha.frag"));
		System.out.println(earthShader.getLog());
		System.out.println(bitAlphaShader.getLog());
		if (!earthShader.isCompiled() || !bitAlphaShader.isCompiled()) {
			throw new RuntimeException("No shader for you!");
		}
		IOTexture.map.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
		IOTexture.clouds.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		if(Gdx.app.getType() == Application.ApplicationType.WebGL){
			IOTexture.map.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			IOTexture.clouds.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		} else {
			IOTexture.map.texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
			IOTexture.clouds.texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
		}

		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/main_theme.ogg"));
		ambientMusic = Gdx.audio.newMusic(Gdx.files.internal("music/ambient.ogg"));
		backgroundMusic.setLooping(false);
		ambientMusic.setLooping(true);
		backgroundMusic.setVolume(0.4f);
		ambientMusic.setVolume(0.4f);
		musicPause = MathUtils.random(Consts.MUSIC_MAX_PAUSE);

		buildModuleTypes = new ArrayList<ModuleType>();
		for (ModuleType moduleType : ModuleType.values()) {
			if (moduleType.canBuild) {
				buildModuleTypes.add(moduleType);
			}
		}

		initBuild();
	}

	private void initBuild() {
		buildType = null;
		buildModule = null;
		buildNeighbor = null;
		buildX = Integer.MAX_VALUE;
		buildY = Integer.MAX_VALUE;
		buildRotation = 0;
		currentDude = null;
	}

	private void initWindows2000(){
		if (buildWindow != null) {
			Window.windows95.remove(buildWindow);
		}
		if (buildInfoWindow != null) {
			Window.windows95.remove(buildInfoWindow);
		}
		if (recourceInfoWindow != null) {
			Window.windows95.remove(recourceInfoWindow);
		}
		if (timeWindow != null) {
			Window.windows95.remove(timeWindow);
		}
		buildWindow = new BuildWindow();
		Window.windows95.add(buildWindow);
		buildInfoWindow = new BuildInfoWindow();
		Window.windows95.add(buildInfoWindow);
		recourceInfoWindow = new ResourceInfoWindow();
		Window.windows95.add(recourceInfoWindow);
		timeWindow = new TimeWindow();
		Window.windows95.add(timeWindow);
	}

	@Override
	public void show() {
		backgroundMusic.play();
		ambientMusic.play();
	}

	private void update(float delta){
		if(!paused) {
			SpaceStation.get().update(delta * timeScale);
			if(!Consts.IGNORE_MESSAGES) {
				if (SpaceStation.get().peekMessage() != null) {
					paused = true;
					messageWindow = new MessageWindow(SpaceStation.get().popMessage());
					Window.windows95.add(messageWindow);
				}
			}
			if (gameOverWindow == null && SpaceStation.get().getGameOverMessage() != null) {
				paused = true;
				gameOverWindow = new GameOverWindow(SpaceStation.get().getGameOverMessage());
				Window.windows95.add(gameOverWindow);
			}
		}

		if (!backgroundMusic.isPlaying()) {
			musicPause -= delta;
			if (musicPause < 0) {
				musicPause = MathUtils.random(Consts.MUSIC_MAX_PAUSE);
				backgroundMusic.play();
			}
		}
	}

	private void findBuildParams(float mousex, float mousey, boolean allowRotation){
		buildModule = null;
		buildNeighbor = null;
		buildDirection = 0;
		float minVal = Float.MAX_VALUE;
		for (int x = buildX - Consts.BUILD_SEARCH_DIST; x <= buildX + Consts.BUILD_SEARCH_DIST; x++) {
			for (int y = buildY - Consts.BUILD_SEARCH_DIST; y <= buildY + Consts.BUILD_SEARCH_DIST; y++) {
				Module neighbor = SpaceStation.get().getModule(x, y);
				if (neighbor != null) {
					for (int dir = 0; dir < 4; dir++) {
						for(int rot = 0; rot < 4; rot++) {
							Module module;
							if (buildType == ModuleType.SolarModule) {
								module = neighbor.tryAddSolar(dir, buildX, buildY);
							} else {
								module = neighbor.tryAddNeighbor(dir, rot, buildType);
							}
							if (module != null) {
								float xdist = (module.getModuleLocation().getRotX() + module.getModuleLocation().getRotWidth() / 2.f - mousex);
								float ydist = (module.getModuleLocation().getRotY() + module.getModuleLocation().getRotHeight() / 2.f - mousey);
								float val = xdist * xdist + ydist * ydist;
								if (val < minVal) {
									buildModule = module;
									buildNeighbor = neighbor;
									buildDirection = dir;
									buildRotation = rot;
									minVal = val;
								}
							}
						}
					}
				}
			}
		}
		if (buildModule != null) {
			buildModule.setFinished(true);
		}
	}

	private void updateInput(float delta){
		// build
		boolean buildChanged = false;
		int x, y;
		Vector3 mouse;
		boolean[] isMouseDown = new boolean[2];
		isMouseDown[0] = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
		isMouseDown[1] = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);

		mouse = unprojectMouseUI();
		x = MathUtils.floor(mouse.x);
		y = MathUtils.floor(mouse.y);
		for (Window window : new HashSet<Window>(Window.windows95)) {
			window.update(delta, x, y, isMouseDown);
		}

		if(!paused) {
			mouse = unprojectMouse();
			x = MathUtils.floor(mouse.x);
			y = MathUtils.floor(mouse.y);
			if (buildType != null) {
				if (x != buildX || y != buildY) {
					buildX = x;
					buildY = y;
					buildChanged = true;
				}
				if (buildChanged) {
					findBuildParams(mouse.x, mouse.y, true);
				}
				if (buildModule != null && wasDown[0] && !isMouseDown[0]) {
					if (SpaceStation.get().buyModule(buildType, false)) {
						if (buildType == ModuleType.SolarModule) {
							buildNeighbor.addSolar(buildDirection, buildX, buildY);
						} else {
							buildNeighbor.addNeighbor(buildDirection, buildRotation, buildType);
						}
						findBuildParams(mouse.x, mouse.y, true);
					}
				}
				if (wasDown[1] && !isMouseDown[1]) {
					initBuild();
				}
			} else {
				if (wasDown[0] && !isMouseDown[0]) {
					if (moduleWindow != null) {
						Window.windows95.remove(moduleWindow);
						moduleWindow = null;
					}
					if(!isDrag) {
						Module module = SpaceStation.get().getModule(x, y);
						if (module != null && module.isFinished()) {
							Vector3 mouse2 = unprojectMouseUI();
							moduleWindow = module.createWindow(mouse2.x, mouse2.y);
							if (moduleWindow != null) {
								Window.windows95.add(moduleWindow);
							}
						}
					}
					isDrag = false;
				}
				if (wasDown[1] && !isMouseDown[1]) {
					if (moduleWindow != null) {
						Window.windows95.remove(moduleWindow);
						moduleWindow = null;
					} else {
						if(currentDude != null){
							if (currentDude.getCurrentTask() == null || currentDude.getCurrentTask().isIdleTask()) {
								Module module = SpaceStation.get().getModule(mouse.x, mouse.y);
								if(module != null && module.canRandomWalk()) {
									WalkTask newTask = new WalkTask(mouse.x, mouse.y);
									newTask.setIdleTask(true);
									currentDude.setCurrentTask(newTask);
								}
							}
							currentDude = null;
						} else {
							currentDude = SpaceStation.get().getDude(x, y);
						}
					}
				}
				if (!wasDown[0] && isMouseDown[0]) {
					dragCamPos.set(camera.position);
					dragDownX = mouse.x;
					dragDownY = mouse.y;
				}
				if (isMouseDown[0]) {
					if((mouse.x - dragDownX)*(mouse.x - dragDownX) + (mouse.y - dragDownY)*(mouse.y - dragDownY) > 0.2f){
						isDrag = true;
					}
					camera.position.set(camera.position).add(dragDownX - mouse.x, dragDownY - mouse.y, 0);
				}
			}
		}
		// view
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			camera.translate(0, Consts.CAMERA_SPEED * delta, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			camera.translate(0, -Consts.CAMERA_SPEED * delta, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			camera.translate(Consts.CAMERA_SPEED * delta, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			camera.translate(-Consts.CAMERA_SPEED * delta, 0, 0);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			muted = !muted;
			if (muted && ((MessageWindow) messageWindow).getMessage() != null && ((MessageWindow) messageWindow).getMessage().narration != null) {
				((MessageWindow) messageWindow).getMessage().narration.stop();
			}
			if (muted && ((MessageWindow) messageWindow).getMessage() != null && ((MessageWindow) messageWindow).getMessage().prenarration != null) {
				((MessageWindow) messageWindow).getMessage().prenarration.stop();
			}
		}

		// cheats
		if(Consts.DEBUG) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
				for (Resource resource : Resource.values()) {
					SpaceStation.get().getResources().get(resource).value += 1000;
					SpaceStation.get().getResourceMax().get(resource).value += 1000;
				}
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)){
				for (ModuleType moduleType : ModuleType.values()) {
					SpaceStation.get().getEnabledModuleTypes().add(moduleType);
				}
			}
		}

		wasDown = isMouseDown;
	}

	private Vector3 unprojectMouse() {
		return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
	}

	private Vector3 unprojectMouseUI() {
		return uiCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
	}

	private void drawDeliveryRocket(float delta){
		float progress = Math.abs(SpaceStation.get().getTimeOfDay() - Consts.DELIVERY_TIME);
		if (SpaceStation.get().isLaunchFailure() || SpaceStation.get().isWorldWarStarted() ||
				progress > Consts.ROCKET_ANIMATION_DURATION + Consts.ROCKET_DOCKED_DURATION ||
				SpaceStation.get().isSavedDeliveryEmpty()) {
			return;
		}
		TextureRegion texture = IOAnimation.Rocket.keyFrame(time);
		float interp = 1, fade = 1, scale = 1, offset = 0;
		if (progress > Consts.ROCKET_DOCKED_DURATION) {
			progress -= Consts.ROCKET_DOCKED_DURATION;
			interp = MathUtils.cos(MathUtils.PI * progress / Consts.ROCKET_ANIMATION_DURATION) / 2 + .5f;
			offset = (1 - interp) * Consts.ROCKET_ANIMATION_OFFSET;
			fade = MathUtils.clamp(interp * Consts.ROCKET_ANIMATION_FADESPEED, 0, 1);
		}
		float width, height;
		width = texture.getRegionWidth() * scale / Consts.SPRITE_SIZE;
		height = texture.getRegionHeight() * scale / Consts.SPRITE_SIZE;
		Color oldColor = batch.getColor().cpy();
		batch.setColor(1, 1, 1, fade);
		batch.draw(texture, Consts.ROCKET_DOCKED_POSITION_X - width / 2,
				Consts.ROCKET_DOCKED_POSITION_Y + offset - height / 2, width, height);
		batch.setColor(oldColor);
	}

	private void drawBG(float delta) {
		float dayProgress = SpaceStation.get().getTimeOfDay() / Consts.DURATION_DAY;
		batch.setProjectionMatrix(uiCamera.combined);
		batch.setColor(1, 1, 1, 1);
		batch.begin();
		for(int ax = 0; ax < wWidth; ax += Consts.DESIGN_WIDTH){
			for(int ay = -MathUtils.floor(Consts.STAR_SPEED * (1 - dayProgress) * Consts.DESIGN_HEIGHT); ay < wHeight; ay += Consts.DESIGN_HEIGHT){
				batch.draw(IOTexture.bg.texture, ax, ay, Consts.DESIGN_WIDTH, Consts.DESIGN_HEIGHT);
			}
		}
		batch.end();
		batch.setShader(earthShader);
		IOTexture.clouds.texture.bind(1);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		int warState = SpaceStation.get().isWorldWarStarted() ? (SpaceStation.get().isWorldWarToday() ? 1 : 2) : 0;
		earthShader.begin();
		earthShader.setUniformi("u_textureClouds", 1);
		earthShader.setUniformf("u_rotation", MathUtils.PI2 * (SpaceStation.get().getTime() / Consts.DURATION_DAY));
		earthShader.setUniformf("u_sun_offset", -SpaceStation.get().getTime() * Consts.SUN_SPEED);
		earthShader.setUniformf("u_cloud_limit", warState == 1 ? 0.2f : (warState == 2 ? 0.3f : 0.4f));
		earthShader.setUniformf("u_cloud_brightness", warState == 0 ? 1.f : (warState == 1 ? 0.2f : 0.5f));
		earthShader.setUniformf("u_ground_color", warState > 0 ? 0.1f : 0.22f, warState > 0 ? 0.15f : 0.37f, 0.05f);
		earthShader.setUniformf("u_sea_color", 0.05f, 0.25f, 0.75f);
		earthShader.setUniformf("u_explosion_limit", warState == 1 ? 0.25f : 1.f);
		earthShader.end();
		batch.begin();
		float size = wWidth * 1.2f;
		batch.draw(IOTexture.map.texture, (wWidth - size) / 2, size*0.25f - size, size, size);
		batch.end();
		batch.setShader(null);
	}

	private void draw(float delta){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		drawBG(delta);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.setColor(1, 1, 1, 1);
		batch.begin();
		drawDeliveryRocket(delta);
		for (Module module : SpaceStation.get().getModules()) {
			module.drawRockets(batch, time);
		}

		batch.flush();
		batch.setShader(bitAlphaShader);
		Gdx.gl.glDepthMask(true);
		Gdx.gl.glClearDepthf(1f);
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_ALWAYS);

		for (Module module : SpaceStation.get().getModules()) {
			module.drawBackground(batch, time);
		}

		batch.flush();
		batch.setShader(null);
		Gdx.gl.glDepthMask(false);
		Gdx.gl.glDepthFunc(GL20.GL_EQUAL);

		for (Dude dude : SpaceStation.get().getDudes()) {
			dude.draw(batch, time);
		}

		batch.flush();
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

		for (Module module : SpaceStation.get().getModules()) {
			module.drawForeground(batch, time);
		}
		if (buildModule != null) {
			Color oldColor = batch.getColor().cpy();
			batch.setColor(1, 1, 1, Consts.BUILD_PREVIEW_ALPHA);
			buildModule.drawBackground(batch, time);
			buildModule.drawForeground(batch, time);
			batch.setColor(oldColor);
		}
		for (Dude dude : SpaceStation.get().getDudes()) {
			dude.drawUI(batch, time);
		}
		drawDebugStuff(delta);
		batch.end();

		drawUI(delta);
	}

	private void drawUI(float delta) {
		batch.setProjectionMatrix(uiCamera.combined);
		batch.setColor(1, 1, 1, 1);
		batch.begin();
		for (Window window : Window.windows95) {
			window.draw(batch, delta);
		}
		IOFont.grusigPunktBdf.draw(batch, 16, 16, Consts.GAME_NAME);
		batch.end();
	}

	public static void addDebugPoint(float x, float y, Color color) {
		debugPoints.add(new Pair<Color, Pair<Float, Float>>(color, new Pair<Float, Float>(x, y)));
	}

	public static void addDebugLine(float x1, float y1, float x2, float y2, Color color) {
		float dx = x2 - x1, dy = y2 - y1;
		dx /= 50;
		dy /= 50;
		for(int i = 0; i < 50; i++) {
			addDebugPoint(x1 + i * dx, y1 + i * dy, color);
		}
	}

	public static void addDebugLine(ModuleLocation loc1, ModuleLocation loc2, Color color) {
		addDebugLine(loc1.getCenter().first, loc1.getCenter().second, loc2.getCenter().first, loc2.getCenter().second, color);
	}

	public static void addDebugLine(Module m1, Module m2, Color color) {
		if (m1 == null || m2 == null) {
			return;
		}
	}

	private void drawDebugStuff(float delta) {
		batch.setColor(1, 0, 0, 1);
		for (Pair<Color, Pair<Float, Float>> point : debugPoints) {
			batch.setColor(point.first);
			batch.draw(IOTexture.pixel, point.second.first, point.second.second, 0.1f, 0.1f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			debugPoints.clear();
		}
	}

	@Override
	public void render(float delta) {
		time += delta;
		updateInput(delta);
		update(delta);
		draw(delta);
	}

	@Override
	public void resize(int width, int height) {
		scale = height / (float) Consts.DESIGN_HEIGHT;
		if(scale >= 0.5f){
			scale = Math.round(scale);
		}
		wWidth = width / scale;
		wHeight = height / scale;
		camera.viewportWidth = width / scale / Consts.SPRITE_SIZE;
		camera.viewportHeight = height / scale / Consts.SPRITE_SIZE;
		camera.update();
		uiCamera.viewportWidth = width / scale;
		uiCamera.viewportHeight = height / scale;
		uiCamera.position.set(uiCamera.viewportWidth / 2, uiCamera.viewportHeight / 2, 0);
		uiCamera.update();

		initWindows2000();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		backgroundMusic.pause();
		ambientMusic.pause();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	private class BuildWindow extends Window {

		public BuildWindow() {
			super(wWidth - 150, 0, 100, 100);
		}

		@Override
		public void init() {
			height = 20 + buildModuleTypes.size() * 20;
			y = wHeight - 100 - height;

			float ay = height - 15;
			for (final ModuleType moduleType : buildModuleTypes) {
				uiElements.add(new ButtonWithHover(5, ay, 90, 10, moduleType.displayName, new LabelActionHandler() {
					@Override
					public void actionHappened(Label label, float delta) {
						if(buildType == moduleType){
							label.color = Color.ORANGE;
						} else if(!SpaceStation.get().getEnabledModuleTypes().contains(moduleType)) {
							label.color = Color.DARK_GRAY;
						} else if (SpaceStation.get().getStoryManager().highlighted == moduleType) {
							label.color = Color.YELLOW;
						} else if (SpaceStation.get().buyModule(moduleType, true)) {
							label.color = Color.BLACK;
						} else {
							label.color = Color.FIREBRICK;
						}
					}
				}, new LabelActionHandler() {
					@Override
					public void actionHappened(Label label, float delta) {
						initBuild();
						if(SpaceStation.get().getEnabledModuleTypes().contains(moduleType)) {
							buildType = moduleType;
						}
						if (moduleWindow != null) {
							Window.windows95.remove(moduleWindow);
							moduleWindow = null;
						}
					}
				}, new LabelActionHandler() {
					@Override
					public void actionHappened(Label label, float delta) {
						hoverBuildType = moduleType;
					}
				}));
				ay -= 20;
			}

			uiElements.add(new Button(5, ay, 90, 10, "Cancel", new LabelActionHandler() {
				@Override
				public void actionHappened(Label label, float delta) {
					label.visible = buildType != null;
				}
			}, new LabelActionHandler() {
				@Override
				public void actionHappened(Label label, float delta) {
					initBuild();
				}
			}));
		}

		@Override
		public void update(float delta, int mouseX, int mouseY, boolean[] mouseButtons) {
			hoverBuildType = null;
			super.update(delta, mouseX, mouseY, mouseButtons);
		}
	}

	private class BuildInfoWindow extends Window {

		private ModuleType lastBuildModule;

		public BuildInfoWindow() {
			super(wWidth - 250, 0, 70, 0);
			visible = false;
			lastBuildModule = null;
		}

		@Override
		public void init() {}

		@Override
		public void update(float delta, int mouseX, int mouseY, boolean[] mouseButtons) {
			super.update(delta, mouseX, mouseY, mouseButtons);

			if (lastBuildModule != hoverBuildType) {
				lastBuildModule = hoverBuildType;
				uiElements.clear();

				if (lastBuildModule == null) {
					visible = false;
				} else {
					visible = true;
					final Map<Resource, Float> moduleBuildCost = lastBuildModule.buildCost;
					int count = moduleBuildCost.size();
					height = 20 + count * 20;
					if (lastBuildModule.tooltip.length() > 0) {
						height += 35 + IOFont.icons.height(lastBuildModule.tooltip);
					}
					y = wHeight - 100 - height;

					uiElements.add(new Label(5, height - 15, 60, 10, "Cost", null));

					float ay = height - 35;
					for (final Resource resource : Resource.values()) {
						if (moduleBuildCost.containsKey(resource)) {
							uiElements.add(new Label(25, ay, 40, 10, leftPad("" + moduleBuildCost.get(resource).intValue(), 5), new LabelActionHandler() {
								@Override
								public void actionHappened(Label label, float delta) {
									label.color = SpaceStation.get().getResource(resource) < moduleBuildCost.get(resource) ? Color.FIREBRICK : Color.BLACK;
								}
							}));
							uiElements.add(new Label(5, ay - 2, 10, 10, resource.icon, IOFont.icons, Color.WHITE, null));
							ay -= 20;
						}
					}
					if(lastBuildModule.tooltip.length() > 0){
						ay -= 15;
						uiElements.add(new Label(5, ay, 60, 10, "Effect", null));
						ay -= 25;
						uiElements.add(new Label(5, ay, 40, 10, lastBuildModule.tooltip, IOFont.icons, Color.WHITE, null));
					}
				}
			}
		}
	}

	private class ResourceInfoWindow extends Window {

		public ResourceInfoWindow() {
			super(0, wHeight - 30, 0, 20);
		}

		@Override
		public void init() {
			width = 90 * Resource.values().length;
			x = (wWidth - width) / 2;

			float ax = 5;
			for (final Resource resource : Resource.values()) {
				uiElements.add(new Label(ax + 20, 2, 70, 10, "", new LabelActionHandler() {
					@Override
					public void actionHappened(Label label, float delta) {
						label.text = ((int) SpaceStation.get().getResource(resource))
								+ "/" + ((int) SpaceStation.get().getResourceMax(resource));
						label.color = SpaceStation.get().getResource(resource) < 0.5f ? Color.FIREBRICK : Color.BLACK;
					}
				}));
				uiElements.add(new Label(ax, 0, 10, 10, resource.icon, IOFont.icons, Color.WHITE, null));
				ax += 90;
			}
		}
	}

	private class TimeWindow extends Window {

		public TimeWindow() {
			super(wWidth - 150, wHeight - 80, 100, 20);
		}

		@Override
		public void init() {
			final float[] scales = new float[] {1, 2, 5, 10, 25};
			for(int i = 0; i < 5; i++) {
				final int finalI = i;
				uiElements.add(new Button(5 + i * 20, 3, 15, 10, "" + (int)(scales[i]), null, new LabelActionHandler() {
					@Override
					public void actionHappened(Label label, float delta) {
						timeScale = scales[finalI];
						for (Label label1 : uiElements) {
							label1.color = Color.BLACK;
						}
						label.color = Color.ORANGE;
					}
				}));
			}
			int active = 0;
			for(int i = 0; i < scales.length; i++) {
				if (timeScale == scales[i]) {
					active = i;
				}
			}
			if(0 <= active && active < uiElements.size()) {
				uiElements.get(active).color = Color.ORANGE;
			}
		}
	}

	private class MessageWindow extends Window {

		private MessageManager.Message message;
		private boolean startedMessage;

		public MessageWindow(MessageManager.Message message) {
			super(100, 100, wWidth - 300, wHeight - 200, true);
			this.message = message;
			init();
		}

		public MessageManager.Message getMessage() {
			return message;
		}

		@Override
		public void init() {
			uiElements.add(new Label(10, height - 25, width - 50, 10, message.message, null, width - 100));
			uiElements.add(new Button(width - 50, 10, 30, 15, "Ok", new LabelActionHandler() {
				@Override
				public void actionHappened(Label label, float delta) {
					if(!startedMessage) {
						if (message.prenarration != null && !message.prenarration.isPlaying()){
							if (!muted) {
								message.narration.play();
								startedMessage = true;
							}
						}
					}
				}
			}, new LabelActionHandler() {
				@Override
				public void actionHappened(Label label, float delta) {
					paused = false;
					if (message.prenarration != null) {
						message.prenarration.stop();
						message.prenarration.dispose();
					}
					if (message.narration != null) {
						message.narration.stop();
						message.narration.dispose();
					}
					windows95.remove(MessageWindow.this);
				}
			}));
			startedMessage = false;
			if (message.prenarration != null) {
				if(!muted) {
					message.prenarration.play();
				}
			} else if (message.narration != null) {
				if(!muted) {
					message.narration.play();
					startedMessage = true;
				}
			}
		}
	}

	private class GameOverWindow extends Window {

		private String message;

		public GameOverWindow(String message) {
			super(200, 150, wWidth - 400, wHeight - 300, true);
			this.message = message;
			init();
		}

		@Override
		public void init() {
			uiElements.add(new Label(10, height - 25, width - 50, 10, message, null));
			uiElements.add(new Button(width - 50, 10, 30, 15, "Ok", null, new LabelActionHandler() {
				@Override
				public void actionHappened(Label label, float delta) {
					paused = false;
					windows95.remove(GameOverWindow.this);
					gameOverWindow = null;
					timeScale = 1;
					SpaceStation.get().reset();
					LD38.me.showMenuScreen();
				}
			}));
			paused = true;
		}
	}
}
