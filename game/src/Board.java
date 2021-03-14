import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Representation of Game Board of Fanorona Game
 *
 * @author Abhishek Inamdar
 */
public class Board {
    /**
     * Width of the board
     */
    private int width;

    /**
     * Height of the board
     */
    private int height;

    /**
     * Map of all the connected positions
     */
    private Map<Position, Set<Position>> connectionMap;

    /**
     * Map of values at each position
     * Value: 0 = No piece, 1 = white piece, 2 = black piece
     */
    private Map<Position, Integer> valueMap;

    /**
     * Constructor method
     *
     * @param width  width
     * @param height height
     */
    public Board(int width, int height) {
        if (!((width == 3 && height == 3)
                || (width == 5 && height == 5)
                || (width == 9 && height == 5))) {
            throw new IllegalArgumentException("Board Dimensions should be either 3*3 or 5*5 or 9*5");
        }
        this.width = width;
        this.height = height;
        connectionMap = new HashMap<>();
        valueMap = new HashMap<>();
        initializePositions();
        initializeConnections();
    }

    /**
     * Initialize positions at the start
     * (0,0) position will be considered as top-left
     * white pieces (Player 1) will be at the bottom
     * black pieces (Player 2) will be at the top
     * middle row will contain equal number of alternating pieces
     * with no piece in the middle
     */
    private void initializePositions() {
        int widthIndex = width - 1;
        int heightIndex = height - 1;
        int midRowIndex = (height - (height / 2)) - 1;
        int midColumnIndex = (width - (width / 2)) - 1;
        Position position = null;
        //Black pieces
        for (int y = 0; y < midRowIndex; y++) {
            for (int x = 0; x <= widthIndex; x++) {
                position = new Position(x, y);
                valueMap.put(position, 2);
            }
        }

        //White pieces
        for (int y = midRowIndex + 1; y <= heightIndex; y++) {
            for (int x = 0; x <= widthIndex; x++) {
                position = new Position(x, y);
                valueMap.put(position, 1);
            }
        }

        boolean isWhite = false;
        for (int x = 0; x <= widthIndex; x++) {
            if (x != midColumnIndex) {
                position = new Position(x, midRowIndex);
                if (isWhite) {
                    valueMap.put(position, 1);
                    isWhite = false;
                } else {
                    valueMap.put(position, 2);
                    isWhite = true;
                }
            }
        }
        // Middle value
        valueMap.put(new Position(midColumnIndex, midRowIndex), 0);
    }

    /**
     * initializes all the connections of  the positions
     * each position will have unique connections based on
     * its location and overall dimensions of the board
     */
    private void initializeConnections() {
        int widthIndex = width - 1;
        int heightIndex = height - 1;

        Position position = null;
        Set<Position> connections = null;
        for (int x = 0; x <= widthIndex; x++) {
            for (int y = 0; y <= heightIndex; y++) {
                position = new Position(x, y);
                connections = getConnections(x, y, widthIndex, heightIndex);
                connectionMap.put(position, connections);
            }
        }
    }

    /**
     * Get connections for the given position
     *
     * @param x           x-coordinate of the position
     * @param y           y-coordinate of the position
     * @param widthIndex  max value of the width index
     * @param heightIndex max value of the height index
     * @return Set of connections
     */
    private Set<Position> getConnections(final int x, final int y,
                                         final int widthIndex, final int heightIndex) {
        Set<Position> connections = new HashSet<>();
        getStraightConnections(connections, x, y, widthIndex, heightIndex);
        if ((x % 2 == 0 && y % 2 == 0) || (x % 2 != 0 && y % 2 != 0)) {
            // both even or both odd - get possible diagonal directions
            getDiagonalConnections(connections, x, y, widthIndex, heightIndex);
        }
        return connections;
    }

