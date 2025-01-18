package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import project.model.simulation.Simulation;
import project.model.simulation.SimulationEvent;
import project.model.simulation.SimulationListener;

public class MapPaneController extends AbstractController {
    @FXML
    private Label mapLabel;

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {
        Platform.runLater(() -> this.mapLabel.setText(this.simulation.getWorldMap().toString()));
        this.mapLabel.setText(this.simulation.getWorldMap().toString());
        System.out.println("Simulation changed mapController");
        System.out.println(this.simulation.getWorldMap().toString());
    }
}
