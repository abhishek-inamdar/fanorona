public interface Search {
    Play searchAndSelect(State currState, Evaluation eval, int depthLimit, int playerNum);
}
