import java.util.Map;

/**
 * Main class for Fanorona Game
 *
 * @author Abhishek Inamdar
 */
public class Fanorona {
    /**
     * Main method
     *
     * @param args Ignored
     */
    public static void main(String[] args) {
        /**
         * Zero evaluation function
         */
        Evaluation evaluation0 = (valueMap, remainingDepth) -> {
            int whitePieces = 0;
            int blackPieces = 0;
            for (Position p : valueMap.keySet()) {
                if (valueMap.get(p) == 1) {
                    whitePieces++;
                } else if (valueMap.get(p) == 2) {
                    blackPieces++;
                }
            }
            double value = ((double) whitePieces - blackPieces);
            if (remainingDepth <= 0) {
                return 0;
            } else {
                //Not necessary, but just in case
                if (value < 0) {
                    return -100;
                } else {
                    return 100;
                }
            }
        };

        /**
         * First evaluation function
         */
        Evaluation evaluation1 = (valueMap, remainingDepth) -> {
            int whitePieces = 0;
            int blackPieces = 0;
            int total = valueMap.size();
            for (Position p : valueMap.keySet()) {
                if (valueMap.get(p) == 1) {
                    whitePieces++;
                } else if (valueMap.get(p) == 2) {
                    blackPieces++;
                }
            }
            double value = ((double) whitePieces - blackPieces) / total * 100;
            return (int) Math.round(value);
        };

        /**
         * Second evaluation function
         */
        Evaluation evaluation2 = (valueMap, remainingDepth) -> {
            int whitePieces = 0;
            int blackPieces = 0;
            int blank = 0;
            int total = valueMap.size();
            for (Position p : valueMap.keySet()) {
                if (valueMap.get(p) == 1) {
                    whitePieces++;
                } else if (valueMap.get(p) == 2) {
                    blackPieces++;
                } else {
                    blank++;
                }
            }
            if (whitePieces < blackPieces) {
                blank = -blank;
            }
            double value = (((double) whitePieces - blackPieces) / total
                    + ((double) blank) / total
            ) * 100;
            return (int) Math.round(value);
        };

        //Testing different scenarios
        System.out.println("\nBrute force with large depth limit and zero evaluation " +
                "function vs the same on 3x3 board");
        GameResult result1 = PlayGame(new Board(3, 3),
                new BruteForceSearch(), evaluation0, 10,
                new BruteForceSearch(), evaluation0, 10);
        printResults(result1);
        System.out.println();

        System.out.println("\nRandom vs brute-force with limit(s) of your choice, " +
                "either non-zero evaluation function, on 5x5 board");
        GameResult result2 = PlayGame(new Board(5, 5),
                new RandomSearch(), evaluation1, 10,
                new BruteForceSearch(), evaluation1, 10);
        printResults(result2);
        System.out.println();

        System.out.println("\nBrute-force with limit 1 vs brute-force with higher " +
                "limit of your choice, same evaluation function, on 5x5 board");
        GameResult result3 = PlayGame(new Board(5, 5),
                new BruteForceSearch(), evaluation2, 1,
                new BruteForceSearch(), evaluation2, 8);
        printResults(result3);
        System.out.println();

        System.out.println("\nBrute-force with limit of 10 and one evaluation function " +
                "\nvs same depth limit and another evaluation function, 5x5 board");
        GameResult result4 = PlayGame(new Board(5, 5),
                new BruteForceSearch(), evaluation1, 7,
                new BruteForceSearch(), evaluation2, 7);
        printResults(result4);
        System.out.println();

        System.out.println("\nBrute-force with limit of 5 and one evaluation function " +
                "\nvs same depth limit and another evaluation function, 5x5 board");
        GameResult result5 = PlayGame(new Board(5, 5),
                new BruteForceSearch(), evaluation1, 5,
                new BruteForceSearch(), evaluation2, 5);
        printResults(result5);
        System.out.println();

        System.out.println("\nBrute-force with limit of 11 and one evaluation function " +
                "\nvs same depth limit and another evaluation function, 5x5 board");
        GameResult result6 = PlayGame(new Board(5, 5),
                new BruteForceSearch(), evaluation1, 15,
                new BruteForceSearch(), evaluation2, 15);
        printResults(result6);
        System.out.println();

        System.out.println("\nAlpha-beta with limit of 15 and first evaluation function " +
                "\nvs same depth limit and second evaluation function, 5x5 board");
        GameResult result7 = PlayGame(new Board(5, 5),
                new AlphaBetaSearch(), evaluation1, 15,
                new AlphaBetaSearch(), evaluation2, 15);
        printResults(result7);
        System.out.println();

        System.out.println("\nAlpha-beta with limit of 15 and second evaluation function " +
                "\nvs same depth limit and first evaluation function, 5x5 board");
        GameResult result8 = PlayGame(new Board(5, 5),
                new AlphaBetaSearch(), evaluation2, 15,
                new AlphaBetaSearch(), evaluation1, 15);
        printResults(result8);
        System.out.println();

        System.out.println("\nBrute-force with limit of your choice and one evaluation function " +
                "\nvs Alpha-beta with same depth limit and same evaluation function, on 5x5 board");
        GameResult result9 = PlayGame(new Board(5, 5),
                new BruteForceSearch(), evaluation1, 6,
                new AlphaBetaSearch(), evaluation1, 6);
        printResults(result9);
        System.out.println();
    }

