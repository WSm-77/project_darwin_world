package project.presenter;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import project.model.map.Boundary;
import project.model.movement.Vector2d;
import project.model.simulation.SimulationEvent;
import project.model.worldelements.WorldElement;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChartPaneController extends AbstractController {
    @FXML
    private BarChart<String, Double> simulationStatisticChart;
    private XYChart.Series<String, Double> animalsCountSeries = new XYChart.Series<>();
    private XYChart.Series<String, Double> plantsCountSeries = new XYChart.Series<>();

    @FXML
    public void initialize() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Simulation statistics");
        yAxis.setLabel("Values");

        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);

        yAxis.setAutoRanging(false);

        XYChart.Data<String, Double> animalsCountChartData = new XYChart.Data<>("Animals count", 10.0);
        XYChart.Data<String, Double> plantsCountChartData = new XYChart.Data<>("Plants count", 15.0);
        this.animalsCountSeries.getData().add(animalsCountChartData);
        this.plantsCountSeries.getData().add(plantsCountChartData);

        this.simulationStatisticChart.getData().addAll(animalsCountSeries, plantsCountSeries);
    }

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {

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
}
