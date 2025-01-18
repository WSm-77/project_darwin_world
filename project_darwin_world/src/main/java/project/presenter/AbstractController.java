package project.presenter;

import project.model.simulation.Simulation;
import project.model.simulation.SimulationListener;

public abstract class AbstractController implements SimulationListener {
    protected Simulation simulation;
    protected Thread simulationThread;

    public void setSimulationThread(Thread simulationThread) {
        this.simulationThread = simulationThread;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }
}
