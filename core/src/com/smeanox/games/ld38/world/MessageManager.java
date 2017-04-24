package com.smeanox.games.ld38.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.ld38.world.event.Event;

public class MessageManager {

	public Message intro(){
		return new Message(„narration/LD38RadioVoiceOver001“, “Commander’s Log, Day 1. We’re about to arrive on the space station Aeneas. What makes this station so special is the fact that it’s completely modular. However, this also means that there’s quite a bit of work ahead of us to get it up and running.\n");
	}

	public Message startGameplay(){
		return new Message(„narration/LD38RadioVoiceOver002“, "We’ve brought some resources with us and we should put them to good use. A storage module will give us a place to put them before we go any further.");
	}

	public Message storeSupplies(){
		return new Message(„narration/LD38RadioVoiceOver003“, "Great, now we can actually store the supplies we brought.");
	}

	public Message electricity(){
		return new Message(„narration/LD38RadioVoiceOver004n5n6“, "Our next priority is to look after the electricity production. We need to construct two solar panels so we have enough power to run this thing. It might be useful to build some empty modules first so we have space for the panels.");
	}

	public Message sleep(){
		return new Message(„narration/LD38RadioVoiceOver007n8“, "Let’s keep in mind these things don’t produce electricity at night. Gotta watch out for our battery capacities.\n\nNow we need to build a place to sleep. ");
	}

	public Message survivePossible(){
		return new Message(„narration/LD38RadioVoiceOver009n10“, "Now we’ve got all the basics done to survive up here. We will receive periodic shipments of the necessary supplies like food, water, oxygen or raw materials. We need to use the command module to give an order for tomorrow.\n\nTool Tip: click on command module, adjust order of the next day"); 
	}

	public Message orderOxygen(){ 
		return new Message(„narration/LD38RadioVoiceOver011“, "We need to make sure to always order enough oxygen, water and food. But then we also need building materials to expand the station.\n\nTool Tip: hover over resource to see requirement");
	}

	public Message dayTwo(){
		return new Message(„narration/LD38RadioVoiceOver012“, "Commander’s Log, Day 2. What a beautiful sight to see the glowing sun emerge behind the blue marble of our planet. \n\n Sleeping in zero g was quite an unusual experience, it’ll take some time getting used to.");
	}

	public Message firstDelvieryArrived(){
		return new Message(„narration/LD38RadioVoiceOver013“, "The daily shipment is about to arrive, let’s see what they brought us this time.");
	}

	public Message shipIsHere(){
		return new Message(„narration/LD38RadioVoiceOver014n15“, "The ship will depart in exactly one minute. It will take home all the resources that we don’t have room for. \n\n We’ll be able to expand our station with the material we’ve received. If we became more self sufficient, a smaller portion of the payload would need to be used for necessities.");
	}

	public Message stationGoal(){
		return new Message(„narration/LD38RadioVoiceOver016“, "The goal of our station up here is to mine asteroids that were brought into orbit around earth. They contain water and metals, which are very useful. We’re going to need to build a rocket module on this station in order to actually mine them. This might take a while.");
	}

	public Message selectShipment(){
		return new Message(„narration/LD38RadioVoiceOver017“, "We’ll need to select a shipment for the next day. Maybe getting some more crew up here wouldn’t be a bad idea. They will want their own sleeping chambers, though.");
	}

	public Message day3(){
		return new Message(„narration/LD38RadioVoiceOver018“, "Commander’s Log, Day 3. We’re slowly getting used to the situation up here. \n\n Within a couple of weeks, we should be able to mine the first asteroids.");
	}

	public Message day4(){
		return new Message(„narration/LD38RadioVoiceOver019“, "Commander’s Log, Day 4.");
	}

	public Message day5(){
		return new Message(„narration/LD38RadioVoiceOver020“, "Commander’s Log, Day 5.“);
	}

	public Message day6(){
		return new Message(„narration/LD38RadioVoiceOver021“, "Commander’s Log, Day 6.“);
	}

	public Message day7(){
		return new Message(„narration/LD38RadioVoiceOver022“, "Commander’s Log, Day 7.“);
	}

	public Message day8(){
		return new Message(„narration/LD38RadioVoiceOver023“, "Commander’s Log, Day 8.“);
	}

	public Message day9(){
		return new Message(„narration/LD38RadioVoiceOver024“, "Commander’s Log, Day 9.“);
	}

	public Message day10(){
		return new Message(„narration/LD38RadioVoiceOver025“, "Commander’s Log, Day 10.“);
	}

	public Message day11(){
		return new Message(„narration/LD38RadioVoiceOver026“, "Commander’s Log, Day 11.“);
	}

	public Message day12(){
		return new Message(„narration/LD38RadioVoiceOver027“, "Commander’s Log, Day 12.“);
	}

	public Message day13(){
		return new Message(„narration/LD38RadioVoiceOver028“, "Commander’s Log, Day 13.“);
	}

	public Message day14(){
		return new Message(„narration/LD38RadioVoiceOver029“, "Commander’s Log, Day 14.“);
	}

	public Message day15(){
		return new Message(„narration/LD38RadioVoiceOver030“, "Commander’s Log, Day 15.“);
	}

	public Message day16(){
		return new Message(„narration/LD38RadioVoiceOver031“, "Commander’s Log, Day 16.“);
	}

	public Message day10After(){
		return new Message(„narration/LD38RadioVoiceOver058“, "Commander’s Log, Day 10.“);
	}

	public Message day11After(){
		return new Message(„narration/LD38RadioVoiceOver059“, "Commander’s Log, Day 11.“);
	}

	public Message day12After(){
		return new Message(„narration/LD38RadioVoiceOver060“, "Commander’s Log, Day 12.“);
	}

	public Message day13After(){
		return new Message(„narration/LD38RadioVoiceOver061“, "Commander’s Log, Day 13.“);
	}

	public Message day14After(){
		return new Message(„narration/LD38RadioVoiceOver062“, "Commander’s Log, Day 14.“);
	}

	public Message day15After(){
		return new Message(„narration/LD38RadioVoiceOver063“, "Day 15.“);
	}

	public Message day16After(){
		return new Message(„narration/LD38RadioVoiceOver064“, "Commander’s Log, Day 16.“);
	}

	public Message day17After(){
		return new Message(„narration/LD38RadioVoiceOver065“, "Commander’s Log, Day 17.“);
	}

	public Message day18After(){
		return new Message(„narration/LD38RadioVoiceOver066“, "Commander’s Log, Day 18.“);
	}

	public Message day19After(){
		return new Message(„narration/LD38RadioVoiceOver067“, "Commander’s Log, Day 19.“);
	}

	public Message day20After(){
		return new Message(„narration/LD38RadioVoiceOver068“, "Commander’s Log, Day 20.“);
	}

	public Message day21After(){
		return new Message(„narration/LD38RadioVoiceOver069“, "Commander’s Log, Day 21.“);
	}

	public Message day22After(){
		return new Message(„narration/LD38RadioVoiceOver070“, "Commander’s Log, Day 22.“);
	}

	public Message day23After(){
		return new Message(„narration/LD38RadioVoiceOver071“, "Commander’s Log, Day 23.“);
	}

	public Message day24After(){
		return new Message(„narration/LD38RadioVoiceOver072“, "Commander’s Log, Day 24.“);
	}

	public Message randomLog1(){
		return new Message(„narration/LD38RadioVoiceOver032“, "It’s all quiet up here. Nothing special’s happening today.“);
	}

	public Message randomLog2(){
		return new Message(„narration/LD38RadioVoiceOver033“, "Another beautiful day up here. Well there’s not exactly a thing like bad weather in space, is there?“);
	}

	public Message randomLog3(){
		return new Message(„narration/LD38RadioVoiceOver035“, "When I look down at the earth like this, I do miss my family. They stayed down there on this huge planet. But now we got a small world of our own up here.“);
	}

	public Message randomLog4(){
		return new Message(„narration/LD38RadioVoiceOver036“, "We’re hearing stories of political turmoil down on earth. None of that matters to us as long as we get our shipments on time.“);
	}

	public Message randomLog5(){
		return new Message(„narration/LD38RadioVoiceOver037“, "Sometimes I miss life on Earth. But it’s important work we’re doing up here.“);
	}

	public Message randomLog6(){
		return new Message(„narration/LD38RadioVoiceOver038“, "I’m looking forward to holding my son again. But I won’t be back on earth for another three months.“);
	}

	public Message randomLog7(){
		return new Message(„narration/LD38RadioVoiceOver039“, "Occasionally I see some asteroids getting within a few kilometers of us. \n But I shouldn’t worry about it.“);
	}

	public Message randomLog8(){
		return new Message(„narration/LD38RadioVoiceOver040“, "There have been no unusual events lately.“);
	}

	public Message randomLog9(){
		return new Message(„narration/LD38RadioVoiceOver041“, "Sometimes I hear a bit of creaking in the storage module. The other crew members assured me that there was nothing to worry about.“);
	}

	public Message randomLog10(){
		return new Message(„narration/LD38RadioVoiceOver042“, "The collaboration in the crew is working really well. I am glad to see that everyone adjusted so quickly to life in space.“);
	}

	public Message randomLog11(){
		return new Message(„narration/LD38RadioVoiceOver043“, „Every day up here makes me feel proud to do this for my country and humanity as a whole.“);
	}

	public Message randomLog12(){
		return new Message(„narration/LD38RadioVoiceOver044“, „The Aeneas is coming along pretty nicely. The new developments are promising.“);
	}

	public Message randomLog1After(){
		return new Message(„narration/LD38RadioVoiceOver046“, "When I look down at earth now, I am overwhelmed with a feeling of sadness that humanity has wiped itself out.“);
	}

	public Message randomLog2After(){
		return new Message(„narration/LD38RadioVoiceOver048“, "We’re alive up here while everyone is dead down there on earth. Sometimes I wonder if it’s even worth it to keep going.“);
	}

	public Message randomLog3After(){
		return new Message(„narration/LD38RadioVoiceOver049“, "Every time I wake up, I hope that this was all a dream… I just want to see my family again.“);
	}

	public Message randomLog4After(){
		return new Message(„narration/LD38RadioVoiceOver050“, "Seeing the dead planet below me makes me feel so … powerless.“);
	}

	public Message randomLog5After(){
		return new Message(„narration/LD38RadioVoiceOver051“, "No more shipments from Earth’s surface. Unless there’s some sort of miracle, our fate will be the same as the one of our brothers on Earth.“);
	}

	public Message randomLog6After(){
		return new Message(„narration/LD38RadioVoiceOver052“, "I would have rather spent the last days of my life with my family than on this station.
“);
	}

	public Message randomLog7After(){
		return new Message(„narration/LD38RadioVoiceOver055“, "Some of the crew are freaking out. They’ve lost their families, they’ve lost their countries. But maybe, maybe there’s a way we can survive.“);
	}

	public Message randomLog8After(){
		return new Message(„narration/LD38RadioVoiceOver056“, „We are struggling to keep going.“);
	}

	public Message randomLog1BeforeScan(){
		return new Message(„narration/LD38RadioVoiceOver045“, "We haven’t received any signals since … the war. I wonder if anyone survived?“);
	}

	public Message randomLog2BeforeScan(){
		return new Message(„narration/LD38RadioVoiceOver053“, "We haven’t given up hope on finding survivors yet. We just need to scan for their signals.“);
	}

	public Message randomLog1AfterScan(){
		return new Message(„narration/LD38RadioVoiceOver054“, "We’re the only humans left… We’re humanity’s last hope. We can’t give up now.“);
	}

	public Message lastLog(){
		return new Message(„narration/LD38RadioVoiceOver057“, "Why am I even still doing this? It’s not like anyone’s ever going to listen to these logs anyway.“);
	}

	public Message dayAfterWar(){
		return new Message(„narration/LD38RadioVoiceOver088useless“, "Seeing the dark side of earth devoid of any light is haunting. How terrible.\n\nIs it possible at least someone survived? Maybe we could look for signals with a radio antenna.");
	}

	public Message scanForSurvivors(){
		return new Message(„narration/LD38RadioVoiceOver089“, "Is it possible at least someone survived? Maybe we could look for signals with a radio antenna.");
	}

	public Message wereDoneHere(){
		return new Message(„narration/LD38RadioVoiceOver092“, "We can now preserve our bodies until the surface of Earth is habitable again. We have lost our families, but we have save our species. The world has ended, but we managed to keep a small world alive up here.");
	}

	public Message noSurvivors(){
		return new Message(„narration/LD38RadioVoiceOver092“, "We didn’t receive any signals. At all. This means there were no survivors… We’re all alone now.");
	}

	public Message dayStart(int day, boolean isWar, Event event) {
		if(event != null) {
			return event.getMessage();
		} else {
			if(day == 1){
				return new Message(„narration/LD38RadioVoiceOver012“, "Commander’s Log, Day 2. What a beautiful sight to see the glowing sun emerge behind the blue marble of our planet. \n\n Sleeping in zero g was quite an unusual experience, it’ll take some time getting used to.");
			} else if (day == 2) {
				return new Message(„narration/LD38RadioVoiceOver018“, "Commander’s Log, Day 3. We’re slowly getting used to the situation up here. \n\n Within a couple of weeks, we should be able to mine the first asteroids.");;
			} else if (SpaceStation.get().isWorldWarToday()){
				SpaceStation.get().addMessage(new Message(„narration/LD38RadioVoiceOver082“, "What the fuck is going on down there? … Are those explosions?"));
				SpaceStation.get().addMessage(new Message(„narration/LD38RadioVoiceOver083“, "The headquarter is not responding. What the hell is going on?"));
				SpaceStation.get().addMessage(new Message(„narration/LD38RadioVoiceOver084“, "Good god … We’re seeing World War Three unfold right below us. This is horrifying."));
				SpaceStation.get().addMessage(new Message(„narration/LD38RadioVoiceOver085“, "Sarah … Robin … NO!"));
				return new Message(„narration/LD38RadioVoiceOver087“, "What does this mean for us? There certainly won’t be any more supply ships now.");
			} else if (SpaceStation.get().isWorldWarStarted()) {
				int val = MathUtils.random(10);
				switch (val){
					case 0:
						if(!SpaceStation.get().isContacetdEarth()) {
							return new Message(„narration/LD38RadioVoiceOver045“, "We haven’t received any signals since … the war. I wonder if anyone survived?“);
						}
						// fall through
					case 1:
						return new Message(„narration/LD38RadioVoiceOver046“, "When I look down at earth now, I am overwhelmed with a feeling of sadness that humanity has wiped itself out.“);
					case 2:
						return new Message(„narration/LD38RadioVoiceOver047useless.ogg“,“Once again, there was no light to be seen on the night side of the earth.");
					case 3:
						return new Message(„narration/LD38RadioVoiceOver048“, "We’re alive up here while everyone is dead down there on earth. Sometimes I wonder if it’s even worth it to keep going.“);
					case 4:
						return new Message(„narration/LD38RadioVoiceOver049“, "Every time I wake up, I hope that this was all a dream… I just want to see my family again.“);
					case 5:
						return new Message(„narration/LD38RadioVoiceOver050“, "Seeing the dead planet below me makes me feel so … powerless.“);
					case 6:
						return new Message(„narration/LD38RadioVoiceOver051“, "No more shipments from Earth’s surface. Unless there’s some sort of miracle, our fate will be the same as the one of our brothers on Earth.“);
					case 7:
						return new Message(„narration/LD38RadioVoiceOver052“, "I would have rather spent the last days of my life with my family than on this station.
					case 8:
						if(SpaceStation.get().isContacetdEarth()){
							return new Message(„narration/LD38RadioVoiceOver054“, "We’re the only humans left… We’re humanity’s last hope. We can’t give up now.“);
						} else {
							return new Message(„narration/LD38RadioVoiceOver053“, "We haven’t given up hope on finding survivors yet. We just need to scan for their signals.“);
						}
					case 9:
						return new Message(„narration/LD38RadioVoiceOver055“, "Some of the crew are freaking out. They’ve lost their families, they’ve lost their countries. But maybe, maybe there’s a way we can survive.“);
					case 10:
						return new Message(„narration/LD38RadioVoiceOver056“, „We are struggling to keep going.“);
				}
			} else {
				int val = MathUtils.random(11);
				switch (val){
					case 0:
						return new Message(„narration/LD38RadioVoiceOver032“, "It’s all quiet up here. Nothing special’s happening today.“);
					case 1:
						return new Message(„narration/LD38RadioVoiceOver033“, "Another beautiful day up here. Well there’s not exactly a thing like bad weather in space, is there?“);
					case 2:
						return new Message(„narration/LD38RadioVoiceOver035“, "When I look down at the earth like this, I do miss my family. They stayed down there on this huge planet. But now we got a small world of our own up here.“);
					case 3:
						return new Message(„narration/LD38RadioVoiceOver036“, "We’re hearing stories of political turmoil down on earth. None of that matters to us as long as we get our shipments on time.“);
					case 4:
						return new Message(„narration/LD38RadioVoiceOver037“, "Sometimes I miss life on Earth. But it’s important work we’re doing up here.“);
					case 5:
						return new Message(„narration/LD38RadioVoiceOver038“, "I’m looking forward to holding my son again. But I won’t be back on earth for another three months.“);
					case 6:
						return new Message(„narration/LD38RadioVoiceOver039“, "Occasionally I see some asteroids getting within a few kilometers of us. \n But I shouldn’t worry about it.“);
					case 7:
						return new Message(„narration/LD38RadioVoiceOver040“, "There have been no unusual events lately.“);
					case 8:
						return new Message(„narration/LD38RadioVoiceOver041“, "Sometimes I hear a bit of creaking in the storage module. The other crew members assured me that there was nothing to worry about.“);
					case 9:
						return new Message(„narration/LD38RadioVoiceOver042“, "The collaboration in the crew is working really well. I am glad to see that everyone adjusted so quickly to life in space.“);
					case 10:
						return new Message(„narration/LD38RadioVoiceOver043“, „Every day up here makes me feel proud to do this for my country and humanity as a whole.“);
					case 11:
						return new Message(„narration/LD38RadioVoiceOver044“, „The Aeneas is coming along pretty nicely. The new developments are promising.“);
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
