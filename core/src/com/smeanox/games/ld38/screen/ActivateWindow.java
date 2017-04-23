package com.smeanox.games.ld38.screen;

import com.smeanox.games.ld38.world.module.Module;

public class ActivateWindow extends Window {

	protected Module module;

	public ActivateWindow(float x, float y, Module module) {
		super(x, y, 60, 20, true);
		this.module = module;
		init();
	}

	@Override
	public void init() {
		uiElements.add(new Button(5, 5, width - 10, 10, module.isActive() ? "Disable" : "Enable", null, new LabelActionHandler() {
			@Override
			public void actionHappened(Label label, float delta) {
				module.setActive(!module.isActive());
				label.text = module.isActive() ? "Disable" : "Enable";
			}
		}));
	}
}
