package test;

import javafx.scene.paint.Color;
import org.testng.Assert;
import org.testng.annotations.Test;
import tp4.Node;
import tp4.Pawn;

public class PawnTest {

    @Test
    public void testPawn() {
        Node node = new Node(75.0d * 4, 75.0d * (4), 75.0d, 75.0d, Color.BEIGE, 4, 4);
        Pawn pawn = new Pawn(6, 3, Color.BLACK);
        pawn.setNode(node);
        Assert.assertEquals(pawn.getI() == node.getI(), pawn.getJ() == node.getJ());
    }

}
