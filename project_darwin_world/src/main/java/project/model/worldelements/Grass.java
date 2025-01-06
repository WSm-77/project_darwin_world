package project.model.worldelements;

import project.model.movement.Vector2d;

public class Grass implements Plant {
    public final static String GRASS_STRING = "*";
    private final Vector2d position;
    private final int nutritiousness;

    public Grass(Vector2d position, int nutritiousness) {
        this.position = position;
        this.nutritiousness = nutritiousness;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public int getEnergy() {
        return nutritiousness;
    }
    @Override
    public String toString() {
        return Grass.GRASS_STRING;
    }
}
