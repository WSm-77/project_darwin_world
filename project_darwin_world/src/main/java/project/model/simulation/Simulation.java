package project.model.simulation;

import project.model.map.WorldMap;
import project.model.worldelements.Animal;

import java.util.*;

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
