package project.model.util;

import project.model.map.MapEvent;
import project.model.map.WorldMap;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap, MapEvent event);
}
