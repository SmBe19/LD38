package com.smeanox.games.ld38.world;

import com.smeanox.games.ld38.world.module.CryogenicModule;
import com.smeanox.games.ld38.world.module.Module;
import com.smeanox.games.ld38.world.module.ModuleType;
import com.smeanox.games.ld38.world.module.RadioModule;
import com.smeanox.games.ld38.world.module.SleepingModule;
import com.smeanox.games.ld38.world.module.SolarModule;
import com.smeanox.games.ld38.world.module.StorageModule;
import com.smeanox.games.ld38.world.task.Task;
import com.smeanox.games.ld38.world.task.WalkTask;

import java.util.ArrayList;
import java.util.List;

public class StoryManager {

	public ModuleType highlighted;

	private MessageManager messageManager;
	private int state = 0;
	private List<Task> goToCryogenicTasks;
	private Pair<Float, Float> cryogenicCenter;
	private float timeout = 0;

	public StoryManager(){
		messageManager = SpaceStation.get().getMessageManager();
		goToCryogenicTasks = new ArrayList<Task>();
	}

	public static boolean hasModule(ModuleChecker checker) {
		for (Module module : SpaceStation.get().getModules()) {
			if (module.isFinished() && checker.check(module)) {
				return true;
			}
		}
		return false;
	}

	public interface ModuleChecker{
		boolean check(Module module);
	}

	private void defaultText(float delta) {

	}

	public void update(float delta) {
		switch (state){
			case 0:
				SpaceStation.get().getEnabledModuleTypes().add(ModuleType.StorageModule);
				highlighted = ModuleType.StorageModule;
				SpaceStation.get().addMessage(messageManager.startGameplay());
				state++;
				break;
			case 1:
				if(hasModule(new ModuleChecker() {
					@Override
					public boolean check(Module module) {
						return module instanceof StorageModule;
					}
				})){
					state++;
					highlighted = null;
				}
				break;
			case 2:
				SpaceStation.get().getEnabledModuleTypes().add(ModuleType.CrossModule);
				SpaceStation.get().getEnabledModuleTypes().add(ModuleType.EmptyModule);
				SpaceStation.get().getEnabledModuleTypes().add(ModuleType.SolarModule);
				highlighted = ModuleType.SolarModule;
				SpaceStation.get().addMessage(messageManager.storeSupplies());
				SpaceStation.get().addMessage(messageManager.electricity());
				state++;
				break;
			case 3:
				if(hasModule(new ModuleChecker() {
					@Override
					public boolean check(Module module) {
						return module instanceof SolarModule;
					}
				})){
					state++;
					highlighted = null;
				}
				break;
			case 4:
				SpaceStation.get().getEnabledModuleTypes().add(ModuleType.SleepingModule);
				highlighted = ModuleType.SleepingModule;
				SpaceStation.get().addMessage(messageManager.sleep());
				state++;
				break;
			case 5:
				if(hasModule(new ModuleChecker() {
					@Override
					public boolean check(Module module) {
						return module instanceof SleepingModule;
					}
				})){
					state++;
					highlighted = null;
				}
				break;
			case 6:
				SpaceStation.get().addMessage(messageManager.survivePossible());
				SpaceStation.get().addMessage(messageManager.orderOxygen());
				state++;
				break;
			case 7:
				if(SpaceStation.get().isEndOfOrderTime()){
					SpaceStation.get().addMessage(messageManager.firstDelvieryArrived());
					state++;
				}
				break;
			case 8:
				if (SpaceStation.get().isDeliveryTime()) {
					SpaceStation.get().addMessage(messageManager.shipIsHere());
					for (ModuleType moduleType : ModuleType.values()) {
						if(moduleType == ModuleType.CryogenicModule){
							continue;
						}
						SpaceStation.get().getEnabledModuleTypes().add(moduleType);
					}
					SpaceStation.get().addMessage(messageManager.stationGoal());
					state++;
				}
				break;
			case 9:
				if (SpaceStation.get().isNightStart()) {
					SpaceStation.get().addMessage(messageManager.selectShipment());
					state++;
				}
				break;
			case 10:
				if (SpaceStation.get().isWorldWarStarted() && !SpaceStation.get().isWorldWarToday()) {
					highlighted = ModuleType.RadioModule;
					SpaceStation.get().addMessage(messageManager.dayAfterWar());
					state++;
				}
				break;
			case 11:
				if(hasModule(new ModuleChecker() {
					@Override
					public boolean check(Module module) {
						return module instanceof RadioModule;
					}
				})){
					state++;
					highlighted = null;
					SpaceStation.get().addMessage(messageManager.scanForSurvivors());
				}
			case 12:
				if(hasModule(new ModuleChecker() {
					@Override
					public boolean check(Module module) {
						return module instanceof CryogenicModule;
					}
				})){
					state++;
					highlighted = null;
					Module cryogenic = null;
					for (Module module : SpaceStation.get().getModules()) {
						if (module.isFinished() && module instanceof CryogenicModule) {
							cryogenic = module;
						}
					}
					if (cryogenic == null) {
						break;
					}
					cryogenicCenter = cryogenic.getModuleLocation().getCenter();
					for (Dude dude : SpaceStation.get().getDudes()) {
						WalkTask walkTask = new WalkTask(cryogenicCenter.first, cryogenicCenter.second);
						goToCryogenicTasks.add(walkTask);
						dude.setCurrentTask(walkTask);
					}
				}
				break;
			case 13:
				boolean allArrived = true;
				for (Task task : goToCryogenicTasks) {
					if (!task.isArrived(cryogenicCenter.first, cryogenicCenter.second)) {
						allArrived = false;
					}
				}
				if(allArrived) {
					state++;
				}
				break;
			case 14:
				SpaceStation.get().addMessage(messageManager.wereDoneHere());
				state++;
				timeout = 7;
				break;
			case 15:
				timeout -= delta;
				if(timeout < 0) {
					SpaceStation.get().setGameOverMessage("Congratulations, you finished the game!\nThank you for playing.");
					state++;
				}
				break;
			default:
				break;
		}
	}
}
