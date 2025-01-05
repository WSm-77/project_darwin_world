package project.app;

import project.model.map.Sphere;
import project.model.movement.Vector2d;
import project.model.simulation.Simulation;
import project.model.util.ConsoleMapDisplay;
import project.model.util.MapChangeListener;
import java.util.List;

public class World {
    public final static int PARSER_ERROR = 1;

    public static void main(String[] args) {
        // start
        System.out.println("System started");

        List<Vector2d> positions = List.of(new Vector2d(4, 0));

        MapChangeListener consoleLog = new ConsoleMapDisplay();

        int numberOfMoves = 12;
        var sphere = new Sphere(5, 6);
        sphere.subscribe(consoleLog);
        var sphereSimulation = new Simulation(positions, numberOfMoves, sphere);

        sphereSimulation.run();

        // stop
        System.out.println("System stopped");
    }
}
