package project.presenter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import project.model.movement.Vector2d;
import project.model.simulation.SimulationEvent;
import project.model.util.AbstractSubscribable;

public class SettingsPaneController extends AbstractController implements MapDrawerListener {
    @FXML
    private Button trackAniamlButton;
    @FXML
    private Label trackedGenome;
    @FXML
    private Label trackedEnergy;
    @FXML
    private Label trackedEatenPlants;
    @FXML
    private Label trackedChildrenCount;
    @FXML
    private Label trackedDeathDay;
    @FXML
    private Button pauseResumeButton;
    @FXML
    private Slider refreshTimeSlider;

    private static final String PAUSE_BUTTON_TEXT = "Pause";
    private static final String RESUME_BUTTON_TEXT = "Resume";
    private BooleanProperty isPaused;

    @FXML
    public void initialize() {
        // Add a listener to the slider's value property
        this.refreshTimeSlider.valueProperty().addListener(event -> {
            this.simulation.setSimulationRefreshTime((int) this.refreshTimeSlider.getValue());
        });

        this.isPaused = new SimpleBooleanProperty(false);
        this.trackAniamlButton.disableProperty().bind(this.isPaused.not());
    }

    @FXML
    public void onPauseResumeButtonClick(ActionEvent event) {
        if (this.isPaused.get()) {
            this.simulation.resume();
            this.pauseResumeButton.setText(PAUSE_BUTTON_TEXT);
        } else {
            this.pauseResumeButton.setText(RESUME_BUTTON_TEXT);
            this.simulation.pause();
        }

        this.isPaused.set(!this.isPaused.get());
    }

    @FXML
    public void onTrackAnimalButtonClick(ActionEvent event) {

    }

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {

    }

    @Override
    public void mapFieldClicked(Vector2d mapPosition) {
        System.out.println("Received MapDrawer change status " + mapPosition);
    }
}
