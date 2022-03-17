public class OthelloAI3 implements IOthelloAI {

    private static final int _boardsize_ = 8;
    private static final int _player_ = 1;
    private static Position Move = null;
    private int _maxDepth_ = 5;
    public int _iterations_ = 0;
    @Override
    public Position decideMove(GameState s) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        var unused = MaxValue(s, alpha, beta, 0);
        return Move;
    }

    public int MaxValue(GameState s, int alpha, int beta, int depth){
        if (s.isFinished()) {
            return getBoardValue(s.getBoard());
        }
        if (depth == _maxDepth_ ) {
            return getBoardValue(s.getBoard());
        }
        depth++;
        int v = Integer.MIN_VALUE;
        for (Position a : s.legalMoves()) {
            var nextState = new GameState(s.getBoard(), s.getPlayerInTurn());
            nextState.insertToken(a);
            var v2 = MinValue(nextState, alpha, beta, depth);
            if(v2 > v)
                v = v2;
                Move = a;
                alpha = Math.max(alpha, v);
            if(v >= beta)
                return v;
        }
        return v;

    }
    public int MinValue(GameState s, int alpha, int beta, int depth){
        if (s.isFinished()) {
            return getBoardValue(s.getBoard());
        }
        if (depth == _maxDepth_ ) {
            return getBoardValue(s.getBoard());
        }
        depth++;
        int v = Integer.MAX_VALUE;
        for (Position a : s.legalMoves()) {
            var nextState = new GameState(s.getBoard(), s.getPlayerInTurn());
            nextState.insertToken(a);
            var v2 = MaxValue(nextState, alpha, beta, depth);
            if(v2 < v)
                v = v2;
                Move = a;
                beta = Math.min(beta, v);
            if(v <= beta)
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
private int getBoardValue(int[][] board){
    var value = 0;
    for (int index1 = 0; index1 < _boardsize_; index1++) {
        for (int index2 = 0; index2 < _boardsize_; index2++) {
            if (board[index1][index2] == _player_)
                value += _weightedBoard_[index1][index2];
        }}
    return value;

}

}