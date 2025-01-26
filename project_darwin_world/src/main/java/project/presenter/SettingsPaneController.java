package project.presenter;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import project.model.map.WorldMap;
import project.model.movement.Vector2d;
import project.model.simulation.SimulationEvent;
import project.model.util.AbstractSubscribable;
import project.model.worldelements.Animal;
import project.model.worldelements.AnimalStatistics;
import project.model.worldelements.AnimalStatisticsListener;

import java.util.Iterator;
import java.util.Optional;
import java.util.Vector;

public class SettingsPaneController extends AbstractController implements MapDrawerListener, AnimalStatisticsListener {
    @FXML
    private Label trackedPosition;
    @FXML
    private Label trackedDescendantsCount;
    @FXML
    private Label trackedDaysAlive;
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
    private static final String NO_TRACKED_STATISTICS_ERROR =
            "SettingsPaneController.statisticsChanged() but no statistics are tracked!!!";
    private BooleanProperty isPaused;
    private boolean choosingAnimalToTrack = false;
    private Optional<AnimalStatistics> trackedAnimalStatistics = Optional.empty();

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
            this.resumeSimulation();
        } else {
            this.pauseSimulation();
        }

        this.isPaused.set(!this.isPaused.get());
    }

    private void resumeSimulation() {
        this.simulation.resume();
        this.pauseResumeButton.setText(PAUSE_BUTTON_TEXT);
        this.choosingAnimalToTrack = false;
    }

    private void pauseSimulation() {
        this.pauseResumeButton.setText(RESUME_BUTTON_TEXT);
        this.simulation.pause();
    }

    @FXML
    public void onTrackAnimalButtonClick(ActionEvent event) {
        this.choosingAnimalToTrack = true;
    }

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {

    }

    private Optional<Animal> pickAnimalToTrack(Vector2d mapPosition) {
        WorldMap worldMap = this.simulation.getWorldMap();
        Optional<Animal> potentiallyTrackedAnimal = worldMap.animalsAt(mapPosition)
                .filter(set -> !set.isEmpty())
                .flatMap(set -> {
                    Iterator<Animal> iterator = set.iterator();
                    return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
                });

        return potentiallyTrackedAnimal;
    }

    @Override
    public void mapFieldClicked(Vector2d mapPosition) {
        if (!this.choosingAnimalToTrack) {
            return;
        }

        Optional<Animal> potentiallyTrackedAnimal = this.pickAnimalToTrack(mapPosition);

        potentiallyTrackedAnimal.ifPresent(animal -> {
            // unsubscribe currently tracked statistics
            this.trackedAnimalStatistics.ifPresent(statistics -> statistics.unsubscribe(this));

            // subscribe to chosen animal statistics
            AnimalStatistics newTrackedAnimalStatistics = animal.getStatistics();
            this.trackedAnimalStatistics = Optional.of(newTrackedAnimalStatistics);
            newTrackedAnimalStatistics.subscribe(this);
        });
    }

    @Override
    public void statisticsChanged() {
        if (this.trackedAnimalStatistics.isEmpty()) {
            throw new IllegalStateException(NO_TRACKED_STATISTICS_ERROR);
        }

        Platform.runLater(this::updateStatistics);
    }

    private void updateStatistics() {
        if (this.trackedAnimalStatistics.isEmpty()) {
            return;
        }

        AnimalStatistics statistics = this.trackedAnimalStatistics.get();

        this.trackedPosition.setText(statistics.getPosition().toString());
        this.trackedGenome.setText(statistics.getGenesList().toString());
        this.trackedEnergy.setText(Integer.toString(statistics.getEnergy()));
        this.trackedEatenPlants.setText(Integer.toString(statistics.getEatenPlants()));
        this.trackedChildrenCount.setText(Integer.toString(statistics.getChildrenCount()));
        this.trackedDaysAlive.setText(Integer.toString(statistics.getDaysAlive()));
        statistics.getDeathDay().ifPresent(
                deathDay -> this.trackedDeathDay.setText(deathDay.toString())
        );
    }
}
