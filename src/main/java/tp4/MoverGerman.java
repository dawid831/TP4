package tp4;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * class that contains rules of german type of checkers.
 */
public class MoverGerman extends MoverI{

    //hashmap with path of best beat
    private HashMap<Integer, List<Pawn>> bestBeatings;

    //pawn that must beat
    private Pawn forcePawn;

    /**
     * constructor of german checkers
     * @param game actual game.
     */
    MoverGerman(Game game) {
        bestBeatings = new HashMap<>();
        bestBeatings.put(0, new ArrayList<>());
        setGame(game);
    }

    /**
     * is a String that gives output what happend affter this move.
     * @param i is cords in vertical of first click.
     * @param j is cords in level of first click.
     * @param k is cords in vertical of second click.
     * @param l is cords in level of second click.
     * @return string which tells if move is correct, tells a result.
     */
    public String move(int i, int j, int k, int l) {

        //checks boeating possible
        boolean whiteHasBeating = colorHasBeating(getGame().getWhitePawns());
        boolean blackHasBeating = colorHasBeating(getGame().getBlackPawns());

        //positions of first and second click on board.
        Node node1 = getNodes()[i][j];
        Node node2 = getNodes()[k][l];

        //how many squares in diagonal is from a first click to a second click.
        int xj = l - j;
        int xi = k - i;

        // given node must contain a pawn, we get nullpointerexception otherwise
        if(node1.getPawn() == null) {
            return "null " + i + " " + j;
        }
        // cant move wrong color
        if(!node1.getPawn().getFill().equals(getGame().getTurn())) {
            return "wrong color move";
        }
        // if player is comitting a multi-beating they cant change the pawn
        if(forcePawn != null) {
            if(!getNodes()[i][j].getPawn().equals(forcePawn)) return "must move pawn " + forcePawn.i + " " + forcePawn.j;
        }
        if(node1.equals(node2)) {
            return "same " + i + " " + j;
        }
        if(node2.getPawn() != null) {
            return "occupied";
        }
        if(Math.abs(xi) != Math.abs(xj)) {
            return "you must move diagonally";
        }
        if((node1.getPawn().getFill().equals(Color.WHITE)) && xi < 0 && Math.abs(xi) != 2 && !(node1.getPawn() instanceof Queen)) {
            return "white must move downwards";
        }
        if(node1.getPawn().getFill().equals(Color.BLACK) && xi > 0 && Math.abs(xi) != 2 && !(node1.getPawn() instanceof Queen)) {
            return "black must move upwards";
        }

        if(bestBeatings.get(0).size() < 2 && (getGame().getTurn().equals(Color.WHITE) ? whiteHasBeating : blackHasBeating)) {
            findBestBeatings(getGame().getTurnsPlayerPawns());
        }

        // ruch o 1 pole, tylko dla PIONA, nie damki
        if(Math.abs(xi) == 1 && !(node1.getPawn() instanceof Queen)) {
            // musi bic, jezeli tylko moze
            if((node1.getPawn().getFill().equals(Color.WHITE)) && whiteHasBeating) {
                return "must beat";
            }
            if((node1.getPawn().getFill().equals(Color.BLACK)) && blackHasBeating) {
                return "must beat";
            }

            // przesuniecie piona w danych
            node2.setPawn(node1.getPawn());
            node1.setPawn(null);
            node2.getPawn().setNode(node2);

            // promocja jezeli mozliwa
            if((Color.WHITE.equals(node2.getPawn().getFill()) && k == 7) || (Color.BLACK.equals(node2.getPawn().getFill()) && k == 0)) {
                Queen queen = new Queen(node2.getPawn());
                getGame().getTurnsPlayerPawns().remove(node2.getPawn());
                getGame().getTurnsPlayerPawns().add(queen);
                queen.setNode(node2);
                queen.setOnMouseClicked(node2.getPawn().getOnMouseClicked());
                node2.setPawn(queen);
            }
            getGame().nextMove();
            return "correct";
        }

        // ruch PIONKA, nie damki, o 2 pola - bicie, gdy ruch o 2 i "skacze nad czymkolwiek"
        if(Math.abs(xi) == 2 && getNodes()[node1.i+xi/2][node1.j+xj/2].getPawn() != null && !(node1.getPawn() instanceof Queen)) {
            // sprawdza czy nie bije "swojego"
            if(getNodes()[node1.i+xi/2][node1.j+xj/2].getPawn().getFill().equals(node1.getPawn().getFill())) {
                return "can't attack your own pawns";
            }

            if(!isPawnOnMap(getNodes()[node1.i+xi/2][node1.j+xj/2].getPawn())) {
                return "this is not the best beating available";
            }


            // zmniejsza rozmiary mapy mozliwych bic jezeli bicie do niej nalezy
            if(bestBeatings.get(0).size() == 2) {
                forcePawn = null;
                bestBeatings.clear();
                bestBeatings.put(0, new ArrayList<>());
            }
            else {
                HashMap<Integer, List<Pawn>> temp = new HashMap<>();
                int counter = -1;
                for (List<Pawn> pawns : bestBeatings.values()) {
                    if (pawns.get(1).equals(getNodes()[node1.i + xi / 2][node1.j + xj / 2].getPawn()) && pawns.get(0).equals(node1.getPawn())) {
                        pawns.remove(getNodes()[node1.i + xi / 2][node1.j + xj / 2].getPawn());
                        counter++;
                        temp.put(counter, pawns);
                    }
                }
                if(counter == -1) {
                    if(forcePawn != null)
                        return "Musisz ruszyc piona " + forcePawn.i + " " + forcePawn.j;
                    else
                        return "to nie jest najlepszy ruch";
                }
                if(forcePawn == null && bestBeatings.size() > 0) {
                    forcePawn = node1.getPawn();
                }
                bestBeatings = temp;
            }

            // if-else zamieniony na ten kod, skrocony, usuwa pionek przeciwnika z jego puli
            getGame().getTurnsOpponentPawns().remove(getNodes()[node1.i+xi/2][node1.j+xj/2].getPawn());
            if(getGame().getTurnsOpponentPawns().size() == 0) {
                getGame().gameOver(getGame().getTurnsPlayer());
            }

            // ustawia brak piona zbitemu polu
            getNodes()[node1.i+xi/2][node1.j+xj/2].getPawn().setNode(null);
            getNodes()[node1.i+xi/2][node1.j+xj/2].setPawn(null);
            node2.setPawn(node1.getPawn());
            node1.setPawn(null);
            node2.getPawn().setNode(node2);

            // zamiana w damke jesli skonczyl sie ruch na polu zmiany
            if(forcePawn == null && (Color.WHITE.equals(node2.getPawn().getFill()) && k == 7) || (Color.BLACK.equals(node2.getPawn().getFill()) && k == 0)) {
                Queen queen = new Queen(node2.getPawn());
                getGame().getTurnsPlayerPawns().add(queen);
                getGame().getTurnsPlayerPawns().remove(node2.getPawn());
                queen.setNode(node2);
                queen.setOnMouseClicked(node2.getPawn().getOnMouseClicked());
                node2.setPawn(queen);
            }

            if(forcePawn == null) {
                getGame().nextMove();
            }

            return "correct";
        }

        // obsluga damki
        else if(node1.getPawn() instanceof Queen) {
            int signI = Math.abs(xi) / xi;
            int signJ = Math.abs(xj) / xj;
            Pawn killedPawn = null;
            for(int move = 1; move <= Math.abs(xi); move++) {
                if(getNodes()[node1.i + move * signI][node1.j + move * signJ].getPawn() != null) {
                    if(getNodes()[node1.i + move * signI][node1.j + move * signJ].getPawn().getFill().equals(node1.getPawn().getFill()))
                        return "queen cant kill their allies";
                    else if(killedPawn == null) {
                        killedPawn = getNodes()[node1.i + move * signI][node1.j + move * signJ].getPawn();
                    }
                    else {
                        return "queen cant kill so many pawns at once";
                    }
                }
            }

            // pokojowy ruch damy, bez bicia
            if(killedPawn == null) {
                if((node1.getPawn().getFill().equals(Color.WHITE)) && whiteHasBeating) {
                    return "must beat";
                }
                if((node1.getPawn().getFill().equals(Color.BLACK)) && blackHasBeating) {
                    return "must beat";
                }
                node2.setPawn(node1.getPawn());
                node1.setPawn(null);
                node2.getPawn().setNode(node2);
                getGame().nextMove();
                return "correct";
            }

            // dalej juz obsluguje bicie

            if(!isPawnOnMap(killedPawn, node2)) {
                return "this is not the best beating available";
            }

            // zmniejsza rozmiary mapy mozliwych bic jezeli bicie do niej nalezy
            if(bestBeatings.get(0).size() == 2) {
                forcePawn = null;
                bestBeatings.clear();
                bestBeatings.put(0, new ArrayList<>());
            }
            else {
                HashMap<Integer, List<Pawn>> temp = new HashMap<>();
                int counter = -1;
                for (List<Pawn> pawns : bestBeatings.values()) {
                    if (pawns.get(1).equals(killedPawn)
                            && pawns.get(0).equals(node1.getPawn())) {
                        pawns.remove(killedPawn);
                        counter++;
                        temp.put(counter, pawns);
                    }
                }
                if(counter == -1) {
                    if(forcePawn != null)
                        return "must move pawn " + forcePawn.i + " " + forcePawn.j;
                    else
                        return "this is not the best move available";
                }
                if(forcePawn == null && bestBeatings.size() > 0) {
                    forcePawn = node1.getPawn();
                }
                bestBeatings = temp;
            }

            // if-else zamieniony na ten kod, skrocony, usuwa pionek przeciwnika z jego puli
            getGame().getTurnsOpponentPawns().remove(killedPawn);
            if(getGame().getTurnsOpponentPawns().size() == 0) {
                getGame().gameOver(getGame().getTurnsPlayer());
            }

            // ustawia brak piona zbitemu polu
            getNodes()[killedPawn.i][killedPawn.j].setPawn(null);
            killedPawn.setNode(null);
            node2.setPawn(node1.getPawn());
            node1.setPawn(null);
            node2.getPawn().setNode(node2);

            // jezeli dalej ten pion ma jakies bicia do wykonania, to nie zmienia rundy gracza w grze
            if(forcePawn == null) {
                getGame().nextMove();
            }

            return "correct";
        }
        else return "unknown error";
    }

