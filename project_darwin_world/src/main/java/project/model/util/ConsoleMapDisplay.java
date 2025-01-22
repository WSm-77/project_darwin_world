package project.model.util;

import project.model.map.MapBuffer;
import project.model.map.MapEvent;
import project.model.map.WorldMap;
import project.model.worldelements.Animal;

import java.util.Optional;

public class ConsoleMapDisplay implements MapChangeListener {
    private final static String MOVE_MESSAGE_TEMPLATE = "Animal movement:\norientation: %s -> %s\nposition: %s -> %s";
    private final static String ANIMAL_PLACE_MESSAGE_TEMPLATE = """
            Animal placed:
            position: %s
            orientation: %s
            """;
    private final static String PLANT_GROWN_MESSAGE_TEMPLATE = """
            Plant grown:
            position: %s
            """;
    private final static String PLANT_REMOVED_MESSAGE_TEMPLATE = "Plant removed from %s";
    private final static String ANIMAL_REMOVED_MESSAGE_TEMPLATE = "Animal removed from %s";
    public static final String MAP_COUNTER_CHANGE_MESSAGE_TEMPLATE = "%d. Change:\n%s";
    public static final String DISPLAY_MAP_MESSAGE_TEMPLATE = "Displaying map: %s";
    private int changesCounter = 0;

    private String createMapCounterChangeMessage(String message) {
        return String.format(ConsoleMapDisplay.MAP_COUNTER_CHANGE_MESSAGE_TEMPLATE, this.changesCounter, message);
    }
    private String createDisplayMapMessage(WorldMap worldMap) {
        return String.format(ConsoleMapDisplay.DISPLAY_MAP_MESSAGE_TEMPLATE, worldMap.getId());
    }

    private String createMapChangeMessage(WorldMap worldMap, MapEvent event) {
        return "";
    }

    @Override
    public synchronized void mapChanged(WorldMap worldMap, MapEvent event) {
        this.changesCounter++;
        System.out.println(this.createMapCounterChangeMessage(this.createMapChangeMessage(worldMap, event)));
        System.out.println(this.createDisplayMapMessage(worldMap));
        System.out.println(worldMap);
    }
}
