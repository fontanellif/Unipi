package espbackgammon;

import java.util.ArrayList;
import static espbackgammon.BackgammonPlayer.*;

/**
 * La classe Move rappresenta una mossa di un determinato player.
 * Una mossa é identificata da un player,dalla position di partenza,dalla positione
 * di destinazione e dal dado a cui é associata.
 *
 * @author filippofontanelli
 */
public class Move {

    /**
     * Identificativo del player a cui la mossa é associta.
     * @see Move(int player, int from, int to,int dice)
     */
    private int player;
    /**
     * Il dado relativo alla mossa.
     * @see Move(int player, int from, int to,int dice)
     */
    private int dice;
    /**
     * Indice da verrá prelevata al pedina.
     * @see Move(int player, int from, int to,int dice)
     */
    private int from;
    /**
     * Indice in cui verrá posizionata la pedina.
     * @see Move(int player, int from, int to,int dice)
     */
    private int to;

    public Move() {
        this.player = 0;
        this.from = -1;
        this.to = -1;
        this.dice = 0;
    }
    
    
    /**
     * Costruttore della classe Move.
     * 
     * @param player Identificativo del player
     * @param from Indice della board da cui prelevare la pedina
     * @param to Indice della board in cui posizinare la pedina
     * @param dice Dado a cui la mossa é associata
     * 
     * @throws IllegalArgumentException Se i parametri non sono corretti.
     */
    public Move(int player, int from, int to,int dice) {
        if (!checkMove(player, from, to)) {
            throw new IllegalArgumentException("Impossibile creare una move!!");
        }
        this.player = player;
        this.from = from;
        this.to = to;
        this.dice = dice;
    }

    /**
     * Restituisce il dado a cui la mossa é associata.
     * @return il valore del dado.
     */
    public int getDice() {
        return dice;
    }

    /**
     * Modifica il dado a cui la mossa é associata.
     * La modifica é accettata solamente se il parametro <code> dice </dice> é logicamente corretto.
     * @param dice Valore del dado.
     */
    public void setDice(int dice) {
        if (Dice.checkDice(dice))
            this.dice = dice;
    }

    /**
     * Restituisce l'identificativo del player
     * @return l'identificativo del player 
     */
    public int getPlayer() {
        return player;
    }

    /**
     * Modifica il player
     * @param player identificativo del player da associare alla mossa
     * @throws IllegalArgumentException Se <code> player </code> risulta errato.
     */
    public void setPlayer(int player) {
        if (checkAllPlayer(player)) 
            throw new IllegalArgumentException("Player inaspettato!!" + player);
        
        this.player = player;
    }

    /**
     * Restituisce la posizione da cui prelevare la pedina
     * @return form indice dalla board
     */
    public int getFrom() {
        return from;
    }
    /**
     * Modifica la posizione da cui prelevare la pedina.
     * @param from indice della board
     * @throws IllegalArgumentException se from é minore di zero.
     */
    public void setFrom(int from) {
        if (from < 0) 
            throw new IllegalArgumentException("from inaspettato!!" + from);
        
        this.from = from;
    }
     /**
     * Restituisce la posizione in cui posizionare la pedina
     * @return to indice dalla board
     */
    public int getTo() {
        return to;
    }
    /**
     * Modifica la posizione in cui posizionare la pedina.
     * @param to indice della board
     * @throws IllegalArgumentException se to é minore di zero.
     */
    public void setTo(int to) {
        if (to < 0) 
            throw new IllegalArgumentException("to inaspettato!!" + to);
        this.to = to;
    }