    // wersja dla damki, sprawdza, czy ruch nastepujacy po danym biciu rowniez jest wykonalny, o ile jakis po nim jest

    /**
     *
     * @param pawn actual check pawn.
     * @param node actual location of pawn.
     * @return returns true if pawn appears somewhere in the HashMap, false otherwise
     */
    private boolean isPawnOnMap(Pawn pawn, Node node) {
        for(List<Pawn> pawns : bestBeatings.values()) {
            if(pawns.size() > 2 && pawns.get(1).equals(pawn) && Math.abs(pawns.get(2).i - node.i) == Math.abs(pawns.get(2).j - node.j)) return true;
            if(pawns.size() == 2 && pawns.get(1).equals(pawn)) return true;
        }
        return false;
    }

    // sprawdza czy podany pionek moze cos zbic wokol siebie

    /**
     * private boolean that checks if pawn can beat.
     * @param pawn actual checked pawn.
     * @return true or false.
     */
    private boolean pawnCanBeat(Pawn pawn) {
        int i = pawn.i;
        int j = pawn.j;

        //sprawdza czy pola do bicia znajduja sie na planszy i czy sa "zamieszkane", jezeli tak, to przez kogo
        if(inBounds(i + 1, j + 1) && getNodes()[i + 1][j + 1].getPawn() != null && !getNodes()[i + 1][j + 1].getPawn().getFill().equals(pawn.getFill()) && inBounds(i + 2, j + 2) && getNodes()[i + 2][j + 2].getPawn() == null) return true;
        else if(inBounds(i + 1, j - 1) && getNodes()[i + 1][j - 1].getPawn() != null && !getNodes()[i + 1][j-1].getPawn().getFill().equals(pawn.getFill()) && inBounds(i + 2, j - 2) && getNodes()[i + 2][j - 2].getPawn() == null) return true;
        else if(inBounds(i - 1, j + 1) && getNodes()[i - 1][j + 1].getPawn() != null && !getNodes()[i - 1][j+1].getPawn().getFill().equals(pawn.getFill()) && inBounds(i - 2, j + 2) && getNodes()[i - 2][j + 2].getPawn() == null) return true;
        else return inBounds(i - 1, j - 1) && getNodes()[i - 1][j - 1].getPawn() != null && !getNodes()[i - 1][j-1].getPawn().getFill().equals(pawn.getFill()) && inBounds(i - 2, j - 2) && getNodes()[i - 2][j - 2].getPawn() == null;
    }

