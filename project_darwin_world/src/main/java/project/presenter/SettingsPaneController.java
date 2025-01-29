package project.presenter;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import project.model.map.WorldMap;
import project.model.movement.Vector2d;
import project.model.simulation.SimulationEvent;
import project.model.simulation.SimulationStatistics;
import project.model.util.PlantGrower;
import project.model.worldelements.Animal;
import project.model.worldelements.AnimalStatistics;
import project.model.worldelements.AnimalStatisticsListener;
import project.model.worldelements.WorldElement;

import java.util.*;
import java.util.stream.Collectors;

public class SettingsPaneController extends AbstractController implements MapDrawerListener, AnimalStatisticsListener {
    @FXML
    private Button showMostPopularGenome;
    @FXML
    private Button hideMostPopularGenome;
    @FXML
    private Button showPreferredPlantsPositions;
    @FXML
    private Button hidePreferredPlantsPositions;
    @FXML
    private Label trackedOrientation;
    @FXML
    private Button untrackAniamlButton;
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
    private static final String NO_INFORMATION_STRING = "-";
    private static final Color TRACKED_ANIMAL_HIGHLIGHT_COLOR = new Color(1.0, 1.0, 0.0, 0.7);
    private static final Color MOST_POPULAR_GENOME_HIGHLIGHT_COLOR = new Color(0.51, 0.0, 0.51, 0.5);
    private BooleanProperty isPaused;
    private boolean choosingAnimalToTrack = false;
    private BooleanProperty isTrackingAnimal;
    private BooleanProperty isHighlightingPreferredPlantsPositions;
    private BooleanProperty isHighlightingMostPopularGenome;
    private Optional<AnimalStatistics> trackedAnimalStatistics = Optional.empty();
    private MapDrawer mapDrawer;

    @FXML
    public void initialize() {
        // Add a listener to the slider's value property
        this.refreshTimeSlider.valueProperty().addListener(event -> {
            this.simulation.setSimulationRefreshTime((int) this.refreshTimeSlider.getValue());
        });

        this.isPaused = new SimpleBooleanProperty(false);
        this.isTrackingAnimal = new SimpleBooleanProperty(false);
        this.isHighlightingPreferredPlantsPositions = new SimpleBooleanProperty(false);
        this.isHighlightingMostPopularGenome = new SimpleBooleanProperty(false);
        this.trackAniamlButton.disableProperty().bind(this.isPaused.not());
        this.untrackAniamlButton.disableProperty().bind(this.isTrackingAnimal.not());
        this.showPreferredPlantsPositions.disableProperty().bind(
                this.isHighlightingPreferredPlantsPositions.or(this.isPaused.not()));
        this.hidePreferredPlantsPositions.disableProperty().bind(this.isHighlightingPreferredPlantsPositions.not());
        this.showMostPopularGenome.disableProperty().bind(
                this.isHighlightingMostPopularGenome.or(this.isPaused.not()));
        this.hideMostPopularGenome.disableProperty().bind(this.isHighlightingMostPopularGenome.not());
    }

    public void setMapDrawer(MapDrawer mapDrawer) {
        this.mapDrawer = mapDrawer;
        this.mapDrawer.subscribe(this);
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
        this.isHighlightingPreferredPlantsPositions.set(false);
        this.isHighlightingMostPopularGenome.set(false);
    }

    private void pauseSimulation() {
        this.pauseResumeButton.setText(RESUME_BUTTON_TEXT);
        this.simulation.pause();
    }

    @FXML
    public void onTrackAnimalButtonClick(ActionEvent event) {
        this.choosingAnimalToTrack = true;
    }

    @FXML
    public void onUntrackAnimalButtonClick(ActionEvent event) {
        this.isTrackingAnimal.set(false);
        this.unsubscribeStatistics();
        this.clearStatistics();
    }

    private void unsubscribeStatistics() {
        this.trackedAnimalStatistics.ifPresent(statistics -> statistics.unsubscribe(this));
    }

