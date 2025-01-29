package project.model.simulation;

import project.model.util.AnimalVariant;
import project.model.util.PlantGrowerVariant;

public record SimulationConfigurationFile (
    double initialAnimalCount,
    double initialAnimalEnergy,
    double energyToReproduce,
    double initialChildEnergy,
    double genomeLength,
    double minMutations,
    double maxMutations,
    double initialPlantCount,
    double plantNutritiousness,
    double dailyPlantGrowth,
    double mapWidth,
    double mapHeight,
    boolean simulationRefreshType,
    boolean csvSaver,
    PlantGrowerVariant mapVariant,
    AnimalVariant animalVariant
){}