    /**
     * private boolean that checks if color has beating.
     * @param pawns list of color pawns.
     * @return true or false.
     */
    private boolean colorHasBeating(List<Pawn> pawns) {
        for(Pawn pawn : pawns) {
            if (!(pawn instanceof Queen) && pawnCanBeat(pawn)) {
                return true;
            }
            else if((pawn instanceof Queen) && LadyHasBeating(getGame().getTurnsOpponent().getColor(), pawn.i, pawn.j)) {
                return true;
            }
        }
        return false;
    }

    /**
     * boolean cheks if pawn is on map of best beat.
     * @param pawn actual checked pawn.
     * @return true or false.
     */
    private boolean isPawnOnMap(Pawn pawn) {
        for(List<Pawn> pawns : bestBeatings.values()) {
            if(pawns.size() > 0 && pawns.get(1).equals(pawn)) return true;
        }
        return false;
    }

    /**
     * void that find best beating
     * @param pawns actual color of pawns to check.
     */
    private void findBestBeatings(List<Pawn> pawns) {
        for(Pawn pawn : pawns) {
            if(!(pawn instanceof Queen) && pawnCanBeat(pawn)) {
                ArrayList<Pawn> temp = new ArrayList<>();
                temp.add(pawn);
                checkAllBeatings(pawn, temp);
            }
            
            else if(pawn instanceof Queen && LadyHasBeating(pawn.getFill().equals(Color.WHITE) ? Color.BLACK : Color.WHITE, pawn.i, pawn.j)) {
                ArrayList<Pawn> temp = new ArrayList<>();
                temp.add(pawn);
                queenCheckAllBeatings(pawn, temp);
            }
        }
    }

