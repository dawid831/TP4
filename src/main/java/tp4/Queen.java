package tp4;

import javafx.scene.paint.Color;

/**
 * class that handle visibility of a queen.
 */
public class Queen extends Pawn {

    /**
     * constuctor of a Class Queen
     * @param pawn pawn that we turn into the queen
     */
    public Queen(Pawn pawn) {
        super(pawn.getCenterX(), pawn.getCenterY(), pawn.getRadius(), (Color) pawn.getFill());
        this.setStroke(Color.YELLOW);
        this.setStrokeWidth(10);
    }

    /**
     * constructor of a Class Queen
     * @param i vertical cords.
     * @param j level cords.
     * @param color color of a pawn.
     */
    public Queen(int i, int j, Color color) {
        super(i, j, color);
        this.setStroke(Color.YELLOW);
        this.setStrokeWidth(10);
    }
}
