package tp4;

/**
 * abstract class MoverI
 */
public abstract class MoverI {

    //array with cords of square
    private Node[][] nodes;

    //actual game, form class Game
    private Game game;

    /**
     * is a String that gives output what happend affter this move.
     * @param i is cords in vertical of first click.
     * @param j is cords in level of first click.
     * @param k is cords in vertical of second click.
     * @param l is cords in level of second click.
     * @return string which tells if move is correct, tells a result.
     */
    public abstract String move(int i, int j, int k, int l);

    /**
     * boolean if a square is on board
     * @param a vertical cord.
     * @param b level cord.
     * @return true or false. (if is true)
     */
    public boolean inBounds(int a, int b) {
        return a >= 0 && a < getGame().getBoard().getSize() && b >= 0 && b < getGame().getBoard().getSize();
    }

    /**
     * @return returns nodes on this cords.
     */
    public Node[][] getNodes() {
        return nodes;
    }

    /**
     * void that sets nodes.
     * @param nodes cords in vertical and leves.
     */
    public void setNodes(Node[][] nodes) {
        this.nodes = nodes;
    }

    /**
     * @return actual game.
     */
    public Game getGame() {
        return game;
    }

    /**
     *
     * @param game set actual game as game.
     */
    public void setGame(Game game) {
        this.game = game;
    }
}
