package project.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import project.model.simulation.SimulationEvent;

public class SettingsPaneController extends AbstractController {
    @FXML
    public Button pauseResumeButton;

    @FXML
    public void onClick(ActionEvent event) {
        System.out.println("SettingsControllerButton clicked");
        this.pauseResumeButton.setText("Resume");
    }

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {

    }
}
