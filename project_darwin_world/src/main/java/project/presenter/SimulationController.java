package project.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import project.model.simulation.Simulation;

public class SimulationController {
    public static final String PATH_TO_FXML_CONFIGURATION_FILE = "simulation.fxml";

    @FXML
    private Label mapLabel;

    private Simulation simulation;

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public void onClick(ActionEvent actionEvent) {
        this.mapLabel.setText(this.simulation.getWorldMap().getId().toString());
    }
}
