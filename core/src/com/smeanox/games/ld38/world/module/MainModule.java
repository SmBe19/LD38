package com.smeanox.games.ld38.world.module;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOAnimation;
import com.smeanox.games.ld38.io.IOFont;
import com.smeanox.games.ld38.screen.Window;
import com.smeanox.games.ld38.world.GenericRapper;
import com.smeanox.games.ld38.world.Resource;
import com.smeanox.games.ld38.world.SpaceStation;

import java.util.HashMap;
import java.util.Map;

public class MainModule extends Module {

	public MainModule(ModuleLocation moduleLocation) {
		super(moduleLocation);

		allowNeighbors[Consts.UP] = false;
	}

	@Override
	public boolean canAttachSolarPanel() {
		return false;
	}

	@Override
	public TextureRegion getTextureInterior(float time) {
		return IOAnimation.ModuleMain.texture();
	}

	@Override
	public TextureRegion getTextureHull(float time) {
		return IOAnimation.HullMain.texture();
	}

	@Override
	public void adjustResourceMax(Map<Resource, GenericRapper<Float>> resources, boolean add) {
		resources.get(Resource.H2).value += (add ? 1 : -1) * 40;
		resources.get(Resource.O2).value += (add ? 1 : -1) * 360;
		resources.get(Resource.H2O).value += (add ? 1 : -1) * 360;
		resources.get(Resource.Food).value += (add ? 1 : -1) * 180;
		resources.get(Resource.Electricity).value += (add ? 1 : -1) * 360;
		resources.get(Resource.Fe).value += (add ? 1 : -1) * 600;
		resources.get(Resource.Si).value += (add ? 1 : -1) * 200;
	}

	@Override
	public void doInputOutputProcessing(Map<Resource, GenericRapper<Float>> resources, float delta) {
		hadEnoughResources = tryUseResource(resources, delta, Resource.Electricity, 60);
	}

	@Override
	public Window createWindow(float x, float y) {
		return new MainModuleWindow(x, y);
	}

	private class MainModuleWindow extends Window{

		public MainModuleWindow(float x, float y) {
			super(x, y, 200, 200);
		}

		@Override
		public void init() {
			if (SpaceStation.get().isWorldWarStarted()) {
				height = 50;
				uiElements.add(new Label(10, height - 15, 200, 10, "No more deliveries\ndue to war.", null));
			} else {
				height = ((Resource.values().length + 1) / 2) * 25 + 50;
				uiElements.add(new Label(10, height - 15, 200, 10, "Order delivery", null));
				int idx = 0;
				float ay = height - 45;
				for (final Resource resource : Resource.values()) {
					final float inc = Math.max(1, 50.f / resource.deliveryCost);
					float off = idx % 2 == 0 ? 10 : 110;
					uiElements.add(new Label(off + 20, ay - 2, 10, 10, resource.icon, IOFont.icons, Color.WHITE, null));
					uiElements.add(new Label(off + 35, ay, 35, 10, "", new LabelActionHandler() {
						@Override
						public void actionHappened(Label label, float delta) {
							label.text = leftPad("" + SpaceStation.get().getDelivery().get(resource).value.intValue(), 4);
						}
					}));
					uiElements.add(new Button(off, ay, 10, 10, "-", null, new LabelActionHandler() {
						@Override
						public void actionHappened(Label label, float delta) {
							SpaceStation.get().getDelivery().get(resource).value -= inc;
							if (!SpaceStation.get().legalDelivery()) {
								SpaceStation.get().getDelivery().get(resource).value += inc;
							}
						}
					}));
					uiElements.add(new Button(off + 70, ay, 10, 10, "+", null, new LabelActionHandler() {
						@Override
						public void actionHappened(Label label, float delta) {
							SpaceStation.get().getDelivery().get(resource).value += inc;
							if (!SpaceStation.get().legalDelivery()) {
								SpaceStation.get().getDelivery().get(resource).value -= inc;
							}
						}
					}));
					idx++;
					if (idx % 2 == 0) {
						ay -= 25;
					}
				}

				uiElements.add(new Label(10, 5, 200, 10, "Used", new LabelActionHandler() {
					@Override
					public void actionHappened(Label label, float delta) {
						label.text = "Used: " + Math.round(SpaceStation.get().getDeliveryCost()) + "/" + Consts.DELIVERY_LIMIT;
					}
				}));
			}
		}
	}
}
