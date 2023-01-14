package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.testng.Assert;
import tp4.Game;

class GameTest {

    @Test
    void WrongTypeOfGame() throws Exception {
        Assert.assertThrows(RuntimeException.class, () -> new Game("se"));
    }

    @Test
    void CorrectTypeOfGame() {
        Game game = new Game("english");
        Game game1 = new Game("german");
        Game game2 = new Game("polish");
    }

    @Test
    void JoiningToGame() {
        Game game = new Game("english");
        game.canJoin();
        game.canJoin();
        Game game2 = new Game("polish");
        game2.canJoin();
        game2.canJoin();
        Game game3 = new Game("german");
        game3.canJoin();
        game3.canJoin();
    }

}