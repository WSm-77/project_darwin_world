package project.model.simulation;

import project.model.movement.Vector2d;
import project.model.util.*;
import project.model.map.WorldMap;
import project.model.worldelements.Animal;
import project.model.worldelements.WorldElement;

import java.util.*;
import java.util.stream.Collectors;

public interface Simulation extends Runnable {
    WorldMap getWorldMap();
    int getDay();
    List<Animal> getDeadAnimals();
    void subscribe(SimulationListener simulationListener);
    void simulationStep();
    void setSimulationRefreshTime(int ms);
    void start();
    void terminate();
    void pause();
    void resume();
    boolean isPaused();
}