    // da controllare
    private boolean checkMove(int player, int from, int to) {
        return true;/*((!Board.checkPlayer(player)) || (from < 0) || (to < 0));*/
    }

    
    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }
        if (!(o instanceof Move)) {
            return false;
        }

        Move m = (Move) o;

        return ((this.dice == m.dice) && (this.from == m.from) && (this.player == m.player) && (this.to == m.to));
    
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + this.player;
        hash = 47 * hash + this.dice;
        hash = 47 * hash + this.from;
        hash = 47 * hash + this.to;
        return hash;
    }


    @Override
    public String toString() {
        return "Move{" + "player=" + player + ", dice=" + dice + ", from=" + from + ", to=" + to + '}';
    }

    
     /* ======== Check Move ========= */
    /**
     * Metodo statico che controlla che la mossa sia legale rispetto alla boardstate
     * assumendo che il controllo dei dadi sia gia stato effettuato.
     * 
     * @param m la mossa da verificare
     * @param bb lo stato attuale del gioco
     * @return true se la mossa é legale, false atrimenti
     */
    public static boolean moveIsLegal(Move m, BackgammonBoard bb) {
        /*
         * I controlli possono esser suddivisi in 2 parti
         * 1) controllo che la rimozione del checker nella posizione from sia legale
         * 2) controllo che l'inserimento del checker nella posizione to si legale
         */
        boolean ret = true;
        
        ret = ret & checkPickCheckers(bb.getPlayboard(),bb.getHasToMove(),m.getFrom());
        ret = ret & checkPutCheckers(bb.getPlayboard(),bb.getHasToMove(),m.getTo());

        return ret;
    }
    
    private static boolean checkPickCheckers(Board playboard,int hasToMove, int from) {
        
        if (checkAllPlayer(hasToMove))
            return false;

        /*
         * Controllo che l'indice sia corretto, in particolare in questo caso, si assume che non sia possibile prelevare
         * checkers dalla home, ma solo dalla board e dal bar
         */
        if ((from < 0) || (from > Board.BLACKBAR)) return false;
        /*
         * previene l'accesso ad una colonna del player sbagliato, cio vale chiaramente
         * anche per il bar
         * 
         * in particolare per come é stata implementata la position, non é possibile che prelevi da una position
         * con 0 checkers, perche in quel caso, essa sarebbe asseganta automaticamente al player empty
         */
        if (playboard.getBoard()[from].getPlayer() != hasToMove)
            return false;

        if (playboard.getOnBar(hasToMove).getNumofcheckers() > 0 && from != playertobar(hasToMove))
            return false;
        
        
       
        return true;

    }

    private static boolean checkPutCheckers(Board playboard,int hasToMove, int to) {
       
        if (checkAllPlayer(hasToMove))
            return false;

        

        /*
         * Controllo che l'indice sia corretto
         */
        if ((to < 0) || (to > Board.NUM_POSITION - 1)) return false;
        
        
            
        
        /*
         * previene l'inseriment del checker in una colonna del player avversario, cio vale chiaramente
         * anche per il bar e per la home
         */
        if ((playboard.getBoard()[to].getPlayer() == otherPlayer(hasToMove)) && (playboard.getBoard()[to].getNumofcheckers() > 1))return false;

       if((to == Board.BLACKBAR) || (to == Board.WHITEBAR))
           return false;
        
       if ((to == Board.BLACKEXIT) || (to == Board.WHITEEXIT)) {
            
          
           /*
             * Nel caso in cui stia tentando di spostare nella home, devo
             * verificare che cio sia possibile ed in seguito
             * effettuare lo spostamento/incremento
             */
            if (!playboard.canMoveHome(hasToMove)) return false;
             
        } 
       
       return true;
    }
    
    
    public static ArrayList<Move> remduplex(ArrayList<Move> moves){
        
        ArrayList<Move> tmp = new ArrayList<Move>();
        
        for (int i = 0; i < moves.size(); i++) {
            boolean gg = true;
            int count = 0;
            for (int j = 0; j < moves.size() && gg == false; j++) {
                
                if (moves.get(i).equals(moves.get(j))){
                    count++;
                    if (count > 0){
                        gg = false;
                        System.out.println("Duplex!!!!" + moves.get(j).toString());
                    }
                }
                
            }
            
            if (gg)
                tmp.add(moves.get(i));
        }
        
        return tmp;
        
    } 
    
}
