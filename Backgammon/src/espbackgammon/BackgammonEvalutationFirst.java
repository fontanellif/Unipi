package espbackgammon;

/**
 *Classe di valutazione dello stato del gioco.
 * Prima funzione di valutazione realizzata. Non molto dettagliata, ma buona dal punto di vista della valutazione.
 * 
 * @author filippofontanelli
 */
public class BackgammonEvalutationFirst extends Evalutation{

    
    
    @Override
    public float boardscore(BackgammonBoard bb, int player) {
        
        float bsum = 0;
        float wsum = 0;
        /*prima valutazione su quante posizioni non chiuse ci sono */
        for (int i = 1; i <= Board.NUM_BOARD; i++) {

            /*
             * Valutazione ponderata della posizione, se ho piu di una pedina, ovvero se la posizione é chiusa, allora incremento di 4 altrimenti se la pozione 
             * non é chiusa, allora decremento di 3
             */
            if (bb.getPlayboard().getBoard()[i].getNumofcheckers() > 1 && bb.getPlayboard().getBoard()[i].getPlayer() == Board.BLACK) {
                bsum += 4;
            } else if (bb.getPlayboard().getBoard()[i].getNumofcheckers() == 1 && bb.getPlayboard().getBoard()[i].getPlayer() == Board.BLACK) {
                bsum -= 3;
            }
            /*
             * Faccio lo stesso per il white
             */
            if (bb.getPlayboard().getBoard()[i].getNumofcheckers() > 1 && bb.getPlayboard().getBoard()[i].getPlayer() == Board.WHITE) {
                wsum += 4;
            } else if (bb.getPlayboard().getBoard()[i].getNumofcheckers() == 1 && bb.getPlayboard().getBoard()[i].getPlayer() == Board.WHITE) {
                wsum -= 3;
            }
        }
        /*
         * Controllo quanti elementi hanno nella Home e nel Bar, incrementando il valore per le pedine nella Home,
         * e decrementandolo per quelle del Bar
         */
         
        int bwhite = bb.getPlayboard().getOnBar(Board.WHITE).getNumofcheckers();
        if (bwhite > 0) {
            wsum -= bwhite * 4;
            bsum += bwhite * 4;
        }

        int ewhite = bb.getPlayboard().getInHome(Board.WHITE).getNumofcheckers();
        if (ewhite > 0) {
            wsum += ewhite * 5;
        }
        int bblack = bb.getPlayboard().getOnBar(Board.BLACK).getNumofcheckers();
        if (bblack > 0) {
            bsum -= bblack * 4;
            wsum += bblack * 4;
        }

        int eblack = bb.getPlayboard().getInHome(Board.BLACK).getNumofcheckers();
        if (eblack > 0) {
            bsum += eblack * 5;
        }

        bb.setValue((float) (bsum - wsum));
        
        
        return bsum - wsum;
    }
    
}
