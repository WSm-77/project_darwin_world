package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import project.model.map.WorldMap;
import project.model.movement.Vector2d;
import project.model.simulation.SimulationEvent;
import project.model.worldelements.Animal;
import project.model.worldelements.WorldElement;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChartPaneController extends AbstractController {
    @FXML
    private Label mostPopularGenomeLabel;
    @FXML
    private Label numberOfEmptySpotsLabel;
    @FXML
    private BarChart<String, Double> simulationStatisticChart;

    private static final String SIMULATION_DAY_TEMPLATE = "Simulation Day: %d";
    private static final String EMPTY_SPOTS_TEMPLATE = "Number of empty spots: %d";
    private static final String MOST_POPULAR_GENOME = "Most popular genome: %s";
    private static final String NO_INFORMATION_STRING = "-";

    private XYChart.Series<String, Double> animalsCountSeries = new XYChart.Series<>();
    private XYChart.Series<String, Double> plantsCountSeries = new XYChart.Series<>();
    private XYChart.Series<String, Double> averageAnimalsEnergy = new XYChart.Series<>();
    private XYChart.Series<String, Double> averageAnimalsLifeTime = new XYChart.Series<>();
    private XYChart.Series<String, Double> averageAnimalsChilderenCount = new XYChart.Series<>();

    @FXML
    public void initialize() {
        Axis<String> xAxis = this.simulationStatisticChart.getXAxis();
        Axis<Double> yAxis = this.simulationStatisticChart.getYAxis();

        xAxis.setLabel("Simulation statistics");
        yAxis.setLabel("Values");

        XYChart.Data<String, Double> animalsCountChartData = new XYChart.Data<>("Animals count", 0.0);
        XYChart.Data<String, Double> plantsCountChartData = new XYChart.Data<>("Plants count", 0.0);
        XYChart.Data<String, Double> averageAnimalsEnergyChartData = new XYChart.Data<>("Average animal energy", 0.0);
        XYChart.Data<String, Double> averageAnimalLifeTimeChartData = new XYChart.Data<>("Average animal life time", 0.0);
        XYChart.Data<String, Double> averageAnimalChildrenCountChartData = new XYChart.Data<>("Average animal children count", 0.0);
        this.animalsCountSeries.getData().add(animalsCountChartData);
        this.plantsCountSeries.getData().add(plantsCountChartData);
        this.averageAnimalsEnergy.getData().add(averageAnimalsEnergyChartData);
        this.averageAnimalsLifeTime.getData().add(averageAnimalLifeTimeChartData);
        this.averageAnimalsChilderenCount.getData().add(averageAnimalChildrenCountChartData);

        this.simulationStatisticChart.getData().addAll(
                animalsCountSeries,
                plantsCountSeries,
                averageAnimalsEnergy,
                averageAnimalsLifeTime,
                averageAnimalsChilderenCount
        );
    }

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {
        switch (simulationEvent) {
            case MAP_CHANGED -> Platform.runLater(this::updateMapInformation);
            case NEXT_DAY -> Platform.runLater(this::updateDay);
        }

        Platform.runLater(this::updateEmptySpots);
    }

    private void updateEmptySpots() {
        String emptySpotsText = String.format(EMPTY_SPOTS_TEMPLATE, this.calculateNumberOfEmptySpots());
        this.numberOfEmptySpotsLabel.setText(emptySpotsText);
    }

    private void updateDay() {
        String simulationDay = String.format(SIMULATION_DAY_TEMPLATE, this.simulation.getDay());
        this.simulationStatisticChart.setTitle(simulationDay);
    }

    private void updateMapInformation() {
        this.updateCharts();
        this.updateEmptySpots();
        this.updateMostPopularGenome();
    }

    private void updateMostPopularGenome() {
        Optional<List<Integer>> mostPopularOptionalGenesList = this.mostPopularGenome();
        String genomeString = mostPopularOptionalGenesList
                .map(Objects::toString)
                .orElse(NO_INFORMATION_STRING);

        String mostPopularGenomeText = String.format(MOST_POPULAR_GENOME, genomeString);
        this.mostPopularGenomeLabel.setText(mostPopularGenomeText);
    }

    private void updateCharts() {
        this.updateSeries(this.animalsCountSeries, (double) this.getAnimalsCount());
        this.updateSeries(this.plantsCountSeries, (double) this.getPlantsCount());
        this.updateSeries(this.averageAnimalsEnergy, this.calculateAverageEnergy());
        this.updateSeries(this.averageAnimalsLifeTime, this.calculateAverageLifeTime());
        this.updateSeries(this.averageAnimalsChilderenCount, this.calculateAverageAnimalChildrenCount());
    }

    private void updateSeries(XYChart.Series<String, Double> series, Double value) {
        var data = series.getData().getFirst();
        data.setYValue(value);
    }

    private int getAnimalsCount() {
        var worldMap = this.simulation.getWorldMap();
        var animalsList = worldMap.getAnimals();

        return  animalsList.size();
    }

    private int getPlantsCount() {
        var worldMap = this.simulation.getWorldMap();
        var plantsList = worldMap.getPlants();

        return plantsList.size();
    }

    private int calculateNumberOfEmptySpots() {
        var worldMap = this.simulation.getWorldMap();

        var plantsList = worldMap.getPlants();
        var animalsList = worldMap.getAnimals();

        Stream<Vector2d> plantsPositionsStream = plantsList.stream().map(WorldElement::getPosition);
        Stream<Vector2d> animalsPositionsStream = animalsList.stream().map(WorldElement::getPosition);
        Set<Vector2d> occupiedPositions = Stream.concat(plantsPositionsStream, animalsPositionsStream)
                .collect(Collectors.toSet());

        int totalSpots = worldMap.getWidth() * worldMap.getHeight();

        return totalSpots - occupiedPositions.size();
    }

    private double calculateAverageEnergy() {
        WorldMap worldMap = this.simulation.getWorldMap();
        List<Animal> animals = worldMap.getAnimals();

        return animals.stream()
                .mapToDouble(animal -> animal.getStatistics().getEnergy())
                .average()
                .orElse(0.0);
    }

    private double calculateAverageLifeTime() {
        List<Animal> deadAnimals = this.simulation.getDeadAnimals();

        return deadAnimals.stream()
                .mapToDouble(animal -> (double) animal.getStatistics().getDaysAlive())
                .average()
                .orElse(0.0);
    }

    private double calculateAverageAnimalChildrenCount() {
        WorldMap worldMap = this.simulation.getWorldMap();
        List<Animal> animals = worldMap.getAnimals();

        return animals.stream()
                .mapToDouble(animal -> animal.getStatistics().getChildrenCount())
                .average()
                .orElse(0.0);
    }

    private Optional<List<Integer>> mostPopularGenome() {
        WorldMap worldMap = this.simulation.getWorldMap();
        List<Animal> aliveAnimals = worldMap.getAnimals();

        HashMap<List<Integer>, Integer> genomeCountMap = new HashMap<>();
        for (var animal : aliveAnimals) {
            List<Integer> genesList = animal.getStatistics().getGenesList();
            Integer genomeCount = genomeCountMap.getOrDefault(genesList, 0) + 1;
            genomeCountMap.put(genesList, genomeCount);
        }

        return genomeCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }
}
