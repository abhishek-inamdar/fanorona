import java.util.*;

/**
 * Search implementation: Random Search
 * Generates all possible moves and selects a random move
 *
 * @author Abhishek Inamdar
 */
public class RandomSearch implements Search{
    /**
     * Search implementation of Random Selection of successor
     * @param currState Current State
     * @param eval Evaluation function
     * @param depthLimit DepthLimit
     * @param playerNum PlayerNum of current player
     * @return Play chosen by current player
     */
    @Override
    public Play searchAndSelect(State currState, Evaluation eval, int depthLimit, int playerNum) {
        Map<Move, State> possibleMoves = currState.getSuccessors();
        List<Move> moves = new ArrayList<>(possibleMoves.keySet());
        Random random = new Random();
        Move randomMove = moves.get(random.nextInt(moves.size()));
        return new Play(randomMove, possibleMoves.get(randomMove), 1);
    }
}