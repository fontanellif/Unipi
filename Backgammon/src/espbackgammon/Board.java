package espbackgammon;

import static espbackgammon.BackgammonPlayer.*;

/**
 * La classe Board rappresenta la tavola di gioco (fisica) del Backgammon.
 *
 * @author filippofontanelli
 */
public class Board implements Cloneable {

    /**
     * Identificativo del giocatore White
     */
    public static final int WHITE = 2;
    /**
     * Identificativo del giocatore Black
     */
    public static final int BLACK = 1;
    /**
     * Identificativo del giocatore Empty
     */
    public static final int EMPTY = 0;
    public static final int BLACKBAR = 25;
    public static final int WHITEBAR = 0;
    public static final int BLACKEXIT = 27;
    public static final int WHITEEXIT = 26;
    public static final int STARTINDEXBOARD = 0;
    public static final int NUM_BOARD = 24;
    public static final int NUM_POSITION = 28;
    public static final int ENDINDEXBOARDWHITE = 6;
    public static final int ENDINDEXBOARDBLACK = 19;
    /**
     * Numero di pedine per player
     */
    public static final int CHECKERS_FOR_PLAYER = 15;
    /**
     * Array di Position che identificano la board,il bar e la home.
     */
    private Position[] board = null;

    /**
     * Costruttore della calsse Board Oltre ad inizializzare le strutture dati,
     * inizializza la tavola da gioco, posizionando il giusto numero di pedine
     * nelle posizoni iniziali
     */
    /**
     * Costruttore della classe Board. Oltre ad inizializzare le strutture dati,
     * dispone le pedine sulla tavola da gioco secondo la normale disposizione
     * di apertura.
     */
    public Board() {

        /*
         * Inizializzo la board
         */
        board = new Position[NUM_POSITION];
        for (int i = 0; i < NUM_POSITION; i++) {
            board[i] = new Position();
        }

        /*
         * Inizializzo la tavola da gioco con i valori previsti dal gioco del
         * backgammon
         */
        //Black Player
        board[1].setPosition(BLACK, 2);
        board[12].setPosition(BLACK, 5);
        board[17].setPosition(BLACK, 3);
        board[19].setPosition(BLACK, 5);
        board[BLACKBAR].setPosition(BLACK, 0);
        board[BLACKEXIT].setPosition(BLACK, 0);

        //White Player
        board[24].setPosition(WHITE, 2);
        board[13].setPosition(WHITE, 5);
        board[8].setPosition(WHITE, 3);
        board[6].setPosition(WHITE, 5);
        board[WHITEBAR].setPosition(WHITE, 0);
        board[WHITEEXIT].setPosition(WHITE, 0);

    }

    /**
     * Costurrore della classe Board. Crea una nuova board a partire dalla
     * <dice> board </dice> ricevuta in ingresso.
     *
     * @param board tavola di gioco
     * @throws IllegalArgumentException se la <dice> board </dice> é null.
     */
    public Board(Position[] board) {
        if (board == null) {
            throw new IllegalArgumentException("Board is null!!");
        }

        this.board = new Position[NUM_POSITION];
        for (int i = 0; i < NUM_POSITION; i++) {
            this.board[i] = new Position(board[i].getPlayer(), board[i].getNumofcheckers());
        }

    }

    /**
     * Restituisce la tavola di gioco
     *
     * @return board
     */
    public Position[] getBoard() {
        return board;
    }

    /**
     * Restituisce la position relativa al bar del player.
     *
     * @param player identificativo del player.
     * @return bar, oggetto di tipo position relativo al bar del player.
     * @throws IllegalArgumentException se il player é errato.
     */
    public Position getOnBar(int player) {
        if (checkPlayer(player)) {
            throw new IllegalArgumentException("Il player puo assumere solo 2 valori: WHITE,BLACK!!" + player);
        }
        return this.board[playertobar(player)];
    }

    /**
     * Restituisce la position relativa alla home del player.
     *
     * @param player identificativo del player.
     * @return home, oggetto di tipo position relativo alla home del player.
     * @throws IllegalArgumentException se il player é errato.
     */
    public Position getInHome(int player) {
        if (checkPlayer(player)) {
            throw new IllegalArgumentException("Il player puo assumere solo 2 valori: WHITE,BLACK!!" + player);
        }
        return this.board[playertohome(player)];
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }
        if (!(o instanceof Board)) {
            return false;
        }

        Board b = (Board) o;

