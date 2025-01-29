package project.model.movement;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    private static final Vector2d NORTH_UNIT_VECTOR = new Vector2d(0, 1);
    private static final Vector2d NORTH_EAST_UNIT_VECTOR = new Vector2d(1, 1);
    private static final Vector2d EAST_UNIT_VECTOR = new Vector2d(1, 0);
    private static final Vector2d SOUTH_EAST_UNIT_VECTOR = new Vector2d(1, -1);
    private static final Vector2d SOUTH_UNIT_VECTOR = new Vector2d(0, -1);
    private static final Vector2d SOUTH_WEST_UNIT_VECTOR = new Vector2d(-1, -1);
    private static final Vector2d WEST_UNIT_VECTOR = new Vector2d(-1, 0);
    private static final Vector2d NORTH_WEST_UNIT_VECTOR = new Vector2d(-1, 1);
    private static final int OPPOSITE_DIRECTION_ROTATE_ANGLE = 4;

    public String toString() {
        return switch (this) {
            case NORTH -> "N";
            case NORTH_EAST -> "NE";
            case EAST -> "E";
            case SOUTH_EAST -> "SE";
            case SOUTH -> "S";
            case SOUTH_WEST -> "SW";
            case WEST -> "W";
            case NORTH_WEST -> "NW";
        };
    }

    public MapDirection rotate(int angleNum) {
        MapDirection[] values = MapDirection.values();
        return values[(this.ordinal() + angleNum) % values.length];
    }

    public MapDirection opposite() {
        return this.rotate(MapDirection.OPPOSITE_DIRECTION_ROTATE_ANGLE);
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> NORTH_WEST;
            case NORTH_EAST -> NORTH;
            case EAST -> NORTH_EAST;
            case SOUTH_EAST -> EAST;
            case SOUTH -> SOUTH_EAST;
            case SOUTH_WEST -> SOUTH;
            case WEST -> SOUTH_WEST;
            case NORTH_WEST -> WEST;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> MapDirection.NORTH_UNIT_VECTOR;
            case NORTH_EAST -> MapDirection.NORTH_EAST_UNIT_VECTOR;
            case EAST -> MapDirection.EAST_UNIT_VECTOR;
            case SOUTH_EAST -> MapDirection.SOUTH_EAST_UNIT_VECTOR;
            case SOUTH -> MapDirection.SOUTH_UNIT_VECTOR;
            case SOUTH_WEST -> MapDirection.SOUTH_WEST_UNIT_VECTOR;
            case WEST -> MapDirection.WEST_UNIT_VECTOR;
            case NORTH_WEST -> MapDirection.NORTH_WEST_UNIT_VECTOR;
        };
    }
}
