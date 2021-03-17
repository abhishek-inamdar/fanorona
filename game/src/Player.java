import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Player representation
 *
 * @author Abhishek Inamdar
 */
public class Player {
    /**
     * Evaluation Object
     */
    private final Evaluation evaluation;

    /**
     * Search Algorithm Object
     */
    private final Search sAlgo;

    /**
     * Depth Limit related search algorithm
     */
    private final int depthLimit;

    /**
     * Player's Identifier
     * 1 = Player 1, 2 = Player 2
     */
    private final int playerNum;

    /**
     * Map to store count of states evaluated for each move
     */
    private Map<Move, Integer> moveCount;

    /**
     * Constructor method
     * @param evaluation Evaluation
     * @param sAlgo Search algorithm
     * @param depthLimit Depth limit related to search algorithm
     * @param playerNum Player identifier
     */
    public Player(Evaluation evaluation, Search sAlgo, int depthLimit, int playerNum) {
        this.evaluation = evaluation;
        this.sAlgo = sAlgo;
        this.depthLimit = depthLimit;
        this.playerNum = playerNum;
        moveCount = new LinkedHashMap<>();
    }

    /**
     * return moveCount Map
     * @return moveCount Map
     */
    public Map<Move, Integer> getMoveCount() {
        return moveCount;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public Search getsAlgo() {
        return sAlgo;
    }

    public int getDepthLimit() {
        return depthLimit;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * plays one move for this player based on current State
     * amd returns new State
     * @param curState Current Game State
     * @return New State after player's move
     */
    public State play(State curState) {
        Play play = sAlgo.searchAndSelect(curState, evaluation, depthLimit, playerNum);
        moveCount.put(play.getMove(), play.getSearchCount());
        return play.getState();
    }

    public State play(State curState, int depthLimit){
        Play play = sAlgo.searchAndSelect(curState, evaluation, depthLimit, playerNum);
        moveCount.put(play.getMove(), play.getSearchCount());
        return play.getState();
    }
}
