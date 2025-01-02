package project.model.worldelements;

import project.model.Vector2d;

public class Grass implements WorldElement {
    public final static String GRASS_STRING = "*";
    private final Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return Grass.GRASS_STRING;
    }
}
