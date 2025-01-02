package project.model;

import java.util.Objects;

public class Vector2d {
    private final int x;

    private final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", this.getX(), this.getY());
    }

    public boolean precedes(Vector2d other) {
        return this.getX() <= other.getX() && this.getY() <= other.getY();
    }

    public boolean follows(Vector2d other) {
        return this.getX() >= other.getX() && this.getY() >= other.getY();
    }

    public Vector2d add(Vector2d other) {
        int newX = this.getX() + other.getX();
        int newY = this.getY() + other.getY();
        return new Vector2d(newX, newY);
    }

    public Vector2d subtract(Vector2d other) {
        int newX = this.getX() - other.getX();
        int newY = this.getY() - other.getY();
        return new Vector2d(newX, newY);
    }

    public Vector2d upperRight(Vector2d other) {
        int upperRightX = Math.max(this.getX(), other.getX());
        int upperRightY = Math.max(this.getY(), other.getY());
        return new Vector2d(upperRightX, upperRightY);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int upperRightX = Math.min(this.getX(), other.getX());
        int upperRightY = Math.min(this.getY(), other.getY());
        return new Vector2d(upperRightX, upperRightY);
    }

    public Vector2d opposite() {
        return new Vector2d(-this.getX(), -this.getY());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Vector2d otherVector2d)) {
            return false;
        }

        return x == otherVector2d.x && y == otherVector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
