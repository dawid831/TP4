package tp4;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * class that shows actual view of a board
 */
public class View extends BorderPane {
    private final Node[][] nodes;

    //color to fill
    private Color filler;

    private Node moveCommand;

    //scanner
    private final Scanner in;

    //printwriter
    private final PrintWriter out;

    //list of pawns.
    private List<Pawn> pawns;

    //showing text
    private final Text text;
    private final Text errorText;

    /**
     * constructor of a class View
     * @param pawns list of all pawns
     * @param d size of side.
     * @param in scanner.
     * @param out printwriter.
     * @param color actual color of pawns.
     */

    public View(List<Pawn> pawns, int d, Scanner in, PrintWriter out, Color color) {
        this.in = in;
        this.out = out;
        this.pawns = pawns;
        this.nodes = new Node[d][d];
        this.text = new Text("");
        this.errorText = new Text("");
        VBox vbox = new VBox(text, errorText);
        this.setRight(vbox);
        //i - od lewej do prawej, j - od dolu do gory
        for(int i = 0; i < nodes.length; i++) {
            for(int j = 0; j < nodes.length; j++) {
                nodes[i][j] = new Node(75.0d * j, 75.0d * (nodes.length - 1 - i), 75.0d, 75.0d, (i+j)%2==0 ? color.equals(Color.BLACK) ? Color.GREEN:Color.BEIGE : color.equals(Color.BLACK) ? Color.BEIGE:Color.GREEN, i, j);
                this.getChildren().add(nodes[i][j]);
            }
        }
        setNodesClick();
        for(Pawn pawn : pawns) {
            nodes[pawn.i][pawn.j].setPawn(pawn);
            pawn.setNode(nodes[pawn.i][pawn.j]);
            pawn.setCenterX(pawn.getNode().getX()+pawn.getNode().getWidth()/2.0d);
            pawn.setCenterY(pawn.getNode().getY()+pawn.getNode().getHeight()/2.0d);
            this.getChildren().add(pawn);
        }
        setPawnsClick(pawns);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> checkServer()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
    }

    /**
     * void that refresh clients
     */
    public void checkServer() {
        out.println("view");
        List<Pawn> pawns = new ArrayList<>();
        String VAR4 = in.nextLine();
        if(VAR4.equals("error")) {
            System.out.println(in.nextLine());
        }
        if(!VAR4.equals("error")) {
            String[] VAR3 = VAR4.split(";");
            read(pawns, VAR3);
            drawNewView(pawns);
        }
    }

    /**
     * void that set node on click.
     */
    private void setNodesClick() {
        for (Node[] node : nodes) {
            for (int i = 0; i < nodes.length; i++) {
                node[i].setOnMouseClicked(event -> {
                    Node node1 = (Node) event.getSource();
                    move(node1);
                });
            }
        }
    }

    /**
     * void that moves a pawn.
     * @param node actual clicked node.
     */
    private synchronized void move(Node node) {
        if(moveCommand == null) {
            moveCommand = node;
            filler = (Color) node.getFill();
            node.setFill(Color.RED);
        }
        else {
            out.println("move");
            out.println(moveCommand.i + " " + moveCommand.j + " " + node.i + " " + node.j);
            moveCommand.setFill(filler);
            moveCommand = null;
            String response = in.nextLine();
            if(response.equals("correct")) {
                List<Pawn> pawns = new ArrayList<>();
                String[] VAR3 = in.nextLine().split(";");
                read(pawns, VAR3);
                setPawnsClick(pawns);
                errorText.setText("");
                this.drawNewView(pawns);
            }
            else if(response.equals("error")){
                errorText.setText(in.nextLine());
            }
        }
    }

    /**
     * void that reads actual positioning of pawns.
     * @param pawns pawns that are read to
     * @param VAR3 source
     */
    static void read(List<Pawn> pawns, String[] VAR3) {
        for(String str : VAR3) {
            String[] pawnStats = str.split(" ");
            if(pawnStats[0].equals("P")) {
                pawns.add(new Pawn(Integer.parseInt(pawnStats[2]),
                        Integer.parseInt(pawnStats[3]),
                        pawnStats[1].equals("BLACK") ? Color.BLACK : Color.WHITE));
            }
            else {
                pawns.add(new Queen(Integer.parseInt(pawnStats[2]),
                        Integer.parseInt(pawnStats[3]),
                        pawnStats[1].equals("BLACK") ? Color.BLACK : Color.WHITE));
            }
        }
    }

    /**
     * void that set pawn as clicked.
     * @param pawns list of pawns.
     */
    private void setPawnsClick(List<Pawn> pawns) {
        for(Pawn pawn : pawns) {
            pawn.setOnMouseClicked(event -> {
                Pawn pawn1 = (Pawn) event.getSource();
                move(pawn1.getNode());
            });
        }
    }

    /**
     * draw new positioning of pawns.
     * @param pawns all pawns.
     */
    private void drawNewView(List<Pawn> pawns) {
        for(Pawn pawn : this.pawns) {
            nodes[pawn.i][pawn.j].setPawn(null);
            this.getChildren().remove(pawn);
        }
        this.pawns = pawns;
        for(Pawn pawn : pawns) {
            nodes[pawn.i][pawn.j].setPawn(pawn);
            pawn.setNode(nodes[pawn.i][pawn.j]);
            pawn.setCenterX(pawn.getNode().getX()+pawn.getNode().getWidth()/2.0d);
            pawn.setCenterY(pawn.getNode().getY()+pawn.getNode().getHeight()/2.0d);
            this.getChildren().add(pawn);
        }
        setPawnsClick(pawns);
    }

    /**
     * text getter.
     * @return text that is in.
     */
    public Text getText() {
        return text;
    }

}
