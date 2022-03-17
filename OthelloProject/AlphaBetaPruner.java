public class AlphaBetaPruner implements IOthelloAI {

    private int _boardsize_ = 8;

    private int _maxDepth_ = 5;

    private int _player_ = 2;

    public Position decideMove(GameState s) {

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        int v = Integer.MIN_VALUE;
        Position bestPos = null;
        for (var pos : s.legalMoves()) {
            var nextState = new GameState(s.getBoard(), s.getPlayerInTurn());
            nextState.insertToken(pos);
            int v2 = MinValue(nextState, alpha, beta, 0);
            if (v2 >= v) {
                v = v2;
                bestPos = pos;
            }
        }
        return bestPos;
    }

    private int MaxValue(GameState s, int alpha, int beta, int depth) {
        // System.out.println("alpha = " + alpha + "\n");
        if (s.isFinished())
            return getEval(s);
        if (depth >= _maxDepth_)
            return getEval(s);

        depth++;
        int v = Integer.MIN_VALUE;

        for (var pos : s.legalMoves()) {
            var nextState = new GameState(s.getBoard(), s.getPlayerInTurn());
            nextState.insertToken(pos);
            var v2 = MinValue(nextState, alpha, beta, depth);
            if (v2 > v){
                v = v2;
                alpha = Math.max(alpha, v);}
            if (v >= beta)
                return v;
        }

        return v;
    }

    private int MinValue(GameState s, int alpha, int beta, int depth) {
        // System.out.println("beta = " + beta + "\n");
        if (s.isFinished())
            return getEval(s);
        if (depth >= _maxDepth_)
            return getEval(s);

        depth++;
        int v = Integer.MAX_VALUE;

        for (var pos : s.legalMoves()) {
            var nextState = new GameState(s.getBoard(), s.getPlayerInTurn());
            nextState.insertToken(pos);
            var v2 = MaxValue(nextState, alpha, beta, depth);
            if (v2 < v){
                v = v2;
                beta = Math.min(beta, v);}
            if (v <= alpha)
                return v;
        }

        return v;
    }

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

    public int getEval(GameState s) {
        int[][] board = s.getBoard();
        int value = 0;
        for (int index1 = 0; index1 < _boardsize_; index1++) {
            for (int index2 = 0; index2 < _boardsize_; index2++) {
                if (board[index1][index2] == _player_)
                    value += _weightedBoard_[index1][index2];
            }
        }
        return value;
    }

}
