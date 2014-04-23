/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package espbackgammon;

/**
 * Classe di valutazione dello stato del gioco. Funzione di valutazione beta.
 *
 * @author filippofontanelli
 */
public class BackgammonEvalutation extends Evalutation {

    private static int BOUNSCHIUSURA = 5;
    private static int BOUNSTAVOLAINTERNA = 20;
    private static int BOUNSTAVOLAESTERNA = 15;
    private static int BOUNSCHECKERSINHOME = 20;
    private static int BOUNSTAVOLAINTERNAOCCUPATA = 6;
    private static int PENALITACHIUSURA = 20;
    private static int PENALITATAVOLAINTERNAAVVERSARIO = 15;
    private static int PENALITATAVOLAESTERNAAVVERSARIO = 10;
    private static int PENALITACHECKERSINBAR = 10;
    private static int PENALITATAVOLAINTERNAOCCUPATA = 6;

    @Override
    public float boardscore(BackgammonBoard bb, int player) {
        float bonus = 0;
        float penalita = 0;
        Position[] board = bb.getPlayboard().getBoard();
        if (player == Board.BLACK) {

            for (int i = 1; i < Board.NUM_BOARD; i++) {
                int checkers = board[i].getNumofcheckers();
                int player_pos = board[i].getPlayer();
                //Controllo la tavola interna dell'avversario
                if (i < 7 && player_pos == Board.BLACK) {
                    //caso in cui non sono chiuso, caso da penalizzare ma non molto
                    //per fare cio lo moltiplico per i che in questa parte della tavola e' piccolo
                    if (checkers == 1) {

                        penalita += i * PENALITACHIUSURA * PENALITATAVOLAINTERNAAVVERSARIO;


                    } else if (checkers > 1) {
                        penalita += i * PENALITATAVOLAINTERNAAVVERSARIO;
                    }

                }

                if (i < 13 && i >= 7 && player_pos == Board.BLACK) {
                    //caso in cui non sono chiuso, caso da penalizzare ma non molto
                    //per fare cio lo moltiplico per i che in questa parte della tavola e' piccolo
                    if (checkers == 1) {

                        penalita += i * PENALITACHIUSURA * PENALITATAVOLAESTERNAAVVERSARIO;


                    } else if (checkers > 1) {
                        penalita += i * PENALITATAVOLAESTERNAAVVERSARIO;
                    }

                }
                if (i < 19 && i >= 13 && player_pos == Board.BLACK) {
                    //caso in cui non sono chiuso, caso da penalizzare ma non molto
                    //per fare cio lo moltiplico per i che in questa parte della tavola e' piccolo
                    if (checkers == 1) {

                        bonus += i * PENALITACHIUSURA * BOUNSTAVOLAESTERNA;


                    } else if (checkers > 1) {
                        bonus += i * BOUNSCHIUSURA * BOUNSTAVOLAESTERNA;
                    }

                }
                if (i > 18 && player_pos == Board.BLACK) {
                    /*
                     * caso in cui non sono chiuso, caso da penalizzare ma non molto 
                     * per fare cio lo moltiplico per i che in questa parte della tavola e' piccolo
                     */
                    if (checkers == 1) {
                        bonus += i * PENALITACHIUSURA;
                    } else if (checkers > 1) {
                        bonus += i * BOUNSTAVOLAINTERNA * BOUNSCHIUSURA;
                    }

                }

                penalita += board[Board.BLACKBAR].getNumofcheckers() * PENALITACHECKERSINBAR;
                bonus += board[Board.BLACKEXIT].getNumofcheckers() * BOUNSCHECKERSINHOME;
                bonus += board[Board.WHITE].getNumofcheckers() * 3;


            }

        } else if (player == Board.WHITE) {

            for (int i = 1; i < Board.NUM_BOARD; i++) {
                int checkers = board[i].getNumofcheckers();
                int player_pos = board[i].getPlayer();
                //Controllo la tavola interna dell'avversario
                if (i > 18 && player_pos == Board.WHITE) {
                    //caso in cui non sono chiuso, caso da penalizzare ma non molto
                    //per fare cio lo moltiplico per i che in questa parte della tavola e' piccolo
                    if (checkers == 1) {

                        penalita += i * PENALITACHIUSURA * PENALITATAVOLAINTERNAAVVERSARIO;


                    } else if (checkers > 1) {
                        penalita += i * PENALITATAVOLAINTERNAAVVERSARIO;
                    }

                }

                if (i > 12 && i <= 18 && player_pos == Board.WHITE) {
                    //caso in cui non sono chiuso, caso da penalizzare ma non molto
                    //per fare cio lo moltiplico per i che in questa parte della tavola e' piccolo
                    if (checkers == 1) {

                        penalita += i * PENALITACHIUSURA * PENALITATAVOLAESTERNAAVVERSARIO;


                    } else if (checkers > 1) {
                        penalita += i * PENALITATAVOLAESTERNAAVVERSARIO;
                    }

                }
                if (i > 6 && i <= 12 && player_pos == Board.WHITE) {
                    //caso in cui non sono chiuso, caso da penalizzare ma non molto
                    //per fare cio lo moltiplico per i che in questa parte della tavola e' piccolo
                    if (checkers == 1) {

                        bonus += i * PENALITACHIUSURA * BOUNSTAVOLAESTERNA;


                    } else if (checkers > 1) {
                        bonus += i * BOUNSCHIUSURA * BOUNSTAVOLAESTERNA;
                    }

                }
                if (i < 7 && player_pos == Board.WHITE) {
                    //caso in cui non sono chiuso, caso da penalizzare ma non molto
                    //per fare cio lo moltiplico per i che in questa parte della tavola e' piccolo
                    if (checkers == 1) {

                        bonus += i * PENALITACHIUSURA;


                    } else if (checkers > 1) {
                        bonus += i * BOUNSTAVOLAINTERNA * BOUNSCHIUSURA;
                    }

                }

                penalita += board[Board.WHITEBAR].getNumofcheckers() * PENALITACHECKERSINBAR;
                bonus += board[Board.WHITEEXIT].getNumofcheckers() * BOUNSCHECKERSINHOME;
                bonus += board[Board.BLACK].getNumofcheckers() * 3;


            }

        }

        return bonus - penalita;




    }
}
