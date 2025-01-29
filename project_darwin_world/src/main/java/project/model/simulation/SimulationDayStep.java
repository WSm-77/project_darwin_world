package project.model.simulation;

import project.model.util.SimulationBuilder;

public class SimulationDayStep extends AbstractSimulation{
    public SimulationDayStep(SimulationBuilder simulationBuilder) {
        super(simulationBuilder);
    }

    @Override
    protected void finishDay() {
        this.notifyListeners(SimulationEvent.MAP_CHANGED);
        super.finishDay();

        this.simulationStep();
    }
}
