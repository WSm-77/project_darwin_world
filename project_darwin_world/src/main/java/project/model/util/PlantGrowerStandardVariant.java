package project.model.util;

import gr.james.sampling.ChaoSampling;
import gr.james.sampling.WeightedRandomSamplingCollector;
import project.model.map.WorldMap;
import project.model.movement.Vector2d;
import project.model.worldelements.Grass;

import java.util.*;
import java.util.stream.Collectors;

public class PlantGrowerStandardVariant implements PlantGrower {

    public static final double PREFERRED_POSITION_FACTOR = 0.8;
    public static final double NOT_PREFERRED_POSITION_FACTOR = 1 - PREFERRED_POSITION_FACTOR;
    public static final double EQUATOR_START = 0.4;
    public static final double EQUATOR_STOP = 0.6;
    public static final int DEFAULT_NUTRITIOUSNESS = 5;
    protected final WorldMap worldMap;
    protected Set<Vector2d> occupiedPositions;
    protected Set<Vector2d> notOccupiedPositions;
    protected int plantNutritiousness;

    public PlantGrowerStandardVariant(WorldMap worldMap) {
        this(worldMap, DEFAULT_NUTRITIOUSNESS);
    }

    public PlantGrowerStandardVariant(WorldMap worldMap, int plantNutritiousness) {
        this.worldMap = worldMap;
        this.plantNutritiousness = plantNutritiousness;
    }

    protected void setOccupiedPositions() {
        this.occupiedPositions = this.worldMap.getPlants().stream()
                .map(plant -> plant.getPosition())
                .collect(Collectors.toSet());
    }

    protected void setNotOccupiedPositions() {
        Vector2d lowerLeft = worldMap.getCurrentBounds().lowerLeft();
        Vector2d upperRight = worldMap.getCurrentBounds().upperRight();

        Set<Vector2d> positions = new HashSet<>();

        for (int x = lowerLeft.getX(); x <= upperRight.getX(); x++) {
            for (int y = lowerLeft.getY(); y <= upperRight.getY(); y++) {
                var position = new Vector2d(x, y);

                if (!this.occupiedPositions.contains(position))
                    positions.add(position);
            }
        }

        this.notOccupiedPositions = positions;
    }

    @Override
    public Map<Vector2d, Double> getPreferencesMap() {
        this.setOccupiedPositions();
        this.setNotOccupiedPositions();

        return notOccupiedPositions.stream()
                .collect(Collectors.toMap(
                   position -> position,
                   position -> this.preference(position)
                ));
    }

    public void growPlants(int number) {
        if (number <= 0)
            return;
        Map<Vector2d, Double> positionsPreferenceMap = this.getPreferencesMap();

        WeightedRandomSamplingCollector<Vector2d> collector = ChaoSampling.weightedCollector(number, new Random());

        Collection<Vector2d> randomPositions = positionsPreferenceMap.entrySet().stream().collect(collector);

        randomPositions.stream()
                .map(position -> new Grass(position, this.plantNutritiousness))
                .forEach(plant -> this.worldMap.growPlants(plant));
    }

    protected double preference(Vector2d position) {
        return isPositionNearEquator(position) ? PREFERRED_POSITION_FACTOR : NOT_PREFERRED_POSITION_FACTOR;
    }

    protected boolean isPositionNearEquator(Vector2d position) {
        int mapHeight = worldMap.getCurrentBounds().upperRight().getY() - worldMap.getCurrentBounds().lowerLeft().getY() + 1;
        int equatorStart = (int) (mapHeight * EQUATOR_START);
        int equatorEnd = (int) (mapHeight * EQUATOR_STOP);
        int relativeY = position.getY() - worldMap.getCurrentBounds().lowerLeft().getY();
        return relativeY >= equatorStart && relativeY < equatorEnd;
    }
}
