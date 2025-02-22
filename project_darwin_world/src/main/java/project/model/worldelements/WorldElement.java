package project.model.worldelements;

import project.model.movement.Vector2d;

public interface WorldElement {

    /**
     * Return object's position
     *
     * @return Vector2d object representing WorldElement object's position on map.
     */
    public Vector2d getPosition();

    /**
     * Return path to the resource that represents given world element
     *
     * @return String object that represents path to resource of given world element
     */
    public String getResourcePath();
}
