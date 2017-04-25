package com.smeanox.games.ld38.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.ld38.world.event.Event;
import com.smeanox.games.ld38.world.event.WorldWarEvent;

public class MessageManager {

	public Message intro(){
		return new Message("narration/LD38RadioVoiceOver001.ogg", "Commander's Log, Day 1. We're about to arrive on the space station Aeneas. What makes this station so special is the fact that it's completely modular. However, this also means that there's quite a bit of work ahead of us to get it up and running.\n");
	}

	public Message startGameplay(){
		return new Message("narration/LD38RadioVoiceOver002.ogg", "We've brought some resources with us and we should put them to good use. A storage module will give us a place to put them before we go any further.");
	}

	public Message storeSupplies(){
		return new Message("narration/LD38RadioVoiceOver003.ogg", "Great, now we can actually store the supplies we brought.");
	}

	public Message electricity(){
		return new Message("narration/LD38RadioVoiceOver004n5n6.ogg", "Our next priority is to look after the electricity production. We need to construct two solar panels so we have enough power to run this thing. It might be useful to build some empty modules first so we have space for the panels.");
	}

	public Message sleep(){
		return new Message("narration/LD38RadioVoiceOver007n8.ogg", "Let's keep in mind these things don't produce electricity at night. Gotta watch out for our battery capacities.\n\nNow we need to build a place to sleep. ");
	}

	public Message survivePossible(){
		return new Message("narration/LD38RadioVoiceOver009n10.ogg", "Now we've got all the basics done to survive up here. We will receive periodic shipments of the necessary supplies like food, water, oxygen or raw materials. We need to use the command module to give an order for tomorrow.\n\nTool Tip: click on command module and adjust the order of the next day");
	}

	public Message orderOxygen(){ 
		return new Message("narration/LD38RadioVoiceOver011.ogg", "We need to make sure to always order enough oxygen, water and food. But then we also need building materials to expand the station.\n\nTool Tip: hover over resource to see requirement");
	}

	public Message firstDelvieryArrived(){
		return new Message("narration/LD38RadioVoiceOver013.ogg", "The daily shipment is about to arrive, let's see what they brought us this time.");
	}

	public Message shipIsHere(){
		return new Message("narration/LD38RadioVoiceOver014n15.ogg", "The ship will depart in exactly one minute. It will take home all the resources that we don't have room for. \n\n We'll be able to expand our station with the material we've received. If we became more self sufficient, a smaller portion of the payload would need to be used for necessities.");
	}

	public Message stationGoal(){
		return new Message("narration/LD38RadioVoiceOver016.ogg", "The goal of our station up here is to mine asteroids that were brought into orbit around earth. They contain water and metals, which are very useful. We're going to need to build a rocket module on this station in order to actually mine them. This might take a while.");
	}

	public Message selectShipment(){
		return new Message("narration/LD38RadioVoiceOver017.ogg", "We'll need to select a shipment for the next day. Maybe getting some more crew up here wouldn't be a bad idea. They will want their own sleeping chambers, though.");
	}

	public Message dayAfterWar(){
		return new Message("narration/LD38RadioVoiceOver088useless.ogg", "Seeing the dark side of earth devoid of any light is haunting. How terrible.\n\nIs it possible at least someone survived? Maybe we could look for signals with a radio antenna.");
	}

	public Message scanForSurvivors(){
		return new Message("narration/LD38RadioVoiceOver089.ogg", "Is it possible at least someone survived? Maybe we could look for signals with a radio antenna.");
	}

	public Message wereDoneHere(){
		return new Message("narration/LD38RadioVoiceOver092.ogg", "We can now preserve our bodies until the surface of Earth is habitable again. We have lost our families, but we have save our species. The world has ended, but we managed to keep a small world alive up here.");
	}

	public Message noSurvivors(){
		return new Message("narration/LD38RadioVoiceOver092.ogg", "We didn't receive any signals. At all. This means there were no survivors... We're all alone now.");
	}

