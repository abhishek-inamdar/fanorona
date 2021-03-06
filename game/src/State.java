import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * State representation
 *
 * @author Abhishek Inamdar
 */
public class State {
    /**
     * Map of all the connected positions
     */
    private final Map<Position, Set<Position>> connectionMap;

    /**
     * Map of values at each position
     * Value: 0 = No piece, 1 = white piece, 2 = black piece
     */
    private final Map<Position, Integer> valueMap;

    /**
     * Player identifier whose turn it is
     */
    private final Player turnPlayer;

    /**
     * Opponent player
     */
    private final Player opponent;

    /**
     * isDraw value
     */
    private boolean isDraw = false;

    /**
     * Constructor method
     *
     * @param connectionMap Connection Map of the board
     * @param valueMap      ValueMap of the board
     * @param turnPlayer    Player whose turn it is
     */
    public State(Map<Position, Set<Position>> connectionMap,
                 Map<Position, Integer> valueMap,
                 Player turnPlayer, Player opponent) {
        this.connectionMap = connectionMap;
        this.valueMap = valueMap;
        this.turnPlayer = turnPlayer;
        this.opponent = opponent;
    }

    /**
     * returns valueMap for this State
     *
     * @return valueMap
     */
    public Map<Position, Integer> getValueMap() {
        return valueMap;
    }

    /**
     * returns isDraw value
     *
     * @return isDraw value
     */
    public boolean isDraw() {
        return isDraw;
    }

    /**
     * sets isDraw value
     *
     * @param draw value for isDraw
     */
    public void setDraw(boolean draw) {
        isDraw = draw;
    }

    /**
     * returns successors of this State
     *
     * @return Map of Successors with each possible move
     */
    public Map<Move, State> getSuccessors() {
        Map<Move, State> successors = new HashMap<>();

        Set<Position> currPositions = new HashSet<>();
        getPlayerPositions(currPositions);

        Set<Move> possibleMoves = new HashSet<>();
        getPossibleMoves(currPositions, possibleMoves);

        Map<Position, Integer> lValueMap;

        for (Move move : possibleMoves) {
            lValueMap = new HashMap<>(valueMap);
            lValueMap.put(move.getStartPos(), 0);
            lValueMap.put(move.getEndPos(), turnPlayer.getPlayerNum());
            for (Position p : move.getAffectedPositions()) {
                lValueMap.put(p, 0);
            }
            successors.put(move, new State(connectionMap, lValueMap, opponent, turnPlayer));
        }
        return successors;
    }

    /**
     * Populates Current position set with player's piece positions
     *
     * @param currPositions all the pieces
     */
    private void getPlayerPositions(Set<Position> currPositions) {
        for (Position p : valueMap.keySet()) {
            if (valueMap.get(p) == turnPlayer.getPlayerNum()) {
                currPositions.add(p);
            }
        }
    }

    /**
     * Populates all the possible moves
     *
     * @param currPositions  Current positions of the pieces
     * @param attackingMoves Possible moves from available pieces
     */
    private void getPossibleMoves(Set<Position> currPositions, Set<Move> attackingMoves) {
        Set<Move> nonAttackingMoves = new HashSet<>();
        for (Position startPos : currPositions) {
            Set<Position> connections = connectionMap.get(startPos);
            for (Position endPos : connections) {
                if (valueMap.get(endPos) == 0) {
                    Set<Position> affectedPositions = new HashSet<>();
                    nonAttackingMoves.add(new Move(startPos, endPos, MoveType.NOATTACK, affectedPositions));
                    getAttackPositions(affectedPositions, startPos, endPos, MoveType.APPROACH);
                    if (!affectedPositions.isEmpty()) {
                        attackingMoves.add(new Move(startPos, endPos, MoveType.APPROACH, affectedPositions));
                    }
                    affectedPositions = new HashSet<>();
                    getAttackPositions(affectedPositions, startPos, endPos, MoveType.RETREAT);
                    if (!affectedPositions.isEmpty()) {
                        attackingMoves.add(new Move(startPos, endPos, MoveType.RETREAT, affectedPositions));
                    }
                }
            }

        }
        if (attackingMoves.isEmpty()) {
            //Attacking moves Not present
            attackingMoves.addAll(nonAttackingMoves);
        }
    }

    /**
     * populates affected positions based on given attacking move
     *
     * @param affectedPositions Set positions affected
     * @param startPos          Starting position of the move
     * @param endPos            Ending position of the move
     * @param moveType          Move Type
     */
    private void getAttackPositions(Set<Position> affectedPositions,
                                    Position startPos, Position endPos,
                                    MoveType moveType) {
        Direction direction = getDirectionOfMove(startPos, endPos);
        if (moveType == MoveType.RETREAT) {
            direction = Direction.getOppositeDirection(direction);
            getAffectedPositions(affectedPositions, startPos, direction);
        } else {
            getAffectedPositions(affectedPositions, endPos, direction);
        }
    }

