package project.model.map;

import project.model.util.MapChangeListener;
import project.model.exceptions.IncorrectPositionException;
import project.model.movement.MapDirection;
import project.model.movement.Vector2d;
import project.model.util.MapVisualizer;
import project.model.worldelements.Animal;
import project.model.worldelements.WorldElement;

import java.util.*;

abstract public class AbstractWorldMap implements WorldMap {
    public static final Vector2d DEFAULT_POSITION = new Vector2d(0, 0);
    public static final String PLACE_MESSAGE_TEMPLATE = "Animal '%s' was placed at %s";
    public static final String CHANGE_POSITION_MESSAGE_TEMPLATE = "Animal '%s' was moved from %s to %s";
    public static final String NO_POSITION_CHANGE_MESSAGE_TEMPLATE = "Animal '%s' at %s wanted to move forward, but this move " +
            "is not allowed";
    public static final String CHANGE_ORIENTATION_MESSAGE_TEMPLATE = "Animal '%s' at %s changed it's orientation " +
            "from %s to %s";
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final MapVisualizer mapVisualizer  = new MapVisualizer(this);
    protected Vector2d mapLowerLeftBoundary = DEFAULT_POSITION;
    protected Vector2d mapUpperRightBoundary = DEFAULT_POSITION;
    private final List<MapChangeListener> subscribers = new ArrayList<>();
    private final UUID id;

    public AbstractWorldMap() {
        this.id = UUID.randomUUID();
    }

    private String createPlaceMessage(Animal animal) {
        return String.format(AbstractWorldMap.PLACE_MESSAGE_TEMPLATE, animal.toString(), animal.getPosition().toString());
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        final Vector2d positionToPlace = animal.getPosition();
        if (this.canMoveTo(positionToPlace)) {
            this.animals.put(positionToPlace, animal);
            this.mapChanged(this.createPlaceMessage(animal));
        }
        else {
            throw new IncorrectPositionException(positionToPlace);
        }
    }

    private String createChangePositionMessage(Animal animal, Vector2d prevPosition) {
        return String.format(AbstractWorldMap.CHANGE_POSITION_MESSAGE_TEMPLATE, animal.toString(),
                prevPosition.toString(), animal.getPosition().toString());
    }

    private String createChangeOrientationMessage(Animal animal, MapDirection prevOrientation) {
        return String.format(AbstractWorldMap.CHANGE_ORIENTATION_MESSAGE_TEMPLATE, animal.toString(),
                animal.getPosition().toString(), prevOrientation.toString(), animal.getOrientation().toString());
    }

    private String createNoPositionChangeMessage(Animal animal) {
        return String.format(AbstractWorldMap.NO_POSITION_CHANGE_MESSAGE_TEMPLATE, animal.toString(),
                animal.getPosition().toString());
    }

    @Override
    public void move(Animal animal) {
        // allow subclasses to perform additional actions
        this.preMove(animal);

        var prevAnimalPosition = animal.getPosition();
        var prevAnimalOrientation = animal.getOrientation();
        animal.move(this);

        String message;

        // check if animal changed it's position
        var currentAnimalPosition = animal.getPosition();
        if (!Objects.equals(prevAnimalPosition, currentAnimalPosition)) {
            this.animals.remove(prevAnimalPosition);
            this.animals.put(currentAnimalPosition, animal);
            message = this.createChangePositionMessage(animal, prevAnimalPosition);
        }
        // check if animal changed it's orientation
        else if (!Objects.equals(prevAnimalOrientation, animal.getOrientation())) {
            message = this.createChangeOrientationMessage(animal, prevAnimalOrientation);
        }
        // move is not allowed
        else {
            message = this.createNoPositionChangeMessage(animal);
        }

        // allow subclasses to perform additional actions
        this.postMove(animal);

        this.mapChanged(message);
    }

    protected void preMove(Animal animal) {
        // space for subclasses to add logic before moving animal
    }

    protected void postMove(Animal animal) {
        // space for subclasses to add logic after moving animal
    }

    @Override
    public Optional<WorldElement> objectAt(Vector2d position) {
        return Optional.ofNullable(this.animals.get(position));
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !this.animals.containsKey(position);
    }

    @Override
    public String toString() {
        var currentBounds = this.getCurrentBounds();
        return this.mapVisualizer.draw(currentBounds.lowerLeft(), currentBounds.upperRight());
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(this.mapLowerLeftBoundary, this.mapUpperRightBoundary);
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public List<WorldElement> getElements() {
        return new ArrayList<>(this.animals.values());
    }

    public void subscribe(MapChangeListener newSubscriber) {
        this.subscribers.add(newSubscriber);
    }

    public void unsubscribe(MapChangeListener subscriber) {
        this.subscribers.remove(subscriber);
    }

    protected void mapChanged(String message) {
        for (var subscriber : this.subscribers) {
            subscriber.mapChanged(this, message);
        }
    }
}
