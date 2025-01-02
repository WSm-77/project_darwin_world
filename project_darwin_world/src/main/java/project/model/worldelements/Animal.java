package project.model.worldelements;

import project.model.MapDirection;
import project.model.MoveDirection;
import project.model.MoveValidator;
import project.model.Vector2d;

public class Animal implements WorldElement {
    public static final Vector2d DEFAULT_POSITION = new Vector2d(2, 2);
    public static final MapDirection DEFAULT_ORIENTATION = MapDirection.NORTH;
    public static final int NO_FIRST_CHARACTERS_OF_ORIENTATION_NAME = 1;

    private MapDirection orientation;
    private Vector2d position;

    public Animal() {
        this(Animal.DEFAULT_POSITION);
    }

    public Animal(Vector2d position) {
        this.position = position;
        this.orientation = Animal.DEFAULT_ORIENTATION;
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveDirection direction, MoveValidator moveValidator) {
        switch (direction) {
            case RIGHT -> this.orientation = this.orientation.next();
            case LEFT -> this.orientation = this.orientation.previous();
            case FORWARD -> {
                Vector2d nextPosition = this.position.add(this.orientation.toUnitVector());

                if (moveValidator.canMoveTo(nextPosition)) {
                    this.position = nextPosition;
                }
            }
            case BACKWARD -> {
                Vector2d nextPosition = this.position.subtract(this.orientation.toUnitVector());

                if (moveValidator.canMoveTo(nextPosition)) {
                    this.position = nextPosition;
                }
            }
        }
    }

    @Override
    public String toString() {
        return this.orientation.name().substring(0, Animal.NO_FIRST_CHARACTERS_OF_ORIENTATION_NAME);
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }
}
