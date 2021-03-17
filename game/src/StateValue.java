/**
 * State Value representation
 */
public class StateValue {
    /**
     * Best value - Integer
     */
    int best;

    /**
     * Number of States explored (searched)
     */
    int searchCount;

    /**
     * Constructor method
     *
     * @param best        best value
     * @param searchCount search count
     */
    public StateValue(int best, int searchCount) {
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
