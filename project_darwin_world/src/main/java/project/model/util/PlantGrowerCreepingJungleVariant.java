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
    public static final double PREFERRED_DOUBLE = 0.8;
    public static final double NOT_PREFERRED_DOUBLE = 0.2;

    public PlantGrowerCreepingJungleVariant(Sphere worldMap) {
        super(worldMap);
    }

    @Override
    protected double preference(Vector2d position) {
        boolean hasPlantNeighbour = hasPlantNeighbour(position);

        double preferenceFactor = super.preference(position);
        preferenceFactor *= hasPlantNeighbour ? PREFERRED_DOUBLE : NOT_PREFERRED_DOUBLE;

        return preferenceFactor;
    }

    private boolean hasPlantNeighbour(Vector2d position) {
        for (var neighbourVector : NEIGHBOURS_VECTORS) {
            var neighbourPosition = this.worldMap.calculateNextPosition(position, neighbourVector);
            if (this.occupiedPositions.contains(neighbourPosition))
                return true;
        }
        return false;
    }
}
