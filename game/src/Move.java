import java.util.Set;

/**
 * Move representation
 *
 * @author Abhishek Inamdar
 */
public class Move {
    /**
     * Starting Position
     */
    private final Position startPos;

    /**
     * Ending Position
     */
    private final Position endPos;

    /**
     * Move Type
     */
    private final MoveType moveType;

    /**
     * Affected Positions
     */
    private final Set<Position> affectedPositions;

    /**
     * Constructor method
     *
     * @param startPos          Starting Position
     * @param endPos            Ending Position
     * @param moveType          Move Type
     * @param affectedPositions Affected Positions
     */
    public Move(Position startPos, Position endPos, MoveType moveType,
                Set<Position> affectedPositions) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.moveType = moveType;
        this.affectedPositions = affectedPositions;
    }

    /**
     * returns Starting position
     *
     * @return Starting position
     */
    public Position getStartPos() {
        return startPos;
    }

    /**
     * returns Ending position
     *
     * @return Ending position
     */
    public Position getEndPos() {
        return endPos;
    }

    /**
     * returns Move Type
     *
     * @return Move Type
     */
    public MoveType getMoveType() {
        return moveType;
    }

    /**
     * returns affected positions
     *
     * @return affected positions
     */
    public Set<Position> getAffectedPositions() {
        return affectedPositions;
    }

    @Override
    public String toString() {
        return "Move{" +
                "startPos=" + startPos +
                ", endPos=" + endPos +
                ", moveType=" + moveType +
                ", affectedPositions=" + affectedPositions +
                '}';
    }
}
