package project.model.map;

import project.model.exceptions.IncorrectPositionException;
import project.model.movement.NextPositionCalculator;
import project.model.movement.Vector2d;
import project.model.worldelements.Animal;
import project.model.worldelements.WorldElement;

import java.util.Optional;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends NextPositionCalculator {
    /**
     * Return boolean value informing whether position is on the map or not.
     *
     * @param position The position to check.
     * @return True, if position is on the map otherwise false.
     */
    boolean isOnMap(Vector2d position);

    /**
     * Place a animal on the map.
     *
     * @param animal The animal to place on the map.
     */
    void place(Animal animal) throws IncorrectPositionException;

    /**
     * Moves an animal (if it is present on the map) according to it's orientation.
     * If the move is not possible, this method has no effect.
     *
     * @param animal The animal to move
     */
    void move(Animal animal);

    /**
     * Return a Optional Object of WorldElement at a given position.
     *
     * @param position The position of the animal.
     * @return Optional object of WorldElement or empty Optional if the position is not occupied.
     */
    Optional<WorldElement> objectAt(Vector2d position);

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
