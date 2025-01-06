package project.app;

import project.model.map.Sphere;
import project.model.movement.Vector2d;
import project.model.simulation.Simulation;
import project.model.util.ConsoleMapDisplay;
import project.model.util.MapChangeListener;
import project.model.worldelements.Grass;
import project.model.worldelements.Plant;

import java.util.List;

public class World {
    public final static int PARSER_ERROR = 1;

    public static void main(String[] args) {
//        // start
//        System.out.println("System started");
//
//        List<Vector2d> positions = List.of(new Vector2d(4, 0));
//
//        MapChangeListener consoleLog = new ConsoleMapDisplay();
//
//        int numberOfMoves = 12;
//        var sphere = new Sphere(5, 6);
//        sphere.subscribe(consoleLog);
//        var sphereSimulation = new Simulation(positions, numberOfMoves, sphere);
//
//        sphereSimulation.run();
//
//        // stop
//        System.out.println("System stopped");

        Sphere map = new Sphere(10, 10);

        Plant plant1 = new Grass(null, 5);
        Plant plant2 = new Grass(null, 8);
        Plant plant3 = new Grass(null, 3);
        Plant plant4 = new Grass(null, 6);
        Plant plant5 = new Grass(null, 4);

        map.growPlants(plant1, plant2, plant3, plant4, plant5);

        System.out.println("Mapa po dodaniu roślin 1-5:");
        System.out.println(map.toString());

        Plant plant6 = new Grass(null, 7);
        Plant plant7 = new Grass(null, 9);
        Plant plant8 = new Grass(null, 2);
        Plant plant9 = new Grass(null, 5);
        Plant plant10 = new Grass(null, 10);

        map.growPlants(plant6, plant7, plant8, plant9, plant10);

        System.out.println("Mapa po dodaniu roślin 6-10:");
        System.out.println(map.toString());

        Plant plant11 = new Grass(null, 3);
        Plant plant12 = new Grass(null, 8);
        Plant plant13 = new Grass(null, 6);
        Plant plant14 = new Grass(null, 4);
        Plant plant15 = new Grass(null, 2);

        map.growPlants(plant11, plant12, plant13, plant14, plant15);

        System.out.println("Mapa po dodaniu roślin 11-15:");
        System.out.println(map.toString());

        Plant plant16 = new Grass(null, 9);
        Plant plant17 = new Grass(null, 5);
        Plant plant18 = new Grass(null, 7);
        Plant plant19 = new Grass(null, 1);
        Plant plant20 = new Grass(null, 6);

        map.growPlants(plant16, plant17, plant18, plant19, plant20);

        System.out.println("Mapa po dodaniu roślin 16-20:");
        System.out.println(map.toString());
    }
}
