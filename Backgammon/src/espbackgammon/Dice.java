package espbackgammon;

import java.util.*;

/**
 * La classe Dice rappresenta i due dadi del gioco del Backgammon.
 *
 *
 * @author filippofontanelli
 */
public class Dice {

    /**
     * Massimo valore di un dado {1,2,...,DIE_MAX}.
     */
    public final static int DIE_MAX = 6;
    /**
     * Oggetto della classe random. Utilizzato per la generazione casuale del
     * valore dei dadi.
     */
        private Random generatore = null;
    /**
     * Identificano il valore corrente del primo dado
     */
    private int dc1;
    /**
     * Identificano il valore corrente del secondo dado
     */
    private int dc2;
    /**
     * Costruttore della classe dice. Inizializza il generatore di numeri
     * casuali e successivamente effettua il lancio dei dadi.
     */
    public Dice() {
        generatore = new Random();
        toss();
    }
    /**
     * Lancia i dadi.
     *
     */
    public void toss() {
        if (generatore != null) {
            setDc1((generatore.nextInt(DIE_MAX) + 1));
            setDc2((generatore.nextInt(DIE_MAX) + 1));
        }
    }
    /**
     * Restituisce il valore attuale del secondo dado.
     *
     * @return valore del dado
     */
    public int getDc2() {
        return dc2;
    }
    /**
     * Modifica il valore del secondo dado.
     *
     * @param dc2 valore del dado.
     */
    private void setDc2(int dc2) {
        this.dc2 = dc2;
    }
    /**
     * Restituisce il valore attuale del primo dado.
     *
     * @return valore del dado
     */
    public int getDc1() {
        return dc1;
    }
    /**
     * Modifica il valore del primo dado.
     *
     * @param dc1 valore del dado.
     */
    private void setDc1(int dc1) {
        this.dc1 = dc1;
    }
    /**
     * Controlla che il valore del dado sia logicamente corretto.
     *
     * @param dice valore del dado da testare
     * @return true se <code> dice </code> risulta compreso tra 1 e 6, falso
     * altrimenti.
     */
    public static boolean checkDice(int dice) {
        return ((dice > 0) && (dice < 7));
    }
    /**
     * Calcola la probabilitá dei due dadi in ingresso. In base alla probabilitá
     * della somma dei due.
     *
     * @param d1 valore del primo dado
     * @param d2 valore del secondo dado
     * @return probabilitá
     */
    public static double dicePorobability(int d1, int d2) {

        switch (d1 + d2) {
            case 2:
            case 12:
                return 1.0 / 36.0;
            case 3:
            case 11:
                return 2.0 / 36.0;
            case 4:
            case 10:
                return 3.0 / 36.0;
            case 5:
            case 9:
                return 4.0 / 36.0;
            case 6:
            case 8:
                return 5.0 / 36.0;
            case 7:
                return 6.0 / 36.0;
        }
        return 0;
    }
}
