package tp4;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * class that contains first window of the project.
 * it is before connecting clients to game.
 */
public class StartScreen extends GridPane {

    //button that shows instructions
    private final Button instructionButton;

    //button that shows id of games.
    private final Button refreshButton;

    //button that starts english checkers.
    private final Button englishButton;

    //button that starts german checkers.
    private final Button germanButton;

    //button that starts polish checkers.
    private final Button polishButton;
    private VBox vbox;

    /**
     * constructor of class StartScreen
     */
    public StartScreen() {
        vbox = new VBox();
        instructionButton = new Button("How to play");
        instructionButton.setOnAction(event -> {
            Dialog<String> dialog = new Dialog<>();
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.setTitle("Instructions");
            dialog.setContentText("""
                    To play the game, the server must be up and running first.
                    Pressing button new *gamemode* prompts the server to create a new instance of game.
                    Use "refresh" button to download all game sessions from the server. The latest one is likely to be the one you have just created.
                    First person to connect to the game will play white pawns, the second person will play black pawns.
                    The game will make sure the player's move will take place only if it is the player's turn.
                    Pawns can only move diagonally, toward the enemy side of the board.
                    To move the pawn, click on the node on which it resides, then click on the destination node. If the move is correct, the pawn will move there.
                    The pawn that reaches the end line turns into a queen with extra movement capabilities depending on gamemode.
                    The aim of the game is to beat every opponent's pawn.
                    """);
            dialog.showAndWait();
        });
        refreshButton = new Button("Refresh");
        englishButton = new Button("New English version");
        germanButton = new Button("New German version");
        polishButton = new Button("New Polish version");
        vbox.getChildren().addAll(instructionButton, refreshButton, englishButton, germanButton, polishButton);
        getChildren().addAll(vbox);
    }

    /**
     * @return return instruction button.
     */
    public Button getInstructionButton() {
        return instructionButton;
    }

    /**
     * @return return refresh button.
     */
    public Button getRefreshButton() {
        return refreshButton;
    }

    /**
     * @return return english button.
     */
    public Button getEnglishButton() {
        return englishButton;
    }

    /**
     * @return return german button.
     */
    public Button getGermanButton() {
        return germanButton;
    }

    /**
     * @return return polish button.
     */
    public Button getPolishButton() {
        return polishButton;
    }

    /**
     * void function that set new vbox.
     * @param vbox setting and showing next vbox.
     */
    public void setVbox(VBox vbox) {
        this.getChildren().remove(this.vbox);
        this.vbox = vbox;
        this.getChildren().add(this.vbox);
    }
}
