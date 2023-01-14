package tp4;

import javafx.scene.paint.Color;

import java.util.List;

/**
 * a class responsible for english checkers.
 * check if move is correct.
 */
public class MoverClassic extends MoverI {
    //boolean that tells if multiple beating is available
    boolean extraBeat;

    // cords in vertical of pawn that was beating last.
    int last_i;

    // cords in level of pawn that was beating last.
    int last_j;

    // how many moves are players moving only queens without beating.
    // 30 == draw.
    int remis = 0;

    /**
     * constructor stars english checkers.
     * @param game is a actual game, shows it on board.
     */
    MoverClassic(Game game) {
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

        //booleans check if beating is available.
        boolean whiteHasBeating = colorHasBeating(getGame().getWhitePawns());
        boolean blackHasBeating = colorHasBeating(getGame().getBlackPawns());

        //positions of first and second click on board.
        Node node1 = getNodes()[i][j];
        Node node2 = getNodes()[k][l];


        //how many squares in diagonal is from a first click to a second click.
        int xj = l - j;
        int xi = k - i;

        if(extraBeat) {
            //sprawdza odleglosc na przekatnej drugiego klikniecia i czy pole o jedno pole na przekatnej jest puste.
            if(Math.abs(xi) == 2 && getNodes()[node1.i+xi/2][node1.j+xj/2].getPawn() != null) {
                if(i != last_i || j != last_j) {
                    return "wrong first pawn";
                }
                // sprawdza czy nie bije "swojego"
                if(getNodes()[node1.i+xi/2][node1.j+xj/2].getPawn().getFill().equals(node1.getPawn().getFill())) {
                    return "can't attack your own pawns";
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
                if(((Color.WHITE.equals(node2.getPawn().getFill()) && k == 7) || (Color.BLACK.equals(node2.getPawn().getFill()) && k == 0)) && (!(node2.getPawn() instanceof Queen))) {
                    Queen queen = new Queen(node2.getPawn());
                    getGame().getTurnsPlayerPawns().add(queen);
                    getGame().getTurnsPlayerPawns().remove(node2.getPawn());
                    queen.setNode(node2);
                    queen.setOnMouseClicked(node2.getPawn().getOnMouseClicked());
                    node2.setPawn(queen);
                    extraBeat = false;
                    getGame().nextMove();
                    return "correct";
                }

                // jezeli nastepne bicie jest mozliwe to trzeba je wykonac tym samym pionkiem
                if ((!(node2.getPawn() instanceof Queen)&&pawnCanBeat(node2.getPawn())) || ((node2.getPawn() instanceof Queen)&&ladyCanBeat(node2.getPawn()))){
                    last_i = k;
                    last_j = l;
                    extraBeat = true;
                    return "next beat";
                }

                //jezeli nastepne bicie nie jest mozliwe zmiana koloru ruchu.
                remis = 0;
                extraBeat = false;
                getGame().nextMove();
                return "correct";
            }
        }

        //pierwszy klik jest pusty
        if(node1.getPawn() == null) {
            return "null";
        }
        //pierwszy klik ma zly kolor ruchu
        if(!node1.getPawn().getFill().equals(getGame().getTurn())) {
            return "wrong color move";
        }
        //drugi klik jest taki sam jak pierwszy
        if(node1.equals(node2)) {
            return "same";
        }
        //drugi klik jest zajety
        if(node2.getPawn() != null) {
            return "occupied";
        }
        //ruch nie jest po przekatnej
        if(Math.abs(xi) != Math.abs(xj)) {
            return "you must move diagonally";
        }
        //biale ruszaja sie w gore, a nie moga.
        if((node1.getPawn().getFill().equals(Color.WHITE)) && xi < 0 && !(node1.getPawn() instanceof Queen)) {
            return "white must move downwards";
        }
        //czarne ruszaja sie w bol, a nie moga.
        if(node1.getPawn().getFill().equals(Color.BLACK) && xi > 0 && !(node1.getPawn() instanceof Queen)) {
            return "black must move upwards";
        }

        // ruch o 1 pole, bez bicia
        if(Math.abs(xi) == 1) {
            // musi bic, jezeli tylko moze
            if((node1.getPawn().getFill().equals(Color.WHITE)) && whiteHasBeating) {
                return "must beat";
            }
            if((node1.getPawn().getFill().equals(Color.BLACK)) && blackHasBeating) {
                return "must beat";
            }

            //jezeli dama poruszy sie o jedno pole po przekatnej liczymy ruchu pod rzad w przypadku liczy na remis.
            if(node1.getPawn() instanceof Queen) {
                remis++;
            }

            // przesuniecie piona w danych
            node2.setPawn(node1.getPawn());
            node1.setPawn(null);
            node2.getPawn().setNode(node2);

            // promocja jezeli mozliwa
            if((Color.WHITE.equals(node2.getPawn().getFill()) && k == 7) || (Color.BLACK.equals(node2.getPawn().getFill()) && k == 0) && (!(node2.getPawn() instanceof Queen))) {
                Queen queen = new Queen(node2.getPawn());
                getGame().getTurnsPlayerPawns().remove(node2.getPawn());
                getGame().getTurnsPlayerPawns().add(queen);
                queen.setNode(node2);
                queen.setOnMouseClicked(node2.getPawn().getOnMouseClicked());
                node2.setPawn(queen);
            }
            //sprawdzamy czy nastapil remis
            if(remis == 30) {
                return "remis";
            }
            //zmiana koloru ruchu
            getGame().nextMove();
            return "correct";
        }

        // ruch o 2 pola, bicie, gdy ruch o 2 i "skacze nad czymkolwiek"
        if(Math.abs(xi) == 2 && getNodes()[node1.i+xi/2][node1.j+xj/2].getPawn() != null) {
            // sprawdza czy nie bije "swojego"
            if(getNodes()[node1.i+xi/2][node1.j+xj/2].getPawn().getFill().equals(node1.getPawn().getFill())) {
                return "can't attack your own pawns";
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
            if((Color.WHITE.equals(node2.getPawn().getFill()) && k == 7) || (Color.BLACK.equals(node2.getPawn().getFill()) && k == 0) && (!(node2.getPawn() instanceof Queen))) {
                Queen queen = new Queen(node2.getPawn());
                getGame().getTurnsPlayerPawns().add(queen);
                getGame().getTurnsPlayerPawns().remove(node2.getPawn());
                queen.setNode(node2);
                queen.setOnMouseClicked(node2.getPawn().getOnMouseClicked());
                node2.setPawn(queen);
                getGame().nextMove();
                return "correct";
            }

            //sprawdza mozliwosc bicia po pierwszy biciu.
            if ((!(node2.getPawn() instanceof Queen)&&pawnCanBeat(node2.getPawn())) || ((node2.getPawn() instanceof Queen)&&ladyCanBeat(node2.getPawn()))){
                last_i = k;
                last_j = l;
                extraBeat = true;
                return "next beat";
            }

            //sprawdza czy czarny moze sie poruszyc, jesli nie to przegrywa.
            if((Color.WHITE.equals(node2.getPawn().getFill()))) {
                if(colorCantMove(getGame().getBlackPawns()) && !colorHasBeating(getGame().getBlackPawns())) {
                    return "White wins";
                }
                //sprawdza czy bialy moze sie poruszyc, jesli nie to przegrywa.
            } else if(Color.BLACK.equals(node2.getPawn().getFill())) {
                if((colorCantMove(getGame().getWhitePawns())) && !colorHasBeating(getGame().getWhitePawns())) {
                    return "Black wins";
                    //getGame().gameOver(player);
                }
            }

            //po biciu lub poruszeniu pionkiem restartujemy ilosc ruchu damami
            remis = 0;
            getGame().nextMove();
            return "correct";
        }
        else return "just wrong";
    }

    /**
     * boolean that tells if pawn can beat.
     * @param pawn actual checked pawn.
     * @return true or false depend if pawn can beat.(if can true)
     */
    private boolean pawnCanBeat(Pawn pawn) {
        int i = pawn.getNode().i;
        int j = pawn.getNode().j;
        int a;

        if (pawn.getFill().equals(Color.WHITE)) {
            a = 1;
        } else a = -1;

        return canBeat(pawn,a,i,j);
    }

    /**
     * boolean that tells if queen can beat.
     * @param pawn actual checked pawn.
     * @return true or false depend if queen can beat.(if can true)
     */
    private boolean ladyCanBeat(Pawn pawn) {
        int i = pawn.getNode().i;
        int j = pawn.getNode().j;
        return canBeat(pawn, 1, i, j) || canBeat(pawn, -1, i, j);
    }

    /**
     * boolean if pawn can beat.
     * @param pawn actual checked pawn.
     * @param a which side can move.
     * @param i vertical cord.
     * @param j level cord.
     * @return true or false. (if can true)
     */
    private boolean canBeat(Pawn pawn,int a, int i, int j) {
        if(inBounds(i + a, j + 1) && getNodes()[i + a][j + 1].getPawn() != null && !getNodes()[i+a][j+1].getPawn().getFill().equals(pawn.getFill()) && inBounds(i + 2 * a, j + 2) && getNodes()[i + 2 * a][j + 2].getPawn() == null) return true;
        else return inBounds(i + a, j - 1) && getNodes()[i + a][j - 1].getPawn() != null && !getNodes()[i+a][j-1].getPawn().getFill().equals(pawn.getFill()) && inBounds(i + 2 * a, j - 2) && getNodes()[i + 2 * a][j - 2].getPawn() == null;
    }

    /**
     * boolean if color can beat
     * @param pawns checked players pawns.
     * @return true or false. (if has true)
     */
    private boolean colorHasBeating(List<Pawn> pawns) {
        for(Pawn pawn : pawns) {
            if (!(pawn instanceof Queen)) {
                if (pawnCanBeat(pawn)) return true;
            }
            else if(ladyCanBeat(pawn)) {
                return true;
            }
        }
        return false;
    }

    /**
     * boolean that check if color can move.
     * @param pawns actual checked pawns.
     * @return true or false. (if can true)
     */
    private boolean colorCantMove(List<Pawn> pawns) {
        for (Pawn pawn : pawns) {
            if(!(pawn instanceof Queen)) {
                if (pawnCanMove(pawn)) return false;
            } else if(ladyCanMove(pawn)) return false;
        }
        return true;
    }

    /**
     * boolean that checks if pawn can move
     * @param pawn actual checked pawn
     * @return true or false. (if can true)
     */
    private boolean pawnCanMove(Pawn pawn) {
        int i = pawn.getNode().i;
        int j = pawn.getNode().j;
        int a;

        if (pawn.getFill().equals(Color.WHITE)) {
            a = 1;
        } else a = -1;

        if(inBounds(i + a, j + 1) && getNodes()[i + a][j + 1].getPawn() == null) return true;
        else return (inBounds(i + a, j - 1) && getNodes()[i + a][j - 1].getPawn() == null);
    }

    /**
     * boolean that tells if queen can move.
     * @param pawn actual checked queen.
     * @return true or false. (if can true)
     */
    private boolean ladyCanMove(Pawn pawn) {
        int i = pawn.getNode().i;
        int j = pawn.getNode().j;
        int a;
        a = 1;
        if(inBounds(i + a, j + 1) && getNodes()[i + a][j + 1].getPawn() == null) return true;
        else {
            if (inBounds(i + a, j - 1)) {
                getNodes();
            }
        }
        a = -1;
        if(inBounds(i + a, j + 1) && getNodes()[i + a][j + 1].getPawn() == null) return true;
        else return (inBounds(i + a, j - 1) && getNodes()[i + a][j - 1].getPawn() == null);
    }
}