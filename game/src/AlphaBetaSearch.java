import java.util.Map;

/**
 * Search implementation of Alpha Beta Game search
 *
 * @author Abhishek Inamdar
 */
public class AlphaBetaSearch implements Search {
    /**
     * Implementation of ALPHA-BETA Game Search
     *
     * @param currState  Current State
     * @param eval       Evaluation function
     * @param depthLimit DepthLimit
     * @param playerNum
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
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        if (playerNum == 1) {
            for (Move m : successors.keySet()) {
                searchCount++;
                StateValue moveValue = maxValue(successors.get(m), eval, depthLimit, alpha, beta);
                searchCount += moveValue.getSearchCount();
                if (moveValue.getBest() > alpha) {
                    alpha = moveValue.getBest();
                    selectedMove = m;
                }
            }
        } else {
            for (Move m : successors.keySet()) {
                searchCount++;
                StateValue moveValue = minValue(successors.get(m), eval, depthLimit, alpha, beta);
                searchCount += moveValue.getSearchCount();
                if (moveValue.getBest() < beta) {
                    beta = moveValue.getBest();
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
     * @param alpha          alpha value
     * @param beta           beta value
     * @return State Value with best value and Search count
     */
    private StateValue maxValue(State state, Evaluation eval, int remainingDepth, int alpha, int beta) {
        int searchCount = 0;
        // value of a state, given it is our move
        if (state.isTerminal() || isCutoff(remainingDepth)) {
            return new StateValue(eval.evaluate(state.getValueMap()), searchCount);
        }
        if (state.isDraw()) {
            return new StateValue(0, searchCount);
        }

        Map<Move, State> successors = state.getSuccessors();
        for (Move m : successors.keySet()) {
            searchCount++;
            //find value of each child state
            // it will be my opponent 's turn
            StateValue stateValue = minValue(successors.get(m), eval, remainingDepth - 1, alpha, beta);
            if (stateValue.getBest() > alpha) {
                alpha = stateValue.getBest();
            }
            // if better (for me) than my opponent's option,
            // no point to continue searching this branch
            if (alpha > beta) {
                return new StateValue(beta, searchCount);
            }
        }
        return new StateValue(alpha, searchCount);
    }

    /**
     * min value function
     *
     * @param state          Current State
     * @param eval           Evaluation function
     * @param remainingDepth remaining DepthLimit
     * @param alpha          alpha value
     * @param beta           beta value
     * @return State Value with best value and Search count
     */
    private StateValue minValue(State state, Evaluation eval, int remainingDepth, int alpha, int beta) {
        int searchCount = 0;
        // value of a state, given it is our move
        if (state.isTerminal() || isCutoff(remainingDepth)) {
            return new StateValue(eval.evaluate(state.getValueMap()), searchCount);
        }
        if (state.isDraw()) {
            return new StateValue(0, searchCount);
        }

        Map<Move, State> successors = state.getSuccessors();
        for (Move m : successors.keySet()) {
            searchCount++;
            //find value of each child state
            // it will be my opponent 's turn
            StateValue stateValue = maxValue(successors.get(m), eval, remainingDepth - 1, alpha, beta);
            if (stateValue.getBest() < beta) {
                beta = stateValue.getBest();
            }
            // if better (for me) than my opponent's option,
            // no point to continue searching this branch
            if (beta > alpha) {
                return new StateValue(alpha, searchCount);
            }
        }
        return new StateValue(beta, searchCount);
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