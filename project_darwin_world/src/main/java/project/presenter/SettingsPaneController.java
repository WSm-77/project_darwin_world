package project.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import project.model.simulation.SimulationEvent;

public class SettingsPaneController extends AbstractController {
    @FXML
    private Button pauseResumeButton;
    @FXML
    private Slider refreshTimeSlider;

    private static final String PAUSE_BUTTON_TEXT = "Pause";
    private static final String RESUME_BUTTON_TEXT = "Resume";

    @FXML
    public void initialize() {
        // Add a listener to the slider's value property
        refreshTimeSlider.valueProperty().addListener(event -> {
            this.simulation.setSimulationRefreshTime((int) this.refreshTimeSlider.getValue());
        });
    }

    @FXML
    public void onPauseResumeButtonClick(ActionEvent event) {
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
