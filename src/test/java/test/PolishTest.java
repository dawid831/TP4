package test;

import org.testng.Assert;
import org.testng.annotations.Test;
import tp4.Game;

public class PolishTest {

    @Test
    void CorrectMove() {
        Game game = new Game("polish");
        Assert.assertEquals(game.getMover().move(2, 0, 3, 1), "correct");
    }

    @Test
    void InCorrectMove() {
        Game game = new Game("polish");
        Assert.assertEquals(game.getMover().move(2, 0, 2, 1), "you must move diagonally");
    }

    @Test
    void SameSquare() {
        Game game = new Game("polish");
        Assert.assertEquals(game.getMover().move(2, 0, 2, 0), "same 2 0");
    }

    @Test
    void WrongMove() {
        Game game = new Game("polish");
        Assert.assertEquals(game.getMover().move(2, 0, 4, 2), "unknown error");
    }

    @Test
    void CantMoveOnThisSquare() {
        Game game = new Game("polish");
        Assert.assertEquals(game.getMover().move(0,0, 2, 2), "occupied");
    }

    @Test
    void WrongColor() {
        Game game = new Game("polish");
        Assert.assertEquals(game.getMover().move(7,7, 2, 2), "wrong color move");
    }

    @Test
    public void testGame() {
        Game game = new Game("polish");
        Assert.assertEquals(game.getMover().move(2, 0, 3, 1), "correct");
        // test non-diagonal move, should not let it happen
        Assert.assertEquals(game.getMover().move(7, 1, 6, 1), "you must move diagonally");
        Assert.assertEquals(game.getMover().move(7, 1, 6, 0), "correct");
        // test wrong color move
        Assert.assertEquals(game.getMover().move(7, 7, 6, 6), "wrong color move");
        Assert.assertEquals(game.getMover().move(3, 1, 4, 2), "correct");
        Assert.assertEquals(game.getMover().move(7, 9, 6, 8), "correct");
        Assert.assertEquals(game.getMover().move(2, 4, 3, 3), "correct");
        Assert.assertEquals(game.getMover().move(6, 8, 5, 9), "correct");
        Assert.assertEquals(game.getMover().move(2, 6, 3, 5), "correct");
        Assert.assertEquals(game.getMover().move(7, 7, 6, 6), "correct");
        Assert.assertEquals(game.getMover().move(2, 2, 3, 1), "correct");
        Assert.assertEquals(game.getMover().move(6, 6, 5, 5), "correct");
        Assert.assertEquals(game.getMover().move(4, 2, 5, 1), "correct");
        // test multi beating
        Assert.assertEquals(game.getMover().move(6, 0, 4, 2), "correct");
        Assert.assertEquals(game.getMover().move(4, 2, 2, 0), "this is not the best beating available");
        Assert.assertEquals(game.getMover().move(5, 9, 4, 8), "must move pawn 4 2");
        Assert.assertEquals(game.getMover().move(4, 2, 2, 4), "correct");
        Assert.assertEquals(game.getMover().move(2, 4, 4, 6), "correct");
    }
}
