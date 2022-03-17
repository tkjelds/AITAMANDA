public class ABAI implements IOthelloAI{
    int boardValues[][]; //a board of values showing which positions are best

	public Position decideMove(GameState gameState) {
        //Here we create a new GameState with the same board and turn as the original one
        GameState gS = new GameState(gameState.getBoard(), gameState.getPlayerInTurn());
		return MINIMAX_SEARCH(gS);
	}

    //Inserts the tile on the position pos in the game
    private GameState insert(GameState gameState, Position pos) {
        gameState.insertToken(pos);
        return gameState;
    }

    private Position MINIMAX_SEARCH(GameState gameState) {
        //creates the boardValues as the same size as the Othello board
        boardValues = new int[gameState.getBoard().length][gameState.getBoard().length];
        //used for giving values to boadValues
        giveBoardValues();
        var UtilityMove = MAX_VALUE(gameState, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return UtilityMove.getMove();
    }

    private UtilityMove MAX_VALUE(GameState gameState, int alpha, int beta) {
        /*if ( gameState.legalMoves().isEmpty() )
            return new UtilityMove(new Position(-1, -1));*/
        var v = Integer.MIN_VALUE;
        var m = new Position(-1, -1);
        for(Position move : gameState.legalMoves()) {
            UtilityMove um2 = MIN_VALUE(insert(gameState, move), alpha, beta); 
            if(um2.move == new Position(-1, -1)) break;
            if (um2.getUtility() > v) {
                v = um2.getUtility();
                m = move;
                alpha = new UtilityMove(move).getUtility();
            }
            if (v >= beta) {
                return new UtilityMove(m);
            }
        }
        return new UtilityMove(m);
    }
	
    private UtilityMove MIN_VALUE(GameState gameState, int alpha, int beta) {
        /*if ( gameState.legalMoves().isEmpty() )
            return new UtilityMove(new Position(-1,-1));*/
        var v = Integer.MAX_VALUE;
        var m = new Position(-1, -1);
        for(var move : gameState.legalMoves()) { 
            UtilityMove um2 = MAX_VALUE(insert(gameState, move), alpha, beta);
            if(um2.move == new Position(-1, -1)) break;
            if(um2.getUtility() < v) {
                v = um2.getUtility();
                m = move;
                beta = new UtilityMove(move).getUtility();
            }
            if(v <= alpha) {
                return new UtilityMove(m);
            }
        }
        return new UtilityMove(m);
    }

    //this method calls all the other methods to fill up boardValues with their utility value
    private void giveBoardValues() {
        //These methods fills out the to other squares
        topBottomValues(0);
        leftRightvalues(0);
        innerTopBottomValues(1);
        innerLeftRightValues(1);
        innerTopBottomValues(boardValues.length-2);
        innerLeftRightValues(boardValues.length-2);
        topBottomValues(boardValues.length-1);
        leftRightvalues(boardValues.length-1);
        
        //The middle of the board all get the utility value 3
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

    /*Is called for both the top and bottom row
    Gives the row (if size == 8): 5 0 4 4 4 4 0 5*/
    private void topBottomValues(int y) {
        boardValues[y][0] = 5;
        boardValues[y][1] = 0;
        for(int x = 2; x < boardValues.length-2; x++) {
            boardValues[y][x] = 4;
        }
        boardValues[y][boardValues.length-2] = 0;
        boardValues[y][boardValues.length-1] = 5;
    }

    /*Is called with the second row both top and bottom
    Gives the row (if size == 8): 0 1 1 1 1 0*/
    private void innerTopBottomValues(int y) {
        boardValues[y][1] = 0;
        for(int x = 2; x < boardValues.length-2; x++) {
            boardValues[y][x] = 2;
        }
        boardValues[y][boardValues.length-2] = 0;
    }

    /*Is called with the row furthest to the left and right
    Gives the row (if size == 8): 
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

    /*Is called with the row second-furthest to the left and right
    Gives the row (if size == 8): 
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

    //new class UtilityMove used for storing a Position and returning the utility
    public class UtilityMove {
        private Position move;
    
        UtilityMove(Position position) {
            move = position;
        }
    
        //Uses boardValues to get the utility and then returns it
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
