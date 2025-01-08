package project.model.util;

import project.model.movement.Vector2d;

public interface PlantGrower {

    void calculatePreferredAndNonPreferredPositions();

    Vector2d selectPlantPosition();
}
