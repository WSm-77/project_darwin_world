package project.presenter;

import project.model.simulation.Simulation;
import project.model.simulation.SimulationListener;

public abstract class AbstractController implements SimulationListener {
    protected Simulation simulation;

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }
}
