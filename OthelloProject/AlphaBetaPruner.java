public class AlphaBetaPruner implements IOthelloAI {

    int _boardsize_ = 8;

    int _maxDepth_ = 8;

    int _player_ = 1;

    public Position decideMove(GameState s) {

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        int v = Integer.MIN_VALUE;
        Position maxPos = null;
        for (var pos : s.legalMoves()) {
            var tempState = new GameState(s.getBoard(), s.getPlayerInTurn());
            tempState.insertToken(pos);
            int v2 = MinValue(tempState, alpha, beta, 0);
            if (v2 >= v) {
                v = v2;
                maxPos = pos;
            }
        }
        return maxPos;
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
            var tempState = new GameState(s.getBoard(), s.getPlayerInTurn());
            tempState.insertToken(pos);
            var v2 = MinValue(tempState, alpha, beta, depth);
            if (v2 > v)
                v = v2;
            if (v >= beta)
                return v;
            alpha = Math.max(alpha, v);
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

        for (var position : s.legalMoves()) {
            var tempState = new GameState(s.getBoard(), s.getPlayerInTurn());
            tempState.insertToken(position);
            var v2 = MaxValue(tempState, alpha, beta, depth);
            if (v2 < v)
                v = v2;
            if (v <= alpha)
                return v;
            beta = Math.min(beta, v);
        }

        return v;
    }

    int[][] WeightedBoard = {
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
                    value += WeightedBoard[index1][index2];
            }
        }
        return value;
    }

}
