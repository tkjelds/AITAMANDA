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
     * @param s
     * @return Position
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
            nextState.insertToken(a);
            var v2 = MinValue(nextState, alpha, beta, depth);
            if (v2 > v){
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
     * @param depth 
     * @return int  The utility of the branching path
     */
    public int MinValue(GameState s, int alpha, int beta, int depth) {
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
            nextState.insertToken(a);
            var v2 = MaxValue(nextState, alpha, beta, depth);
            if (v2 < v){
                v = v2;
                beta = Math.min(beta, v);
            }
            if (v <= alpha)
                return v;
        }
        return v;

    }

    /**
     * InnerAlphaMemer
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