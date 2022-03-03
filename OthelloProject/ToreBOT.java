import java.io.Console;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ToreBOT implements IOthelloAI {
    
    public Position decideMove(GameState s) {
        return oneDepth(s);
    }

    //1 Depth search for new boardstate
    
    public Position oneDepth(GameState s) {
        var LegalMoves = s.legalMoves();
        var bestMove = LegalMoves.get(0);
        var bestTokens = Integer.MIN_VALUE;
        //var gameStates = new ArrayList<GameState>();
        for (Position position : LegalMoves) {
            var copyState = s;
            if (copyState.insertToken(position)) {
                var nmbrTokens = copyState.countTokens()[0];
                System.out.println("Row =  " + position.row + " Col = " + position.col + "\n");
                System.out.println(Arrays.deepToString(copyState.getBoard()).replace("], ", "]\n").replace("[[", "[").replace("]]", "]") + "\n");
                if(nmbrTokens>bestTokens){
                    bestMove = position;
                }
            }  
            else return new Position (-1,-1);        
        }
        return bestMove;
    }
    
}
