package espbackgammon;

/**Classe astratta che definisce una generica funzione di valutazione dello stato del gioco.
 *
 * @author filippofontanelli
 */
public abstract class Evalutation {
    /**
     * Massimo valore ritornato dalla funzione
     */
    protected float MAXSCORE;
    /**
     * Minimo valore ritornato dalla funzione
     */
    protected float MINSCORE;

    /**
     * Restituisce il massimo valore ritornato dalla funzione
     * @return MAXSCORE
     */
    public float getMAXSCORE() {
        return MAXSCORE;
    }
    /**
     * Restituisce il minimi valore ritornato dalla funzione
     * @return MINSCORE
     */
    public float getMINSCORE() {
        return MINSCORE;
    }

    /**
     * Metodo astratto che definisce la funzione di valutazione.
     * @param bb stato del gioco
     * @param player identificativo del player
     * @return valutazione dello stato del gioco
     */
    public abstract float boardscore(BackgammonBoard bb, int player);
    
    
}
