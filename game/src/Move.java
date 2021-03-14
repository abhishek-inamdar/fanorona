import java.util.Set;

public class Move {
    private Position startPos;
    private Position endPos;
    private MoveType moveType;
    private Set<Position> affectedPositions;

    public Move(Position startPos, Position endPos, MoveType moveType, Set<Position> affectedPositions) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.moveType = moveType;
        this.affectedPositions = affectedPositions;
    }

    public Position getStartPos() {
        return startPos;
    }

    public Position getEndPos() {
        return endPos;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public Set<Position> getAffectedPositions() {
        return affectedPositions;
    }
}