	public Message dayStart(int day, Event event) {
		if(event != null && !(event instanceof WorldWarEvent)) {
			String prefile = "narration/LD38RadioVoiceOver0" + (49 + SpaceStation.get().getDay()) + ".ogg";
			if(SpaceStation.get().getDay() < 9 || !SpaceStation.get().isWorldWarStarted()){
				prefile = "narration/LD38RadioVoiceOver0" + (16 + SpaceStation.get().getDay()) + ".ogg";
			}
			Message message = event.getMessage();
			message.prenarration = Gdx.audio.newMusic(Gdx.files.internal(prefile));
			return message;
		} else {
			if(day > 23){
				return null;
			}
			if(day == 23) {
				return new MessageManager.Message("narration/LD38RadioVoiceOver057.ogg", "Why am I even still doing this? It's not like anyone's ever going to listen to these logs anyway.");
			} else if(day == 1){
				return new Message("narration/LD38RadioVoiceOver012.ogg", "Commander's Log, Day 2. What a beautiful sight to see the glowing sun emerge behind the blue marble of our planet. \n\n Sleeping in zero g was quite an unusual experience, it'll take some time getting used to.");
			} else if (day == 2) {
				return new Message("narration/LD38RadioVoiceOver018.ogg", "Commander's Log, Day 3. We're slowly getting used to the situation up here. \n\n Within a couple of weeks, we should be able to mine the first asteroids.");
			} else if (SpaceStation.get().isWorldWarToday()){
				SpaceStation.get().addMessage(new Message("narration/LD38RadioVoiceOver082.ogg", "What the fuck is going on down there? ... Are those explosions?"));
				SpaceStation.get().addMessage(new Message("narration/LD38RadioVoiceOver083.ogg", "The headquarter is not responding. What the hell is going on?"));
				SpaceStation.get().addMessage(new Message("narration/LD38RadioVoiceOver084.ogg", "Good god ... We're seeing World War Three unfold right below us. This is horrifying."));
				SpaceStation.get().addMessage(new Message("narration/LD38RadioVoiceOver085.ogg", "Sarah ... Robin ... NO!"));
				return new Message("narration/LD38RadioVoiceOver087.ogg", "What does this mean for us? There certainly won't be any more supply ships now.");
			} else if (SpaceStation.get().isWorldWarStarted()) {
				String prefile = "narration/LD38RadioVoiceOver0" + (49 + SpaceStation.get().getDay()) + ".ogg";
				String premessage = "Commander's Log, Day " + (1 + SpaceStation.get().getDay()) + ".\n\n";
				if(SpaceStation.get().getDay() < 9){
					prefile = "narration/LD38RadioVoiceOver0" + (16 + SpaceStation.get().getDay()) + ".ogg";
				}

				int val = MathUtils.random(10);
				switch (val){
					case 0:
						if(!SpaceStation.get().isContacetdEarth()) {
							return new Message(prefile, "narration/LD38RadioVoiceOver045.ogg", premessage + "We haven't received any signals since ... the war. I wonder if anyone survived?");
						}
						// fall through
					case 1:
						return new Message(prefile, "narration/LD38RadioVoiceOver046.ogg", premessage + "When I look down at earth now, I am overwhelmed with a feeling of sadness that humanity has wiped itself out.");
					case 2:
						return new Message(prefile, "narration/LD38RadioVoiceOver047useless.ogg", premessage + "Once again, there was no light to be seen on the night side of the earth.");
					case 3:
						return new Message(prefile, "narration/LD38RadioVoiceOver048.ogg", premessage + "We're alive up here while everyone is dead down there on earth. Sometimes I wonder if it's even worth it to keep going.");
					case 4:
						return new Message(prefile, "narration/LD38RadioVoiceOver049.ogg", premessage + "Every time I wake up, I hope that this was all a dream... I just want to see my family again.");
					case 5:
						return new Message(prefile, "narration/LD38RadioVoiceOver050.ogg", premessage + "Seeing the dead planet below me makes me feel so ... powerless.");
					case 6:
						return new Message(prefile, "narration/LD38RadioVoiceOver051.ogg", premessage + "No more shipments from Earth's surface. Unless there's some sort of miracle, our fate will be the same as the one of our brothers on Earth.");
					case 7:
						return new Message(prefile, "narration/LD38RadioVoiceOver052.ogg", premessage + "I would have rather spent the last days of my life with my family than on this station.");
					case 8:
						if(SpaceStation.get().isContacetdEarth()){
							return new Message(prefile, "narration/LD38RadioVoiceOver054.ogg", premessage + "We're the only humans left... We're humanity's last hope. We can't give up now.");
						} else {
							return new Message(prefile, "narration/LD38RadioVoiceOver053.ogg", premessage + "We haven't given up hope on finding survivors yet. We just need to scan for their signals.");
						}
					case 9:
						return new Message(prefile, "narration/LD38RadioVoiceOver055.ogg", premessage + "Some of the crew are freaking out. They've lost their families, they've lost their countries. But maybe, maybe there's a way we can survive.");
					case 10:
						return new Message(prefile, "narration/LD38RadioVoiceOver056.ogg", premessage + "We are struggling to keep going.");
				}
			} else {
				String prefile = "narration/LD38RadioVoiceOver0" + (16 + SpaceStation.get().getDay()) + ".ogg";
				String premessage = "Commander's Log, Day " + (1 + SpaceStation.get().getDay()) + ".\n\n";
				int val = MathUtils.random(11);
				switch (val){
					case 0:
						return new Message(prefile, "narration/LD38RadioVoiceOver032.ogg", premessage + "It's all quiet up here. Nothing special's happening today.");
					case 1:
						return new Message(prefile, "narration/LD38RadioVoiceOver033.ogg", premessage + "Another beautiful day up here. Well there's not exactly a thing like bad weather in space, is there?");
					case 2:
						return new Message(prefile, "narration/LD38RadioVoiceOver035.ogg", premessage + "When I look down at the earth like this, I do miss my family. They stayed down there on this huge planet. But now we got a small world of our own up here.");
					case 3:
						return new Message(prefile, "narration/LD38RadioVoiceOver036.ogg", premessage + "We're hearing stories of political turmoil down on earth. None of that matters to us as long as we get our shipments on time.");
					case 4:
						return new Message(prefile, "narration/LD38RadioVoiceOver037.ogg", premessage + "Sometimes I miss life on Earth. But it's important work we're doing up here.");
					case 5:
						return new Message(prefile, "narration/LD38RadioVoiceOver038.ogg", premessage + "I'm looking forward to holding my son again. But I won't be back on earth for another three months.");
					case 6:
						return new Message(prefile, "narration/LD38RadioVoiceOver039.ogg", premessage + "Occasionally I see some asteroids getting within a few kilometers of us. \n But I shouldn't worry about it.");
					case 7:
						return new Message(prefile, "narration/LD38RadioVoiceOver040.ogg", premessage + "There have been no unusual events lately.");
					case 8:
						return new Message(prefile, "narration/LD38RadioVoiceOver041.ogg", premessage + "Sometimes I hear a bit of creaking in the storage module. The other crew members assured me that there was nothing to worry about.");
					case 9:
						return new Message(prefile, "narration/LD38RadioVoiceOver042.ogg", premessage + "The collaboration in the crew is working really well. I am glad to see that everyone adjusted so quickly to life in space.");
					case 10:
						return new Message(prefile, "narration/LD38RadioVoiceOver043.ogg", premessage + "Every day up here makes me feel proud to do this for my country and humanity as a whole.");
					case 11:
						return new Message(prefile, "narration/LD38RadioVoiceOver044.ogg", premessage + "The Aeneas is coming along pretty nicely. The new developments are promising.");
				}
			}
		}
		return null;
	}

	public static class Message{
		public String message;
		public Music prenarration;
		public Music narration;

		public Message(String message) {
			this.message = message;
			this.narration = null;
			this.prenarration = null;
		}

		public Message(Music narration, String message) {
			this.narration = narration;
			this.message = message;
			this.prenarration = null;
		}

		public Message(Music prenarration, Music narration, String message) {
			this.message = message;
			this.prenarration = prenarration;
			this.narration = narration;
		}

		public Message(String file, String message) {
			this(Gdx.audio.newMusic(Gdx.files.internal(file)), message);
		}

		public Message(String prefile, String file, String message) {
			this(Gdx.audio.newMusic(Gdx.files.internal(prefile)), Gdx.audio.newMusic(Gdx.files.internal(file)), message);
		}
	}
}
