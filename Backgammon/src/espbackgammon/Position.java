package espbackgammon;

import static espbackgammon.BackgammonPlayer.*;

/**
 * Position é la classe che identifica una generica posizione in cui possono esser posizononate le
 * pedine. Essa viene utilizzata per le punte della board, per il bar e per la home.
 * 
 * @see Board
 *
 * @author Filippo Fontanelli
 */
public class Position {
    /**
     * Identificativo del player a cui é associata la posizione
     * @see Position()
     */
    private int player;
    /**
     * Numero di pedine all'interno della posizione
     * @see Position()
     */
    private int numofcheckers;

    /**
     * Costruttore della classe Positon.
     * Inizializza la posizione associandola al player <code> EMPTY </code> e con un numero di pedine pari a zero.
     */
    public Position() {
        this.player = Board.EMPTY;
        this.numofcheckers = 0;
    }

    /**
     * Costruttore della classe Positon.
     * Inizializza la posizione associandola al player <code>player</code> e con un numero di pedine pari<code> numofcheckers </code>.
     * 
     * @param player Identificativo del player da associare alla posizione.
     * @param numofcheckers Numero di pedine da associare alla posizione.
     * 
     * @throws IllegalArgumentException Nel caso si stia tentando di creare una Position non associata a nessun player ma con un numero di pedine > 0.
     * 
     */
    public Position(int player, int numofcheckers) throws IllegalArgumentException{
        if ((player == Board.EMPTY)&&(numofcheckers > 0))
            throw new IllegalArgumentException("Non é possibile creare una position con player empty e numofchechers >0!!" + player);
        this.player = player;
        this.numofcheckers = numofcheckers;
    }

    /**
     * Controlla se la posizione é vuota
     * @return true se il numero di pedine = 0,
     *          false altrimenti
     */
    public boolean isEmpty(){
        return (numofcheckers == 0);
    }
    /**
     * Resetta la posizione, associandola al player <code> EMPTY </code> e azzera il numero delle pedine.
     * 
     */
    public void setEmpty(){
        this.player = Board.EMPTY;
        this.numofcheckers = 0;
    }
    /**
     * Incrementa di uno il numero di pedine della posizione.
     * 
     * @throws IllegalArgumentException se tentiamo di incrementare il numero di pedine di una posizione associata al player <code> EMPTY </code>.
     */
    public void incrChecerks() throws IllegalArgumentException{
        if (player == Board.EMPTY)
            throw new IllegalArgumentException("Non é possibile incrementare il numero di checkers ad una positione empty!!" + player);
        this.numofcheckers++;
    }
     /**
     * Decrementa di uno il numero di pedine della posizione.
     * Nel caso in cui il numero di pedine dopo il decremento sia pari a zero, la posizone diventera empty.
     * 
     * @throws IllegalArgumentException se tentiamo di decrementare il numero di pedine di una posizione associata al player <code> EMPTY </code>.
     */
    public void decrChecerks() throws IllegalArgumentException{
        if (player == Board.EMPTY)
            throw new IllegalArgumentException("Non é possibile decrementare il numero dicheckers ad una positione empty!!" + player);
        this.numofcheckers--;
        /*
         * Nel caso in cui abbia tolto tutti i chechers dalla Position, allora automaticamente
         * essa diventa empty
         */
        if (this.numofcheckers == 0)
            this.setEmpty();
    }
     /**
     * Permette la modifica contemporanea del palyer e del numero di pedine.
     * 
     * @param player Identificativo del player da associare alla posizione.
     * @param numofcheckers Numero di pedine da associare alla posizione.
     * 
     * @throws IllegalArgumentException Nel caso si stia tentando di creare una Position non associata a nessun player ma con un numero di pedine > 0.
     */
    public void setPosition(int player, int numofcheckers) throws IllegalArgumentException{
        if ((player == Board.EMPTY)&&(numofcheckers > 0))
            throw new IllegalArgumentException("Non é possibile creare una position con player empty e numofchechers >0!!" + player);
        this.player = player;
        this.numofcheckers = numofcheckers;
    }
    /**
     * Restituise l'identificativo del player.
     * @return l'identificativo del player
     */
    public int getPlayer() {
        return player;
    }
    /**
     * Modifica l'identificativo del player.
     * @param player Identificativo del player da associare alla posizione.
     * @throws IllegalArgumentException se <code> player </code> é diverso da WHITE,BLACK,EMPTY.
     */
    public void setPlayer(int player) throws IllegalArgumentException{
        if (checkAllPlayer(player)) {
            throw new IllegalArgumentException("Il player puo assumere solo 3 valori: WHITE,BLACK,EMPTY!!" + player);
        }
        this.player = player;
    }
    /**
     * Restituise il numero di pedine.
     * @return Numero di pedine da associate alla posizione.
     */
    public int getNumofcheckers() {
        return numofcheckers;
    }

    /**
     * Modifia il numero di pedine.
     * @param numofcheckers Numero di pedine da associare alla posizione.
     * @throws IllegalArgumentException se <code> numofcheckers < 0 </code>
     */
    public void setNumofcheckers(int numofcheckers) throws IllegalArgumentException{
        if (numofcheckers < 0) {
            throw new IllegalArgumentException("I checkers non possono esser < 0!!" + numofcheckers);
        }
        this.numofcheckers = numofcheckers;
    }
    
    /**
     * Controlla che la posizione sia associata al player e che abbia un numero di pedine > 0.
     * @param player Identificativo del player
     * @return true se la posizione é associata al <code> player </code> e se il numero di pedine é >0, false altrimenti.
     */
    public boolean isplayerandnonEmpty(int player){
        return ((this.player == player) && (this.numofcheckers > 0));
    }

    @Override
    public String toString() {
        return "Position{" + "player=" + player + ", numofcheckers=" + numofcheckers + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Position)) {
            return false;
        }

        Position b = (Position) o;

        return ((b.getPlayer() == this.player) && (b.getNumofcheckers() == this.numofcheckers));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.player;
        hash = 67 * hash + this.numofcheckers;
        return hash;
    }
 
    
}
