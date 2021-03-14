import java.util.Objects;

/**
 * Representation of Position in Fanorona game
 * at each intersection
 *
 * @author Abhishek Inamdar
 */
public class Position {
    /**
     * x-coordinate of the positions
     */
    private int x;

    /**
     * y-coordinate of the positions
     */
    private int y;

    /**
     * Constructor method
     *
     * @param x x-coordinate of the positions
     * @param y y-coordinate of the positions
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * return x-coordinate
     * @return x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * returns y-coordinate
     * @return y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * checks if other object is equal to this object
     *
     * @param o object to be checked
     * @return true if object is same as this, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    /**
     * calculates hashCode of this object
     *
     * @return hashCode value
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * return String representation
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}