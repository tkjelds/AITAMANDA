import java.io.PipedOutputStream;
import java.util.ArrayList;

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
            var CurrentState = s;
            CurrentState.insertToken(position);
            var nmbrTokens = CurrentState.countTokens()[0];
            if(nmbrTokens>bestTokens){
                bestMove = position;
            }
            
        }
        return bestMove;
    }
    
}
