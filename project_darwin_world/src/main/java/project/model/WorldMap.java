package project.model;

import project.model.exceptions.IncorrectPositionException;
import project.model.worldelements.Animal;
import project.model.worldelements.WorldElement;

import java.util.List;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {

    /**
     * Place a animal on the map.
     *
     * @param animal The animal to place on the map.
     */
    void place(Animal animal) throws IncorrectPositionException;

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal, MoveDirection direction);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return a WorldElement at a given position.
     *
     * @param position The position of the animal.
     * @return WorldElement or null if the position is not occupied.
     */
    WorldElement objectAt(Vector2d position);

    /**
     * Return list of all map elements that are one the map.
     *
     * @return list of all WorldElements on the map.
     */
    List<WorldElement> getElements();

    /**
     * Return map boundaries
     *
     * @return Boundary object representing map boundaries.
     */
    Boundary getCurrentBounds();

    /**
     * Return object's UUID
     *
     * @return object's UUID
     */
    UUID getId();
}
