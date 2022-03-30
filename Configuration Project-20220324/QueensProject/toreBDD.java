import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;

public class toreBDD implements IQueensLogic{
    private int size;
    private int[][] board;
    BDDFactory fact;
    int nVars = size * size;
    BDD True;
    BDD False;
    BDD myBDD;
    @Override
    public void initializeBoard(int size) {
        this.size = size;
        this.board = new int[size][size];
        buildBDD();
    }

    @Override
    public int[][] getBoard() {
        return board;
    }

    @Override
    public void insertQueen(int column, int row) {
        board[column][row] = 1;
    }
    // Build initial BDD
    public void buildBDD(){
        fact = JFactory.init(200000,200000);
        fact.setVarNum(nVars);
        True = fact.one();
        False = fact.zero();
        myBDD = null;
    }

    public int getVar(int column, int row){
        return row*size+column;
    }
}
