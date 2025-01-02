package project;

import project.model.*;
import project.model.util.ConsoleMapDisplay;
import project.model.worldelements.Animal;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class World {
    private final static String PET_NAME = "WSm";
    public final static int PARSER_ERROR = 1;

    public static void main(String[] args) {
        // start
        System.out.println("system wystartował");

        List<MoveDirection> directions = null;

        try {
            directions = OptionsParser.parseStringArray(args);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
            System.exit(World.PARSER_ERROR);
        }

        World.run(directions);

        // Vector2d verification
        Vector2d position1 = new Vector2d(1, 2);
        System.out.println(position1);
        Vector2d position2 = new Vector2d(-2, 1);
        System.out.println(position2);
        System.out.println(position1.add(position2));

        // MapDirections verification
        MapDirection direction = MapDirection.NORTH;
        System.out.println(direction.toString());
        System.out.println(direction.next().toString());
        System.out.println(direction.previous().toString());
        System.out.println(direction.toUnitVector().toString());

        // create animal
        Animal animalWSm = new Animal();
        System.out.println("status zwierzaka:");
        System.out.println(animalWSm.toString());

        // Simulation
        var repeatedPosition = new Vector2d(2, 2);
        List<Vector2d> positions = List.of(repeatedPosition, repeatedPosition, new Vector2d(3, 4));

        // 1. RectangularMap Simulation
        var rectangularMap = new RectangularMap(4, 4);
        MapChangeListener consoleLog = new ConsoleMapDisplay();
        rectangularMap.subscribe(consoleLog);
        var rectangularMapSimulation = new Simulation(positions, directions, rectangularMap);

        // 2. GrassField Simulation
        var grassField = new GrassField(10);
        grassField.subscribe(consoleLog);
        var grassFieldMapSimulation = new Simulation(positions, directions, grassField);

        // Utilize Simulation Engine
        var simulationEngine = new SimulationEngine(List.of(
            rectangularMapSimulation,
            grassFieldMapSimulation
        ));

        // synchronous run test
        // simulationEngine.runSync();

        // asynchronous run test
        try {
            simulationEngine.runAsync();
            simulationEngine.awaitSimulationsEnd();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // run a lot of simulations
        int totalNumberOfSimulations = 1000;
        int numberOfRectangularMapSimulations = totalNumberOfSimulations / 2;
        int numberOfGrassFieldSimulations = totalNumberOfSimulations - numberOfRectangularMapSimulations;
        List<Simulation> vastNumberOfSimulationsList = new ArrayList<>();

        for (int i = 0; i < numberOfRectangularMapSimulations; i++) {
            var testRectangularMap = new RectangularMap(4, 4);
            var testRectangularMapSimulation = new Simulation(positions, directions, testRectangularMap);
            testRectangularMap.subscribe(consoleLog);
            vastNumberOfSimulationsList.add(testRectangularMapSimulation);
        }

        for (int i = 0; i < numberOfGrassFieldSimulations; i++) {
            var testGrassField = new GrassField(10);
            var testGrassFieldSimulation = new Simulation(positions, directions, testGrassField);
            testGrassField.subscribe(consoleLog);
            vastNumberOfSimulationsList.add(testGrassFieldSimulation);
        }

        var vastNumberOfSimulationsEngine = new SimulationEngine(vastNumberOfSimulationsList);

        Instant start = Instant.now();

        try {
            vastNumberOfSimulationsEngine.runAsyncInThreadPool();
            vastNumberOfSimulationsEngine.awaitSimulationsEnd();
        } catch (InterruptedException e) {
            System.out.println("Execution interruped!!!");
            e.printStackTrace();
        }

        Instant end = Instant.now();

        long timeElapsed = Duration.between(start, end).toMillis();

        System.out.println(String.format("Time elapsed = %d [s]", timeElapsed));

        // stop
        System.out.println("system zakończył działanie");
    }

    public static void run(List<MoveDirection> directions) {
        for (var direction : directions) {
            String directionMessage = switch (direction) {
                case MoveDirection.FORWARD -> "idzie do przodu";
                case MoveDirection.BACKWARD -> "idzie do tyłu";
                case MoveDirection.LEFT -> "skręca w lewo";
                case MoveDirection.RIGHT -> "skręca w prawo";
                };

            System.out.println(World.PET_NAME + " " + directionMessage);
        }
    }
}
