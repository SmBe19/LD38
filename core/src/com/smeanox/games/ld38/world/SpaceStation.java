package com.smeanox.games.ld38.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.StringBuilder;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.world.event.EclipseEvent;
import com.smeanox.games.ld38.world.event.Event;
import com.smeanox.games.ld38.world.event.LaunchFailureEvent;
import com.smeanox.games.ld38.world.event.SolarFlareEvent;
import com.smeanox.games.ld38.world.event.WorldWarEvent;
import com.smeanox.games.ld38.world.module.Module;
import com.smeanox.games.ld38.world.module.ModuleFactory;
import com.smeanox.games.ld38.world.module.ModuleType;
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

	private MessageManager messageManager;
	private StoryManager storyManager;
	private Set<Module> modules;
	private Set<Dude> dudes;
	private Module mainModule, rocketPlaceholderModule;
	private Map<Resource, GenericRapper<Float>> resources;
	private Map<Resource, GenericRapper<Float>> resourceMax;
	private Map<Resource, GenericRapper<Float>> delivery, savedDelivery;
	private Queue<Task> taskQueue;
	private Queue<MessageManager.Message> messageQueue;
	private Event currentEvent;
	private Set<ModuleType> enabledModuleTypes;
	private float time;
	private boolean nightStart, newDay, endOfOrderTime, deliveryTime, worldWarStarted;
	private boolean savedDeliveryEmpty, contacetdEarth;
	private String gameOverMessage;

	private SpaceStation() {
		singleton = this;
		modules = new HashSet<Module>();
		dudes = new HashSet<Dude>();
		resources = new HashMap<Resource, GenericRapper<Float>>();
		resourceMax = new HashMap<Resource, GenericRapper<Float>>();
		delivery = new HashMap<Resource, GenericRapper<Float>>();
		savedDelivery = new HashMap<Resource, GenericRapper<Float>>();
		taskQueue = new Queue<Task>();
		messageQueue = new Queue<MessageManager.Message>();
		enabledModuleTypes = new HashSet<ModuleType>();
		init();
	}

	public void init(){
		time = 0;
		currentEvent = null;
		nightStart = false;
		newDay = false;
		deliveryTime = false;
		savedDeliveryEmpty = false;
		worldWarStarted = false;
		gameOverMessage = null;
		contacetdEarth = false;

		messageManager = new MessageManager();
		storyManager = new StoryManager();
		modules.clear();
		dudes.clear();
		resources.clear();
		resourceMax.clear();
		delivery.clear();
		savedDelivery.clear();
		taskQueue.clear();
		messageQueue.clear();
		enabledModuleTypes.clear();

		for (Resource resource : Resource.values()) {
			resources.put(resource, new GenericRapper<Float>(0.f));
			resourceMax.put(resource, new GenericRapper<Float>(0.f));
			delivery.put(resource, new GenericRapper<Float>(0.f));
			savedDelivery.put(resource, new GenericRapper<Float>(0.f));
		}
		resources.get(Resource.H2).value = 0.f;
		resources.get(Resource.O2).value = 360.f;
		resources.get(Resource.H2O).value = 300.f;
		resources.get(Resource.Fe).value = 600.f;
		resources.get(Resource.Si).value = 100.f;
		resources.get(Resource.Electricity).value = 300.f;
		resources.get(Resource.Food).value = 160.f;

		mainModule = ModuleFactory.createModule(ModuleType.MainModule, 0, 0);
		mainModule.setBuildProgress(1);
		mainModule.setFinished(true);
		mainModule.setJustFinished(true);
		addModule(mainModule);
		rocketPlaceholderModule = ModuleFactory.createModule(ModuleType.RocketPlaceholder, 0, 2);
		rocketPlaceholderModule.setBuildProgress(1);
		rocketPlaceholderModule.setFinished(true);
		addModule(rocketPlaceholderModule);

		addDude(Dude.getRandomDude(Consts.NEW_DUDE_POSITION_X, Consts.NEW_DUDE_POSITION_Y));
		addDude(Dude.getRandomDude(Consts.NEW_DUDE_POSITION_X, Consts.NEW_DUDE_POSITION_Y));

		addMessage(messageManager.intro());
	}

	public String getGameOverMessage() {
		return gameOverMessage;
	}

	public void setGameOverMessage(String gameOverMessage) {
		this.gameOverMessage = gameOverMessage;
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
		addMessage(new MessageManager.Message(message));
	}

	public void addMessage(MessageManager.Message message) {
		System.out.println(message.message);
		messageQueue.addLast(message);
	}

	public MessageManager.Message popMessage(){
		if (messageQueue.size == 0) {
			return null;
		}
		return messageQueue.removeFirst();
	}

	public MessageManager.Message peekMessage(){
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

	public Set<ModuleType> getEnabledModuleTypes() {
		return enabledModuleTypes;
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

	public boolean buyModule(ModuleType moduleType, boolean tryOnly) {
		if (!tryOnly) {
			if (!buyModule(moduleType, true)) {
				return false;
			}
		}
		Map<Resource, Float> moduleBuildCost = moduleType.buildCost;
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

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public StoryManager getStoryManager() {
		return storyManager;
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

	public Module getModule(float x, float y) {
		return getModule(MathUtils.floor(x), MathUtils.floor(y));
	}

	public Module getModule(int x, int y){
		for(Module module : modules){
			if (module.getModuleLocation().isPointInModule(x, y)) {
				return module;
			}
		}
		return null;
	}

	public Dude getDude(float x, float y) {
		return getDude(MathUtils.floor(x), MathUtils.floor(y));
	}

	public Dude getDude(int x, int y) {
		for (Dude dude : dudes) {
			if (dude.isVisible() && MathUtils.floor(dude.getX()) == x && MathUtils.floor(dude.getY()) == y) {
				return dude;
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

	public boolean isNightStart() {
		return nightStart;
	}

	public boolean isNewDay() {
		return newDay;
	}

	public boolean isDeliveryTime() {
		return deliveryTime;
	}

	public boolean isEndOfOrderTime() {
		return endOfOrderTime;
	}

	public boolean isSavedDeliveryEmpty() {
		return savedDeliveryEmpty;
	}

	public boolean isContacetdEarth() {
		return contacetdEarth;
	}

	public void setContacetdEarth(boolean contacetdEarth) {
		this.contacetdEarth = contacetdEarth;
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

	public void setWorldWarStarted(boolean worldWarStarted) {
		this.worldWarStarted = worldWarStarted;
	}

	private void updateEvents(float delta) {
		if (currentEvent == null && isNewDay()) {
			currentEvent = Event.getRandomEvent();
			if (currentEvent != null) {
				currentEvent.startEvent();
			}
			MessageManager.Message message = messageManager.dayStart(getDay(), currentEvent);
			if(message != null) {
				addMessage(message);
			}
		}
		if (currentEvent != null) {
			currentEvent.update(delta);
			if (currentEvent.isFinished()) {
				currentEvent = null;
			}
		}
	}

	public void clampResources(){
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
		if(isSavedDeliveryEmpty() || isWorldWarStarted()){
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("A delivery arrived with\n");
		for (Resource resource : Resource.values()) {
			if(savedDelivery.get(resource).value < 0.01f){
				continue;
			}
			if(resource != Resource.Humans) {
				resources.get(resource).value += delivery.get(resource).value;
			}
			sb.append(savedDelivery.get(resource).value.intValue());
			sb.append(" ");
			sb.append(resource.displayName);
			sb.append(",\n");
		}
		sb.replace(sb.length - 2, sb.length, ".");
		addMessage(sb.toString());
		for(int i = 0; i < savedDelivery.get(Resource.Humans).value.intValue(); i++) {
			addDude(Dude.getRandomDude(Consts.NEW_DUDE_POSITION_X, Consts.NEW_DUDE_POSITION_Y));
		}
		delivery.get(Resource.Humans).value = 0.f;
	}

	private void saveDelivery(){
		System.out.println("save delivery");
		savedDeliveryEmpty = true;
		if(!legalDelivery() || getDeliveryCost() < 0.01f){
			for (Resource resource : Resource.values()) {
				savedDelivery.get(resource).value = 0.f;
			}
			return;
		}
		for (Resource resource : Resource.values()) {
			savedDelivery.get(resource).value = delivery.get(resource).value;
			if (delivery.get(resource).value > 0.01f) {
				savedDeliveryEmpty = false;
			}
		}
	}

	public void update(float delta){
		newDay = (getTimeOfDay() + delta >= Consts.DURATION_DAY);
		if(newDay && getDay() == 0 && !storyManager.canGoToSecondDay()){
			time -= Consts.DURATION_DAY;
			newDay = false;
		}
		nightStart = getTimeOfDay() < Consts.DURATION_DAY - Consts.DURATION_NIGHT && getTimeOfDay() + delta >= Consts.DURATION_DAY - Consts.DURATION_NIGHT;
		deliveryTime = getTimeOfDay() < Consts.DELIVERY_TIME && getTimeOfDay() + delta >= Consts.DELIVERY_TIME;
		endOfOrderTime = getTimeOfDay() < Consts.ENDOFORDER_TIME && getTimeOfDay() + delta >= Consts.ENDOFORDER_TIME;

		time += delta;

		if (isEndOfOrderTime()) {
			saveDelivery();
		}
		if (isDeliveryTime() && !isLaunchFailure()) {
			acceptDelivery();
		}

		updateEvents(delta);

		storyManager.update(delta);

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

		if (dudes.size() == 0) {
			gameOverMessage = "All the astronauts died. Try again.";
		}
	}
}
