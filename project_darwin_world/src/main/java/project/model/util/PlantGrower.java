package project.model.util;

import project.model.movement.Vector2d;

public interface PlantGrower {

    void growPlants(int number);

    double preference(Vector2d position);
}
