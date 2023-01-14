package tp4;

import javafx.scene.paint.Color;

/**
 * class with information about player.
 */
public class Player {

    //color of actual player.
    private Color color;

    //constructor of class Player.
    public Player() {}

    /**
     * @return actual player's color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * void that set color on actual.
     * @param color actual color.
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
