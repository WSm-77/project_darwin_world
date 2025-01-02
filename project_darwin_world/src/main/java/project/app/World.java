package project.app;

import project.model.map.GrassField;
import project.model.movement.MoveDirection;
import project.model.movement.Vector2d;
import project.model.simulation.Simulation;
import project.model.util.ConsoleMapDisplay;
import project.model.util.MapChangeListener;
import project.model.util.OptionsParser;
import java.util.List;

public class World {
    public final static int PARSER_ERROR = 1;

    public static void main(String[] args) {
        // start
        System.out.println("System started");

        List<MoveDirection> directions = null;

        try {
            directions = OptionsParser.parseStringArray(args);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
            System.exit(World.PARSER_ERROR);
        }

        // GrassField Simulation
        var repeatedPosition = new Vector2d(2, 2);
        List<Vector2d> positions = List.of(repeatedPosition, repeatedPosition, new Vector2d(3, 4));

        MapChangeListener consoleLog = new ConsoleMapDisplay();

        var grassField = new GrassField(10);
        grassField.subscribe(consoleLog);
        var grassFieldMapSimulation = new Simulation(positions, directions, grassField);

        grassFieldMapSimulation.run();

        // stop
        System.out.println("System stopped");
    }
}
