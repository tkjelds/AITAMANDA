import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;

public class toreBDD implements IQueensLogic{
    private int size;
    private static int[][] board;
    static BDDFactory fact;
    BDD True;
    BDD False;
    static BDD rulesBDD;

    /**
     * @param size The size of the board ( i.e. size = #rows = #columns)
     * Initializes the board and ruleset BDD.
     */
    @Override
    public void initializeBoard(int size) {
        this.size = size;
        board = new int[size][size];
        fact = JFactory.init(200000,200000);
        fact.setVarNum(size * size);
        True = fact.one();
        False = fact.zero();
        rulesBDD = True;
        assignRules();
    }

    /**
     * @return board
     */
    @Override
    public int[][] getBoard() {
        return board;
    }

    /**
     * @param column
     * @param row
     * Inserts queen at column, row
     * Then assigns the variable in the ruleset.
     * Then updates board with new domain
     */
    @Override
    public void insertQueen(int column, int row) {
        board[column][row] = 1;
        assignVar(column,row);
        updateBoard();
    }

    /**
     * @param column
     * @param row
     * @return The number of variable in the factory.
     * To be used when fact.ith or fact.nith
     */
    public int getVar(int column, int row){
        int rowPlacement = column*size;
        return rowPlacement+row;
    }

    /**
     * Goes through each variable and assign each with horizontal, vertical and diagonal rules.
     *
     */
    public void assignRules(){
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                horizontalRule(x,y);
                verticalRule(x,y);
                topLeftdownRightDiagonal(x,y);
                topRightDownLeftDiagonal(x,y);
            }
        }
        nQueensRule();
    }

    /**
     * n Queens rule, rule that is satisfied if there is at least n queens able to be placed on the board.
     * N is regulated by size.
     */
    public void nQueensRule(){
        BDD rules = True;
        for (int y = 0; y < size; y++) {
            BDD rowRule = False; // Starts with false, since the rule is using 'or'
            // For each row, create a rule that states that there has to be at least one queen.
            for (int x = 0; x < size; x++) {
                rowRule = rowRule.or(fact.ithVar(getVar(x,y)));
            }

            rules = rules.and(rowRule);
        }
        rulesBDD = rulesBDD.and(rules); // adds to the overall ruleset.
    }
    public void horizontalRule(int column, int row){
        BDD rules = True;
        for (int x = 0; x < size; x++) {
            if (x!=column){ // Check to make sure to skip the variable in question.
                // Creates a long line of ands that says that they all have to be false.
                rules = rules.and(fact.nithVar(getVar(x,row)));
            }
        }
        // Rule that says either that the above and below variables are false, or that the variable(column,row) is false.
        rules = (fact.nithVar(getVar(column,row))).or(rules);
        rulesBDD = rulesBDD.and(rules); // Adding to the overall ruleset.
    }

    /**
     * @param column
     * @param row
     * Creates a BDD, that represents the queen vertical rule.
     */
    public void verticalRule(int column, int row){
        BDD rules = True;
        for (int y = 0; y < size; y++) {
            if (y!=row){ // Check to make sure to skip the variable in question
                // Creates a long line of ands that says that they all have to be false
                rules = rules.and(fact.nithVar(getVar(column,y)));
            }
        }
        // Rule that says either that the above and below variables are false, or that the variable(column,row) is false.
        rules = (fact.nithVar(getVar(column,row))).or(rules);
        rulesBDD = rulesBDD.and(rules); // Adding to the overall ruleest.
    }

    /**
     * @param column
     * @param row
     * Creates a BBD, that represents the queen diagonal rule.
     * From top left through down right
     */
    public void topLeftdownRightDiagonal(int column, int row){
        BDD rules = True;
        int x = column+1;
        int y = row+1;
        while(x < size && y < size){
            rules = rules.and(fact.nithVar(getVar(x,y)));
            x++;
            y++;
        }
        x = column-1;
        y = row-1;
        while(x > 0 && y >0){
            rules = rules.and(fact.nithVar(getVar(x,y)));
            x--;
            y--;
        }
        // rule that says either the variable at (column,row) is not true or the above diagonal ruleset is true.
        rules = (fact.nithVar(getVar(column,row))).or(rules);
        rulesBDD = rulesBDD.and(rules); // adding rules to the overall ruleset.
    }

    /**
     * @param column
     * @param row
     * Creates a BBD, that represents the queen diagonal rule.
     * From top right through down left
     */
    public void topRightDownLeftDiagonal(int column, int row){
        BDD rules = True;
        int x = column-1;
        int y = row+1;
        while(x >= 0 && y < size){
            rules = rules.and(fact.nithVar(getVar(x,y)));
            x--;
            y++;
        }
        x = column+1;
        y = row-1;
        while(y >= 0 && x < size){
            rules = rules.and(fact.nithVar(getVar(x,y)));
            x++;
            y--;
        }
        // rule that says either the variable at (column,row) is not true or the above diagonal ruleset is true.
        rules = (fact.nithVar(getVar(column,row))).or(rules);
        rulesBDD = rulesBDD.and(rules); // Adding rules to the overall ruleset.
    }

    /**
     * @param column
     * @param row
     * Assigns a variable to the ruleset.
     */
    public void assignVar(int column, int row){
        rulesBDD = rulesBDD.restrict(fact.ithVar(getVar(column,row)));
    }

    /**
     * @param column
     * @param row
     * @return Whether the variable satisfies the domain.
     */
    public Boolean checkIfValid(int column, int row){
        BDD validCheck = rulesBDD.restrict(fact.ithVar(getVar(column,row))); // Restricting bdd, if the variable is true
        return !validCheck.isZero(); // Returns if valid bdd
    }

    public void updateBoard(){
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (!checkIfValid(x,y)){
                    board[x][y] = -1;
                }
            }
        }
    }
}
