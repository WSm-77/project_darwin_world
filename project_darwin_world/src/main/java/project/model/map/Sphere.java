package project.model.map;

import project.model.exceptions.IncorrectPositionException;
import project.model.movement.MapDirection;
import project.model.movement.PositionDirectionPair;
import project.model.movement.Vector2d;
import project.model.util.MapChangeListener;
import project.model.util.MapVisualizer;
import project.model.worldelements.Animal;
import project.model.worldelements.Plant;
import project.model.worldelements.WorldElement;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Sphere implements WorldMap {
    private final static Vector2d DEFAULT_LOWER_LEFT = new Vector2d(0, 0);
    private final static String INCORRECT_ANIMAL_TO_REMOVE_MESSAGE_TEMPLATE = "There is no %s Animal at %s position!!!";
    public static final String ANIMAL_AND_PLANT_MUST_NOT_BE_NULL = "Animal and Plant must not be null.";
    final private Vector2d lowerLeft = Sphere.DEFAULT_LOWER_LEFT;
    final private Vector2d upperRight;
    final private Map<Vector2d, Set<Animal>> animals = new HashMap<>();
    final private Map<Vector2d, Plant> grass = new HashMap<>();
    final private Boundary boundary;
    final private UUID id;
    final private List<MapChangeListener> listeners = new ArrayList<>();
    final private MapVisualizer mapVisualizer = new MapVisualizer(this);
    final private int width;
    final private int height;
    final private MapBuffer mapBuffer = new MapBuffer();

    public Sphere(int width, int height) {
        this.upperRight = this.lowerLeft.add(new Vector2d(width - 1, height - 1));
        this.boundary = new Boundary(this.lowerLeft, this.upperRight);
        this.id = UUID.randomUUID();
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean isOnMap(Vector2d position) {
        return position.follows(this.lowerLeft) && position.precedes(upperRight);
    }

    @Override
    public void growPlants(Plant... plants) throws IncorrectPositionException {
        for (Plant plant : plants) {
            Vector2d position = plant.getPosition();

            if (!this.isOnMap(position) || this.grass.containsKey(position)) {
                throw new IncorrectPositionException(position);
            }

            this.grass.put(position, plant);

            this.mapBuffer.setLastPlantPlaced(plant);
            this.mapChanged(MapEvent.GROW_PLANT);
        }
    }

    public void feedAnimal(Animal animal, Plant plant) {
        if (animal == null || plant == null) {
            throw new IllegalArgumentException(ANIMAL_AND_PLANT_MUST_NOT_BE_NULL);
        }

        int nutritionalValue = plant.getNutritiousness();
        animal.getStatistics().updateEnergy(nutritionalValue);

        Vector2d plantPosition = plant.getPosition();
        removePlantFromMap(plantPosition);
    }

    public void removePlantFromMap(Vector2d position) {
        this.mapBuffer.setLastPlantRemoved(this.grass.get(position));

        if (this.grass.containsKey(position)) {
            this.grass.remove(position);
        } else {
            throw new IncorrectPositionException(position);
        }

        this.mapChanged(MapEvent.REMOVE_PLANT);
    }

    private String createIncorrectAnimalToRemoveMessage(Animal animal) {
        return String.format(Sphere.INCORRECT_ANIMAL_TO_REMOVE_MESSAGE_TEMPLATE, animal, animal.getPosition());
    }

    @Override
    public void removeAnimal(Animal animal) throws IllegalArgumentException {
        Vector2d animalPosition = animal.getPosition();
        this.mapBuffer.setLastAnimalRemoved(animal);

        Consumer<? super Set<Animal>> actionIfPresent = (animalSet) -> {
            if (!animalSet.contains(animal)) {
                throw new IllegalArgumentException();
            }

            animalSet.remove(animal);

            if (animalSet.isEmpty()) {
                this.animals.remove(animalPosition);
            }

            this.mapChanged(MapEvent.REMOVE_ANIMAL);
        };

        Runnable actionIfNotPresent = () -> {
            throw new IllegalArgumentException(this.createIncorrectAnimalToRemoveMessage(animal));
        };

        this.animalsAt(animalPosition).ifPresentOrElse(actionIfPresent, actionIfNotPresent);
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

        this.mapBuffer.setLastAnimalPlaced(animal);
        this.mapChanged(MapEvent.PLACE_ANIMAL);
    }

    @Override
    public List<Plant> getPlants() {
        return new ArrayList<>(this.grass.values());
    }

    @Override
    public Optional<Plant> plantAt(Vector2d position) {
        return Optional.ofNullable(this.grass.get(position));
    }

    @Override
    public void move(Animal animal) {
        Vector2d prevPosition = animal.getPosition();
        MapDirection prevOrientation = animal.getStatistics().getOrientation();

        if (animal.move(this)) {
            Set<Animal> animalSet = this.animals.get(prevPosition);
            animalSet.remove(animal);

            if (animalSet.isEmpty()) {
                this.animals.remove(prevPosition);
            }

            this.place(animal);
        }

        this.mapChanged(MapEvent.MOVE_ANIMAL);
    }

    @Override
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
    public List<Animal> getAnimals() {
        return this.animals.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public MapBuffer getMapBuffer() {
        return this.mapBuffer;
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

    @Override
    public PositionDirectionPair calculateNextPositionDirectionPair(PositionDirectionPair currentPositionDirectionPair, Vector2d moveVector) {
        Vector2d nextPosition = this.calculateNextPosition(currentPositionDirectionPair.position(), moveVector);
        MapDirection nextDirection = currentPositionDirectionPair.mapDirection();

        if (Objects.equals(currentPositionDirectionPair.position(), nextPosition)) {
            nextDirection = nextDirection.opposite();
        }

        return new PositionDirectionPair(nextPosition, nextDirection);
    }

    public void subscribe(MapChangeListener listener) {
        this.listeners.add(listener);
    }

    private void mapChanged(MapEvent event) {
        for (var listener : this.listeners) {
            listener.mapChanged(this, event);
        }
    }

    @Override
    public String toString() {
        var currentBounds = this.getCurrentBounds();
        return this.mapVisualizer.draw(currentBounds.lowerLeft(), currentBounds.upperRight());
    }
}
