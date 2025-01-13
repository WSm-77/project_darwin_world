package project.app;

import project.model.map.Sphere;
import project.model.movement.Vector2d;
import project.model.simulation.Simulation;
import project.model.util.AnimalMediatorStandardVariant;
import project.model.util.ConsoleMapDisplay;
import project.model.util.MapChangeListener;
import project.model.util.SimulationBuilder;
import project.model.worldelements.AnimalStandardVariant;

import java.util.List;

public class World {
    public final static int PARSER_ERROR = 1;

    public static void main(String[] args) {
        // start
        System.out.println("System started");

        MapChangeListener consoleLog = new ConsoleMapDisplay();

        var sphere = new Sphere(5, 6);
        sphere.subscribe(consoleLog);

        var sphereSimulation = new SimulationBuilder()
                .setMapWidth(5)
                .setMapHeight(6)
                .setInitialPlantCount(3)
                .setEnergyPerPlant(20)
                .setDailyPlantGrowth(5)
                .setInitialAnimalCount(2)
                .setInitialAnimalEnergy(15)
                .setEnergyToReproduce(10)
                .setChildInitialEnergy(10)
                .setMinMutations(0)
                .setMaxMutations(3)
                .setGenomeLength(8)
                .setAnimalConstructor(AnimalStandardVariant::new)
                .setAnimalMediatorConstructor(AnimalMediatorStandardVariant::new)
                .build();

        Thread thread = new Thread(sphereSimulation);
        thread.start();

        try {
            thread.join(3000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        thread.interrupt();

        // stop
        System.out.println("System stopped");
    }
}
