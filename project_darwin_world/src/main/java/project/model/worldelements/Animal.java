package project.model.worldelements;

import project.model.movement.MapDirection;
import project.model.movement.NextPositionCalculator;
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

    public boolean move(NextPositionCalculator nextPositionCalculator) {
        int angleVal = this.genome.next();
        this.orientation = this.orientation.rotate(angleVal);
        Vector2d prevPosition = this.getPosition();

        this.position = nextPositionCalculator.calculateNextPosition(prevPosition, this.getOrientation().toUnitVector());

        return this.position != prevPosition;
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

    public int getEnergy() {
        return energy;
    }

    public Genome getGenome() {
        return genome;
    }
}
