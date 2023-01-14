package test;

import org.testng.Assert;
import org.testng.annotations.Test;
import tp4.Game;

public class GermanTest {
    @Test
    void NoPawnOnThisSquare() {
        Game game = new Game("german");
        Assert.assertEquals(game.getMover().move(2, 0, 3, 1), "null 2 0");
    }

    @Test
    void NotDiagonallyMove() {
        Game game = new Game("german");
        Assert.assertEquals(game.getMover().move(2, 7, 3, 7), "you must move diagonally");
        Assert.assertEquals(game.getMover().move(2, 7, 2, 6), "you must move diagonally");
        Assert.assertEquals(game.getMover().move(2, 7, 1, 7), "you must move diagonally");
    }

    @Test
    void SamePawn() {
        Game game = new Game("german");
        Assert.assertEquals(game.getMover().move(2, 7, 2, 7), "same 2 7");
        Assert.assertEquals(game.getMover().move(1, 2, 1, 2), "same 1 2");
    }

    @Test
    void WrongColorMove() {
        Game game = new Game("german");
        Assert.assertEquals(game.getMover().move(5, 6, 4, 5), "wrong color move");
        Assert.assertEquals(game.getMover().move(5, 6, 6, 5), "wrong color move");
    }

    @Test
    void OccupiedSquare() {
        Game game = new Game("german");
        Assert.assertEquals(game.getMover().move(1, 2, 2, 3), "occupied");
        Assert.assertEquals(game.getMover().move(1, 2, 0, 3), "occupied");
    }

    @Test
    public void testGame() {
        Game game = new Game("german");
        Assert.assertEquals(game.getMover().move(2, 1, 3, 0), "correct");
        Assert.assertEquals(game.getMover().move(5, 2, 4, 1), "correct");
        // beating
        Assert.assertEquals(game.getMover().move(3, 0, 5, 2), "correct");
        Assert.assertEquals(game.getMover().move(6, 1, 4, 3), "correct");
        Assert.assertEquals(game.getMover().move(1, 0, 2, 1), "correct");
        Assert.assertEquals(game.getMover().move(5, 0, 4, 1), "correct");
        Assert.assertEquals(game.getMover().move(2, 3, 3, 4), "correct");
        Assert.assertEquals(game.getMover().move(5, 6, 4, 5), "correct");
        // multi beating
        Assert.assertEquals(game.getMover().move(3, 4, 5, 6), "this is not the best beating available");
        Assert.assertEquals(game.getMover().move(3, 4, 5, 2), "correct");
        Assert.assertEquals(game.getMover().move(5, 2, 3, 0), "correct");
    }
}
