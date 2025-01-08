package project.model.util;

import project.model.movement.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlantGrowerStandardVariant implements PlantGrower {
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final List<Vector2d> preferredPositions = new ArrayList<>();
    private final List<Vector2d> nonPreferredPositions = new ArrayList<>();
    private final Random random = new Random();

    public PlantGrowerStandardVariant(Vector2d lowerLeft, Vector2d upperRight) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
        calculatePreferredAndNonPreferredPositions();
    }
    @Override
    public void calculatePreferredAndNonPreferredPositions() {
        int mapHeight = upperRight.getY() - lowerLeft.getY() + 1;
        int equatorStart = lowerLeft.getY() + mapHeight / 3;
        int equatorEnd = lowerLeft.getY() + 2 * mapHeight / 3;

        for (int x = lowerLeft.getX(); x <= upperRight.getX(); x++) {
            for (int y = lowerLeft.getY(); y <= upperRight.getY(); y++) {
                Vector2d position = new Vector2d(x, y);
                if (y >= equatorStart && y <= equatorEnd) {
                    preferredPositions.add(position);
                } else {
                    nonPreferredPositions.add(position);
                }
            }
        }
    }

    @Override
    public Vector2d selectPlantPosition() {
        if (random.nextDouble() < 0.8) {
            return preferredPositions.get(random.nextInt(preferredPositions.size()));
        } else {
            return nonPreferredPositions.get(random.nextInt(nonPreferredPositions.size()));
        }
    }
}
