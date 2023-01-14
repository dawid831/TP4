package tp4;

import javafx.scene.paint.Color;

/**
 * The Board class that creates a main board.
 * There are 2 types of colors,bord is divided on square in 2 colors(size of squares is defined here).
 * @version 1.3
 */
public class Board {

    /**
     * side of a board.
     */
    private final int size;
    /**
     * color of bottom-left square
     */
    private final Color color;

    /**
     * an array 2d where are cords of a particular square
     */
    private final Node[][] nodeArray;

    /**
     *
     * @param size side of a board.
     * @param mover a constructor of class Moverl, there are parameters defined game
     * @param color color of bottom-left square
     */
    public Board(int size, MoverI mover, Color color) {
        this.size = size;
        this.color = color;
        nodeArray = new Node[size][size];
        mover.setNodes(nodeArray);

        // i - szerokosc, j - wysokosc
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                Color colour;
                if ((i + j) % 2 == 0) colour = Color.GREEN;
                else colour = Color.BEIGE;
                nodeArray[i][j] = new Node(75.0d * j, 75.0d * (size - 1 - i), 75.0d, 75.0d, colour, i, j);
            }
        }
    }

    /**
     * int function.
     * @return size of board (integral).
     */
    public int getSize() {
        return size;
    }

    /**
     * @return an array 2d where are cords of a particular square
     */
    public Node[][] getNodeArray() {
        return nodeArray;
    }

    /**
     * @return players color.
     */
    public Color getColor() {
        return color;
    }

}
