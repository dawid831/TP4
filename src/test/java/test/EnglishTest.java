package test;

import org.testng.Assert;
import org.testng.annotations.Test;
import tp4.Game;

public class EnglishTest {

    @Test
    void CorrectMove() {
        Game game = new Game("english");
        Assert.assertEquals(game.getMover().move(2, 0, 3, 1), "correct");
    }

    @Test
    void InCorrectMove() {
        Game game = new Game("english");
        Assert.assertEquals(game.getMover().move(2, 0, 2, 1), "you must move diagonally");
    }

    @Test
    void SameSquare() {
        Game game = new Game("english");
        Assert.assertEquals(game.getMover().move(2, 0, 2, 0), "same");
    }

    @Test
    void WrongMove() {
        Game game = new Game("english");
        Assert.assertEquals(game.getMover().move(2, 0, 4, 2), "just wrong");
    }

    @Test
    void CantMoveOnThisSquare() {
        Game game = new Game("english");
        Assert.assertEquals(game.getMover().move(0,0, 2, 2), "occupied");
    }

    @Test
    void WrongColor() {
        Game game = new Game("english");
        Assert.assertEquals(game.getMover().move(7,7, 2, 2), "wrong color move");
    }

    @Test
    public void testGame() {
        Game game = new Game("english");
        Assert.assertEquals(game.getMover().move(2, 0, 3, 1), "correct");
        // test non-diagonal move, should not let it happen
        Assert.assertEquals(game.getMover().move(5, 1, 4, 1), "you must move diagonally");
        Assert.assertEquals(game.getMover().move(5, 1, 4, 0), "correct");
        // test wrong color move
        Assert.assertEquals(game.getMover().move(5, 7, 4, 6), "wrong color move");
        Assert.assertEquals(game.getMover().move(2, 2, 3, 3), "correct");
        // beating
        Assert.assertEquals(game.getMover().move(4, 0, 2, 2), "correct");
        // must beat, singular beating
        Assert.assertEquals(game.getMover().move(2, 4, 3, 5), "must beat");
        Assert.assertEquals(game.getMover().move(1, 3, 3, 1), "correct");
        Assert.assertEquals(game.getMover().move(5, 7, 4, 6), "correct");
        Assert.assertEquals(game.getMover().move(3, 3, 4, 4), "correct");
        // double beating
        Assert.assertEquals(game.getMover().move(5, 3, 3, 5), "next beat");
        Assert.assertEquals(game.getMover().move(3, 5, 1, 3), "correct");
        // beating
        Assert.assertEquals(game.getMover().move(0, 4, 2, 2), "correct");
        Assert.assertEquals(game.getMover().move(4, 6, 3, 5), "correct");
        Assert.assertEquals(game.getMover().move(2, 6, 4, 4), "correct");
        Assert.assertEquals(game.getMover().move(5, 5, 3, 3), "correct");
        Assert.assertEquals(game.getMover().move(2, 2, 4, 4), "correct");
        Assert.assertEquals(game.getMover().move(6, 4, 5, 3), "correct");
        Assert.assertEquals(game.getMover().move(1, 7, 2, 6), "correct");
        // double beating
        Assert.assertEquals(game.getMover().move(5, 3, 3, 5), "next beat");
        // cant stop during multi-beating
        Assert.assertEquals(game.getMover().move(3, 5, 2, 4), "must beat");
        Assert.assertEquals(game.getMover().move(3, 5, 1, 7), "correct");
        Assert.assertEquals(game.getMover().move(1, 5, 2, 6), "correct");
        Assert.assertEquals(game.getMover().move(6, 0, 5, 1), "correct");
        Assert.assertEquals(game.getMover().move(0, 6, 1, 5), "correct");
        // PROMOTION
        Assert.assertEquals(game.getMover().move(1, 7, 0, 6), "correct");
        Assert.assertEquals(game.getMover().move(2, 6, 3, 5), "correct");
        // Queen multi beating
        Assert.assertEquals(game.getMover().move(0, 6, 2, 4), "next beat");
        Assert.assertEquals(game.getMover().move(2, 4, 4, 6), "correct");
        Assert.assertEquals(game.getMover().move(1, 1, 2, 2), "correct");
        // Queen backward move
        Assert.assertEquals(game.getMover().move(4, 6, 5, 7), "correct");
        Assert.assertEquals(game.getMover().move(2, 2, 3, 3), "correct");
        // Queen onward move
        Assert.assertEquals(game.getMover().move(5, 7, 4, 6), "correct");
    }
}
