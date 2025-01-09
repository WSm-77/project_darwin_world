package project.model.util;

import project.model.map.Sphere;
import project.model.movement.Vector2d;
import project.model.worldelements.Grass;
import project.model.worldelements.Plant;

import java.util.*;
import java.util.stream.Collectors;

public class PlantGrowerStandardVariant {

    protected final Sphere worldMap;
    private final double randomValue = Math.random();
    private final int DEFAULT_NUTRITIOUSNESS = 5;

    public PlantGrowerStandardVariant(Sphere worldMap) {
        this.worldMap = worldMap;
    }

    private void growPlants(int number) {
        Set<Vector2d> occupiedPositions = worldMap.getAnimals().stream()
                .map(animal -> animal.getPosition())
                .collect(Collectors.toSet());

        List<Vector2d> positions = new ArrayList<>();
        Vector2d lowerLeft = worldMap.getCurrentBounds().lowerLeft();
        Vector2d upperRight = worldMap.getCurrentBounds().upperRight();

        for (int x = lowerLeft.getX(); x <= upperRight.getX(); x++) {
            for (int y = lowerLeft.getY(); y <= upperRight.getY(); y++) {
                positions.add(new Vector2d(x, y));
            }
        }

        positions.stream()
                .filter(position -> !occupiedPositions.contains(position))
                .sorted((pos1, pos2) -> Double.compare(this.preference(pos2) * randomValue, this.preference(pos1) * randomValue))
                .limit(number)
                .map(position -> new Grass(position, DEFAULT_NUTRITIOUSNESS))
                .forEach(plant -> worldMap.growPlants(plant));
    }

    protected double preference(Vector2d position) {
        boolean isNearEquator = isPositionNearEquator(position);
        double preferenceFactor = 1;
        preferenceFactor *= isNearEquator ? 0.8 : 0.2;

        return preferenceFactor;
    }

    boolean isPositionNearEquator(Vector2d position) {
        int mapHeight = worldMap.getCurrentBounds().upperRight().getY() - worldMap.getCurrentBounds().lowerLeft().getY() + 1;
        int equatorStart = (int) (mapHeight * 0.4);
        int equatorEnd = (int) (mapHeight * 0.6);
        int relativeY = position.getY() - worldMap.getCurrentBounds().lowerLeft().getY();
        return relativeY >= equatorStart && relativeY <= equatorEnd;
    }
}

