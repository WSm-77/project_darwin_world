package project.app;

import project.model.map.GrassField;
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

        // GrassField Simulation
        var repeatedPosition = new Vector2d(2, 2);
        List<Vector2d> positions = List.of(repeatedPosition, repeatedPosition, new Vector2d(3, 4));

        MapChangeListener consoleLog = new ConsoleMapDisplay();

        int numberOfMoves = 10;
        var grassField = new GrassField(10);
        grassField.subscribe(consoleLog);
        var grassFieldMapSimulation = new Simulation(positions, numberOfMoves, grassField);

        grassFieldMapSimulation.run();

        // stop
        System.out.println("System stopped");
    }
}
