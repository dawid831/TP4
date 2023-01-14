package test;

import javafx.scene.paint.Color;
import org.testng.Assert;
import org.junit.jupiter.api.Test;
import tp4.Board;
import tp4.MoverI;

class BoardTest {

    @Test
    void ConstructorAndMethodsTest() {
        MoverI mover = new MoverI() {
            @Override
            public String move(int i, int j, int k, int l) {
                return null;
            }
        };
        //english
        Board board = new Board(8,mover, Color.BLACK);
        Assert.assertEquals(board.getSize(),8);
        Assert.assertEquals(board.getColor(),Color.BLACK);
        //polish
        Board board2 = new Board(10,mover, Color.BLACK);
        Assert.assertEquals(board2.getSize(),10);
        Assert.assertEquals(board2.getColor(),Color.BLACK);
        //german
        Board board3 = new Board(8,mover, Color.WHITE);
        Assert.assertEquals(board3.getSize(),8);
        Assert.assertEquals(board3.getColor(),Color.WHITE);
    }

}