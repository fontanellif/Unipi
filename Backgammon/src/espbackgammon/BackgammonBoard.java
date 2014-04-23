package espbackgammon;

import java.util.*;
import static espbackgammon.BackgammonPlayer.*;

/**
 * La classe BackgammonPlayer rappresenta lo stato della tavola di gioco.
 *
 * Lo stato é composto da: la board, i due dadi, dal player attualmente di
 * turno, le mosse effettuate nel turno precedente e dal valore associato allo
 * stato.
 *
 * @author filippofontanelli
 */
public class BackgammonBoard implements Cloneable {

    boolean verbose = false;
    /**
     * Playboard dello stato.
     */
    private Board playboard = null;
    /**
     * Valore del primo dado. Si riferisce alla mossa che il player
     * <code> hastomove </code> deve effettuare.
     */
    private int dice1;
    /**
     * Valore del secondo dado. Si riferisce alla mossa che il player
     * <code> hastomove </code> deve effettuare.
     */
    private int dice2;
    /**
     * Valore del nodo associato allo stato.
     */
    private float value;
    /**
     * Giocatore che ha la mano e che quindi deve effettuare la mossa.
     */
    private int hasToMove;
    /**
     * Mosse effetuate per raggiungere lo stato attuale. Effettuate nel turno
     * precedente dall'altro giocatore.
     */
    private ArrayList<Move> moveDone;

    /* ======== Costruttori ========= */
    /**
     * Costruttore della classe.
     *
     * @param playboard la tavola di gioco
     * @param dice1 il primo dado
     * @param dice2 il secondo dado
     * @param value il valore dello stato
     * @param hasToMove il giocatore che deve giocare il turno
     * @param moveDone le mosse effettuate per raggiungere lo stato attuale
     *
     * @throws IllegalArgumentException se i parametri in ingresso non sono
     * corretti.
     */
    public BackgammonBoard(Board playboard, byte dice1, byte dice2, float value, int hasToMove, ArrayList<Move> moveDone) {
        if (!(Dice.checkDice(dice1)) || (Dice.checkDice(dice2))) {
            throw new IllegalArgumentException("Dice errati!!" + dice1 + "," + dice2);
        }

        if (playboard == null) {
            throw new IllegalArgumentException("Bord null");
        }

        if (checkAllPlayer(hasToMove)) {
            throw new IllegalArgumentException("HasToMove deve esser un player valido" + hasToMove);
        }

        this.playboard = playboard;
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.value = value;
        this.hasToMove = hasToMove;
        this.moveDone = moveDone;
    }

    /**
     * Costuttore base della classe. Inizializza lo stato della tavola di gioco.
     * Da utilizzare in fase di startup del gioco.
     */
    public BackgammonBoard() {
        /*
         * Inizializzamo la board allo disposizione iniziale, e gli altri attributi
         * ad un valore di startup
         */
        this.playboard = new Board();
        this.dice1 = 0;
        this.dice2 = 0;
        this.value = 0;
        this.hasToMove = Board.EMPTY;
        this.moveDone = new ArrayList<>();
    }

    /**
     * Costurrore della classe. Crea una nuova board a partire dallo stato
     * <dice> bb </dice> ricevuto in ingresso.
     *
     * @param bb stato del gioco
     *
     */
    public BackgammonBoard(BackgammonBoard bb) {

        this.playboard = new Board(bb.getPlayboard().getBoard());
        this.dice1 = bb.getDice1();
        this.dice2 = bb.getDice2();
        this.value = bb.getValue();
        this.hasToMove = bb.getHasToMove();
        this.moveDone = new ArrayList<>();
        for (Move move : bb.getMoveDone()) {
            this.moveDone.add(move);

        }
    }

    /* ======== Getter & Setter ========= */
    /**
     * Restituisce la tavola di gioco
     *
     * @return playboard
     */
    public Board getPlayboard() {
        return playboard;
    }

    /**
     * Modifica la tavola di gioco
     *
     * @throws IllegalArgumentException in caso la playboard risulti nulla.
     */
    public void setPlayboard(Board playboard) {
        if (playboard == null) {
            throw new IllegalArgumentException("Board is null");
        }
        this.playboard = playboard;
    }

