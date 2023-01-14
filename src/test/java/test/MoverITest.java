package test;

import org.testng.Assert;
import org.junit.jupiter.api.Test;
import tp4.Game;
import tp4.MoverI;

class MoverITest {

    @Test
    void ConstructorAndInbounds() {
        Game game = new Game("english");
        MoverI mover = new MoverI() {
            @Override
            public String move(int i, int j, int k, int l) {
                return null;
            }
        };
        mover.setGame(game);
        Assert.assertTrue(mover.inBounds(0,0));
        Assert.assertFalse(mover.inBounds(9,9));

        // polish and german got the same mover, so test only one
        Game game2 = new Game("polish");
        mover.setGame(game2);
        Assert.assertTrue(mover.inBounds(0,0));
        Assert.assertTrue(mover.inBounds(9,9));
        Assert.assertFalse(mover.inBounds(-1,9));
    }
}