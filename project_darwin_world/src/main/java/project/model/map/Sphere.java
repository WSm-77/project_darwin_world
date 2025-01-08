package project.model.map;

import project.model.exceptions.IncorrectPositionException;
import project.model.movement.MapDirection;
import project.model.movement.Vector2d;
import project.model.util.MapChangeListener;
import project.model.util.MapVisualizer;
import project.model.util.PlantGrowerStandardVariant;
import project.model.worldelements.Animal;
import project.model.worldelements.Plant;
import project.model.worldelements.WorldElement;

import java.util.*;

public class Sphere implements WorldMap {
    private final static Vector2d DEFAULT_LOWER_LEFT = new Vector2d(0, 0);
    private final static String MOVE_MESSAGE_TEMPLATE = "Animal movement:\norientation: %s -> %s\nposition: %s -> %s";
    final private Vector2d lowerLeft = Sphere.DEFAULT_LOWER_LEFT;
    final private Vector2d upperRight;
    final private Map<Vector2d, Set<Animal>> animals = new HashMap<>();
    final private Map<Vector2d, Plant> grass = new HashMap<>();
    final private Boundary boundary;
    final private UUID id;
    final private List<MapChangeListener> listeners = new ArrayList<>();
    final private MapVisualizer mapVisualizer = new MapVisualizer(this);
    private final PlantGrowerStandardVariant plantGrower;

    public Sphere(int width, int height) {
        this.upperRight = this.lowerLeft.add(new Vector2d(width - 1, height - 1));
        this.boundary = new Boundary(this.lowerLeft, this.upperRight);
        this.id = UUID.randomUUID();
        this.plantGrower = new PlantGrowerStandardVariant(this.lowerLeft, this.upperRight);
    }

    @Override
    public boolean isOnMap(Vector2d position) {
        return position.follows(this.lowerLeft) && position.precedes(upperRight);
    }

    public void growPlants(Plant... plants) {
        for (Plant plant : plants) {
            Vector2d position = plantGrower.selectPlantPosition();
            this.grass.put(position, plant);
        }
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        Vector2d position = animal.getPosition();

        if (this.isOnMap(position)) {
            Optional<Set<Animal>> optionalAnimals = this.animalsAt(position);

            if (optionalAnimals.isPresent()) {
                optionalAnimals.get().add(animal);
            }
            else {
                Set<Animal> animalSet = new HashSet<>();
                animalSet.add(animal);
                this.animals.put(position, animalSet);
            }
        }
        else {
            throw new IncorrectPositionException(position);
        }
    }

    public Map<Vector2d, Plant> getGrassMap() {
        return this.grass;
    }

    @Override
    public void move(Animal animal) {
        Vector2d prevPosition = animal.getPosition();
        MapDirection prevOrientation = animal.getOrientation();

        if (animal.move(this)) {
            Set<Animal> animalSet = this.animals.get(prevPosition);
            animalSet.remove(animal);

            if (animalSet.isEmpty()) {
                this.animals.remove(prevPosition);
            }

            this.place(animal);
        }

        this.mapChanged(String.format(Sphere.MOVE_MESSAGE_TEMPLATE, prevOrientation, animal.getOrientation(),
                prevPosition, animal.getPosition()));
    }

    public Optional<Set<Animal>> animalsAt(Vector2d position) {
        return this.animals.containsKey(position) ? Optional.of(this.animals.get(position)) : Optional.empty();
    }

    @Override
    public Optional<WorldElement> objectAt(Vector2d position) {
        var animalsAtPosition = this.animalsAt(position);
        Optional<WorldElement> optionalObject = Optional.ofNullable(this.grass.get(position));

        if (animalsAtPosition.isPresent()) {
            optionalObject = animalsAtPosition.flatMap(set -> set.stream().findFirst());
        }

        return optionalObject;
    }

    @Override
    public Boundary getCurrentBounds() {
        return this.boundary;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public Vector2d calculateNextPosition(Vector2d currentPosition, Vector2d moveVector) {
        Vector2d normalizationVector = this.lowerLeft.opposite();
        Vector2d normalizedUpperRight = this.upperRight.add(normalizationVector);
        Vector2d normalizedCurrentPosition = currentPosition.add(normalizationVector);
        Vector2d normalizedNewPosition = normalizedCurrentPosition.add(moveVector);

        int mapWidth = normalizedUpperRight.getX() + 1;
        int newX = normalizedNewPosition.getX() % mapWidth;

        if (newX < 0)
            newX += mapWidth;

        int newY = normalizedNewPosition.getY();
        normalizedNewPosition = new Vector2d(newX, newY);

        Vector2d newPosition = normalizedNewPosition.subtract(normalizationVector);

        boolean canMoveTo = this.lowerLeft.getY() <= newPosition.getY() && newPosition.getY() <= this.upperRight.getY();

        return canMoveTo ? newPosition : currentPosition;
    }

    public void subscribe(MapChangeListener listener) {
        this.listeners.add(listener);
    }

    private void mapChanged(String message) {
        for (var listener : this.listeners) {
            listener.mapChanged(this, message);
        }
    }

    @Override
    public String toString() {
        var currentBounds = this.getCurrentBounds();
        return this.mapVisualizer.draw(currentBounds.lowerLeft(), currentBounds.upperRight());
    }
}
