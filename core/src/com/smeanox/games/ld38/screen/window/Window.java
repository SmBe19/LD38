package com.smeanox.games.ld38.screen.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.io.IOFont;
import com.smeanox.games.ld38.io.IOTexture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Window {

	public static final Set<Window> windows95 = new HashSet<Window>();

	protected List<Label> uiElements;
	protected float x, y, width, height;

	public Window(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		uiElements = new ArrayList<Label>();

		init();
	}

	public abstract void init();

	public void update(float delta, int mouseX, int mouseY) {
		for (Label label : uiElements) {
			label.update(delta, mouseX, mouseY);
		}
	}

	public void draw(SpriteBatch batch, float delta) {
		batch.draw(IOTexture.windowBorder.texture, x - 8, y + height, 0, 0, 8, 8);
		batch.draw(IOTexture.windowBorder.texture, x + width, y + height, 9, 0, 8, 8);
		batch.draw(IOTexture.windowBorder.texture, x + width, y - 8, 9, 9, 8, 8);
		batch.draw(IOTexture.windowBorder.texture, x - 8, y - 8, 0, 9, 8, 8);
		batch.draw(IOTexture.windowBorder.texture, x, y, width, height, 8, 8, 1, 1, false, false);
		batch.draw(IOTexture.windowBorder.texture, x - 8, y, 8, height, 0, 8, 8, 1, false, false);
		batch.draw(IOTexture.windowBorder.texture, x + width, y, 8, height, 9, 8, 8, 1, false, false);
		batch.draw(IOTexture.windowBorder.texture, x, y - 8, width, 8, 8, 9, 1, 8, false, false);
		batch.draw(IOTexture.windowBorder.texture, x, y + height, width, 8, 8, 0, 1, 8, false, false);
		for (Label label : uiElements) {
			label.draw(batch, delta);
		}
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public class Label {
		protected float x, y, width, height;
		protected String text;
		protected LabelActionHandler updater;

		public Label(float x, float y, float width, float height, String text, LabelActionHandler updater) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.text = text;
			this.updater = updater;
		}

		public void update(float delta, int mouseX, int mouseY){
			if(updater != null) {
				updater.actionHappened(this, delta);
			}
		}

		public void draw(SpriteBatch batch, float delta) {
			batch.setColor(0, 0, 0, 1);
			IOFont.grusigPunktBdf.draw(batch, (int) (Window.this.x + x), (int) (Window.this.y + y), Consts.SPRITE_SIZE, text);
			batch.setColor(1, 1, 1, 1);
		}
	}

	public abstract interface LabelActionHandler {
		void actionHappened(Label label, float delta);
	}

	public class Button extends Label {

		private LabelActionHandler onClick;
		private boolean wasDown;

		public Button(float x, float y, float width, float height, String text, LabelActionHandler updater, LabelActionHandler onClick) {
			super(x, y, width, height, text, updater);
			this.onClick = onClick;
		}

		@Override
		public void update(float delta, int mouseX, int mouseY) {
			super.update(delta, mouseX, mouseY);

			if (wasDown && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				if ((int) (Window.this.x + x) <= mouseX && mouseX <= (int) (Window.this.x + x + width) &&
						(int) (Window.this.y + y) <= mouseY && mouseY <= (int) (Window.this.y + y + height)) {
					onClick.actionHappened(this, delta);
				}
			}

			wasDown = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
		}
	}
}
