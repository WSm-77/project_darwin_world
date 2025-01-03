package project.model.worldelements;

import project.model.movement.MapDirection;
import project.model.movement.MoveDirection;
import project.model.movement.MoveValidator;
import project.model.movement.Vector2d;

import java.util.List;

public class Animal implements WorldElement {
    public static final MapDirection DEFAULT_ORIENTATION = MapDirection.NORTH;
    public static final List<Integer> DEFAULT_GENOME_LIST = List.of(1, 2, 3);
    public static final int DEFAULT_ENERGY = 0;
    public static final int DEFAULT_ACTIVE_GENE_IDX = 0;

    private MapDirection orientation;
    private Vector2d position;
    private int energy;
    final private Genome genome;

    public Animal(Vector2d position) {
        this(position, new Genome(DEFAULT_GENOME_LIST, DEFAULT_ACTIVE_GENE_IDX), DEFAULT_ENERGY);
    }

    public Animal(Vector2d position, Genome genome, int startEnergy) {
        this.position = position;
        this.genome = genome;
        this.energy = startEnergy;
        this.orientation = Animal.DEFAULT_ORIENTATION;
    }

    // TODO: remove
    public void move(MoveDirection direction, MoveValidator moveValidator) {
        this.move(moveValidator);
    }

    public boolean move(MoveValidator moveValidator) {
        int angleVal = this.genome.next();
        this.orientation = this.orientation.rotate(angleVal);
        Vector2d nextPosition = this.position.add(this.orientation.toUnitVector());

        boolean canMove = false;

        if (moveValidator.canMoveTo(nextPosition)) {
            this.position = nextPosition;
            canMove = true;
        }

        return canMove;
    }

    @Override
    public String toString() {
        return this.orientation.toString();
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    public void updateEnergy(int energyAmount) {
        this.energy += energyAmount;
    }

    public boolean isAlive() {
        return this.energy > 0;
    }
}
