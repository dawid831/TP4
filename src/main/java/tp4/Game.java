package tp4;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

/**
 * a class that stars game.
 * there are 3 types of games to choose.
 */
public class Game {

    /**
     * types of pawns.
     * different start of game.
     */
    private ServerServer.Capitalizer white, black;

    /**
     * which player is already supossed to move.
     */
    private Color turn;

    /**
     * is suposed to tell about what is going on.
     * gives output on server.
     */
    private final Text text = new Text("");

    //constructor of class MoverI
    MoverI mover;

    //actual board
    private final Board board;

    /**
     * a list of white pawns in array.
     */
    private final List<Pawn> whitePawns = new ArrayList<>();

    /**
     * a list of black pawns in array
     */
    private final List<Pawn> blackPawns = new ArrayList<>();

    /**
     * con structor of class Game.
     * @param type is a type of game which will be played.
     */
    public Game(String type) {
        this.turn = Color.WHITE;
        switch (type) {
            case "english" -> {
                this.mover = new MoverClassic(this);
                this.board = new Board(8, mover, Color.BLACK);
            }
            case "german" -> {
                this.mover = new MoverGerman(this);
                this.board = new Board(8, mover, Color.WHITE);
            }
            case "polish" -> {
                this.mover = new MoverGerman(this);
                this.board = new Board(10, mover, Color.BLACK);
            }
            default -> throw new RuntimeException();
        }
        NormalBoardFiller normalBoardFiller = new NormalBoardFiller();
        normalBoardFiller.fill(this.board, 3);
        for(Node[] node : this.board.getNodeArray()) {
            for(Node nodePrim : node) {
                if(nodePrim.getPawn() != null) {
                    if(nodePrim.getPawn().getFill().equals(Color.WHITE)) whitePawns.add(nodePrim.getPawn());
                    else blackPawns.add(nodePrim.getPawn());
                    nodePrim.getPawn().setNode(nodePrim);
                }
            }
        }
    }

    /**
     * boolean that avoid joining more than 2 players on server.
     * @return true or false. Depend on amount of users on server.(>2==true)
     */
    public synchronized boolean canJoin() {
        return white == null || black == null;
    }

    /**
     * void that connects player on server
     * @param player is a color of player.
     */
    public synchronized void addPlayer(ServerServer.Capitalizer player) {
        if(white == null) {
            white = player;
            player.setColor(Color.WHITE);
        }
        else if(black == null) {
            black = player;
            player.setColor(Color.BLACK);
        }
    }

    /**
     * void which change color of actual move.
     */
    public synchronized void nextMove() {
        if(turn.equals(Color.BLACK)) {
            turn = Color.WHITE;
            text.setText("white's move");
        }
        else {
            turn = Color.BLACK;
            text.setText("black's move");
        }
    }

    /**
     * void that output text of winning player.
     * @param winner a color of wining player.
     */
    public synchronized void gameOver(Player winner) {
        text.setText(winner.getColor().equals(Color.WHITE) ? "white wins!" : "black wins!");
    }

    /**
     * @return give color of actual turn.
     */
    public synchronized ServerServer.Capitalizer getTurnsPlayer() {
        if(turn.equals(Color.WHITE)) {
            return white;
        }
        else return black;
    }

    /**
     * @return pawns of actual moving player.
     */
    public synchronized List<Pawn> getTurnsPlayerPawns() {
        if(turn.equals(Color.WHITE)) {
            return whitePawns;
        }
        else return blackPawns;
    }

    /**
     * @return enemy pawns.
     */
    public synchronized List<Pawn> getTurnsOpponentPawns() {
        if(turn.equals(Color.WHITE)) {
            return blackPawns;
        }
        else return whitePawns;
    }

    /**
     * @return oponent's turn.
     */
    public synchronized ServerServer.Capitalizer getTurnsOpponent() {
        if(turn.equals(Color.WHITE)) {
            return black;
        }
        else return white;
    }

    /**
     * @return actual player turn.
     */
    public synchronized Color getTurn() {
        return turn;
    }

    /**
     * @return get actual board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     *  void function get pawns as white.
     * @return whitePawns get white pawns.
     */
    public synchronized List<Pawn> getWhitePawns() {
        return whitePawns;
    }

    /**
     * @return list of black pawns.
     */
    public synchronized List<Pawn> getBlackPawns() {
        return blackPawns;
    }

    public MoverI getMover() {
        return mover;
    }
}
