package tp4;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * class that is responsible for handling and creating pawns.
 */
public class Pawn extends Circle {

    //constructor of class Node
    Node node;

    //vertical cords.
    int i;

    //level cords.
    int j;

    /**
     * constructor of class Pawn
     * @param v which game.
     * @param v1 pixels level.
     * @param v2 pixels vertical.
     * @param color color of pawn.
     */
    public Pawn(double v, double v1, double v2, Color color) {
        super(v, v1, v2, color);
        this.setOnMouseMoved(new PawnHoverEventHandler());
        this.setOnMouseExited(new PawnHoverEventHandler());
    }

    /**
     * set size and color of a pawn.
     * @param i vertical cords.
     * @param j level cords.
     * @param color color of pawn(black or white)
     */
    public Pawn(int i, int j, Color color) {
        this.i = i;
        this.j = j;
        this.setFill(color);
        this.setRadius(20.0d);
    }

    /**
     * @return actual node.
     */
    public Node getNode() {
        return node;
    }

    /**
     *  void function seting actual node.
     * @param node actual square.
     */
    public void setNode(Node node) {
        if(node == null) {
            this.node = null;
            return;
        }
        this.node = node;
        this.i = node.i;
        this.j = node.j;
    }

    @Override
    public String toString() {
        return "Pawn{" +
                "i=" + i +
                ", j=" + j +
                ", fill=" + getFill() +
                '}';
    }

    /**
     * handling of pawn.
     */
    static class PawnHoverEventHandler implements EventHandler<MouseEvent> {
        Pawn pawn;

        @Override
        public void handle(MouseEvent event) {
            pawn = (Pawn) event.getSource();
            if(pawn.getNode() == null) {
                System.out.println("nul");
                pawn.getNode().getOnMouseClicked();
                return;
            }
            if(event.getEventType()==MouseEvent.MOUSE_EXITED) {
                pawn.getNode().setStrokeWidth(0);
            }else if(event.getEventType()==MouseEvent.MOUSE_MOVED) {
                System.out.println("A");
                pawn.getNode().setStrokeWidth(10);
            }
        }
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }
}
