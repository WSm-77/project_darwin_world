package project.model.movement;

public interface NextPositionCalculator {

    /**
     * Calculates new position of an object based current position and move vector.
     *
     * @param currentPosition
     *              Current position of an object
     * @param moveVector
     *              Vector that represents how many steps in each direction object should be moved.
     * @return Position after objects move.
     */
    Vector2d calculateNextPosition(Vector2d currentPosition, Vector2d moveVector);
}
