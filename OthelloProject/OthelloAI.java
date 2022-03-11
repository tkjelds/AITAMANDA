import java.util.ArrayList;

public class OthelloAI implements IOthelloAI {

    /**
     * minimaxDecision() takes a decision on the basis of the utility values returned and chooses
     * the action that maximizes the minimum value of the position from the opponent's possible following moves.
     * @param s is a object of type GameState, and is the current state of the game.
     * @param depth is of type Integer and defines the depth of the tree to be created.
     * @return Returns a Position object that is used by the player to make a move in the game.
     */
    public Position minimaxDecision(GameState s, int depth){
        int v = Integer.MIN_VALUE;
        Position position = null;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        ArrayList<Position> legalMoves = s.legalMoves();
        for (Position p : legalMoves){
            int vTemp = minValue(result(s, p), alpha, beta,depth-1);
            if( v < vTemp) {
                v = vTemp;
                position = p;
            }
        }
        return position;
    }

    /**
     * maxValue() finds the utility values for all the actions that can be taken by max in that state.
     * @param s is an object of type GameState, and is the current state of the game.
     * @param alpha is the best maxValue currently for that level or above.
     * @param beta is the best minValue currently for that level or above.
     * @param depth is of type Integer and defines the depth of the tree to be created.
     * @return Return an utility value for max.
     */
    public int maxValue(GameState s, int alpha, int beta, int depth){
        if(s.isFinished() || depth == 0) return utility(s);
        int v = Integer.MIN_VALUE;
        int sum = 0;

        for ( Position p : s.legalMoves()) {
            sum += v ;
            v = Math.max(v, minValue(result(s,p), alpha, beta,depth-1));
            alpha = Math.max(alpha, v);
            if (beta <= alpha){
                return utility(s) + sum;
            }
        }
        return v;
    }

    /**
     * minValue() finds the utility values for all the actions that can be taken by min in that state.
     * @param s is an object of type GameState, and is the current state of the game.
     * @param alpha is the best maxValue currently for that level or above.
     * @param beta is the best minValue currently for that level or above.
     * @param depth is of type Integer and defines the depth of the tree to be created.
     * @return Return an utility value for min.
     */
    public int minValue(GameState s,  int alpha, int beta, int depth){
        if(s.isFinished() || depth == 0) return utility(s);
        int v =Integer.MAX_VALUE;
        int sum = 0;

        for ( Position p : s.legalMoves()) {
            sum += v ;
            v = Math.min(v, maxValue(result(s,p), alpha, beta,depth-1));
            beta = Math.min(beta, v);
            if (beta <= alpha){
                return utility(s) + sum;
            }
        }
        return v;
    }

    /**
     * result() changes the state of the game by making an action/move.
     * @param s is an object of type GameState, and is the current state of the game.
     * @param p is of type Position and is the action/move to be taken.
     * @return Returns the GameState after it has been changed by the player making a move.
     */
    private GameState result(GameState s, Position p) {
        s.insertToken(p);
        return s;
    }

    /**
     * utility() calculates the difference between max and min tokens.
     * @param s is an object of type GameState, and is the current state of the game.
     * @return Returns the utility value calculated by the method as an Integer.
     */
    private int utility(GameState s) {
        int[] tokens = s.countTokens();
        return tokens[1] - tokens[0];
    }

    @Override
    public Position decideMove(GameState s) {
        GameState state = new GameState(s.getBoard(), s.getPlayerInTurn());
        return minimaxDecision(state,10);
    }

}