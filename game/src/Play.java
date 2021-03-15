/**
 * Play representation
 *
 * @author Abhishek Inamdar
 */
public class Play {
    /**
     * Move object
     */
    private Move move;

    /**
     * State object
     */
    private State state;

    /**
     * Count of evaluated States
     */
    private int searchCount;

    /**
     * Constructor method
     *
     * @param move        Move
     * @param state       State
     * @param searchCount count of searched states
     */
    public Play(Move move, State state, int searchCount) {
        this.move = move;
        this.state = state;
        this.searchCount = searchCount;
    }

    /**
     * returns Move object
     *
     * @return Move object
     */
    public Move getMove() {
        return move;
    }

    /**
     * returns State object
     *
     * @return State object
     */
    public State getState() {
        return state;
    }

    /**
     * returns number of searched states
     *
     * @return number of searched states
     */
    public int getSearchCount() {
        return searchCount;
    }
}