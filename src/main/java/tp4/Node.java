package tp4;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * class that makes squares.
 */
public class Node extends Rectangle {

    private Pawn pawn;

    //cords of square
    int i, j;

    /**
     * constructor of class Node.
     * @param x pixels in level.
     * @param y pixels in vertical.
     * @param width width of square.
     * @param height height of square.
     * @param color color of square.
     * @param i cord of square in level.
     * @param j cord of square in vertical.
     */
    public Node(double x, double y, double width, double height, Color color, int i, int j) {
        super(x, y, width, height);
        this.i = i;
        this.j = j;
        setFill(color);
        setStroke(Color.BLUE);
        setStrokeWidth(0);
        setStrokeType(StrokeType.INSIDE);
        setOnMouseEntered(new NodeHoverEventHandler());
        setOnMouseExited(new NodeHoverEventHandler());
    }

    /**
     * @return actual pawn.
     */
    public Pawn getPawn() {
        return pawn;
    }

    /**
     * void that creates new pawn.
     * @param color tell which color pawn is.
     */
    public void newPawn(Color color) {
        this.pawn = new Pawn(getX()+getWidth()/2.0d, getY()+getHeight()/2.0d, 20.0d, color);
    }

    /**
     * void function that set actual pawn.
     * @param pawn set actual pawn.
     */
    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }

    /**
     * class responsible for handling clicking on board.
     * makes clicked square more visible.
     */
    static class NodeHoverEventHandler implements EventHandler<MouseEvent> {
        Node node;

        @Override
        public void handle(MouseEvent event) {
            node = (Node) event.getSource();
            if(event.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
                node.setStrokeWidth(0);
            }else if(event.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
                node.setStrokeWidth(10);
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
