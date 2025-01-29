package project.presenter;

import project.model.movement.Vector2d;

public interface MapDrawerListener {
    void mapFieldClicked(Vector2d mapPosition);

    void mapDrawn();
}
