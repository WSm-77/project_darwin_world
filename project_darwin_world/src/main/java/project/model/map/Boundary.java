package project.model.map;

import project.model.movement.Vector2d;

public record Boundary(Vector2d lowerLeft, Vector2d upperRight) {
}
