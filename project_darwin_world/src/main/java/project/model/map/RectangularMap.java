package project.model.map;

import project.model.movement.Vector2d;

public class RectangularMap extends AbstractWorldMap {
    public RectangularMap(int width, int height) {
        this.mapUpperRightBoundary = new Vector2d(width, height);
    }

    private boolean isOnMap(Vector2d position) {
        return position.precedes(this.mapUpperRightBoundary) &&
                position.follows(this.mapLowerLeftBoundary);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.canMoveTo(position) && this.isOnMap(position);
    }
}
