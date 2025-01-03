package project.model.map;

import project.model.exceptions.IncorrectPositionException;
import project.model.movement.MoveValidator;
import project.model.movement.Vector2d;
import project.model.worldelements.Animal;
import project.model.worldelements.WorldElement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
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
     * Moves an animal (if it is present on the map) according to it's orientation.
     * If the move is not possible, this method has no effect.
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
