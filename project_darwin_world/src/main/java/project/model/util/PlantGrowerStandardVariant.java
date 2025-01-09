package project.model.util;

import project.model.map.Sphere;
import project.model.movement.Vector2d;
import project.model.worldelements.Grass;

import java.util.*;
import java.util.stream.Collectors;

public class PlantGrowerStandardVariant {

    protected final Sphere worldMap;
    private final Random random = new Random();
    private final int DEFAULT_NUTRITIOUSNESS = 5;

    public PlantGrowerStandardVariant(Sphere worldMap) {
        this.worldMap = worldMap;
    }

    private Set<Vector2d> getOccupiedPositions() {
        return this.worldMap.getAnimals().stream()
                .map(animal -> animal.getPosition())
                .collect(Collectors.toSet());
    }

    private Set<Vector2d> getNotOccupiedPositions(Set<Vector2d> occupiedPositions) {
        Vector2d lowerLeft = worldMap.getCurrentBounds().lowerLeft();
        Vector2d upperRight = worldMap.getCurrentBounds().upperRight();

        Set<Vector2d> positions = new HashSet<>();

        for (int x = lowerLeft.getX(); x <= upperRight.getX(); x++) {
            for (int y = lowerLeft.getY(); y <= upperRight.getY(); y++) {
                var position = new Vector2d(x, y);

                if (!occupiedPositions.contains(position))
                    positions.add(position);
            }
        }

        return positions;
    }

    private Map<Vector2d, Double> getPreferencesMap(Collection<Vector2d> notOccupiedPositions) {
        Map<Vector2d, Double> positionsPreferenceMap = new HashMap<>();

        notOccupiedPositions
                .forEach(position -> {
                    var randomPreference = this.preference(position) * this.random.nextDouble();
                    positionsPreferenceMap.put(position, randomPreference);
                });

        return positionsPreferenceMap;
    }

    private void growPlants(int number) {
        Set<Vector2d> occupiedPositions = this.getOccupiedPositions();
        Set<Vector2d> notOccupiedPositions = this.getNotOccupiedPositions(occupiedPositions);
        Map<Vector2d, Double> positionsPreferenceMap = this.getPreferencesMap(notOccupiedPositions);

        notOccupiedPositions.stream()
                .sorted(Comparator.comparingDouble(positionsPreferenceMap::get))
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
