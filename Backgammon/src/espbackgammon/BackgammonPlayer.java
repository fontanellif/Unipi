package espbackgammon;

import static espbackgammon.Board.*;
import java.util.ArrayList;

/**
 *Classe astratta che definisce un generico player di backgammon
 * 
 * 
 * @author filippofontanelli
 */
public abstract class BackgammonPlayer {
    private int player_num;

    /**
     * Costruttore
     * @param player_num identificativo del player
     */
    public BackgammonPlayer(int player_num) {
        this.player_num = player_num;
    }

    /**
     * Restituisce l'identificativo del player
     * @return player_num
     */
    public int getPlayer_num() {
        return player_num;
    }

    /**
     * Modifica l'identificativo del player
     * @param player_num 
     */
    public void setPlayer_num(int player_num) {
        this.player_num = player_num;
    }
    
    /**
     * Inizializza le strutture dati della BackgammonBoard relative al player.
     * Lanciando i dadi, stampando la tavola da gioco,e inizializzando lo storico delle mosse.
     * @param bb oggetto della BackgammonBoard
     * @param d oggetto della calsse Dice
     * @return bb con le strutture dati relative al player inizializzate
     */
    public static BackgammonBoard initplayturn(BackgammonBoard bb, Dice d) {

        bb.setDice1(d.getDc1());
        bb.setDice2(d.getDc2());

        System.out.println(bb.toString());
        bb.setMoveDone(new ArrayList<Move>());
        return bb;
    }
    
    /**
     * Controlla che player sia effettivamente uno dei 3 player ammessi
     *
     * @param player identificativo del giocatore
     * @return True se player é uguale a WHITE or BLACK or EMPTY, False altrimenti
     */
    public static boolean checkAllPlayer(int player) {
        return !((player == Board.BLACK) || (player == Board.WHITE) || (player == Board.EMPTY));

    }
    /**
     * Controlla che player sia effettivamente uno tra White e Black
     *
     * @param player identificativo del giocatore
     * @return True se player é uguale a WHITE or BLACK, False altrimenti
     */
    public static boolean checkPlayer(int player) {
        return !((player == Board.BLACK) || (player == Board.WHITE));

    }

    /**
     * Restituisce l'indice della posizione del bar relativa la player.
     * 
     * @param player identificativo del giocatore
     * @return WHITEBAR se player = WHITE, BLACKBAR altrimenti
     */
    public static int playertobar(int player) {
        return (player == WHITE) ? WHITEBAR : BLACKBAR;
    }
    /**
     * Restituisce l'indice della posizione del bar relativa all'avversario.
     * @param player identificativo del giocatore
     * @return WHITEBAR se player = BLACK, BLACKBAR altrimenti
     */
    public static int playertoOtherobar(int player) {
        return (player == WHITE) ? BLACKBAR:WHITEBAR;
    }

    /**
     * Restituisce l'indice della posizione della Home relativa la player.
     * 
     * @param player identificativo del giocatore
     * @return WHITEEXIT se player = WHITE, BLACKEXIT altrimenti
     */
    public static int playertohome(int player) {
        return (player == WHITE) ? WHITEEXIT : BLACKEXIT;
    }
    /**
     * Restituisce l'indice della posizione della Home relativa all'avversario.
     * @param player identificativo del giocatore
     * @return WHITEEXIT se player = BLACK, BLACKEXIT altrimenti
     */
    public static int playertoOtherhome(int player) {
        return (player == WHITE) ? BLACKEXIT:WHITEEXIT;
    }

    /**
     * Restituisce l'identificativo dell'avversario.
     * 
     * @param player identificativo del giocatore
     * @return WHITE se player = BLACK,BLACK altrimenti.
     */
    public static int otherPlayer(int player) {
        if (checkPlayer(player)) {
            throw new IllegalArgumentException("Il player puo assumere solo 2 valori: WHITE,BLACK!!" + player);
        }

        return (player == BLACK) ? WHITE : BLACK;


    }
    /**
     * Restituisce l'indice iniziale della Board interna del player
     * 
     * @param player identificativo del giocatore
     * @return l'indice iniziale della Board interna
     */
    public static int startIndexBoard(int player){
        return (player == WHITE)? STARTINDEXBOARD+1:NUM_BOARD;
    }
    
     /**
     * Restituisce l'indice finale della Board interna del player
     * 
     * @param player identificativo del giocatore
     * @return l'indice finale della Board interna
     */
    public static int endIndexBoard(int player){
        return (player == WHITE)? ENDINDEXBOARDWHITE:ENDINDEXBOARDBLACK;
    }
    /**
     * Metodo astratto che definisce la funzione relativa alla logica di gioco del player.
     * 
     *
     * @param bb oggetto della classe BackgammonBoard (stato del gioco)
     * @return nuovo stato del gioco, modificato dalla player di turno
     */
    public abstract BackgammonBoard playturn(BackgammonBoard bb);
    
    
    
}
