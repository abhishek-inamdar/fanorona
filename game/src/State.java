import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class State {
    /**
     * Map of all the connected positions
     */
    private Map<Position, Set<Position>> connectionMap;

    /**
     * Map of values at each position
     * Value: 0 = No piece, 1 = white piece, 2 = black piece
     */
    private Map<Position, Integer> valueMap;

    private int turnPlayer;


    public State(Map<Position, Set<Position>> connectionMap,
                 Map<Position, Integer> valueMap, int turnPlayer) {
        this.connectionMap = connectionMap;
        this.valueMap = valueMap;
        this.turnPlayer = turnPlayer;
    }

    public Set<State> getSuccessors() {
        Set<State> successors = new HashSet<>();
        Set<Position> currPositions = new HashSet<>();
        getPlayerPositions(currPositions);

        Set<Move> possibleMoves = new HashSet<>();
        getPossibleMoves(currPositions, possibleMoves);

        Map<Position, Integer> lValueMap;
        int nextTurnPlayer;
        if (turnPlayer == 1) {
            nextTurnPlayer = 2;
        } else {
            nextTurnPlayer = 1;
        }
        for (Move move : possibleMoves) {
            lValueMap = new HashMap<>(valueMap);
            lValueMap.put(move.getStartPos(), 0);
            lValueMap.put(move.getEndPos(), turnPlayer);
            for (Position p : move.getAffectedPositions()) {
                lValueMap.put(p, 0);
            }
            successors.add(new State(connectionMap, lValueMap, nextTurnPlayer));
        }
        return successors;
    }

    private void getPlayerPositions(Set<Position> currPositions) {
        for (Position p : valueMap.keySet()) {
            if (valueMap.get(p) == turnPlayer) {
                currPositions.add(p);
            }
        }
    }

    private void getPossibleMoves(Set<Position> currPositions, Set<Move> possibleMoves) {
        Set<Move> nonAttckingMoves = new HashSet<>();
        for (Position startPos : currPositions) {
            Set<Position> connections = connectionMap.get(startPos);
            for (Position endPos : connections) {
                if (valueMap.get(endPos) == 0) {
                    Set<Position> affectedPositions = new HashSet<>();
                    nonAttckingMoves.add(new Move(startPos, endPos, MoveType.NOATTACK, affectedPositions));
                    getAttackPositions(affectedPositions, startPos, endPos, MoveType.APPROACH);
                    if (!affectedPositions.isEmpty()) {
                        possibleMoves.add(new Move(startPos, endPos, MoveType.APPROACH, affectedPositions));
                    }
                    affectedPositions = new HashSet<>();
                    getAttackPositions(affectedPositions, startPos, endPos, MoveType.RETREAT);
                    if (!affectedPositions.isEmpty()) {
                        possibleMoves.add(new Move(startPos, endPos, MoveType.RETREAT, affectedPositions));
                    }
                }
            }

        }
        if (possibleMoves.isEmpty()) {
            //Attacking moves Not present
            possibleMoves.addAll(nonAttckingMoves);
        }
    }

    private void getAttackPositions(Set<Position> affectedPositions,
                                    Position startPos, Position endPos,
                                    MoveType moveType) {
        Direction direction = getDirectionOfMove(startPos, endPos);
        if (moveType == MoveType.RETREAT) {
            direction = getOppositeDirection(direction);
            getAffectedPositions(affectedPositions, startPos, direction);
        } else {
            getAffectedPositions(affectedPositions, endPos, direction);
        }
    }

    private void getAffectedPositions(Set<Position> affectedPositions,
                                      Position position, Direction direction) {
        boolean flag = true;
        Position currPos = new Position(position.getX(), position.getY());
        while (flag) {
            Position nextPos = getValidNextPosition(currPos, direction);
            if (nextPos != null
                    && valueMap.get(nextPos) != 0
                    && valueMap.get(nextPos) != turnPlayer) {
                affectedPositions.add(nextPos);
                currPos = nextPos;
            } else {
                flag = false;
            }
        }
    }

    private Position getValidNextPosition(Position currPos, Direction direction) {
        int curX = currPos.getX();
        int curY = currPos.getY();
        int nextX = -1;
        int nextY = -1;
        switch (direction) {
            case TOP:
                nextX = curX;
                nextY = curY - 1;
                break;
            case BOTTOM:
                nextX = curX;
                nextY = curY + 1;
                break;
            case LEFT:
                nextX = curX - 1;
                nextY = curY;
                break;
            case RIGHT:
                nextX = curX + 1;
                nextY = curY;
                break;
            case TOP_LEFT:
                nextX = curX - 1;
                nextY = curY - 1;
                break;
            case TOP_RIGHT:
                nextX = curX + 1;
                nextY = curY - 1;
                break;
            case BOTTOM_LEFT:
                nextX = curX - 1;
                nextY = curY + 1;
                break;
            case BOTTOM_RIGHT:
                nextX = curX + 1;
                nextY = curY + 1;
                break;
            default:
                nextX = -1;
                nextY = -1;
                break;
        }
        if (nextX != -1 && nextY != -1 && valueMap.containsKey(new Position(nextX, nextY))) {
            return new Position(nextX, nextY);
        } else {
            return null;
        }
    }

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

    private Direction getOppositeDirection(final Direction direction) {
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
