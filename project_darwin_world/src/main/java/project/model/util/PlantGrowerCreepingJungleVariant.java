package project.model.util;

import project.model.map.Sphere;
import project.model.movement.MapDirection;
import project.model.movement.Vector2d;
import project.model.worldelements.Plant;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlantGrowerCreepingJungleVariant extends PlantGrowerStandardVariant{
    public static final List<Vector2d> NEIGHBOURS_VECTORS = Arrays.stream(MapDirection.values())
                                                            .map(MapDirection::toUnitVector)
                                                            .toList();

    public PlantGrowerCreepingJungleVariant(Sphere worldMap) {
        super(worldMap);
    }

    @Override
    public double preference(Vector2d position) {
        Set<Vector2d> occupiedPositions = getOccupiedPositions();
        boolean hasPlantNeighbour = hasPlantNeighbour(position, occupiedPositions);

        double preferenceFactor = super.preference(position);
        preferenceFactor *= hasPlantNeighbour ? 0.8 : 0.2;

        return preferenceFactor;
    }

    private boolean hasPlantNeighbour(Vector2d position, Set<Vector2d> occupiedPositions) {
        for (var neighbourVector : NEIGHBOURS_VECTORS) {
            var neighbourPosition = this.worldMap.calculateNextPosition(position, neighbourVector);
            if (occupiedPositions.contains(neighbourPosition))
                return true;
        }
        return false;
    }
}
