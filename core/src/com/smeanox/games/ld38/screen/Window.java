package com.smeanox.games.ld38.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.StringBuilder;
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
	protected boolean visible;

	public Window(float x, float y, float width, float height) {
		this(x, y, width, height, false);
	}

	public Window(float x, float y, float width, float height, boolean omitInit) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.visible = true;
		uiElements = new ArrayList<Label>();

		if(!omitInit) {
			init();
		}
	}

	public abstract void init();

	public void update(float delta, int mouseX, int mouseY, boolean[] mouseButtons) {
		if(!visible){
			return;
		}

		for (Label label : uiElements) {
			label.update(delta, mouseX, mouseY, mouseButtons);
		}

		if (x <= mouseX && mouseX <= x + width && y <= mouseY && mouseY <= y + height) {
			mouseButtons[0] = false;
			mouseButtons[1] = false;
		}
	}

	public static void drawBorder(SpriteBatch batch, Texture texture, float x, float y, float width, float height, int sx, int sy, int size){
		batch.draw(IOTexture.ui.texture, x - size, y + height, sx, sy, size, size); // top left
		batch.draw(IOTexture.ui.texture, x + width, y + height, sx + size + 1, sy, size, size); // top right
		batch.draw(IOTexture.ui.texture, x + width, y - size, sx + size + 1, sy + size + 1, size, size); // bottom right
		batch.draw(IOTexture.ui.texture, x - size, y - size, sx, sy + size + 1, size, size); // bottom left
		batch.draw(IOTexture.ui.texture, x, y, width, height, sx + size, sy + size, 1, 1, false, false); // background
		batch.draw(IOTexture.ui.texture, x - size, y, size, height, sx, sy + size, size, 1, false, false); // left
		batch.draw(IOTexture.ui.texture, x + width, y, size, height, sx + size + 1, sy + size, size, 1, false, false); // right
		batch.draw(IOTexture.ui.texture, x, y - size, width, size, sx + size, sy + size + 1, 1, size, false, false); // bottom
		batch.draw(IOTexture.ui.texture, x, y + height, width, size, sx + size, sy, 1, size, false, false); // top
	}

	public void draw(SpriteBatch batch, float delta) {
		if (!visible) {
			return;
		}

		drawBorder(batch, IOTexture.ui.texture, x, y, width, height, 0, 0, 8);

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

	protected String leftPad(String text, int width) {
		StringBuilder sb = new StringBuilder();
		while (sb.length() + text.length() < width) {
			sb.append(" ");
		}
		sb.append(text);
		return sb.toString();
	}

	public interface LabelActionHandler {
		void actionHappened(Label label, float delta);
	}

	public class Label {
		public float x, y, width, height;
		public String text;
		public IOFont font;
		public Color color;
		public float autoWrap;
		protected LabelActionHandler updater;
		public boolean visible;

		public Label(float x, float y, float width, float height, String text, LabelActionHandler updater) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.text = text;
			this.updater = updater;
			this.visible = true;
			this.font = IOFont.grusigPunktBdf;
			this.color = Color.BLACK;
			this.autoWrap = -1;
		}

		public Label(float x, float y, float width, float height, String text, LabelActionHandler updater, float autoWrap) {
			this(x, y, width, height, text, updater);
			this.autoWrap = autoWrap;
		}

		public Label(float x, float y, float width, float height, String text, IOFont font, Color color, LabelActionHandler updater) {
			this(x, y, width, height, text, updater);
			this.font = font;
			this.color = color;
		}

		public void update(float delta, int mouseX, int mouseY, boolean[] mouseButtons){
			if(updater != null) {
				updater.actionHappened(this, delta);
			}
		}

		public void draw(SpriteBatch batch, float delta) {
			if (!visible) {
				return;
			}
			Color oldColor = batch.getColor().cpy();
			batch.setColor(color);
			font.draw(batch, (int) (Window.this.x + x), (int) (Window.this.y + y), text, autoWrap);
			batch.setColor(oldColor);
		}
	}

	public class Button extends Label {

		protected LabelActionHandler onClick;
		protected boolean wasDown;

		public Button(float x, float y, float width, float height, String text, LabelActionHandler updater, LabelActionHandler onClick) {
			super(x, y, width, height, text, updater);
			this.onClick = onClick;
		}

		@Override
		public void update(float delta, int mouseX, int mouseY, boolean[] mouseButtons) {
			super.update(delta, mouseX, mouseY, mouseButtons);

			if (wasDown && !mouseButtons[0]) {
				if ((int) (Window.this.x + x) <= mouseX && mouseX <= (int) (Window.this.x + x + width) &&
						(int) (Window.this.y + y) <= mouseY && mouseY <= (int) (Window.this.y + y + height)) {
					onClick.actionHappened(this, delta);
				}
			}

			wasDown = mouseButtons[0];
		}

		@Override
		public void draw(SpriteBatch batch, float delta) {
			if (!visible) {
				return;
			}
			drawBorder(batch, IOTexture.ui.texture, Window.this.x + x - 2, Window.this.y + y, width + 2, height, 0, 17, 8);
			super.draw(batch, delta);
		}
	}

	public class ButtonWithHover extends Button {
		protected LabelActionHandler onHover;

		public ButtonWithHover(float x, float y, float width, float height, String text, LabelActionHandler updater, LabelActionHandler onClick, LabelActionHandler onHover) {
			super(x, y, width, height, text, updater, onClick);
			this.onHover = onHover;
		}

		@Override
		public void update(float delta, int mouseX, int mouseY, boolean[] mouseButtons) {
			super.update(delta, mouseX, mouseY, mouseButtons);

			if ((int) (Window.this.x + x) <= mouseX && mouseX <= (int) (Window.this.x + x + width) &&
					(int) (Window.this.y + y) <= mouseY && mouseY <= (int) (Window.this.y + y + height)) {
				onHover.actionHappened(this, delta);
			}
		}
	}

}
