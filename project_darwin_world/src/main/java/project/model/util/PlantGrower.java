package project.model.util;

import project.model.movement.Vector2d;

public interface PlantGrower {

    /**
     * generate a specified number of plant objects
     * and place them on the map according to predefined preferences
     *
     * @param number the number of plants to be generated and placed on the map
     */
    void growPlants(int number);
}
