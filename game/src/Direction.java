/**
 * Enum Direction
 *
 * @author Abhishek Inamdar
 */
public enum Direction {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT,
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT;

    /**
     * returns opposite direction of the given direction
     *
     * @param direction direction
     * @return Opposite Direction
     */
    public static Direction getOppositeDirection(Direction direction){
        if (direction == Direction.TOP) {
            return Direction.BOTTOM;
        } else if (direction == Direction.BOTTOM) {
            return Direction.TOP;
        } else if (direction == Direction.LEFT) {
            return Direction.RIGHT;
        } else if (direction == Direction.RIGHT) {
            return Direction.LEFT;
        } else if (direction == Direction.TOP_LEFT) {
            return Direction.BOTTOM_RIGHT;
        } else if (direction == Direction.TOP_RIGHT) {
            return Direction.BOTTOM_LEFT;
        } else if (direction == Direction.BOTTOM_LEFT) {
            return Direction.TOP_RIGHT;
        } else {
            return Direction.TOP_LEFT;
        }
    }
}
