package project.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import project.model.simulation.SimulationEvent;

public class SettingsPaneController extends AbstractController {
    @FXML
    public Button pauseResumeButton;

    private static final String PAUSE_BUTTON_TEXT = "Pause";
    private static final String RESUME_BUTTON_TEXT = "Resume";

    @FXML
    public void onClick(ActionEvent event) {
        if (this.simulation.isPaused()) {
            this.simulation.resume();
            this.pauseResumeButton.setText(PAUSE_BUTTON_TEXT);
        } else {
            this.pauseResumeButton.setText(RESUME_BUTTON_TEXT);
            this.simulation.pause();
        }
    }

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {

    }
}