    /**
     * returns the direction of the move
     * based on starting and ending positions
     *
     * @param startPos Starting position
     * @param endPos   ending positions
     * @return direction
     */
    private Direction getDirectionOfMove(Position startPos, Position endPos) {
        int startX = startPos.getX();
        int startY = startPos.getY();
        int endX = endPos.getX();
        int endY = endPos.getY();

        if (startX == endX) {
            if (startY < endY) {
                return Direction.BOTTOM;
            } else {
                return Direction.TOP;
            }
        } else if (startY == endY) {
            if (startX < endX) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else if (startX > endX && startY > endY) {
            return Direction.TOP_LEFT;
        } else if (startX < endX && startY > endY) {
            return Direction.TOP_RIGHT;
        } else if (startX > endX && startY < endY) {
            return Direction.BOTTOM_LEFT;
        } else {
            return Direction.BOTTOM_RIGHT;
        }
    }

    /**
     * populates affected positions based on Position and Direction
     *
     * @param affectedPositions affected positions
     * @param position          original positions
     * @param direction         Direction of the effect
     */
    private void getAffectedPositions(Set<Position> affectedPositions,
                                      Position position, Direction direction) {
        boolean flag = true;
        Position currPos = new Position(position.getX(), position.getY());
        while (flag) {
            Position nextPos = getValidNextPosition(currPos, direction);
            if (nextPos != null
                    && valueMap.get(nextPos) != 0
                    && valueMap.get(nextPos) != turnPlayer.getPlayerNum()) {
                affectedPositions.add(nextPos);
                currPos = nextPos;
            } else {
                flag = false;
            }
        }
    }

    /**
     * returns next valid position on the board,
     * based on current position and direction
     *
     * @param currPos   current position
     * @param direction desired direction
     * @return next position if valid, null otherwise
     */
    private Position getValidNextPosition(Position currPos, Direction direction) {
        int curX = currPos.getX();
        int curY = currPos.getY();
        int nextX, nextY;
        switch (direction) {
            case TOP -> {
                nextX = curX;
                nextY = curY - 1;
            }
            case BOTTOM -> {
                nextX = curX;
                nextY = curY + 1;
            }
            case LEFT -> {
                nextX = curX - 1;
                nextY = curY;
            }
            case RIGHT -> {
                nextX = curX + 1;
                nextY = curY;
            }
            case TOP_LEFT -> {
                nextX = curX - 1;
                nextY = curY - 1;
            }
            case TOP_RIGHT -> {
                nextX = curX + 1;
                nextY = curY - 1;
            }
            case BOTTOM_LEFT -> {
                nextX = curX - 1;
                nextY = curY + 1;
            }
            case BOTTOM_RIGHT -> {
                nextX = curX + 1;
                nextY = curY + 1;
            }
            default -> {
                nextX = -1;
                nextY = -1;
            }
        }
        if (nextX != -1 && nextY != -1 && valueMap.containsKey(new Position(nextX, nextY))) {
            return new Position(nextX, nextY);
        } else {
            return null;
        }
    }

    /**
     * checks for terminal condition
     * terminal state will contain pieces of only one color
     *
     * @return true if terminal state, false otherwise
     */
    public boolean isTerminal() {
        Set<Position> currPlayerPieces = new HashSet<>();
        Set<Position> oppPlayerPieces = new HashSet<>();
        for (Position p : valueMap.keySet()) {
            if (valueMap.get(p) == turnPlayer.getPlayerNum()) {
                currPlayerPieces.add(p);
            } else {
                oppPlayerPieces.add(p);
            }
        }
        return currPlayerPieces.isEmpty() || oppPlayerPieces.isEmpty();
    }

    /**
     * Returns payOff of this State
     * :PRECONDITION: isTerminal() is true
     * assigns 100 if Player1 is winning or -100 if Player 2 is winning
     *
     * @return payoff value
     */
    public int payOff() {
        int player1Count = 0, player2Count = 0;
        for (Position p : valueMap.keySet()) {
            if (valueMap.get(p) == 1) {
                player1Count++;
            } else if (valueMap.get(p) == 2) {
                player2Count++;
            }
        }
        if (player1Count > player2Count) {
            return 100;
        } else if (player1Count < player2Count) {
            return -100;
        } else {
            return 0;
        }
    }

    /**
     * returns Set Positions for given Player
     *
     * @param playerNum Player number
     * @return Positions
     */
    public Set<Position> getPieces(int playerNum) {
        Set<Position> pieces = new HashSet<>();
        for (Position p : valueMap.keySet()) {
            if (valueMap.get(p) == playerNum) {
                pieces.add(p);
            }
        }
        return pieces;
    }
}
