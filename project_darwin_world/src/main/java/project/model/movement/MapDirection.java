package project.model.movement;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    private static final Vector2d NORTH_UNIT_VECTOR = new Vector2d(0, 1);
    private static final Vector2d EAST_UNIT_VECTOR = new Vector2d(1, 0);
    private static final Vector2d SOUTH_UNIT_VECTOR = new Vector2d(0, -1);
    private static final Vector2d WEST_UNIT_VECTOR = new Vector2d(-1, 0);

    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case EAST -> "Wschód";
            case SOUTH -> "Południe";
            case WEST -> "Zachód";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> WEST;
            case EAST -> NORTH;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> MapDirection.NORTH_UNIT_VECTOR;
            case EAST -> MapDirection.EAST_UNIT_VECTOR;
            case SOUTH -> MapDirection.SOUTH_UNIT_VECTOR;
            case WEST -> MapDirection.WEST_UNIT_VECTOR;
        };
    }
}