    /**
     * Adds all legal Straight line connections of the given position,
     * i.e. RIGHT, LEFT, TOP, BOTTOM
     *
     * @param connections connections of the positions
     * @param x           x-coordinate of the position
     * @param y           y-coordinate of the position
     * @param widthIndex  max value of the width index
     * @param heightIndex max value of the height index
     */
    private void getStraightConnections(Set<Position> connections, int x, int y,
                                        int widthIndex, int heightIndex) {
        // Right
        if (isLegalPosition(x + 1, y, widthIndex, heightIndex)) {
            connections.add(new Position(x + 1, y));
        }
        // Left
        if (isLegalPosition(x - 1, y, widthIndex, heightIndex)) {
            connections.add(new Position(x - 1, y));
        }
        // Bottom
        if (isLegalPosition(x, y + 1, widthIndex, heightIndex)) {
            connections.add(new Position(x, y + 1));
        }
        // Top
        if (isLegalPosition(x, y - 1, widthIndex, heightIndex)) {
            connections.add(new Position(x, y - 1));
        }
    }

    /**
     * Adds all legal Diagonal line connections of the given position,
     * i.e. LEFT-TOP, RIGHT-TOP, LEFT-BOTTOM, RIGHT-BOTTOM
     *
     * @param connections connections of the positions
     * @param x           x-coordinate of the position
     * @param y           y-coordinate of the position
     * @param widthIndex  max value of the width index
     * @param heightIndex max value of the height index
     */
    private void getDiagonalConnections(Set<Position> connections, int x, int y,
                                        int widthIndex, int heightIndex) {
        // Left-Top
        if (isLegalPosition(x - 1, y - 1, widthIndex, heightIndex)) {
            connections.add(new Position(x - 1, y - 1));
        }
        // Right-Top
        if (isLegalPosition(x + 1, y - 1, widthIndex, heightIndex)) {
            connections.add(new Position(x + 1, y - 1));
        }
        // Left-Bottom
        if (isLegalPosition(x - 1, y + 1, widthIndex, heightIndex)) {
            connections.add(new Position(x - 1, y + 1));
        }
        // Right-Bottom
        if (isLegalPosition(x + 1, y + 1, widthIndex, heightIndex)) {
            connections.add(new Position(x + 1, y + 1));
        }
    }

    /**
     * Determines if desired position is legal or not
     *
     * @param x           x-coordinate of the desired position
     * @param y           y-coordinate of the desired position
     * @param widthIndex  max value of the width index
     * @param heightIndex max value of the height index
     * @return true if position is legal, false otherwise
     */
    private boolean isLegalPosition(int x, int y, int widthIndex, int heightIndex) {
        return x >= 0 && x <= widthIndex && y >= 0 && y <= heightIndex;
    }

    /**
     * Gets Connection Map for this board
     * @return Connection Map for this board
     */
    public Map<Position, Set<Position>> getConnectionMap() {
        return connectionMap;
    }

    /**
     * Gets Value Map for this board
     * @return Value Map for this board
     */
    public Map<Position, Integer> getValueMap() {
        return valueMap;
    }

    /**
     * Changes value at given position
     *
     * @param x     x-coordinate of the position
     * @param y     y-coordinate of the position
     * @param value desired value at the position
     * @return true if value is set, false otherwise
     */
    public boolean setValue(int x, int y, int value) {
        Position position = new Position(x, y);
        return setValue(position, value);
    }

    /**
     * Changes value at given position
     *
     * @param position desired position
     * @param value    desired value at the position
     * @return true if value is set, false otherwise
     */
    public boolean setValue(Position position, int value) {
        if (value < 0 || value > 2)
            throw new IllegalArgumentException("Value should be either 0 or 1 or 2");

        if (valueMap.containsKey(position)) {
            valueMap.put(position, value);
            return true;
        }
        return false;
    }

    /**
     * String representation of the board
     * It will print all positions of the board
     * with their respective values
     *
     * @return String representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append("[").append(valueMap.get(new Position(x, y))).append("]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
