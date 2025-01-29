package project.model.util;

import project.model.map.Boundary;
import project.model.movement.MapDirection;
import project.model.movement.Vector2d;
import project.model.worldelements.Animal;
import project.model.worldelements.AnimalStatistics;
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
        if (strongerParent.getStatistics().getEnergy() < weakerParent.getStatistics().getEnergy()) {
            Animal temp = strongerParent;
            strongerParent = weakerParent;
            weakerParent = temp;
        }

        int strongerParentEnergy = strongerParent.getStatistics().getEnergy();
        int weakerParentEnergy = weakerParent.getStatistics().getEnergy();

        double totalEnergy = strongerParentEnergy + weakerParentEnergy;
        int strongerParentEnergyLoss = (int) Math.round(strongerParentEnergy / totalEnergy * this.childAnimalStartEnergy);
        int weakerParentEnergyLoss = this.childAnimalStartEnergy - strongerParentEnergyLoss;

        strongerParent.getStatistics().updateEnergy(-strongerParentEnergyLoss);
        weakerParent.getStatistics().updateEnergy(-weakerParentEnergyLoss);
    }

    private void updateParentsChildren(Animal parent1, Animal parent2, Animal child) {
        parent1.getStatistics().addChild(child);
        parent2.getStatistics().addChild(child);
    }

    public Animal createRandomAnimal() {
        Vector2d position = this.generateRandomPosition();
        Genome genome = genomeFactory.createRandomGenome();
        MapDirection startOrientation = this.generateRandomMapDirection();

        AnimalStatistics animalStatistics = new AnimalStatistics(position, genome, this.randomAnimalStartEnergy, startOrientation);
        return this.constructor.construct(animalStatistics);
    }

    public Animal createFromParents(Animal parent1, Animal parent2) {
        if (!Objects.equals(parent1.getPosition(), parent2.getPosition())) {
            throw new IllegalArgumentException(AnimalFactory.PARENTS_DIFFERENT_POSITIONS_MESSAGE);
        }

        Vector2d position = parent1.getPosition();
        Genome genome = genomeFactory.createFromParents(parent1, parent2);
        MapDirection startOrientation = this.generateRandomMapDirection();

        AnimalStatistics animalStatistics = new AnimalStatistics(position, genome, this.childAnimalStartEnergy, startOrientation);
        Animal child =  this.constructor.construct(animalStatistics);

        this.updateParentsEnergy(parent1, parent2);
        this.updateParentsChildren(parent1, parent2, child);

        return child;
    }
}
