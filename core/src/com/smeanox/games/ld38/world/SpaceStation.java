package com.smeanox.games.ld38.world;

import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.StringBuilder;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.event.EclipseEvent;
import com.smeanox.games.ld38.world.event.Event;
import com.smeanox.games.ld38.world.event.LaunchFailureEvent;
import com.smeanox.games.ld38.world.event.SolarFlareEvent;
import com.smeanox.games.ld38.world.event.WorldWarEvent;
import com.smeanox.games.ld38.world.module.MainModule;
import com.smeanox.games.ld38.world.module.Module;
import com.smeanox.games.ld38.world.module.ModuleFactory;
import com.smeanox.games.ld38.world.module.RocketPlaceholderModule;
import com.smeanox.games.ld38.world.task.Task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpaceStation {

	private static SpaceStation singleton;

	public static SpaceStation get(){
		if (singleton == null) {
			singleton = new SpaceStation();
		}
		return singleton;
	}

	private Set<Module> modules;
	private Set<Dude> dudes;
	private Module mainModule, rocketPlaceholderModule;
	private Map<Resource, GenericRapper<Float>> resources;
	private Map<Resource, GenericRapper<Float>> resourceMax;
	private Map<Resource, GenericRapper<Float>> delivery;
	private Queue<Task> taskQueue;
	private Queue<String> messageQueue;
	private Event currentEvent;
	private float time;
	private boolean newDay, deliveryTime, worldWarStarted;

	private SpaceStation() {
		singleton = this;
		modules = new HashSet<Module>();
		dudes = new HashSet<Dude>();
		resources = new HashMap<Resource, GenericRapper<Float>>();
		resourceMax = new HashMap<Resource, GenericRapper<Float>>();
		delivery = new HashMap<Resource, GenericRapper<Float>>();
		taskQueue = new Queue<Task>();
		messageQueue = new Queue<String>();
		init();
	}

	private void init(){
		time = 0;
		currentEvent = null;
		newDay = false;
		deliveryTime = false;
		worldWarStarted = false;

		modules.clear();
		dudes.clear();
		resources.clear();
		resourceMax.clear();
		delivery.clear();
		taskQueue.clear();
		messageQueue.clear();

		for (Resource resource : Resource.values()) {
			resources.put(resource, new GenericRapper<Float>(0.f));
			resourceMax.put(resource, new GenericRapper<Float>(0.f));
			delivery.put(resource, new GenericRapper<Float>(0.f));
		}
		resources.get(Resource.H2).value = 0.f;
		resources.get(Resource.O2).value = 360.f;
		resources.get(Resource.H2O).value = 300.f;
		resources.get(Resource.Fe).value = 600.f;
		resources.get(Resource.Si).value = 100.f;
		resources.get(Resource.Electricity).value = 300.f;
		resources.get(Resource.Food).value = 160.f;

		mainModule = ModuleFactory.createModule(MainModule.class, 0, 0);
		mainModule.setBuildProgress(1);
		mainModule.setFinished(true);
		mainModule.setJustFinished(true);
		addModule(mainModule);
		rocketPlaceholderModule = ModuleFactory.createModule(RocketPlaceholderModule.class, 0, 2);
		rocketPlaceholderModule.setBuildProgress(1);
		rocketPlaceholderModule.setFinished(true);
		addModule(rocketPlaceholderModule);

		addDude(Dude.getRandomDude(1.5f, 1.5f));
		addDude(Dude.getRandomDude(1.5f, 0.5f));
	}

	public void addModule(Module module) {
		modules.add(module);
	}

	public void removeModule(Module module) {
		modules.remove(module);
		module.adjustResourceMax(resourceMax, false);
	}

	public void addDude(Dude dude) {
		dudes.add(dude);
		resources.get(Resource.Humans).value += 1.f;
	}

	public void removeDude(Dude dude) {
		dudes.remove(dude);
		resources.get(Resource.Humans).value -= 1.f;
	}

	public void addTask(Task task) {
		taskQueue.addLast(task);
	}

	public Task popTask(){
		if (taskQueue.size == 0) {
			return null;
		}
		return taskQueue.removeFirst();
	}

	public Task peekTask(){
		if (taskQueue.size == 0) {
			return null;
		}
		return taskQueue.first();
	}

	public void addMessage(String message) {
		System.out.println(message);
		messageQueue.addLast(message);
	}

	public String popMessage(){
		if (messageQueue.size == 0) {
			return null;
		}
		return messageQueue.removeFirst();
	}

	public String peekMessage(){
		if (messageQueue.size == 0) {
			return null;
		}
		return messageQueue.first();
	}

	public Map<Resource, GenericRapper<Float>> getResources() {
		return resources;
	}

	public Map<Resource, GenericRapper<Float>> getResourceMax() {
		return resourceMax;
	}

	public float getResource(Resource resource) {
		return resources.get(resource).value;
	}

	public float getResourceMax(Resource resource) {
		return resourceMax.get(resource).value;
	}

	public Map<Resource, GenericRapper<Float>> getDelivery() {
		return delivery;
	}

	public boolean legalDelivery(){
		if (delivery.get(Resource.Humans).value > 0.01f &&
				delivery.get(Resource.Humans).value + resources.get(Resource.Humans).value
						> resourceMax.get(Resource.Humans).value) {
			return false;
		}
		return getDeliveryCost() <= Consts.DELIVERY_LIMIT;
	}

	public float getDeliveryCost(){
		float sum = 0;
		for (Resource resource : Resource.values()) {
			if (delivery.get(resource).value < 0) {
				return 2 * Consts.DELIVERY_LIMIT;
			}
			sum += delivery.get(resource).value * resource.deliveryCost;
		}
		return sum;
	}

	public boolean buyModule(Class<? extends Module> clazz, boolean tryOnly) {
		if (!tryOnly) {
			if (!buyModule(clazz, true)) {
				return false;
			}
		}
		Map<Resource, Float> moduleBuildCost = ModuleFactory.getModuleBuildCost(clazz);
		for (Resource resource : moduleBuildCost.keySet()) {
			if (moduleBuildCost.get(resource) > resources.get(resource).value) {
				return false;
			}
			if (!tryOnly) {
				resources.get(resource).value -= moduleBuildCost.get(resource);
			}
		}
		return true;
	}

	public Set<Module> getModules() {
		return modules;
	}

	public Set<Dude> getDudes() {
		return dudes;
	}

	public Module getMainModule() {
		return mainModule;
	}

	public Module getRocketPlaceholderModule() {
		return rocketPlaceholderModule;
	}

	public Module getModule(int x, int y){
		for(Module module : modules){
			if (module.getModuleLocation().isPointInModule(x, y)) {
				return module;
			}
		}
		return null;
	}

	public float getTime() {
		return time;
	}

	public float getTimeOfDay(){
		return time % Consts.DURATION_DAY;
	}

	public int getDay(){
		return (int) (time / Consts.DURATION_DAY);
	}

	public boolean isNewDay() {
		return newDay;
	}

	public boolean isDeliveryTime() {
		return deliveryTime;
	}

	public Event getCurrentEvent() {
		return currentEvent;
	}

	public boolean isSolarFlare(){
		return getCurrentEvent() != null && getCurrentEvent() instanceof SolarFlareEvent;
	}

	public boolean isEclipse(){
		return getCurrentEvent() != null && getCurrentEvent() instanceof EclipseEvent;
	}

	public boolean isLaunchFailure(){
		return getCurrentEvent() != null && getCurrentEvent() instanceof LaunchFailureEvent;
	}

	public boolean isWorldWarToday(){
		return getCurrentEvent() != null && getCurrentEvent() instanceof WorldWarEvent;
	}

	public boolean isWorldWarStarted() {
		return worldWarStarted;
	}

	private void updateEvents(float delta) {
		if (currentEvent == null && isNewDay()) {
			currentEvent = Event.getRandomEvent();
			if (currentEvent != null) {
				currentEvent.startEvent();
				addMessage(currentEvent.getDescription());
			}
		}
		if (currentEvent != null) {
			currentEvent.update(delta);
			if (currentEvent.isFinished()) {
				currentEvent = null;
			}
		}
	}

	private void clampResources(){
		for (Resource resource : Resource.values()) {
			if (resource == Resource.Humans) {
				continue;
			}
			if (resources.get(resource).value > resourceMax.get(resource).value) {
				resources.get(resource).value = resourceMax.get(resource).value;
			}
			if (resources.get(resource).value < 0) {
				resources.get(resource).value = 0.f;
			}
		}
	}

	private void acceptDelivery(){
		if(!legalDelivery() || getDeliveryCost() < 0.01f){
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("A delivery arrived with ");
		for (Resource resource : Resource.values()) {
			if(delivery.get(resource).value < 0.01f){
				continue;
			}
			if(resource != Resource.Humans) {
				resources.get(resource).value += delivery.get(resource).value;
			}
			sb.append(delivery.get(resource).value.intValue());
			sb.append(" ");
			sb.append(resource.displayName);
			sb.append(", ");
		}
		sb.replace(sb.length - 2, sb.length, ".");
		addMessage(sb.toString());
		for(int i = 0; i < delivery.get(Resource.Humans).value.intValue(); i++) {
			addDude(Dude.getRandomDude(1.5f, 1.5f));
		}
		delivery.get(Resource.Humans).value = 0.f;
	}

	public void update(float delta){
		newDay = (getTimeOfDay() + delta > Consts.DURATION_DAY);
		deliveryTime = getTimeOfDay() < Consts.DELIVERY_TIME && getTimeOfDay() + delta >= Consts.DELIVERY_TIME;

		time += delta;

		if (isDeliveryTime() && !isLaunchFailure()) {
			acceptDelivery();
		}

		updateEvents(delta);

		for (Module module : modules) {
			if (module.isJustFinished()) {
				module.adjustResourceMax(resourceMax, true);
				module.setJustFinished(false);
			}
			if(module.isWorking()) {
				module.doInputOutputProcessing(resources, delta);
			}
		}

		clampResources();

		for (Dude dude : new HashSet<Dude>(dudes)) {
			dude.doInputOutputProcessing(resources, delta);
			dude.update(delta);
		}
	}
}
