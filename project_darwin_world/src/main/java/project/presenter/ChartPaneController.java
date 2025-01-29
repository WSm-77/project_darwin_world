package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import project.model.simulation.SimulationEvent;
import project.model.simulation.SimulationListener;
import project.model.simulation.SimulationStatistics;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

public class ChartPaneController extends AbstractController implements SimulationListener {
    public static final String SIMULATION_STATISTICS = "Simulation statistics";
    public static final String VALUES = "Values";
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
    private static final String ANIMALS_COUNT_SERIES_NAME = "Animals count";
    private static final String PLANTS_COUNT_SERIES_NAME = "Plants count";
    private static final String AVERAGE_ANIMAL_ENERGY_SERIES_NAME = "Average animal energy";
    private static final String AVERAGE_ANIMAL_LIFE_TIME_SERIES_NAME = "Average animal life time";
    private static final String AVERAGE_ANIMAL_CHILDREN_COUNT_SERIES_NAME = "Average animal children count";

    private XYChart.Series<String, Double> animalsCountSeries = new XYChart.Series<>();
    private XYChart.Series<String, Double> plantsCountSeries = new XYChart.Series<>();
    private XYChart.Series<String, Double> averageAnimalsEnergy = new XYChart.Series<>();
    private XYChart.Series<String, Double> averageAnimalsLifeTime = new XYChart.Series<>();
    private XYChart.Series<String, Double> averageAnimalsChildrenCount = new XYChart.Series<>();

    @FXML
    public void initialize() {
        Axis<String> xAxis = this.simulationStatisticChart.getXAxis();
        Axis<Double> yAxis = this.simulationStatisticChart.getYAxis();

        xAxis.setLabel(SIMULATION_STATISTICS);
        yAxis.setLabel(VALUES);

        XYChart.Data<String, Double> animalsCountChartData = new XYChart.Data<>(ANIMALS_COUNT_SERIES_NAME, 0.0);
        XYChart.Data<String, Double> plantsCountChartData = new XYChart.Data<>(PLANTS_COUNT_SERIES_NAME, 0.0);
        XYChart.Data<String, Double> averageAnimalsEnergyChartData = new XYChart.Data<>(AVERAGE_ANIMAL_ENERGY_SERIES_NAME, 0.0);
        XYChart.Data<String, Double> averageAnimalLifeTimeChartData = new XYChart.Data<>(AVERAGE_ANIMAL_LIFE_TIME_SERIES_NAME, 0.0);
        XYChart.Data<String, Double> averageAnimalChildrenCountChartData = new XYChart.Data<>(AVERAGE_ANIMAL_CHILDREN_COUNT_SERIES_NAME, 0.0);

        this.animalsCountSeries.getData().add(animalsCountChartData);
        this.plantsCountSeries.getData().add(plantsCountChartData);
        this.averageAnimalsEnergy.getData().add(averageAnimalsEnergyChartData);
        this.averageAnimalsLifeTime.getData().add(averageAnimalLifeTimeChartData);
        this.averageAnimalsChildrenCount.getData().add(averageAnimalChildrenCountChartData);

        this.animalsCountSeries.setName(ANIMALS_COUNT_SERIES_NAME);
        this.plantsCountSeries.setName(PLANTS_COUNT_SERIES_NAME);
        this.averageAnimalsEnergy.setName(AVERAGE_ANIMAL_ENERGY_SERIES_NAME);
        this.averageAnimalsLifeTime.setName(AVERAGE_ANIMAL_LIFE_TIME_SERIES_NAME);
        this.averageAnimalsChildrenCount.setName(AVERAGE_ANIMAL_CHILDREN_COUNT_SERIES_NAME);

        this.simulationStatisticChart.getData().addAll(
                this.animalsCountSeries,
                this.plantsCountSeries,
                this.averageAnimalsEnergy,
                this.averageAnimalsLifeTime,
                this.averageAnimalsChildrenCount
        );
    }

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {
        switch (simulationEvent) {
            case MAP_CHANGED -> Platform.runLater(this::updateMapInformation);
            case NEXT_DAY -> Platform.runLater(this::updateDay);
        }
    }

    private void updateMapInformation() {
        this.updateCharts();
        this.updateEmptySpots();
        this.updateMostPopularGenome();
    }

    private void updateCharts() {
        SimulationStatistics statistics = this.simulation.getStatistics();

        this.updateSeries(animalsCountSeries, (double) statistics.getAnimalsCount());
        this.updateSeries(plantsCountSeries, (double) statistics.getPlantsCount());
        this.updateSeries(averageAnimalsEnergy, statistics.calculateAverageEnergy());
        this.updateSeries(averageAnimalsLifeTime, statistics.calculateAverageLifeTime());
        this.updateSeries(averageAnimalsChildrenCount, statistics.calculateAverageAnimalChildrenCount());
    }

    private void updateEmptySpots() {
        SimulationStatistics statistics = this.simulation.getStatistics();

        int emptySpots = statistics.calculateNumberOfEmptySpots();
        this.numberOfEmptySpotsLabel.setText(String.format(EMPTY_SPOTS_TEMPLATE, emptySpots));
    }

    private void updateMostPopularGenome() {
        SimulationStatistics statistics = this.simulation.getStatistics();

        Optional<List<Integer>> mostPopularOptionalGenesList = statistics.mostPopularGenome();
        String genomeString = mostPopularOptionalGenesList
                .map(Objects::toString)
                .orElse(NO_INFORMATION_STRING);

        String mostPopularGenomeText = String.format(MOST_POPULAR_GENOME, genomeString);
        this.mostPopularGenomeLabel.setText(mostPopularGenomeText);
    }

    private void updateDay() {
        String simulationDay = String.format(SIMULATION_DAY_TEMPLATE, this.simulation.getDay());
        this.simulationStatisticChart.setTitle(simulationDay);
    }

    private void updateSeries(XYChart.Series<String, Double> series, Double value) {
        var data = series.getData().getFirst();
        data.setYValue(value);
    }
}
