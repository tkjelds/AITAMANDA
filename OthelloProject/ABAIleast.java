import java.util.ArrayList;
import java.lang.Object.*;  

public class ABAIleast implements IOthelloAI{
    int boardValues[][];
    boolean overHalfWay;

	public Position decideMove(GameState gameState) {
        GameState gS = new GameState(gameState.getBoard(), gameState.getPlayerInTurn());
		return MINIMAX_SEARCH(gS);
	}

    private GameState insert(GameState gameState, Position pos) {
        gameState.insertToken(pos);
        return gameState;
    }

    private Position MINIMAX_SEARCH(GameState gameState) {
        boardValues = new int[gameState.getBoard().length][gameState.getBoard().length];
        giveBoardValues();
        if((gameState.countTokens()[0] + gameState.countTokens()[1]) < (boardValues.length*boardValues.length * 0.75)) {
            //System.out.println((gameState.countTokens()[0] + gameState.countTokens()[1])+" < "+(boardValues.length*boardValues.length * 0.90));
            overHalfWay = false;
        }
        else {
            //System.out.println((gameState.countTokens()[0] + gameState.countTokens()[1])+" >= "+(boardValues.length*boardValues.length * 0.90));
            overHalfWay = true;
        }
        //overHalfWay = true;
        var UtilityMove = MAX_VALUE(gameState, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return UtilityMove.getMove();
    }

    private UtilityMove MAX_VALUE(GameState gameState, int alpha, int beta) {
        if ( gameState.legalMoves().isEmpty() )
            return new UtilityMove(new Position(-1, -1));
        var v = Integer.MIN_VALUE;
        var m = new Position(-1, -1);
        int captures;
        if(overHalfWay) { //We want to turn as many as possible
            captures = Integer.MIN_VALUE;
        } else {
            captures = Integer.MAX_VALUE;
        }
        for(Position move : gameState.legalMoves()) {
            //System.out.println("--------BEST -- v: "+v+" m: "+m);
            UtilityMove um2 = MIN_VALUE(insert(gameState, move), alpha, beta); 
            if(um2.move == new Position(-1, -1)) break;
            if (um2.getUtility() > v) {
                //System.out.println("Move is now "+move+" util "+um2.getUtility());
                v = um2.getUtility();
                m = move;
                alpha = new UtilityMove(move).getUtility();
                captures = gameState.getCaptures(move);
            } else if (um2.getUtility() == v) {
                if((overHalfWay && captures < gameState.getCaptures(move)) || (!overHalfWay && captures > gameState.getCaptures(move))) {
                    //System.out.println("MAX : util +"+v+" move "+move+", "+overHalfWay+", capB "+captures+", capN "+gameState.getCaptures(move));
                    m = move;
                    alpha = new UtilityMove(move).getUtility();
                    captures = gameState.getCaptures(move);
                } 
            }
            if (v >= beta) {
                return new UtilityMove(m);
            }
        }
        return new UtilityMove(m);
    }
	
    private UtilityMove MIN_VALUE(GameState gameState, int alpha, int beta) {
        if ( gameState.legalMoves().isEmpty() )
            return new UtilityMove(new Position(-1,-1));    
        var v = Integer.MAX_VALUE;
        var m = new Position(-1, -1);
        int captures;
        if(overHalfWay) { //We want to turn as many as possible
            captures = Integer.MAX_VALUE;
        } else {
            captures = Integer.MIN_VALUE;
        }
        for(var move : gameState.legalMoves()) { 
            UtilityMove um2 = MAX_VALUE(insert(gameState, move), alpha, beta);
            if(um2.move == new Position(-1, -1)) break;
            //System.out.print(move+" ");
            if(um2.getUtility() < v) {
                //System.out.print(um2.getUtility()+" < "+v);
                v = um2.getUtility();
                m = move;
                beta = new UtilityMove(move).getUtility();
                captures = gameState.getCaptures(move);
            } else if(um2.getUtility() == v) {
                //System.out.print(um2.getUtility()+" == "+v);
                if((overHalfWay && captures > gameState.getCaptures(move)) || (!overHalfWay && captures < gameState.getCaptures(move))) {
                    //System.out.println("MIN : util +"+v+" move "+move+", "+overHalfWay+", capB "+captures+", capN "+gameState.getCaptures(move));
                    m = move;
                    beta = new UtilityMove(move).getUtility();
                    captures = gameState.getCaptures(move);
                } 
            }
            //System.out.println();
            if(v <= alpha) {
                return new UtilityMove(m);
            }
        }
        return new UtilityMove(m);
    }

    private void giveBoardValues() {
        topBottomValues(0);
        leftRightvalues(0);
        innerTopBottomValues(1);
        innerLeftRightValues(1);
        innerTopBottomValues(boardValues.length-2);
        innerLeftRightValues(boardValues.length-2);
        topBottomValues(boardValues.length-1);
        leftRightvalues(boardValues.length-1);
        
        for(int y = 2; y < boardValues.length-2; y++) {
            for(int x = 2; x < boardValues.length-2; x++) {
                boardValues[y][x] = 3;
            }
        }
        //Lastly the corners right before the two outer circles
        boardValues[2][2] = 4;
        boardValues[2][boardValues.length-3] = 4;
        boardValues[boardValues.length-3][2] = 4;
        boardValues[boardValues.length-3][boardValues.length-3] = 4;
    }

    //Gives the row (if size == 8): 5 0 4 4 4 4 0 5
    private void topBottomValues(int y) {
        boardValues[y][0] = 5;
        boardValues[y][1] = 0;
        for(int x = 2; x < boardValues.length-2; x++) {
            boardValues[y][x] = 4;
        }
        boardValues[y][boardValues.length-2] = 0;
        boardValues[y][boardValues.length-1] = 5;
    }

    //Gives the row (if size == 8): 0 1 1 1 1 0
    private void innerTopBottomValues(int y) {
        boardValues[y][1] = 0;
        for(int x = 2; x < boardValues.length-2; x++) {
            boardValues[y][x] = 2;
        }
        boardValues[y][boardValues.length-2] = 0;
    }

    /*Gives the row (if size == 8): 
    5 
    0
    4
    4
    4
    4
    0
    5
    */
    private void leftRightvalues(int x) {
        boardValues[0][x] = 5;
        boardValues[1][x] = 0;
        for(int y = 2; y < boardValues.length-2; y++) {
            boardValues[y][x] = 4;
        }
        boardValues[boardValues.length-1][boardValues.length-2] = 0;
        boardValues[boardValues.length-1][boardValues.length-1] = 5;
    }

    /*Gives the row (if size == 8): 
    0
    2
    2
    2
    2
    0
    */
    private void innerLeftRightValues(int x) {
        boardValues[1][x] = 0;
        for(int y = 2; y < boardValues.length-2; y++) {
            boardValues[y][x] = 2;
        }
        boardValues[boardValues.length-1][x] = 0;
    }


    public void printBoard(int[][] board) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                System.out.print(board[i][j]+" ");
            }
            System.out.println();
        }
    }

    public class UtilityMove {
        private Position move;
    
        UtilityMove(Position position) {
            move = position;
        }
    
        public int getUtility() {
            if(move.col == -1 || move.row == -1) return 0;
            int value = boardValues[move.col][move.row];
            return value;
        }
    
        public Position getMove() {
            return move;
        }
    }
}