    private void clearStatistics() {
        this.trackedPosition.setText(NO_INFORMATION_STRING);
        this.trackedOrientation.setText(NO_INFORMATION_STRING);
        this.trackedGenome.setText(NO_INFORMATION_STRING);
        this.trackedEnergy.setText(NO_INFORMATION_STRING);
        this.trackedEatenPlants.setText(NO_INFORMATION_STRING);
        this.trackedChildrenCount.setText(NO_INFORMATION_STRING);
        this.trackedDescendantsCount.setText(NO_INFORMATION_STRING);
        this.trackedDaysAlive.setText(NO_INFORMATION_STRING);
        this.trackedDeathDay.setText(NO_INFORMATION_STRING);
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
            this.unsubscribeStatistics();

            // subscribe to chosen animal statistics
            AnimalStatistics newTrackedAnimalStatistics = animal.getStatistics();
            this.trackedAnimalStatistics = Optional.of(newTrackedAnimalStatistics);
            newTrackedAnimalStatistics.subscribe(this);

            this.updateStatistics();
            this.isTrackingAnimal.set(true);
        });
    }

    private void highlightTrackedAnimal() {
        this.trackedAnimalStatistics.ifPresent(statistics ->  {
            // highlight animal only if it is still alive
            if (statistics.getDeathDay().isEmpty()) {
                this.mapDrawer.highlightPositions(
                        List.of(statistics.getPosition()),
                        TRACKED_ANIMAL_HIGHLIGHT_COLOR
                );
            }
        });
    }

    private void highlightPreferredPlantsPositions() {
        PlantGrower plantGrower = this.simulation.getPlantGrower();

        Map<Vector2d, Double> preferencesMap = plantGrower.getPreferencesMap();
        if (preferencesMap.isEmpty()) {
            return;
        }

        Double max = preferencesMap.values().stream()
                .max(Double::compareTo)
                .orElse(0.0);

        List<Vector2d> positionsToHighlight = preferencesMap.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), max))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        this.mapDrawer.highlightPositions(positionsToHighlight);
    }

    private void highlightMostPopularGenome() {
        SimulationStatistics simulationStatistics = this.simulation.getStatistics();
        Optional<List<Integer>> mostPopularGenome = simulationStatistics.mostPopularGenome();

        if (mostPopularGenome.isEmpty()) {
            return;
        }

        WorldMap worldMap = this.simulation.getWorldMap();

        List<Vector2d> positionsToHighlight = worldMap.getAnimals().stream()
                .filter(animal -> Objects.equals(animal.getStatistics().getGenesList(), mostPopularGenome.get()))
                .map(WorldElement::getPosition)
                .distinct()
                .collect(Collectors.toList());

        this.mapDrawer.highlightPositions(positionsToHighlight, MOST_POPULAR_GENOME_HIGHLIGHT_COLOR);
    }

    @Override
    public void mapDrawn() {
        if (this.isTrackingAnimal.get()) {
            Platform.runLater(this::highlightTrackedAnimal);
        }

        if (this.isHighlightingPreferredPlantsPositions.get()) {
            Platform.runLater(this::highlightPreferredPlantsPositions);
        }

        if (this.isHighlightingMostPopularGenome.get()) {
            Platform.runLater(this::highlightMostPopularGenome);
        }
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
        this.trackedOrientation.setText(statistics.getOrientation().toString());
        this.trackedGenome.setText(statistics.getGenome().toString());
        this.trackedEnergy.setText(Integer.toString(statistics.getEnergy()));
        this.trackedEatenPlants.setText(Integer.toString(statistics.getEatenPlants()));
        this.trackedChildrenCount.setText(Integer.toString(statistics.getChildrenCount()));
        this.trackedDescendantsCount.setText(Integer.toString(statistics.getDescendantsCount()));
        this.trackedDaysAlive.setText(Integer.toString(statistics.getDaysAlive()));
        statistics.getDeathDay().ifPresent(
                deathDay -> this.trackedDeathDay.setText(deathDay.toString())
        );
    }

    public void onShowPreferredPlantsPositionsClick(ActionEvent actionEvent) {
        this.isHighlightingPreferredPlantsPositions.set(true);

        Platform.runLater(this::highlightPreferredPlantsPositions);
    }

    public void onHidePreferredPlantsPositionsClick(ActionEvent actionEvent) {
        this.isHighlightingPreferredPlantsPositions.set(false);

        Platform.runLater(this.mapDrawer::drawMap);
    }

    public void onShowMostPopularGenomeClick(ActionEvent actionEvent) {
        this.isHighlightingMostPopularGenome.set(true);

        Platform.runLater(this::highlightMostPopularGenome);
    }

    public void onHideMostPopularGenomeClick(ActionEvent actionEvent) {
        this.isHighlightingMostPopularGenome.set(false);

        Platform.runLater(this.mapDrawer::drawMap);
    }
}
