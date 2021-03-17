import java.util.Map;

public class Fanorona {
    public static void main(String[] args) {
        //TODO add main code

        Board boardThreeByThree = new Board(3, 3);
        Board boardFiveByFive = new Board(5, 5);
        Board boardFiveByNine = new Board(9, 5);

        Search bruteForceSearch = new BruteForceSearch();
        Search randomSearch = new BruteForceSearch();

        int depthLimitOne = 1;
        int depthLimitChoice = 3;
        int depthLimitLarge = 8;

        Evaluation evaluation = valueMap -> {
            int whitePieces = 0;
            int blackPieces = 0;
            for (Position p : valueMap.keySet()) {
                if (valueMap.get(p) == 1) {
                    whitePieces++;
                } else if (valueMap.get(p) == 2) {
                    blackPieces++;
                }
            }
            return whitePieces - blackPieces;
        };


        GameResult result = PlayGame(boardFiveByFive,
                bruteForceSearch, evaluation, depthLimitChoice,
                bruteForceSearch, evaluation, depthLimitChoice);

        printResults(result);
    }

    private static void printResults(GameResult result) {
        int playerWon = result.getPlayerWon();
        Map<Move, Integer> moveCountMapP1 = result.getPlayer1moveCounts();
        Map<Move, Integer> moveCountMapP2 = result.getPlayer2moveCounts();
        System.out.println("Player Won: " + playerWon);
        System.out.println("Player 1 moveCount:");
        System.out.println(moveCountMapP1.values().stream().mapToLong(i -> i).sum());
        System.out.println("Player 2 moveCount:");
        System.out.println(moveCountMapP2.values().stream().mapToLong(i -> i).sum());
    }

    private static GameResult PlayGame(Board board,
                                       Search player1Search, Evaluation player1Eval, int player1DepthLimit,
                                       Search player2Search, Evaluation player2Eval, int player2DepthLimit) {
        Player p1 = new Player(player1Eval, player1Search, player1DepthLimit, 1);
        Player p2 = new Player(player2Eval, player2Search, player2DepthLimit, 2);
        State startState = new State(board.getConnectionMap(), board.getValueMap(), p1, p2);
        boolean isGameOn = true;
        int playerNum = 1;
        boolean isDraw = false;
        State currState = startState;
        while (isGameOn) {
            if (playerNum == 1) {
                currState = p1.play(currState);
                playerNum = 2;
            } else {
                currState = p2.play(currState);
                playerNum = 1;
            }
            if (currState.isTerminal()) {
                isGameOn = false;
            }
            if (currState.isDraw()){
                isGameOn = false;
                isDraw = true;
            }
        }

        int playerWon = 0;
        if(playerNum == 1){
            playerWon = 2;
        } else {
            playerWon = 1;
        }

        return new GameResult(playerWon, isDraw, p1.getMoveCount(), p2.getMoveCount());
    }


}

class GameResult{
    private int playerWon;
    private boolean isDraw;
    private Map<Move, Integer> player1moveCounts;
    private Map<Move, Integer> player2moveCounts;

    public GameResult(int playerWon, boolean isDraw, Map<Move, Integer> player1moveCounts, Map<Move, Integer> player2moveCounts) {
        this.playerWon = playerWon;
        this.isDraw = isDraw;
        this.player1moveCounts = player1moveCounts;
        this.player2moveCounts = player2moveCounts;
    }

    public int getPlayerWon() {
        return playerWon;
    }

    public boolean isDraw() {
        return isDraw;
    }

    public Map<Move, Integer> getPlayer1moveCounts() {
        return player1moveCounts;
    }

    public Map<Move, Integer> getPlayer2moveCounts() {
        return player2moveCounts;
    }
}
