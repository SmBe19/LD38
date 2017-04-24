package com.smeanox.games.ld38.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.ld38.world.event.Event;

public class MessageManager {

	public Message intro(){
		return new Message("Commander’s Log, Day 1. We’re about to arrive on the space station Aeneas. What makes this station so special is the fact that it’s completely modular. However, this also means that there’s quite a bit of work ahead of us to get it up and running.\n");
	}

	public Message startGameplay(){
		return new Message("We’ve brought some resources with us and we should put them to good use. A storage module will give us a place to put them before we go any further.");
	}

	public Message storeSupplies(){
		return new Message("Great, now we can actually store the supplies we brought.");
	}

	public Message electricity(){
		return new Message("Our next priority is to look after the electricity production. We need to construct two solar panels so we have enough power to run this thing. It might be useful to build some empty modules first so we have space for the panels.");
	}

	public Message sleep(){
		return new Message("Let’s keep in mind these things don’t produce electricity at night. Gotta watch out for our battery capacities.\n\nNow we need to build a place to sleep. ");
	}

	public Message survivePossible(){
		return new Message("Now we’ve got all the basics done to survive up here. We will receive periodic shipments of the necessary supplies like food, water, oxygen or raw materials. We need to use the command module to give an order for tomorrow.\n\nTool Tip: click on command module, adjust order of the next day");
	}

	public Message orderOxygen(){
		return new Message("We need to make sure to always order enough oxygen, water and food. But then we also need building materials to expand the station.\n\nTool Tip: hover over resource to see requirement");
	}

	public Message firstDelvieryArrived(){
		return new Message("The daily shipment is about to arrive, let’s see what they brought us this time.");
	}

	public Message shipIsHere(){
		return new Message("The ship will depart in exactly one minute. It will take home all the resources that we don’t have room for. We’ll be able to expand our station with the material we’ve received. If we became more self sufficient, a smaller portion of the payload would need to be used for necessities.");
	}

	public Message stationGoal(){
		return new Message("The goal of our station up here is to mine asteroids that were brought into orbit around earth. They contain water and metals, which are very useful. We’re going to need to build a rocket module on this station in order to actually mine them. This might take a while.");
	}

	public Message selectShipment(){
		return new Message("We’ll need to select a shipment for the next day. Maybe getting some more crew up here wouldn’t be a bad idea. They will want their own sleeping chambers, though.");
	}

	public Message dayAfterWar(){
		return new Message("Seeing the dark side of earth devoid of any light is haunting. How terrible.\n\nIs it possible at least someone survived? Maybe we could look for signals with a radio antenna.");
	}

	public Message scanForSurvivors(){
		return new Message("We should scan for survivors with the radio antenna");
	}

	public Message wereDoneHere(){
		return new Message("We can now preserve our bodies until the surface of Earth is habitable again. We have lost our families, but we have save our species. The world has ended, but we managed to keep a small world alive up here.");
	}

	public Message noSurvivors(){
		return new Message("no survivors");
	}

	public Message dayStart(int day, boolean isWar, Event event) {
		if(event != null) {
			return event.getMessage();
		} else {
			if(day == 1){
				return new Message("Commander’s Log, Day 2. What a beautiful sight to see the glowing sun emerge behind the blue marble of our planet.\n\nSleeping in zero g was quite an unusual experience, it will take some time to get used to that.");
			} else if (day == 2) {
				return new Message("Commander’s Log, Day 3. We’re slowly getting used to the situation up here.\n\nWithin a couple of weeks, we should be able to mine the first asteroids.");
			} else if (SpaceStation.get().isWorldWarToday()){
				SpaceStation.get().addMessage(new Message("What the fuck is going on down there? … Are those explosions?"));
				SpaceStation.get().addMessage(new Message("The headquarter is not responding. What the hell is going on?"));
				SpaceStation.get().addMessage(new Message("Good god … We’re seeing World War Three unfold right below us. This is horrifying."));
				SpaceStation.get().addMessage(new Message("Sarah … Robin … NO!"));
				return new Message("What does this mean for us? There certainly won’t be any more supply ships now.");
			} else if (SpaceStation.get().isWorldWarStarted()) {
				int val = MathUtils.random(10);
				switch (val){
					case 0:
						if(!SpaceStation.get().isContacetdEarth()) {
							return new Message("We haven’t received any signals since … the war. I wonder if anyone survived?");
						}
						// fall through
					case 1:
						return new Message("When I look down at earth now, I am overwhelmed with a feeling of sadness that humanity has wiped itself out.");
					case 2:
						return new Message("Once again, there was no light to be seen on the night side of the earth.");
					case 3:
						return new Message("We’re alive up here while everyone is dead down there on earth. Sometimes I wonder if it’s even worth it to keep going.");
					case 4:
						return new Message("Every time I wake up, I hope that this was all a dream… I just want to see my family again.");
					case 5:
						return new Message("Seeing the dead planet below me makes me feel so … powerless.");
					case 6:
						return new Message("No more shipments from Earth’s surface. Unless there’s some sort of miracle, our fate will be the same as the one of our brothers on Earth.");
					case 7:
						return new Message("I would have rather spent the last days of my life with my family than on this station.");
					case 8:
						if(SpaceStation.get().isContacetdEarth()){
							return new Message("We’re the only humans left… We’re humanity’s last hope. We can’t give up now.");
						} else {
							return new Message("We haven’t given up hope on finding survivors yet. We just need to scan for their signals.");
						}
					case 9:
						return new Message("Some of the crew are freaking out. They’ve lost their families, they’ve lost their countries. But maybe, maybe there’s a way we can survive.");
					case 10:
						return new Message("We are struggling to keep going. ");
				}
			} else {
				int val = MathUtils.random(11);
				switch (val){
					case 0:
						return new Message("It’s all quiet up here. Nothing special’s happening today.");
					case 1:
						return new Message("Another beautiful day up here. Well there’s not exactly a thing like bad weather in space, is there?");
					case 2:
						return new Message("When I look down at the earth like this, I do miss my family. They stayed down there on this huge planet. But now we got a small world of our own up here.");
					case 3:
						return new Message("We’re hearing stories of political turmoil down on earth. None of that matters to us as long as we get our shipments on time.");
					case 4:
						return new Message("Sometimes I miss life on Earth. But it’s important work we’re doing up here.");
					case 5:
						return new Message("I’m looking forward to holding my son again. But I won’t be back on earth for another three months.");
					case 6:
						return new Message("Occasionally I see some asteroids getting within a few kilometers of us. But I shouldn’t worry about it.");
					case 7:
						return new Message("There have been no unusual events lately.");
					case 8:
						return new Message("Sometimes I hear a bit of creaking in the storage module. The other crew members assured me that there was nothing to worry about.");
					case 9:
						return new Message("The collaboration in the crew is working really well. I am glad to see that everyone adjusted so quickly to life in space.");
					case 10:
						return new Message("Every day up here makes me feel proud to do this for my country and humanity as a whole.");
					case 11:
						return new Message("The Aeneas is coming along pretty nicely. The new developments are promising.");
				}
			}
		}
		return null;
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
