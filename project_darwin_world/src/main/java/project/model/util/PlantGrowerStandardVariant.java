package project.model.util;

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
    private final Random random = new Random();
    protected Set<Vector2d> occupiedPositions;
    protected Set<Vector2d> notOccupiedPositions;
    protected Map<Vector2d, Double> positionsPreferenceMap;
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

    protected void setPreferencesMap() {
        Map<Vector2d, Double> newPositionsPreferenceMap = new HashMap<>();

        notOccupiedPositions
                .forEach(position -> {
                    var randomPreference = this.preference(position) * this.random.nextDouble();
                    newPositionsPreferenceMap.put(position, randomPreference);
                });

        this.positionsPreferenceMap = newPositionsPreferenceMap;
    }

    public void growPlants(int number) {
        this.setOccupiedPositions();
        this.setNotOccupiedPositions();
        this.setPreferencesMap();

        notOccupiedPositions.stream()
                .sorted(Comparator.comparingDouble(this.positionsPreferenceMap::get).reversed())
                .limit(number)
                .map(position -> new Grass(position, this.plantNutritiousness))
                .forEach(plant -> worldMap.growPlants(plant));
    }

    protected double preference(Vector2d position) {
        boolean isNearEquator = isPositionNearEquator(position);
        double preferenceFactor = 1;
        preferenceFactor *= isNearEquator ? PREFERRED_POSITION_FACTOR : NOT_PREFERRED_POSITION_FACTOR;

        return preferenceFactor;
    }

    protected boolean isPositionNearEquator(Vector2d position) {
        int mapHeight = worldMap.getCurrentBounds().upperRight().getY() - worldMap.getCurrentBounds().lowerLeft().getY() + 1;
        int equatorStart = (int) (mapHeight * EQUATOR_START);
        int equatorEnd = (int) (mapHeight * EQUATOR_STOP);
        int relativeY = position.getY() - worldMap.getCurrentBounds().lowerLeft().getY();
        return relativeY >= equatorStart && relativeY < equatorEnd;
    }
}
