package project.model.util;

import project.model.map.WorldMap;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap, String message);
}