        for (int i = 0; i < NUM_POSITION; i++) {
            if (!this.board[i].equals(b.board[i])) {
                return false;
            }
        }
        return true;
    }
    /**
     * Controlla sei il player ha vinto
     *
     * @param player identificativo del player
     * @return True se il player ha vinto la partita ,False altrimenti
     */
    private boolean hasWon(int player) {
        return board[playertohome(player)].getNumofcheckers() == CHECKERS_FOR_PLAYER;
    }

    /**
     * Controlla se la board é in uno stato terminale della partita
     *
     * @return True se la partita é finita, False altrimenti
     */
    public boolean isTerminal() {
        return (hasWon(BLACK) || hasWon(WHITE));
    }

    /**
     * Controlla se la board ha un vincitore
     *
     * @return l'identificativo del vincitore, EMPTY altrimenti.
     */
    public int whoWin() {
        if (hasWon(WHITE)) {
            return WHITE;
        }
        if (hasWon(BLACK)) {
            return BLACK;
        }
        return EMPTY;
    }

    /**
     * Esegue in modo atomico la mossa ricevuta in ingresso.
     *
     * Andando ad implementare il meccanismo di spostamento nel bar e nella home
     *
     * @param m la mossa da effettuare
     * @return True se é stato possibile effettuare la mossa, False altrimenti.
     */
    public boolean playMove(Move m) {
        return pickCheckers(m.getPlayer(), m.getFrom()) && putCheckers(m.getPlayer(), m.getTo());
    }
    
    //NBBBBBBB: DA TESTARE!!!!!!
    /**
     * Ripristina la board effettuando la mossa ricevuta in ingresso in senso opposto.
     *
     * Andando ad implementare il meccanismo di spostamento nel bar e nella home
     *
     * @param m la mossa da effettuare
     * @return True se é stato possibile effettuare la mossa, False altrimenti.
     */
    public boolean unPlayMove(Move m) {
        return pickCheckers(m.getPlayer(), m.getTo()) && putCheckers(m.getPlayer(), m.getFrom());
    }

    /**
     * Rimuove dalla position un checker del player
     *
     * @param player identifica il player che sta effettuando la move
     * @param column identifica la colonna su cui effettuare la move
     */
    private boolean pickCheckers(int player, int column) {
        /*
         * Controllo che il player sia lo stesso
         */
        if (board[column].getPlayer() == player) {
            /*
             * Tolgo il checker dalla position
             */
            board[column].decrChecerks();
            /*
             * Nel caso in cui stia prelevando dal bar, nel caso in cui il bar diventi = 0 devo 
             * settare nuovamente il player, effetto collaterale della decr
             */
            if ((column == BLACKBAR) || (column == WHITEBAR)) {
                board[column].setPlayer(player);
            }
            return true;
        } else {
            /*
             * Nel caso non possa effettuare il prelievo ritorno false
             */
            return false;
        }
    }
    /**
     * Effettua l'incremento dei checkers presenti nella colonna del player.
     * Solamente se rispetta le politiche di gioco.
     *
     * @param player identifica il player che effettua la mossa
     * @param column identifica la colonna da incrementare
     */
    private boolean putCheckers(int player, int column) {

        if ((board[column].getPlayer() == otherPlayer(player)) && (board[column].getNumofcheckers() > 1)) {
            /*
             * Nel caso in cui stia tentando in inserire un checker nella posizione 
             * occupata dal mio avversario ritorno false
             * 
             * CONTROLLO RIDONDANTE
             */
            return false;
        }

        if ((board[column].getPlayer() == otherPlayer(player)) && (board[column].getNumofcheckers() == 1)) {
            /*
             * Nel caso in cui stia per mangiare il checker del mio avversario
             * dato che la sua position non riusta chiusa
             */
            board[column].setPosition(player, 1);
            /*
             * Tramite otherPlayer recupero l'indice identificativo del mio avversario
             * Tramite playertobar applicata al risultato precedente, identivico
             * il bar del mio avversario
             * ed in fine procedo con il suo incremento
             */
            board[playertobar(otherPlayer(player))].incrChecerks();
        } else if (board[column].getPlayer() == EMPTY) {
            board[column].setPosition(player, 1);
        } else {
            /*
             * In fine caso di un classico e giusto spostamento di un checker
             * in cui dobbiamo semplicemnte incrementare il numero di checker persenti
             */
            board[column].incrChecerks();
        }
        return true;
    }
    /**
     * Controlla se il player ha la possibilita di posizionare checkers nella
     * home
     *
     * @param player identificativo del player 
     * @return True se il player é autorizzato, False altrimenti
     * 
     */
    public boolean canMoveHome(int player) {
        boolean ret = true;

        if (player == BLACK) {
            ret = board[BLACKBAR].isEmpty();
            for (int i = 1; i < 19 && ret == true; i++) {
                if (board[i].getPlayer() == player)
                    return false; 
            }
        } else {
            //i'm white player
            ret = board[WHITEBAR].isEmpty();
            for (int i = 7; i < 25 && ret == true; i++) {
                if (board[i].getPlayer() == player)
                    return false;
            }
        }
        return ret;

    }

    /*=========================== Print Board ===============================*/
    
    private final static String TOP_BORDER = "---------------------------------BLACK----------------------------------------\n||=13===14===15===16===17===18==|=BAR=|==19==20===21===22===23===24==|=HOME=||\n||==v====v====v====v====v====v==|==25=|==v====v====v====v====v====v==|==27==||";
    private final static String BOT_BORDER = "||==A====A====A====A====A====A==|==0==|==A====A====A====A====A====A==|==26==||\n||=12===11===10====9====8====7==|=BAR=|==6====5====4====3====2====1==|=HOME=||\n---------------------------------WHITE----------------------------------------";
    private final static String WHITE_CHECKER = "  O  ";
    private final static String BLACK_CHECKER = "  X  ";
    private final static String EMPTY_SLOT = "     ";
    private final static String SIDE_BORDER_RIGHT = "|      ||";
    private final static String SIDE_BORDER_LEFT = "||";
    private final static String EMPTYBAR = "|     |";
    private final static String P_WHITEBAR = "|  O  |";
    private final static String P_BLACKBAR = "|  X  |";
    private final static String EMPTYHOME = "|      ||";
    private final static String STARTHOME = "|  ";
    private final static String ENDHOME = "  ||";
    private final static int BOARDSIZE = 23;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TOP_BORDER + "\n");
        int pointIndex;
        int maxCheckers = 6;
        /*
         * Determino il massimo numero di checkers per position
         * per default = 6
         */
        for (int i = 0; i < NUM_POSITION; i++) {
            if (board[i].getNumofcheckers() > maxCheckers) {
                maxCheckers = board[i].getNumofcheckers();
            }
        }
        int barblack = board[BLACKBAR].getNumofcheckers();
        // Top Half of board
        for (int row = 0; row <= maxCheckers; row++) {
            stringBuilder.append(SIDE_BORDER_LEFT);
            for (pointIndex = 13; pointIndex <= 24; pointIndex++) {
                if (pointIndex == 19) {
                    if (barblack-- > 0) {
                        stringBuilder.append(P_BLACKBAR);
                    } else {
                        stringBuilder.append(EMPTYBAR);
                    }
                }
                if (board[pointIndex].getNumofcheckers() > row) {
                    if (board[pointIndex].getPlayer() == WHITE) {
                        stringBuilder.append(WHITE_CHECKER);
                    } else if (board[pointIndex].getPlayer() == BLACK) {
                        stringBuilder.append(BLACK_CHECKER);
                    } else if (board[pointIndex].getPlayer() == EMPTY) {
                        stringBuilder.append(EMPTY_SLOT);
                    }
                } else {
                    stringBuilder.append(EMPTY_SLOT);
                }
            }
            int blackhome = board[BLACKEXIT].getNumofcheckers();
            if (blackhome > 0 && row == 0) {
                stringBuilder.append(STARTHOME);
                if (blackhome > 9) {
                    stringBuilder.append(blackhome);
                } else {
                    stringBuilder.append(" " + blackhome);
                }
                stringBuilder.append(ENDHOME + "\n");
            } else {
                stringBuilder.append(SIDE_BORDER_RIGHT + "\n");
            }

        }
        int whitebar = board[WHITEBAR].getNumofcheckers();
        // Bottom Half of board
        for (int row = maxCheckers; row >= 0; row--) {
            stringBuilder.append(SIDE_BORDER_LEFT);
            for (pointIndex = 12; pointIndex > 0; pointIndex--) {
                if (pointIndex == 6) {
                    if (whitebar > 0 && row < whitebar) {
                        stringBuilder.append(P_WHITEBAR);
                        whitebar--;
                    } else {
                        stringBuilder.append(EMPTYBAR);
                    }
                }

                if (board[pointIndex].getNumofcheckers() > row) {
                    if (board[pointIndex].getPlayer() == WHITE) {
                        stringBuilder.append(WHITE_CHECKER);
                    } else if (board[pointIndex].getPlayer() == BLACK) {
                        stringBuilder.append(BLACK_CHECKER);
                    } else if (board[pointIndex].getPlayer() == EMPTY) {
                        stringBuilder.append(EMPTY_SLOT);
                    }
                } else {
                    stringBuilder.append(EMPTY_SLOT);
                }
            }
            int whitehome = board[WHITEEXIT].getNumofcheckers();
            if (whitehome > 0 && row == 0) {
                stringBuilder.append(STARTHOME);
                if (whitehome > 9) {
                    stringBuilder.append(whitehome);
                } else {
                    stringBuilder.append(" " + whitehome);
                }
                stringBuilder.append(ENDHOME + "\n");
            } else {
                stringBuilder.append(SIDE_BORDER_RIGHT + "\n");
            }

        }
        stringBuilder.append(BOT_BORDER + "\n");
        stringBuilder.append("\nBar White = 0 ; Bar Black = 25 ; Home White = 26 ; Home Black = 27" + "\n");
        return stringBuilder.toString();
    }
}