    /**
     * check all beating for best beat.
     * @param pawn pawn that is checked.
     * @param found best beat.
     */
    private void checkAllBeatings(Pawn pawn, List<Pawn> found) {
        // Gdy nie ma juz mozliwych bic LUB wszystkie potencjalne bicia juz zostaly znalezione w sekwencji "found"
        if(!(inBounds(pawn.i+2, pawn.j+2) && !found.contains(getNodes()[pawn.i+1][pawn.j+1].getPawn()) && pawnCanBeatNode(pawn, getNodes()[pawn.i+1][pawn.j+1])
            || inBounds(pawn.i+2, pawn.j-2) && !found.contains(getNodes()[pawn.i+1][pawn.j-1].getPawn()) && pawnCanBeatNode(pawn, getNodes()[pawn.i+1][pawn.j-1])
            || inBounds(pawn.i-2, pawn.j+2) && !found.contains(getNodes()[pawn.i-1][pawn.j+1].getPawn()) && pawnCanBeatNode(pawn, getNodes()[pawn.i-1][pawn.j+1])
            || inBounds(pawn.i-2, pawn.j-2) && !found.contains(getNodes()[pawn.i-1][pawn.j-1].getPawn()) && pawnCanBeatNode(pawn, getNodes()[pawn.i-1][pawn.j-1]))) {
            // dodaj liste jako potencjalne najlepsze bicia lub olej
            if(bestBeatings.get(0).size() < found.size()) {
                bestBeatings.clear();
                bestBeatings.put(0, found);
            }
            else if(bestBeatings.get(0).size() == found.size()) {
                bestBeatings.put(bestBeatings.keySet().size(), found);
            }
        }

        // Gdy da sie zejsc glebiej, zejdz.
        else {
            int tempI = pawn.i;
            int tempJ = pawn.j;
            if(inBounds(pawn.i+2, pawn.j+2) && pawnCanBeatNode(pawn, getNodes()[pawn.i+1][pawn.j+1]) && !found.contains(getNodes()[pawn.i+1][pawn.j+1].getPawn())) {
                ArrayList<Pawn> tempList = new ArrayList<>(found);
                tempList.add(getNodes()[pawn.i+1][pawn.j+1].getPawn());
                pawn.i += 2;
                pawn.j += 2;
                checkAllBeatings(pawn, tempList);
                pawn.i = tempI;
                pawn.j = tempJ;
            }
            if(inBounds(pawn.i+2, pawn.j-2) && pawnCanBeatNode(pawn, getNodes()[pawn.i+1][pawn.j-1]) && !found.contains(getNodes()[pawn.i+1][pawn.j-1].getPawn())) {
                ArrayList<Pawn> tempList = new ArrayList<>(found);
                tempList.add(getNodes()[pawn.i+1][pawn.j-1].getPawn());
                pawn.i += 2;
                pawn.j -= 2;
                checkAllBeatings(pawn, tempList);
                pawn.i = tempI;
                pawn.j = tempJ;
            }
            if(inBounds(pawn.i-2, pawn.j+2) && pawnCanBeatNode(pawn, getNodes()[pawn.i-1][pawn.j+1]) && !found.contains(getNodes()[pawn.i-1][pawn.j+1].getPawn())) {
                ArrayList<Pawn> tempList = new ArrayList<>(found);
                tempList.add(getNodes()[pawn.i-1][pawn.j+1].getPawn());
                pawn.i -= 2;
                pawn.j += 2;
                checkAllBeatings(pawn, tempList);
                pawn.i = tempI;
                pawn.j = tempJ;
            }
            if(inBounds(pawn.i-2, pawn.j-2) && pawnCanBeatNode(pawn, getNodes()[pawn.i-1][pawn.j-1]) && !found.contains(getNodes()[pawn.i-1][pawn.j-1].getPawn())) {
                ArrayList<Pawn> tempList = new ArrayList<>(found);
                tempList.add(getNodes()[pawn.i-1][pawn.j-1].getPawn());
                pawn.i -= 2;
                pawn.j -= 2;
                checkAllBeatings(pawn, tempList);
                pawn.i = tempI;
                pawn.j = tempJ;
            }
        }
    }


