package com.smeanox.games.ld38.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.smeanox.games.ld38.world.event.Event;

public class MessageManager {

	public Message intro(){
		return new Message("Commander’s Log, Day 1. We’re about to arrive on the space station Aeneas. What makes this station so special is the fact that it’s completely modular. However, this also means that there’s quite a bit of work ahead of us to get it up and running.\n");
	}

	public Message tut1(){
		return new Message("tut1");
	}

	public Message tut2(){
		return new Message("tut2");
	}

	public Message tut3(){
		return new Message("tut3");
	}

	public Message tut4(){
		return new Message("tut4");
	}

	public Message tut5(){
		return new Message("tut5");
	}

	public Message tut6(){
		return new Message("tut6");
	}

	public Message tut7(){
		return new Message("tut7");
	}

	public Message tut8(){
		return new Message("tut8");
	}

	public Message tut9(){
		return new Message("tut9");
	}

	public Message tut10(){
		return new Message("tut10");
	}

	public Message tut11(){
		return new Message("We should scan for survivors with the radio antenna");
	}

	public Message tut12(){
		return new Message("tut12");
	}

	public Message noSurvivors(){
		return new Message("no survivors");
	}

	public Message dayStart(int day, boolean isWar, Event event) {
		if(event != null) {
			return new Message(event.getDescription());
		} else {
			return new Message("Nothing happened");
		}
	}

	public static class Message{
		public String message;
		public Music narration;

		public Message(String message) {
			this.message = message;
			this.narration = null;
		}

		public Message(Music narration, String message) {
			this.narration = narration;
			this.message = message;
		}

		public Message(String file, String message) {
			this(Gdx.audio.newMusic(Gdx.files.internal(file)), message);
		}
	}
}