    /**
     * Ritorna il valore del primo dado
     *
     * @return dice1
     */
    public int getDice1() {
        return dice1;
    }

    /**
     * Modifica il valore del primo dado
     *
     * @param dice1 valore del dado
     */
    public void setDice1(int dice1) {
        this.dice1 = dice1;
    }

    /**
     * Ritorna il valore del secondo dado
     *
     * @return dice2
     */
    public int getDice2() {
        return dice2;
    }

    /**
     * Modifica il valore del secondo dado
     *
     * @param dice2 valore del dado
     */
    public void setDice2(int dice2) {
        this.dice2 = dice2;
    }

    /**
     * Controlla se i dadi rappresentano un tiro doppo.
     *
     * @return True se i due dadi hanno lo stesso valore, False altrimenti.
     */
    public boolean isDouble() {
        return (dice1 == dice2);
    }

    /**
     * Restituisce il valore dell'ultimo dado giocato.
     *
     * @return il valore del dado se é stata effettuata una mossa, -1 nel caso
     * in cui non sia stata ancora effettuata una mossa.
     */
    public int getLastDice() {
        int sizemovedone = moveDone.size();
        if (sizemovedone == 0) {
            return -1;
        } else {
            return moveDone.get(sizemovedone - 1).getDice();
        }
    }

    /**
     * Restituisce il valore dello stato attuale.
     *
     * @return value
     */
    public float getValue() {
        return value;
    }

    /**
     * Modifica il valore dello stato attuale.
     *
     * @param value nuovo valore dello stato
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Restituisce l'identificativo del giocatore che é di turno.
     *
     * @return id_player
     */
    public int getHasToMove() {
        return hasToMove;
    }

    /**
     * Modifica l'identificativo del giocatore che é di turno.
     *
     * @param hasToMove identificativo del player
     */
    public void setHasToMove(int hasToMove) {
        this.hasToMove = hasToMove;

    }

    /**
     * Restituisce le mosse effettuate al turno precedente
     *
     * @return null se non esistono mosse effettuate, le mosse effettuate
     * altrimenti.
     */
    public ArrayList<Move> getMoveDone() {
        return moveDone;
    }

    /**
     * Modifica le mosse effettuate al turno precedente.
     *
     * @param moveDone
     */
    public void setMoveDone(ArrayList<Move> moveDone) {
        this.moveDone = moveDone;
    }

    /**
     * Verifica se lo stato é uno stato terminale.
     *
     * @return True se é uno stato terminale, False altrimenti.
     */
    public boolean isTerminal() {
        return playboard.isTerminal();
    }