    /**
     * Method to play the game based on given parameters
     *
     * @param board             Board
     * @param player1Search     Player 1's search algorithm
     * @param player1Eval       Player 1's Evaluation Function
     * @param player1DepthLimit Player 1's Depth limit
     * @param player2Search     Player 2's search algorithm
     * @param player2Eval       Player 2's Evaluation Function
     * @param player2DepthLimit Player 2's Depth limit
     * @return Results of the game
     */
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
            if (currState.isDraw()) {
                playerNum = 0;
                isGameOn = false;
            }
        }

        int playerWon = 0;
        if (playerNum == 1) {
            playerWon = 2;
        } else if (playerNum == 2) {
            playerWon = 1;
        }

        return new GameResult(playerWon, p1.getMoveCount(), p2.getMoveCount());
    }

    /**
     * method to print results
     *
     * @param result Game result object
     */
    private static void printResults(GameResult result) {
        int playerWon = result.getPlayerWon();
        Map<Move, Integer> moveCountMapP1 = result.getPlayer1moveCounts();
        Map<Move, Integer> moveCountMapP2 = result.getPlayer2moveCounts();
        System.out.println("************************************************************************");
        if (playerWon == 0) {
            System.out.println("Game Drawn!!");
        } else {
            System.out.println("Player Won: " + playerWon);
        }
        System.out.print("Player 1 Number of moves: ");
        System.out.println(moveCountMapP1.size());
        System.out.print("Player 1 amount of states visited: ");
        System.out.println(moveCountMapP1.values().stream().mapToLong(i -> i).sum());

        System.out.print("Player 2 Number of moves: ");
        System.out.println(moveCountMapP2.size());
        System.out.print("Player 2 amount of states visited: ");
        System.out.println(moveCountMapP2.values().stream().mapToLong(i -> i).sum());
    }
}

/**
 * Plain Object for Game Result representation
 */
class GameResult {
    private int playerWon;
    private Map<Move, Integer> player1moveCounts;
    private Map<Move, Integer> player2moveCounts;

    public GameResult(int playerWon, Map<Move, Integer> player1moveCounts,
                      Map<Move, Integer> player2moveCounts) {
        this.playerWon = playerWon;
        this.player1moveCounts = player1moveCounts;
        this.player2moveCounts = player2moveCounts;
    }

    public int getPlayerWon() {
        return playerWon;
    }

    public Map<Move, Integer> getPlayer1moveCounts() {
        return player1moveCounts;
    }

    public Map<Move, Integer> getPlayer2moveCounts() {
        return player2moveCounts;
    }
}
