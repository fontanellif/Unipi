package espbackgammon;

/**
 * Classe di valutazione dello stato del gioco.
 * Funzione di valutazione realizzata sulla base di alcuni principi di valutazione
 * noti nel gioco del backgammon.
 * 
 * @author filippofontanelli
 */
public class BackgammonEvalutationSecond extends Evalutation {

    private static int BLOCKBONUS = 3;
    private static float BLOTBONUS = (float) .25;
    private static int ANCHORBONUS = 5;
    private static int DISTRIBUTIONBONUS = 2;
    private static float GETGOINGBONUS = (float) 0;

    /**
     * Costruttore
     */
    public BackgammonEvalutationSecond() {
        MAXSCORE = 15;
        MINSCORE = 0;
    }

    @Override
    public float boardscore(BackgammonBoard bb, int player) {
        float sum = (float) 0.0;
        int anchorsinhome = 0;
        int anchorsinopponenthome = 0;
        int primebonus = 0;
        int opponentprimebonus = 0;
        int proximitybonus = 0;
        int opponentproximitybonus = 0;
        boolean endgame = false;
        boolean seenself = false;
        int opponentcount = 0;
        int owncount = 0;
        int blockinrow = 0;
        int anchorcount = 0;
        int opponentanchorcount = 0;
        int opponentblots = 0;
        int exiting = 0;
        int anchorblot = 0;
        int opanchorblot = 0;
        boolean countopblots = false;
        
       Position[] board = bb.getPlayboard().getBoard();
        /*
         * Black case
         */
        if (player == Board.BLACK) {
            for (int i = 1; i <= Board.NUM_BOARD; i++) {
               
                if (board[i].getPlayer() == Board.BLACK) {
                    seenself = true;
                    if (owncount == 0) {
                        countopblots = true;
                    }
                    owncount += board[i].getNumofcheckers();
                    if (blockinrow < 0) {
                        blockinrow = 0;
                    }
                    /*
                     * Differenzio la valutazione in basa alla porsione di tavola in cui mi trovo
                     */
                    if (i < 13) {
                        sum -= GETGOINGBONUS * board[i].getNumofcheckers() * (12 - i);
                    } else {
                        sum += GETGOINGBONUS * board[i].getNumofcheckers() * (i - 12);
                    }
                    /*
                     * Fino a che il gioco non é "finito"
                     */
                    if (!endgame) {
                        if (board[i].getNumofcheckers() > 1) {
                            blockinrow++;
                            sum += blockinrow * BLOCKBONUS;
                            /* Nel caso in cui sia nella mia board interna*/
                            if (i > 18) {
                                anchorcount++;
                            }
                        } else {
                            /*se non ho la posizione chiusa oppure se la posizione é vuota*/
                            blockinrow = 0;
                            /*Calcolo un incremento relativo alla posizione*/
                            sum += (opponentcount - 15 - i) * BLOTBONUS;
                            if (i > 18) {
                                anchorblot++;
                            }
                        }
                    }
                    /*Nel caso in cui una posizione appartenga al mio avversario ( in questo caso White)*/
                } else if (board[i].getPlayer() == Board.WHITE) {
                    /*Incremento il contatore dei blocchi avversari*/
                    opponentcount += board[i].getNumofcheckers();
                    if (blockinrow > 0) {
                        blockinrow = 0;
                    }
                    if (!endgame) {
                        if (board[i].getNumofcheckers() > 1) {
                            blockinrow--;
                            sum += blockinrow * BLOCKBONUS;
                            /*Nel caso in cui sia nella tavola interna*/
                            if (i < 7) {
                                opponentanchorcount++;
                            }
                        } else {
                            if (countopblots) {
                                opponentblots++;
                            }
                            blockinrow = 0;
                            if (owncount + board[Board.BLACKBAR].getNumofcheckers() > 0) {
                                sum += ((owncount + (23 - i)) * BLOTBONUS);
                            }
                        }
                    }
                } else {
                    blockinrow = 0;
                }
            }
            /*
             * Calcolo bonus finale ottenuto dalla differenza delle posizioni ancorare, le pedine del bar e il numero di posizioni
             * bloccate dell'avversario
             */
            float asdf = ((anchorcount - anchorblot) * (board[Board.WHITEBAR].getNumofcheckers() + opponentblots)) * ANCHORBONUS;
            sum += asdf;
        } else if (player == Board.WHITE) {
            countopblots = true;
            for (int i = 1; i <= Board.NUM_BOARD; i++) {
                if (board[i].getPlayer() == Board.WHITE) {
                    seenself = true;
                    if (owncount == 15) {
                        countopblots = false;
                    }
                    owncount += board[i].getNumofcheckers();
                    if (blockinrow < 0) {
                        blockinrow = 0;
                    }
                    if (i > 12) {
                        sum -= GETGOINGBONUS * board[i].getNumofcheckers() * (i - 12);
                    } else {
                        sum += GETGOINGBONUS * board[i].getNumofcheckers() * (12 - i);
                    }
                    if (!endgame) {
                        if (board[i].getNumofcheckers() > 1) {
                            blockinrow++;
                            sum += blockinrow * BLOCKBONUS;
                            if (i < 7) {
                                anchorcount++;
                            }
                        } else {
                            blockinrow = 0;
                            sum += (opponentcount - (24 - i)) * BLOTBONUS;
                            if (i < 7) {
                                anchorblot++;
                            }
                        }
                    }
                } else if (board[i].getPlayer() == Board.BLACK) {
                    opponentcount += board[i].getNumofcheckers();
                    if (blockinrow > 0) {
                        blockinrow = 0;
                    }
                    if (!endgame) {
                        if (board[i].getNumofcheckers() > 1) {
                            blockinrow--;
                            sum += blockinrow * BLOCKBONUS;
                            if (i > 18) {
                                opponentanchorcount++;
                            }
                        } else {
                            if (countopblots) {
                                opponentblots++;
                            }
                            blockinrow = 0;
                            if (owncount < 15) {
                                sum += ((15 - owncount + i) * BLOTBONUS);
                            }
                        }
                    }
                } else {
                    blockinrow = 0;
                }
            }
            float asdf = ((anchorcount - anchorblot) * (board[Board.BLACK].getNumofcheckers() + opponentblots)) * ANCHORBONUS;
            sum += asdf;
        }
        return (float) sum;
    }
}
