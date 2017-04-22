package com.smeanox.games.ld38.world.module;

import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.Pair;

public class ModuleLocation {
	private int x, y;
	private int width, height;
	private int rotation;
	private int rotX, rotY, rotWidth, rotHeight;

	public static int flipDirection(int direction){
		return (direction + 2) % 4;
	}

	public ModuleLocation(int x, int y, int width, int height, int rotation) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		setRotation(rotation);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getRotation() {
		return rotation;
	}

	@SuppressWarnings("SuspiciousNameCombination")
	public void setRotation(int rotation) {
		this.rotation = rotation;
		switch (rotation){
			case Consts.UP:
				rotX = x - height + 1;
				rotY = y;
				rotWidth = height;
				rotHeight = width;
				break;
			case Consts.RIGHT:
				rotX = x;
				rotY = y;
				rotWidth = width;
				rotHeight = height;
				break;
			case Consts.DOWN:
				rotX = x;
				rotY = y - width + 1;
				rotWidth = height;
				rotHeight = width;
				break;
			case Consts.LEFT:
				rotX = x - width + 1;
				rotY = y - height + 1;
				rotWidth = width;
				rotHeight = height;
				break;
		}
	}

	public int getRotX() {
		return rotX;
	}

	public int getRotY() {
		return rotY;
	}

	public int getRotWidth() {
		return rotWidth;
	}

	public int getRotHeight() {
		return rotHeight;
	}

	public void setRotX(int rotX) {
		if(rotation == Consts.LEFT || rotation == Consts.UP){
			this.x = rotX + getRotWidth() - 1;
		} else {
			this.x = rotX;
		}
		setRotation(rotation);
	}

	public void setRotY(int rotY) {
		if(rotation == Consts.DOWN || rotation == Consts.LEFT){
			this.y = rotY + getRotHeight() - 1;
		} else {
			this.y = rotY;
		}
		setRotation(rotation);
	}

	public void setRotWidth(int rotWidth) {
		if(rotation == Consts.UP || rotation == Consts.DOWN){
			//noinspection SuspiciousNameCombination
			this.height = rotWidth;
		} else {
			this.width = rotWidth;
		}
		setRotation(rotation);
	}

	public void setRotHeight(int rotHeight) {
		if(rotation == Consts.UP || rotation == Consts.DOWN){
			//noinspection SuspiciousNameCombination
			this.width = rotHeight;
		} else {
			this.height = rotHeight;
		}
		setRotation(rotation);
	}

	public int getRotRight(){
		return getRotX() + getRotWidth();
	}

	public int getRotTop(){
		return getRotY() + getRotHeight();
	}

	public boolean isPointInModule(int x, int y){
		return getRotX() <= x && x < getRotRight() && getRotY() <= y && y < getRotTop();
	}

	public Pair<Integer, Integer> getPortLocation(int direction){
		switch(direction){
			case Consts.UP:
				return new Pair<Integer, Integer>(getRotX() + getRotWidth() / 2, getRotY() + getRotHeight());
			case Consts.RIGHT:
				return new Pair<Integer, Integer>(getRotX() + getRotWidth(), getRotY() + getRotHeight() / 2);
			case Consts.DOWN:
				return new Pair<Integer, Integer>(getRotX() + getRotWidth() / 2, getRotY() - 1);
			case Consts.LEFT:
				return new Pair<Integer, Integer>(getRotX() - 1, getRotY() + getRotHeight() / 2);
		}
		return null;
	}

	public void foreach(ForeachRunnable runnable){
		int startX = getRotX();
		int startY = getRotY();
		int endX = startX + getRotWidth();
		int endY = startY + getRotHeight();
		for(int x = startX; x < endX; x++){
			for(int y = startY; y < endY; y++){
				runnable.run(x, y);
			}
		}
	}

	public interface ForeachRunnable{
		void run(int x, int y);
	}

	@Override
	public String toString() {
		return getRotX() + "/" + getRotY() + "/" + getRotWidth() + "/" + getRotHeight();
	}
}
