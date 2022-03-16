import java.io.Console;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ToreBOT implements IOthelloAI {
    
    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;

    public Position decideMove(GameState s) {
        return oneDepth(s);
    }

    //1 Depth search for new boardstate
    
    public Position oneDepth(GameState s) {
        var LegalMoves = s.legalMoves();
        var bestMove = LegalMoves.get(0);
        var bestTokens = Integer.MIN_VALUE;
       // System.out.println(LegalMoves.size());
        for (Position position : LegalMoves) {
            var copyState = new GameState(s.getBoard(), s.getPlayerInTurn());
            //System.out.println(s.getPlayerInTurn());
            if (copyState.insertToken(position)) {
                var nmbrTokens = copyState.countTokens()[0];
               // System.out.println("Row =  " + position.row + " Col = " + position.col + "\n");
               // System.out.println(Arrays.deepToString(copyState.getBoard()).replace("], ", "]\n").replace("[[", "[").replace("]]", "]") + "\n");
                if(nmbrTokens>bestTokens){
                    bestMove = position;
                }
            }  
            else return new Position (-1,-1);        
        }
        return bestMove;
    }

    public int EvalBoard(int[][] board){
        return 0;
    }

    public Position nDepth(GameState s, int depth){
        var legalMoves = s.legalMoves();
        var bestMove = legalMoves.get(0);
        var bestValue = Integer.MIN_VALUE;
        for (int i = 0; i < depth; i++) {
            
        }

    }
    public Position AlphaBetaSearch(GameState s, Integer Depth) {
        var ValueMove = Max_Value(s, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, Depth);
        return ValueMove.pos;
    }
    
    public UtilPos Max_Value(GameState s, int alpha, int beta, int currentDepth, int MaxDepth){
        if 
    }

    class UtilPos{
        int value;
        Position pos;

        public UtilPos(int value, Position pos) {
            this.value = value;
            this.pos = pos;
        }
    }
}
