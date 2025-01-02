package project.model.util;

import project.model.map.WorldMap;

public class ConsoleMapDisplay implements MapChangeListener {
    public static final String MAP_COUNTER_CHANGE_MESSAGE_TEMPLATE = "%d. Change: %s";
    public static final String DISPLAY_MAP_MESSAGE_TEMPLATE = "Displaying map: %s";
    private int changesCounter = 0;

    private String createMapCounterChangeMessage(String message) {
        return String.format(ConsoleMapDisplay.MAP_COUNTER_CHANGE_MESSAGE_TEMPLATE, this.changesCounter, message);
    }
    private String createDisplayMapMessage(WorldMap worldMap) {
        return String.format(ConsoleMapDisplay.DISPLAY_MAP_MESSAGE_TEMPLATE, worldMap.getId());
    }

    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        this.changesCounter++;
        System.out.println(this.createMapCounterChangeMessage(message));
        System.out.println(this.createDisplayMapMessage(worldMap));
        System.out.println(worldMap);
    }
}
