package project.model.util;

import project.model.map.WorldMap;

public interface PlantGrowerConstructor<PlantGrowerType extends PlantGrower> {
    PlantGrowerType construct(WorldMap worldMap, int plantNutritiousness);
}
