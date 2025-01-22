package project.presenter;

import project.model.map.Boundary;
import project.model.movement.Vector2d;
import project.model.simulation.SimulationEvent;
import project.model.worldelements.WorldElement;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChartPaneController extends AbstractController {
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
