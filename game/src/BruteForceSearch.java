import java.util.Map;

/**
 * Search implementation of Brute Force minimax search
 *
 * @author Abhishek Inamdar
 */
public class BruteForceSearch implements Search {
    /**
     * Implementation of MINIMAX Game Search
     *
     * @param currState  Current State
     * @param eval       Evaluation function
     * @param depthLimit DepthLimit
     * @param playerNum  PlayerNum of current player
     * @return Play chosen by current player
     */
    @Override
    public Play searchAndSelect(State currState, Evaluation eval, int depthLimit, int playerNum) {
        if (currState == null || eval == null) {
            throw new IllegalArgumentException("State and Evaluation can not be null");
        }
        if (playerNum != 1 && playerNum != 2) {
            throw new IllegalArgumentException("playerNum must be either 1 or 2");
        }
        int searchCount = 0;
        Map<Move, State> successors = currState.getSuccessors();
        Move selectedMove = null;
        if (playerNum == 1) {
            int value = Integer.MIN_VALUE;
            for (Move m : successors.keySet()) {
                searchCount++;
                StateValueBF moveValue = maxValue(successors.get(m), eval, depthLimit);
                searchCount += moveValue.getSearchCount();
                if (moveValue.getBest() > value) {
                    value = moveValue.getBest();
                    selectedMove = m;
                }
            }
        } else {
            int value = Integer.MAX_VALUE;
            for (Move m : successors.keySet()) {
                searchCount++;
                StateValueBF moveValue = minValue(successors.get(m), eval, depthLimit);
                searchCount += moveValue.getSearchCount();
                if (moveValue.getBest() < value) {
                    value = moveValue.getBest();
                    selectedMove = m;
                }
            }
        }
        return new Play(selectedMove, successors.get(selectedMove), searchCount);
    }

    /**
     * max value function
     *
     * @param state          Current State
     * @param eval           Evaluation function
     * @param remainingDepth remaining DepthLimit
     * @return State Value with best value and Search count
     */
    private StateValueBF maxValue(State state, Evaluation eval, int remainingDepth) {
        int searchCount = 0;
        // value of a state, given it is our move
        if (state.isTerminal()) {
            return new StateValueBF(state.payOff(), searchCount);
        }
        if (isCutoff(remainingDepth)) {
            return new StateValueBF(eval.evaluate(state.getValueMap()), searchCount);
        }
        if (state.isDraw()) {
            return new StateValueBF(0, searchCount);
        }
        int best = Integer.MIN_VALUE;
        Map<Move, State> successors = state.getSuccessors();
        for (Move m : successors.keySet()) {
            searchCount++;
            //find value of each child state
            // it will be my opponent 's turn
            StateValueBF stateValue = minValue(successors.get(m), eval, remainingDepth - 1);
            if (stateValue.getBest() > best) {
                best = stateValue.getBest();
            }
        }
        return new StateValueBF(best, searchCount);
    }

    /**
     * min value function
     *
     * @param state          Current State
     * @param eval           Evaluation function
     * @param remainingDepth remaining DepthLimit
     * @return State Value with best value and Search count
     */
    private StateValueBF minValue(State state, Evaluation eval, int remainingDepth) {
        int searchCount = 0;
        // value of a state, given it is our move
        if (state.isTerminal()) {
            return new StateValueBF(state.payOff(), searchCount);
        }
        if (isCutoff(remainingDepth)) {
            return new StateValueBF(eval.evaluate(state.getValueMap()), searchCount);
        }
        if (state.isDraw()) {
            return new StateValueBF(0, searchCount);
        }
        int best = Integer.MAX_VALUE;
        Map<Move, State> successors = state.getSuccessors();
        for (Move m : successors.keySet()) {
            searchCount++;
            //find value of each child state
            // it will be my opponent 's turn
            StateValueBF stateValue = maxValue(successors.get(m), eval, remainingDepth - 1);
            if (stateValue.getBest() < best) {
                best = stateValue.getBest();
            }
        }
        return new StateValueBF(best, searchCount);
    }

    /**
     * indicates to stop search
     *
     * @param remainingDepth remaining depth limit
     * @return true if remaining depth limit has reached zero
     */
    private boolean isCutoff(final int remainingDepth) {
        return remainingDepth <= 0;
    }
}

/**
 * State Value representation
 */
class StateValueBF {
    /**
     * Best value - Integer
     */
    private final int best;

    /**
     * Number of States explored (searched)
     */
    private final int searchCount;

    /**
     * Constructor method
     *
     * @param best        best value
     * @param searchCount search count
     */
    public StateValueBF(int best, int searchCount) {
        this.best = best;
        this.searchCount = searchCount;
    }

    /**
     * returns the best value
     *
     * @return best value
     */
    public int getBest() {
        return best;
    }

    /**
     * returns the search count
     *
     * @return search count
     */
    public int getSearchCount() {
        return searchCount;
    }
}
