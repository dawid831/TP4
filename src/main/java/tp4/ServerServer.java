package tp4;

import javafx.scene.paint.Color;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * class that handle server.
 */
public class ServerServer{

    //id of a server.
    private static int ID = 0;

    /**
     * int that get integral ID.
     * @return integral ID.
     */
    public static int getID() {
        return ID;
    }

    /**
     * void function that next one bigger ID.
     */
    public static void addID() {
        ID++;
    }
    public static void main(String[] args) {
        HashMap<String, Game> games = new HashMap<>();
        try(var listener = new ServerSocket(58901)) {
            System.out.println("Server running");
            var pool = Executors.newFixedThreadPool(20);
            while(true) {
                pool.execute(new Capitalizer(listener.accept(), games));
            }
        }
        catch (Exception e) {
            System.out.println("Exception");
        }
    }

    /**
     * static class that has informtions about game.
     */
    public static class Capitalizer extends Player implements Runnable {
        private final Socket socket;
        HashMap<String, Game> games;
        Game game;
        PrintWriter out;

        /**
         * constuctor of a class Capitalizer
         * @param socket actual socker.
         * @param games actual games.
         */

        Capitalizer(Socket socket, HashMap<String, Game> games) {
            this.socket = socket;
            this.games = games;
        }

        @Override
        public void run() {
            System.out.println("Connected: " + socket);
            try {
                Scanner in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                while (in.hasNextLine()) {
                    var VAR = in.nextLine();
                    if(VAR != null) {
                        if(VAR.equals("games")) {
                            out.println(gamesAsString());
                        }
                        else if(VAR.equals("new english")) {
                            String temp = String.valueOf(ServerServer.getID());
                            games.put(temp, new Game("english"));
                            ServerServer.addID();
                        }
                        else if(VAR.equals("new german")) {
                            String temp = String.valueOf(ServerServer.getID());
                            games.put(temp, new Game("german"));
                            ServerServer.addID();
                        }
                        else if(VAR.equals("new polish")) {
                            String temp = String.valueOf(ServerServer.getID());
                            games.put(temp, new Game("polish"));
                            ServerServer.addID();
                        }
                        else if(VAR.equals("view")) {
                            out.println(view());
                        }
                        else if(VAR.equals("move")) {
                            VAR = in.nextLine();
                            if(game.getTurnsPlayer() == null || !game.getTurnsPlayer().equals(this)) {
                                out.println("error");
                                out.println("not your turn");
                                continue;
                            }
                            String[] str = VAR.split(" ");
                            String VAR1 = game.mover.move(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]), Integer.parseInt(str[3]));
                            System.out.println(VAR1);
                            if(VAR1.equals("correct")) {
                                out.println("correct");
                                out.println(view());
                            }
                            else {
                                out.println("error");
                                out.println(VAR1);
                            }
                        }
                        else if(games.get(VAR) != null) {
                            if(games.get(VAR).canJoin()) {
                                games.get(VAR).addPlayer(this);
                                this.game = games.get(VAR);
                                out.println(view());
                                out.println(game.getBoard().getSize() + " " + (game.getBoard().getColor().equals(Color.WHITE) ? "white" : "black"));
                                out.println(this.getColor().equals(Color.WHITE) ? "you play white" : "you play black");
                            }
                            else {
                                out.println("error");
                                out.println("unknown error");
                            }
                        }
                        else {
                            out.println("error");
                            out.println("idk bro");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error1: " + socket);
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error2 " + socket);
                }
                System.out.println("Closed: " + socket);
            }
        }

        /**
         * String that return game.
         * @return game as string.
         */
        private String gamesAsString() {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < games.size(); i++) {
                if(i != games.size() - 1)
                    sb.append(i).append(" ");
                else sb.append(i);
            }
            return sb.toString().equals("") ? "none" : sb.toString();
        }

        /**
         * actual view of a board as string in one line.
         * @return string with positioning of all pawns.
         */
        private String view() {
            StringBuilder result = new StringBuilder();
            for(Pawn pawn : game.getWhitePawns()) {
                if(result.toString().equals("")) {
                    result.append(pawn instanceof Queen ? "Q" : "P")
                            .append(" ").append(pawn.getFill().equals(Color.BLACK)?"BLACK":"WHITE")
                            .append(" ").append(pawn.i).append(" ").append(pawn.j);
                }
                else {
                result.append(";").append(pawn instanceof Queen ? "Q" : "P")
                            .append(" ").append(pawn.getFill().equals(Color.BLACK)?"BLACK":"WHITE")
                            .append(" ").append(pawn.i).append(" ").append(pawn.j);
                }
            }
            for(Pawn pawn : game.getBlackPawns()) {
                if(result.toString().equals("")) {
                    result.append(pawn instanceof Queen ? "Q" : "P")
                            .append(" ").append(pawn.getFill().equals(Color.BLACK)?"BLACK":"WHITE")
                            .append(" ").append(pawn.i).append(" ").append(pawn.j);
                }
                else {
                    result.append(";").append(pawn instanceof Queen ? "Q" : "P")
                            .append(" ").append(pawn.getFill().equals(Color.BLACK)?"BLACK":"WHITE")
                            .append(" ").append(pawn.i).append(" ").append(pawn.j);
                }
            }
            return result.toString();
        }
    }
}
