package tp4;

import javafx.scene.paint.Color;

/**
 * class responsible for filling board with pawns.
 */
public class NormalBoardFiller {

    /**
     *
     * @param board which board should be filled.
     * @param rows which rows pawns should be created.
     */
    public void fill(Board board, int rows) {
        if(2 * rows > board.getSize() || rows < 0) throw new RuntimeException("Zla wartosc rzedow");
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < board.getSize(); j++) {
                if((i + j) % 2 == 0 && board.getColor().equals(Color.BLACK)) {
                    board.getNodeArray()[i][j].newPawn(Color.WHITE);
                }
                else if((i + j) % 2 != 0 && board.getColor().equals(Color.WHITE)) {
                    board.getNodeArray()[i][j].newPawn(Color.WHITE);
                }
            }
        }
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < board.getSize(); j++) {
                if ((i + j - 1) % 2 == 0 && board.getColor().equals(Color.BLACK)) {
                    board.getNodeArray()[board.getSize() - i - 1][j].newPawn(Color.BLACK);
                }
                else if ((i + j - 1) % 2 != 0 && board.getColor().equals(Color.WHITE)) {
                    board.getNodeArray()[board.getSize() - i - 1][j].newPawn(Color.BLACK);
                }
            }
        }
    }
}
