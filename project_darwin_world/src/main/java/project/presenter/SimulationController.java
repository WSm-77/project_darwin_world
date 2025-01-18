package project.presenter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import project.model.simulation.Simulation;
import project.model.simulation.SimulationEvent;
import project.model.simulation.SimulationListener;

public class SimulationController implements SimulationListener {
    public static final String PATH_TO_FXML_CONFIGURATION_FILE = "simulation.fxml";

    @FXML
    private Label mapLabel;

    private Simulation simulation;
    private Thread simulationThread;

    public void initializeSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.simulation.subscribe(this);

        this.simulationThread = new Thread(this.simulation);
        this.simulationThread.start();
    }

    public void onClick(ActionEvent actionEvent) {
        this.simulationThread.interrupt();
    }

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {
        Platform.runLater(() -> this.mapLabel.setText(this.simulation.getWorldMap().toString()));
    }
}