    /**
     * boolean that check if pawn can beat.
     * @param pawn actual checked pawn.
     * @param node to check if node is empty.
     * @return true or false.
     */
    private boolean pawnCanBeatNode(Pawn pawn, Node node) {
        return node.getPawn() != null && !node.getPawn().getFill().equals(pawn.getFill()) && getNodes()[pawn.i + 2 *(node.i - pawn.i)][pawn.j + 2 *(node.j - pawn.j)].getPawn() == null;
    }

    /**
     * void that check if queen has beating.
     * @param pawn check one queen.
     * @param found array of best beating pawns.
     */
    private void queenCheckAllBeatings(Pawn pawn, ArrayList<Pawn> found) {
        // Gdy nie ma juz mozliwych bic LUB wszystkie potencjalne bicia juz zostaly znalezione w sekwencji "found"
        if(!(canLadyReallyBeat(pawn, found))) {
            // dodaj liste jako potencjalne najlepsze bicia lub olej
            if(bestBeatings.get(0).size() < found.size()) {
                bestBeatings.clear();
                bestBeatings.put(0, found);
            }
            else if(bestBeatings.get(0).size() == found.size()) {
                bestBeatings.put(bestBeatings.keySet().size(), found);
            }
        }
        else {
            int n = pawn.i;
            int m = pawn.j;
            Color color = pawn.getFill().equals(Color.BLACK) ? Color.WHITE : Color.BLACK;

            int i = 1;
            // sprawdza po kolei wszystkie przekatne od damki
            while(inBounds(n + i, m + i)) {
                // poki nic nie ma, a pole jest na planszy, sprawdzaj dalej
                if (getNodes()[n + i][m + i].getPawn() == null || found.contains(getNodes()[n + i][m + i].getPawn())) {
                    i++;
                }
                else {
                    if(getNodes()[n + i][m + i].getPawn().getFill().equals(color)) {
                        i++;
                        if(inBounds(n + i, m + i) && getNodes()[n + i][m + i].getPawn() == null) found.add(getNodes()[n + (i - 1)][m + (i -1)].getPawn());
                        while(inBounds(n + i, m + i) && getNodes()[n + i][m + i].getPawn() == null) {
                            pawn.i += i;
                            pawn.j += i;
                            queenCheckAllBeatings(pawn, found);
                            pawn.i = n;
                            pawn.j = m;
                            i++;
                        }
                        break;
                    }
                    break;
                }
            }

            i = 1;
            while(inBounds(n + i, m - i)) {
                // poki nic nie ma, a pole jest na planszy, sprawdzaj dalej
                if (getNodes()[n + i][m - i].getPawn() == null || found.contains(getNodes()[n + i][m - i].getPawn())) {
                    i++;
                }
                else {
                    if(getNodes()[n + i][m - i].getPawn().getFill().equals(color)) {
                        i++;
                        if(inBounds(n + i, m - i) && getNodes()[n + i][m - i].getPawn() == null) found.add(getNodes()[n + (i - 1)][m - (i -1)].getPawn());
                        while(inBounds(n + i, m - i) && getNodes()[n + i][m - i].getPawn() == null) {
                            pawn.i += i;
                            pawn.j -= i;
                            queenCheckAllBeatings(pawn, found);
                            pawn.i = n;
                            pawn.j = m;
                            i++;
                        }
                        break;
                    }
                    break;
                }
            }

            i = 1;
            while(inBounds(n - i, m + i)) {
                // poki nic nie ma, a pole jest na planszy, sprawdzaj dalej
                if (getNodes()[n - i][m + i].getPawn() == null || found.contains(getNodes()[n - i][m + i].getPawn())) {
                    i++;
                }
                else {
                    if(getNodes()[n - i][m + i].getPawn().getFill().equals(color)) {
                        i++;
                        if(inBounds(n - i, m + i) && getNodes()[n - i][m + i].getPawn() == null) found.add(getNodes()[n - (i - 1)][m + (i -1)].getPawn());
                        while(inBounds(n - i, m + i) && getNodes()[n - i][m + i].getPawn() == null) {
                            pawn.i -= i;
                            pawn.j += i;
                            queenCheckAllBeatings(pawn, found);
                            pawn.i = n;
                            pawn.j = m;
                            i++;
                        }
                        break;
                    }
                    break;
                }
            }

            i = 1;
            while(inBounds(n - i, m - i)) {
                // poki nic nie ma, a pole jest na planszy, sprawdzaj dalej
                if (getNodes()[n - i][m - i].getPawn() == null || found.contains(getNodes()[n - i][m - i].getPawn())) {
                    i++;
                }
                else {
                    if(getNodes()[n - i][m - i].getPawn().getFill().equals(color)) {
                        i++;
                        if(inBounds(n - i, m - i) && getNodes()[n - i][m - i].getPawn() == null) found.add(getNodes()[n - (i - 1)][m - (i -1)].getPawn());
                        while(inBounds(n - i, m - i) && getNodes()[n - i][m - i].getPawn() == null) {
                            pawn.i -= i;
                            pawn.j -= i;
                            queenCheckAllBeatings(pawn, found);
                            pawn.i = n;
                            pawn.j = m;
                            i++;
                        }
                        break;
                    }
                    break;
                }
            }
        }
    }