    /* ======== Override ========= */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof BackgammonBoard)) {
            return false;
        }

        BackgammonBoard b = (BackgammonBoard) o;

        if (!this.playboard.equals(b.playboard)) {
            return false;
        }
        if (!this.moveDone.equals(b.moveDone)) {
            return false;
        }
        if ((this.dice1 != b.dice1) || (this.dice2 != b.dice2) || (this.hasToMove != b.hasToMove)) {
            return false;
        }

        return true;

    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.playboard.hashCode();
        hash = 67 * hash + this.dice1;
        hash = 67 * hash + this.dice2;
        hash = 67 * hash + Float.floatToIntBits(this.value);
        hash = 67 * hash + this.hasToMove;
        hash = 67 * hash + this.moveDone.hashCode();
        return hash;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(playboard.toString());
        stringBuilder.append("\nValue =").append(this.value);
        stringBuilder.append("\nDice: ").append(this.dice1).append(",").append(this.dice2);
        if (moveDone != null) {
            stringBuilder.append("\n").append(this.moveDone.toString());
        }
        stringBuilder.append("\nHasToMove: ").append(this.hasToMove);

        return stringBuilder.toString();


    }

    /* ======== Move Play ========= */
    /**
     * Gioca la mossa. Si presume che la mossa sia gia stata controllata.
     *
     * @param m la mossa da eseguire
     * @return True se é stato possibile eseguire la mossa, False altrimenti.
     */
    public boolean playMove(Move m) {

        boolean ret = true;
        /*
         * Effettuo la mossa e aggiorno le variabili d'istanza
         */
        try {
            ret = playboard.playMove(m);
        } catch (Exception e) {
            System.out.println("playMove -> " + m.toString() + ", " + e.toString());
            ret = false;
        }

        if (ret) {
            moveDone.add(m);
        }

        return ret;
    }

    /* ======== Move Generator ========= */
    /**
     * Dato un identificativo di un player, restituisce il fattore
     * moltiplicativo da associare alla mossa per quel determinato player.
     *
     * Dato che il player Black si muove incrementando l'indice delle proprie
     * posizioni la mossa verra moltiplicata per +1 , mentre per il player White
     * che si muove in senso contrario la mossa verra moltiplicata per -1.
     *
     * @param player identificativo del player
     * @return fattore moltiplicativo che esprime la direzione di marcia.
     */
    private int playertomove(int player) {
        return (player == Board.BLACK) ? +1 : -1;
    }

    /**
     * Metodo che genera tutte le possibili mosse legali, a partire dallo stato
     * attuale. Generando le mosse tramite l'utilizzo dei dadi descritti dallo
     * stato, per il player = hasToMove
     *
     * @return insieme di mosse legali.
     */
    public ArrayList<Move> getLegalMove(boolean used1, boolean used2) {

        ArrayList<Move> moves;

        moves = checkMoveFrom(used1, used2);
        moves = checkMoveTo(moves);


        return moves;
    }

    /**
     * Scansiona la playboard e determina le mosse possibili dallo stato attuale per il player di turno.
     * 
     * 
     * @param moves ArrayList di move da populare
     * @param used1 se true, nel calcolo delle mosse verra utilizzato il primo dado
     * @param used2 se true, nel calcolo delle mosse verra utilizzato il secondo dado
     * @return moves, ArrayList contenente le mosse legali (from)
     */
    private ArrayList<Move> checkMoveFrom(boolean used1, boolean used2) {
        ArrayList<Move> moves = new ArrayList<>();
        int mulplayer = playertomove(hasToMove);
        boolean find = false;
        boolean findleft = false;
        
        /*
         * Nel caso in cui mi trovi nello stato finale del gioco e che quindi 
         * possa effettivamente far uscire le pedine dalla tavola
         */
        if (playboard.canMoveHome(hasToMove)) {

            if (playboard.getOnBar(hasToMove).isEmpty()) {
                /*
                 * In base al player che sta giocando, devo adattare il valore del dado al senso di marcia.
                 */
                int tmpdc1 = (hasToMove == Board.WHITE) ? dice1 : (Board.NUM_BOARD - dice1 + 1);
                int tmpdc2 = (hasToMove == Board.WHITE) ? dice2 : (Board.NUM_BOARD - dice2 + 1);
                if (used1) {
                    /*
                     * Controllo se posso far uscire i checkers contenuti nella board di posizione dice
                     * importante nella fase finale del gioco.
                     */
                    if (playboard.getBoard()[tmpdc1].isplayerandnonEmpty(hasToMove)) {
                        moves.add(new Move(hasToMove, tmpdc1, playertoOtherhome(hasToMove), dice1));
                    } else {
                        
                        for (int i = tmpdc1; (((hasToMove == Board.BLACK) && (i >= endIndexBoard(hasToMove))) || ((hasToMove == Board.WHITE) && (i <= endIndexBoard(hasToMove)))); i = i + (1 * mulplayer * -1)) {
                            if (playboard.getBoard()[i].isplayerandnonEmpty(hasToMove)) {
                                moves.add(new Move(hasToMove, i, i + (dice1 * mulplayer), dice1));
                                find = true;
                            }
                        }

                        if (!find) {
                            for (int i = tmpdc1; (((hasToMove == Board.BLACK) && (i <= startIndexBoard(hasToMove))) || ((hasToMove == Board.WHITE) && (i >= startIndexBoard(hasToMove)))); i = i - (1 * mulplayer * -1)) {
                                if (playboard.getBoard()[i].isplayerandnonEmpty(hasToMove) && !findleft) {
                                    moves.add(new Move(hasToMove, i, playertoOtherhome(hasToMove), dice1));
                                    findleft = true;
                                }
                            }

                        }

                    }

                }

                if (used2) {
                    /*
                     * Controllo se posso far uscire i checkers contenuti nella board = dice
                     */
                    if (playboard.getBoard()[tmpdc2].isplayerandnonEmpty(hasToMove)) {
                        moves.add(new Move(hasToMove, tmpdc2, playertoOtherhome(hasToMove), dice2));
                    } else {


                        for (int i = tmpdc2; (((hasToMove == Board.BLACK) && (i >= endIndexBoard(hasToMove))) || ((hasToMove == Board.WHITE) && (i <= endIndexBoard(hasToMove)))); i = i + (1 * mulplayer * -1)) {
                            if (playboard.getBoard()[i].isplayerandnonEmpty(hasToMove)) {
                                moves.add(new Move(hasToMove, i, i + (dice2 * mulplayer), dice2));
                                find = true;
                            }
                        }

                        if (!find) {
                            for (int i = tmpdc2; (((hasToMove == Board.BLACK) && (i <= startIndexBoard(hasToMove))) || ((hasToMove == Board.WHITE) && (i >= startIndexBoard(hasToMove)))); i = i - (1 * mulplayer * -1)) {
                                if (playboard.getBoard()[i].isplayerandnonEmpty(hasToMove) && !findleft) {
                                    moves.add(new Move(hasToMove, i, playertoOtherhome(hasToMove), dice2));
                                    findleft = true;
                                }
                            }
                        }

                    }

                }

            }
        } else {

            for (int i = 1; (i <= 24 && playboard.getOnBar(hasToMove).isEmpty()); i++) {
                if (playboard.getBoard()[i].getPlayer() == hasToMove) {
                    /*
                     * Creo le 2 mosse relative ai dadi
                     */

                    if (used1) {
                        moves.add(new Move(hasToMove, i, i + (dice1 * mulplayer), dice1));
                    }
                    if (used2) {
                        moves.add(new Move(hasToMove, i, i + (dice2 * mulplayer), dice2));
                    }

                }
            }
        }

        /*
         * Creo 2 move proventi dal bar
         */
        int bar = playboard.getOnBar(hasToMove).getNumofcheckers();

        if ((bar > 0) && (used1)) {
            moves.add(new Move(hasToMove, playertobar(hasToMove), playertoOtherobar(hasToMove) + (dice1 * mulplayer), dice1));

        }
        if ((bar > 0) && (used2)) {
            moves.add(new Move(hasToMove, playertobar(hasToMove), playertoOtherobar(hasToMove) + (dice2 * mulplayer), dice2));
        }

        if (verbose) {
            for (Move move : moves) {
                System.out.println("checkfrom - " + move.toString());
            }
        }

        return moves;
    }
    /**
     * Riesamina le move selezionate dalla checkMoveFrom, adattando e aggiustando la parte to delle move.
     * 
     * @param moves ArrayList contenente le mosse legali (from)
     * @return moves, ArrayList contenente le mosse legali (from + to)
     */
    private ArrayList<Move> checkMoveTo(ArrayList<Move> moves) {
        /*
         * Per ogni move, controllo gli indici from and to, in particolare, 
         * li adatto alla configurazione della board
         */
        ArrayList<Move> temp = new ArrayList<>();
        
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            int from = move.getFrom();
            int to = move.getTo();
           
            /*
             * Se il to equivale ad uno spostamento in home
             */
            if ((to > Board.NUM_BOARD) || (to <= Board.STARTINDEXBOARD)) {
                if (playboard.canMoveHome(hasToMove)) {
                    move.setTo(playertohome(hasToMove));
                    temp.add(move);
                }
            } else if (!((playboard.getBoard()[to].getPlayer() == otherPlayer(hasToMove)) && (playboard.getBoard()[to].getNumofcheckers() > 1))) {
                if (Move.moveIsLegal(move, this)) {
                    temp.add(move);
                }
            }
        }
        return temp;
    }    
}
