package project.model.simulation;

import project.model.simulation.Simulation;
import project.model.worldelements.Animal;
import project.model.worldelements.WorldElement;
import project.model.map.WorldMap;
import project.model.movement.Vector2d;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimulationStatistics {
    public static final String N_A = "N/A";
    public static final String F = "%.2f";
    private final Simulation simulation;

    public SimulationStatistics(Simulation simulation) {
        this.simulation = simulation;
    }

    public List<String> getCurrentStatistics() {
        int currentDay = simulation.getDay();
        int animalsCount = getAnimalsCount();
        int plantsCount = getPlantsCount();
        double averageEnergy = calculateAverageEnergy();
        double averageLifeTime = calculateAverageLifeTime();
        int emptySpaces = calculateNumberOfEmptySpots();
        String mostPopularGenome = mostPopularGenome()
                .map(Objects::toString)
                .orElse(N_A);
        double averageChildren = calculateAverageAnimalChildrenCount();

        return List.of(
                String.valueOf(currentDay),
                String.valueOf(animalsCount),
                String.valueOf(plantsCount),
                String.format(F, averageEnergy),
                String.format(F, averageLifeTime),
                String.format(F, averageChildren),
                mostPopularGenome,
                String.valueOf(emptySpaces)
        );
    }

    public int getAnimalsCount() {
        WorldMap worldMap = simulation.getWorldMap();
        return worldMap.getAnimals().size();
    }

    public int getPlantsCount() {
        WorldMap worldMap = simulation.getWorldMap();
        return worldMap.getPlants().size();
    }

    public int calculateNumberOfEmptySpots() {
        WorldMap worldMap = simulation.getWorldMap();
        var plantsList = worldMap.getPlants();
        var animalsList = worldMap.getAnimals();

        Stream<Vector2d> plantsPositionsStream = plantsList.stream().map(WorldElement::getPosition);
        Stream<Vector2d> animalsPositionsStream = animalsList.stream().map(WorldElement::getPosition);
        Set<Vector2d> occupiedPositions = Stream.concat(plantsPositionsStream, animalsPositionsStream)
                .collect(Collectors.toSet());

        int totalSpots = worldMap.getWidth() * worldMap.getHeight();
        return totalSpots - occupiedPositions.size();
    }

    public double calculateAverageEnergy() {
        WorldMap worldMap = simulation.getWorldMap();
        List<Animal> animals = worldMap.getAnimals();

        return animals.stream()
                .mapToDouble(animal -> animal.getStatistics().getEnergy())
                .average()
                .orElse(0.0);
    }

    public double calculateAverageLifeTime() {
        List<Animal> deadAnimals = simulation.getDeadAnimals();
        return deadAnimals.stream()
                .mapToDouble(animal -> (double) animal.getStatistics().getDaysAlive())
                .average()
                .orElse(0.0);
    }

    public double calculateAverageAnimalChildrenCount() {
        WorldMap worldMap = simulation.getWorldMap();
        List<Animal> animals = worldMap.getAnimals();

        return animals.stream()
                .mapToDouble(animal -> animal.getStatistics().getChildrenCount())
                .average()
                .orElse(0.0);
    }

    public Optional<List<Integer>> mostPopularGenome() {
        WorldMap worldMap = simulation.getWorldMap();
        List<Animal> aliveAnimals = worldMap.getAnimals();

        HashMap<List<Integer>, Integer> genomeCountMap = new HashMap<>();
        for (var animal : aliveAnimals) {
            List<Integer> genesList = animal.getStatistics().getGenesList();
            Integer genomeCount = genomeCountMap.getOrDefault(genesList, 0) + 1;
            genomeCountMap.put(genesList, genomeCount);
        }

        return genomeCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }
}