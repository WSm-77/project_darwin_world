package project.model.util;

import project.model.map.Boundary;
import project.model.movement.MapDirection;
import project.model.movement.Vector2d;
import project.model.worldelements.Animal;
import project.model.worldelements.Genome;

import java.util.Objects;
import java.util.Random;

public class AnimalFactory {
    private final static String PARENTS_DIFFERENT_POSITIONS_MESSAGE = "Parents must be on the same position!!!";
    private final Random random = new Random();
    private final GenomeFactory genomeFactory;
    private final int randomAnimalStartEnergy;
    private final int childAnimalStartEnergy;
    private final Boundary startPositionBoundary;
    private final AnimalConstructor<? extends Animal> constructor;

    public AnimalFactory(GenomeFactory genomeFactory, int randomAnimalStartEnergy, int childAnimalStartEnergy,
                         Boundary startPositionBoundary, AnimalConstructor<? extends Animal> constructor) {
        this.randomAnimalStartEnergy = randomAnimalStartEnergy;
        this.genomeFactory = genomeFactory;
        this.childAnimalStartEnergy = childAnimalStartEnergy;
        this.startPositionBoundary = startPositionBoundary;
        this.constructor = constructor;
    }

    private Vector2d generateRandomPosition() {
        int minX = this.startPositionBoundary.lowerLeft().getX();
        int maxX = this.startPositionBoundary.upperRight().getX();
        int minY = this.startPositionBoundary.lowerLeft().getY();
        int maxY = this.startPositionBoundary.upperRight().getY();

        int randomX = this.random.nextInt(maxX - minX + 1) + minX;
        int randomY = this.random.nextInt(maxY - minY + 1) + minY;

        return new Vector2d(randomX, randomY);
    }

    private MapDirection generateRandomMapDirection() {
        var mapDirections = MapDirection.values();
        int randomIdx = this.random.nextInt(mapDirections.length);

        return mapDirections[randomIdx];
    }

    private void updateParentsEnergy(Animal parent1, Animal parent2) {
        Animal strongerParent = parent1;
        Animal weakerParent = parent2;

        // Ensure strongerParent is indeed the animal with higher energy
        if (strongerParent.getEnergy() < weakerParent.getEnergy()) {
            Animal temp = strongerParent;
            strongerParent = weakerParent;
            weakerParent = temp;
        }

        int strongerParentEnergy = strongerParent.getEnergy();
        int weakerParentEnergy = weakerParent.getEnergy();

        double totalEnergy = strongerParentEnergy + weakerParentEnergy;
        int strongerParentEnergyLoss = (int) Math.round(strongerParentEnergy / totalEnergy * strongerParentEnergy);
        int weakerParentEnergyLoss = this.childAnimalStartEnergy - strongerParentEnergyLoss;

        strongerParent.updateEnergy(-strongerParentEnergyLoss);
        weakerParent.updateEnergy(-weakerParentEnergyLoss);
    }

    public Animal createRandomAnimal() {
        Vector2d position = this.generateRandomPosition();
        Genome genome = genomeFactory.createRandomGenome();
        MapDirection startOrientation = this.generateRandomMapDirection();
        return this.constructor.construct(position, genome, this.randomAnimalStartEnergy, startOrientation);
    }

    public Animal createFromParents(Animal parent1, Animal parent2) {
        if (!Objects.equals(parent1.getPosition(), parent2.getPosition())) {
            throw new IllegalArgumentException(AnimalFactory.PARENTS_DIFFERENT_POSITIONS_MESSAGE);
        }

        Vector2d position = parent1.getPosition();
        Genome genome = genomeFactory.createFromParents(parent1, parent2);
        MapDirection startOrientation = this.generateRandomMapDirection();

        this.updateParentsEnergy(parent1, parent2);

        return this.constructor.construct(position, genome, this.childAnimalStartEnergy, startOrientation);
    }
}
