package project.model.map;

import project.model.exceptions.IncorrectPositionException;
import project.model.movement.Vector2d;
import project.model.util.MapChangeListener;
import project.model.util.MapVisualizer;
import project.model.worldelements.Animal;
import project.model.worldelements.Grass;
import project.model.worldelements.WorldElement;

import java.util.*;

public class Sphere implements WorldMap {
    private final static Vector2d DEFAULT_LOWER_LEFT = new Vector2d(0, 0);
    final private Vector2d lowerLeft = Sphere.DEFAULT_LOWER_LEFT;
    final private Vector2d upperRight;
    private Map<Vector2d, Set<Animal>> animals = new HashMap<>();
    private Map<Vector2d, Grass> grass = new HashMap<>();
    final private Boundary boundary;
    final private UUID id;
    final private List<MapChangeListener> listeners = new ArrayList<>();
    final private MapVisualizer mapVisualizer = new MapVisualizer(this);

    public Sphere(int width, int height) {
        this.upperRight = this.lowerLeft.add(new Vector2d(width, height));
        this.boundary = new Boundary(this.lowerLeft, this.upperRight);
        this.id = UUID.randomUUID();
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        Vector2d position = animal.getPosition();

        if (position.follows(this.lowerLeft) && position.precedes(upperRight)) {
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

    @Override
    public void move(Animal animal) {
        Vector2d prevPosition = animal.getPosition();

        if (animal.move(this)) {
            Set<Animal> animalSet = this.animals.get(prevPosition);
            animalSet.remove(animal);

            if (animalSet.isEmpty()) {
                this.animals.remove(prevPosition);
            }

            try {
                this.place(animal);
            }
            catch (IncorrectPositionException e) {
                e.printStackTrace();
            }
        }

        this.mapChanged("Animal moved");
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
    public List<WorldElement> getElements() {
        // TODO
        return List.of();
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
    public boolean canMoveTo(Vector2d position) {
        return this.lowerLeft.getY() <= position.getY() && position.getY() <= this.upperRight.getY();
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
