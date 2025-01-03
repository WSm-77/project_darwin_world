package project.model.map;

import project.model.exceptions.IncorrectPositionException;
import project.model.movement.Vector2d;
import project.model.util.RandomPositionGenerator;
import project.model.worldelements.Animal;
import project.model.worldelements.Grass;
import project.model.worldelements.WorldElement;

import java.util.*;

import java.lang.Math;

public class GrassField extends AbstractWorldMap {
    private final Map<Vector2d, Grass> grassFields = new HashMap<>();

    public GrassField(int totalGrassFieldOnTheMap) {
        int grassMaxXCoordinate = (int)Math.floor(Math.sqrt(10*totalGrassFieldOnTheMap));
        int grassMaxYCoordinate = grassMaxXCoordinate;

        var randomGrassPositions = new RandomPositionGenerator(grassMaxXCoordinate, grassMaxYCoordinate, totalGrassFieldOnTheMap);
        for (var grassPosition : randomGrassPositions) {
            var grass = new Grass(grassPosition);
            this.grassFields.put(grassPosition, grass);

            // update map boundaries
            this.updateBoundaries(grassPosition);
        }
    }

    private void updateUpperRightBoundary(Vector2d position) {
        this.mapUpperRightBoundary = this.mapUpperRightBoundary.upperRight(position);
    }

    private void updateLowerLeftBoundary(Vector2d position) {
        this.mapLowerLeftBoundary = this.mapLowerLeftBoundary.lowerLeft(position);
    }

    private void updateBoundaries(Vector2d position) {
        this.updateLowerLeftBoundary(position);
        this.updateUpperRightBoundary(position);
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        super.place(animal);
        this.updateBoundaries(animal.getPosition());
    }

    @Override
    protected void postMove(Animal animal) {
        this.updateBoundaries(animal.getPosition());
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied((position)) || this.grassFields.containsKey(position);
    }

    @Override
    public Optional<WorldElement> objectAt(Vector2d position) {
        var worldElement = super.objectAt(position);
        return worldElement.isPresent() ? worldElement : Optional.ofNullable(this.grassFields.get(position));
    }

    @Override
    public List<WorldElement> getElements() {
        var concatenatedList = super.getElements();
        concatenatedList.addAll(this.grassFields.values());
        return concatenatedList;
    }
}
