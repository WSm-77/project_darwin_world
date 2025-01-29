package project.model.util;

import project.model.simulation.Simulation;

public interface SimulationConstructor<SimulationType extends Simulation> {
    public SimulationType construct(SimulationBuilder simulationBuilder);
}
