package project.model.util;

import project.model.map.Sphere;
import project.model.movement.Vector2d;
import project.model.worldelements.Plant;

import java.util.List;

public class PlantGrowerCreepingJungleVariant extends PlantGrowerStandardVariant{
    public PlantGrowerCreepingJungleVariant(Sphere worldMap) {
        super(worldMap);
    }
    @Override
    protected double preference(Vector2d position) {
        boolean isNearEquator = isPositionNearEquator(position);
        boolean hasPlantNeighbour = hasPlantNeighbour(position);
        double preferenceFactor = 1;
        preferenceFactor *= isNearEquator ? 0.8 : 0.2;
        preferenceFactor *= hasPlantNeighbour ? 0.8 : 0.2;

        return preferenceFactor;
    }

    private boolean hasPlantNeighbour(Vector2d position) {
        List<Plant> plants = worldMap.getPlants();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Vector2d neighbour = position.add(new Vector2d(dx, dy));
                if (plants.stream().anyMatch(plant -> plant.getPosition().equals(neighbour))) {
                    return true;
                }
            }
        }
        return false;
    }
}
