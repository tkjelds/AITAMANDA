import java.util.ArrayList;

public class OthelloAI3 implements IOthelloAI {

    private static final int _boardsize_ = 8;
    private static final int _player_ = 1;
    private int _maxDepth_ = 5;

    @Override
    public Position decideMove(GameState s) {
        return MiniMax(s);
    }

    
    /** 
     * @param s Takes in the current gameState
     * @return Position best position given at the given depth
     * This function initializes the MinMax Algorithm. 
     * All of the variables have been nanmed as close to the books variables as possible.
     */
    public Position MiniMax(GameState s){
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int v = Integer.MIN_VALUE;
        Position move = null;
        for (var a : s.legalMoves()) { 
            var nextState = new GameState(s.getBoard(), s.getPlayerInTurn());
            nextState.insertToken(a);
            var v2 = MinValue(nextState, alpha, beta, 0);
            if (v2 >= v) {
                v = v2;
                move = a;
                alpha = Math.max(alpha, v);
            }
            if (v >= beta)
                return move;
        }
        return move;
    }
    
    /** 
     * @param s The given gameState
     * @param alpha 
     * @param beta
     * @param depth The current depth of the search
     * @return int. The utility of the branching path
     * The max porition of the of the MinMax Algorithm
     */
    public int MaxValue(GameState s, int alpha, int beta, int depth) {
        if (s.isFinished()) { // Check if game is terminal
            return getBoardValue(s.getBoard());
        }
        if (depth == _maxDepth_) { // Check if max depth is reached
            return getBoardValue(s.getBoard());
        }
        depth++;
        int v = Integer.MIN_VALUE;
        for (Position a : s.legalMoves()) { // Check all legal moves given a gamestate.
            var nextState = new GameState(s.getBoard(), s.getPlayerInTurn());
            nextState.insertToken(a);  // Creates a new gamestate branch
            var v2 = MinValue(nextState, alpha, beta, depth);
            if (v2 > v){ // Updates the value if a better value is found.
                v = v2;
                alpha = Math.max(alpha, v);
            }
            if (v >= beta) // Prunin step
                return v;
        }
        return v;

    }

    
    /** 
     * @param s     The given gamestate
     * @param alpha
     * @param beta
     * @param depth The current depth
     * @return int  The utility of the branching path
     */
    public int MinValue(GameState s, int alpha, int beta, int depth) {
         // Guard statements
        if (s.isFinished()) {
            return getBoardValue(s.getBoard());
        }
        if (depth == _maxDepth_) {
            return getBoardValue(s.getBoard());
        }
        depth++;
        int v = Integer.MAX_VALUE;
        for (Position a : s.legalMoves()) {
            var nextState = new GameState(s.getBoard(), s.getPlayerInTurn());
            nextState.insertToken(a); // New gamestate branch
            var v2 = MaxValue(nextState, alpha, beta, depth);
            if (v2 < v){
                v = v2;
                beta = Math.min(beta, v);
            }
            if (v <= alpha) // Prunin step
                return v;
        }
        return v;

    }

    /**
     * Point values are related to the boardspaces favourability
     * Corners have the most utility and are therefore given 4, utility.
     * Inspiration taken from the following paper: 
     * https://courses.cs.washington.edu/courses/cse573/04au/Project/mini1/RUSSIA/Final_Paper.pdf
     */
    private int[][] _weightedBoard_ = {
            { 4, -3, 2, 2, 2, 2, -3, 4 },
            { -3, -4, -1, -1, -1, -1, -4, -3 },
            { 2, -1, 1, 0, 0, 1, -1, 2 },
            { 2, -1, 0, 1, 1, 0, -1, 2 },
            { 2, -1, 0, 1, 1, 0, -1, 2 },
            { 2, -1, 1, 0, 0, 1, -1, 2 },
            { -3, -4, -1, -1, -1, -1, -4, -3 },
            { 4, -3, 2, 2, 2, 2, -3, 4 }
    };

    
    /** 
     * Given a board, returns the utility of that given board.
     * @param board
     * @return int
     */
    private int getBoardValue(int[][] board) {
        var value = 0;
        for (int index1 = 0; index1 < _boardsize_; index1++) {
            for (int index2 = 0; index2 < _boardsize_; index2++) {
                if (board[index1][index2] == _player_)
                    value += _weightedBoard_[index1][index2];
            }
        }
        return value;

    }

}