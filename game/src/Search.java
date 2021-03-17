/**
 * Search interface
 *
 * @author Abhishek Inamdar
 */
public interface Search {
    /**
     * Performs Search and selects best successor
     *
     * @param currState  Current State
     * @param eval       Evaluation function
     * @param depthLimit DepthLimit
     * @param playerNum  PlayerNum of current player
     * @return Play chosen by current player
     */
    Play searchAndSelect(State currState, Evaluation eval, int depthLimit, int playerNum);
}
