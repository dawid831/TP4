package tp4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * a class that starts appliation.
 * is responsible for starting screen.
 * buttons with names of types checkers.
 * main class is here.
 * PASS "127.0.0.1" AS LINE ARGUMENT WHILE BOOTING
 */
public class HelloApplication extends Application{

    /**
     * void function to start stage.
     * @param stage is a stage.
     */
    @Override
    public void start(Stage stage) {
        List<String> list = getParameters().getRaw();
        try {
            var socket = new Socket(list.get(0), 58901);
            StartScreen pane = new StartScreen();
            stage.setScene(new Scene(pane, 800, 600));
            stage.setTitle("Checkers");
            stage.show();
            var in = new Scanner(socket.getInputStream());
            var out = new PrintWriter(socket.getOutputStream(), true);

            pane.getEnglishButton().setOnAction(e -> out.println("new english"));

            pane.getGermanButton().setOnAction(e -> out.println("new german"));

            pane.getPolishButton().setOnAction(e -> out.println("new polish"));

            //przycisk odswiezenia pobiera z serwera aktualnie toczace sie rozgrywki i wyswietla na ekranie
            pane.getRefreshButton().setOnAction(e -> {
                out.println("games");
                VBox vbox = new VBox();
                vbox.getChildren().addAll(pane.getInstructionButton(), pane.getRefreshButton(), pane.getEnglishButton(), pane.getGermanButton(), pane.getPolishButton());
                String VAR = in.nextLine();
                String[] VAR2 = VAR.split(" ");
                if(!VAR2[0].equals("none")) {
                    for (String s : VAR2) {
                        Button gameButton = new Button(s);
                        vbox.getChildren().add(gameButton);
                        gameButton.setOnAction(e1 -> {
                            out.println(s);
                            List<Pawn> pawns = new ArrayList<>();
                            String VAR4 = in.nextLine();
                            System.out.println(VAR4);
                            if(!VAR4.equals("error")) {
                                String[] boardParams = in.nextLine().split(" ");
                                String[] VAR3 = VAR4.split(";");
                                View.read(pawns, VAR3);
                                View view = new View(pawns, Integer.parseInt(boardParams[0]), in, out, boardParams[1].equals("white") ? Color.WHITE : Color.BLACK);
                                view.getText().setText(in.nextLine());
                                Scene scene1 = new Scene(view, 800, 600);
                                stage.setScene(scene1);
                            }
                        });
                    }
                }
                pane.setVbox(vbox);
                System.out.println(VAR);
            });
        }
        catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        Application.launch(args[0]);
    }
}