    /**
     * boolean checks if lady can beat for sure.
     * @param pawn lady that is checked.
     * @param found list of best way.
     * @return true or false.
     */
    private boolean canLadyReallyBeat(Pawn pawn, ArrayList<Pawn> found) {
        int n = pawn.i;
        int m = pawn.j;
        Color color = pawn.getFill().equals(Color.BLACK) ? Color.WHITE : Color.BLACK;
        int i = 1;

        // sprawdza po kolei wszystkie przekatne od damki
        while(inBounds(n + i, m + i)) {
            // poki nic nie ma, a pole jest na planszy, sprawdzaj dalej
            if (getNodes()[n + i][m + i].getPawn() == null || found.contains(getNodes()[n + i][m + i].getPawn())) {
                i++;
            }
            else {
                if(getNodes()[n + i][m + i].getPawn().getFill().equals(color)) {
                    i++;
                    if(inBounds(n + i, m + i)) {
                        if(getNodes()[n + i][m + i].getPawn() == null) return true;
                    }
                    else break;
                }
                break;
            }
        }

        i = 1;
        while(inBounds(n + i, m - i)) {
            // poki nic nie ma, a pole jest na planszy, sprawdzaj dalej
            if (getNodes()[n + i][m - i].getPawn() == null || found.contains(getNodes()[n + i][m - i].getPawn())) {
                i++;
            }
            else {
                if(getNodes()[n + i][m - i].getPawn().getFill().equals(color)) {
                    i++;
                    if(inBounds(n + i, m - i)) {
                        if(getNodes()[n + i][m - i].getPawn() == null) return true;
                    }
                    else break;
                }
                break;
            }
        }

        i = 1;
        while(inBounds(n - i, m + i)) {
            // poki nic nie ma, a pole jest na planszy, sprawdzaj dalej
            if (getNodes()[n - i][m + i].getPawn() == null || found.contains(getNodes()[n - i][m + i].getPawn())) {
                i++;
            }
            else {
                if(getNodes()[n - i][m + i].getPawn().getFill().equals(color)) {
                    i++;
                    if(inBounds(n - i, m + i)) {
                        if(getNodes()[n - i][m + i].getPawn() == null) return true;
                    }
                    else break;
                }
                break;
            }
        }

        i = 1;
        while(inBounds(n - i, m - i)) {
            // poki nic nie ma, a pole jest na planszy, sprawdzaj dalej
            if (getNodes()[n - i][m - i].getPawn() == null || found.contains(getNodes()[n - i][m - i].getPawn())) {
                i++;
            }
            else {
                if(getNodes()[n - i][m - i].getPawn().getFill().equals(color)) {
                    i++;
                    if(inBounds(n - i, m - i)) {
                        if(getNodes()[n - i][m - i].getPawn() == null) return true;
                    }
                    else break;
                }
                break;
            }
        }

        // jesli nigdzie nie znalazlo, zwraca false
        return false;
    }

