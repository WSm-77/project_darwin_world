package project.model.simulation;

import project.model.map.WorldMap;
import project.model.util.MapChangeListener;
import project.model.util.SimulationBuilder;

public class SimulationEachChangeStep extends AbstractSimulation implements MapChangeListener {
    public SimulationEachChangeStep(SimulationBuilder simulationBuilder) {
        super(simulationBuilder);
        this.worldMap.subscribe(this);
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        this.notifyListeners(SimulationEvent.MAP_CHANGED);
    }
}