    /**
     * boolean checks if lady has beating.
     * @param color actual color.
     * @param n vertical cords.
     * @param m level cords.
     * @return true or false.
     */
    private boolean LadyHasBeating(Color color, int n, int m) {
        int i = 1;

        // sprawdza po kolei wszystkie przekatne od damki
        while(inBounds(n + i, m + i)) {
            // poki nic nie ma, a pole jest na planszy, sprawdzaj dalej
            if (getNodes()[n + i][m + i].getPawn() == null) {
                i++;
            }
            else {
                if(getNodes()[n + i][m + i].getPawn().getFill().equals(color)) {
                    i++;
                    if(inBounds(n + i, m + i)) {
                        if(getNodes()[n + i][m + i].getPawn() == null) return true;
                    }
                    else break;
                }
                break;
            }
        }

        i = 1;
        while(inBounds(n + i, m - i)) {
            // poki nic nie ma, a pole jest na planszy, sprawdzaj dalej
            if (getNodes()[n + i][m - i].getPawn() == null) {
                i++;
            }
            else {
                if(getNodes()[n + i][m - i].getPawn().getFill().equals(color)) {
                    i++;
                    if(inBounds(n + i, m - i)) {
                        if(getNodes()[n + i][m - i].getPawn() == null) return true;
                    }
                    else break;
                }
                break;
            }
        }

        i = 1;
        while(inBounds(n - i, m + i)) {
            // poki nic nie ma, a pole jest na planszy, sprawdzaj dalej
            if (getNodes()[n - i][m + i].getPawn() == null) {
                i++;
            }
            else {
                if(getNodes()[n - i][m + i].getPawn().getFill().equals(color)) {
                    i++;
                    if(inBounds(n - i, m + i)) {
                        if(getNodes()[n - i][m + i].getPawn() == null) return true;
                    }
                    else break;
                }
                break;
            }
        }

        i = 1;
        while(inBounds(n - i, m - i)) {
            // poki nic nie ma, a pole jest na planszy, sprawdzaj dalej
            if (getNodes()[n - i][m - i].getPawn() == null) {
                i++;
            }
            else {
                if(getNodes()[n - i][m - i].getPawn().getFill().equals(color)) {
                    i++;
                    if(inBounds(n - i, m - i)) {
                        if(getNodes()[n - i][m - i].getPawn() == null) return true;
                    }
                    else break;
                }
                break;
            }
        }

        // jesli nigdzie nie znalazlo, zwraca false
        return false;
    }
}
