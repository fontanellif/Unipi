/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package espbackgammon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import static espbackgammon.BackgammonPlayer.*;

/**
 *
 * @author filippofontanelli
 */
public class HumanPlayer extends BackgammonPlayer {

    public HumanPlayer(int player_num) {
        super(player_num);
    }

    @Override
    public BackgammonBoard playturn(BackgammonBoard bb) {
        InputStreamReader read = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(read);
        boolean fine = false;
        int count = 0;
        int from = 0;
        int to = 0;

        do {
            try {
                System.out.println("Enter where you want to move from or -10 se vuoi passare il turno :");

                from = Integer.parseInt(in.readLine());

                /*
                 * Se il giocatore ha deciso di saltare il turno, evito di chiedere il valore del to
                 */
                if (from != -10){
                System.out.println("Enter where you want to move to :");
                to = Integer.parseInt(in.readLine());
                }
            } catch (IOException ex) {
                System.out.println("Input Error..");
            }

            if (from != -10) {
                
                int dice = checkDice(from, to, bb);
                
                Move m = new Move(this.getPlayer_num(), from, to, dice);
                    
                if (((dice == bb.getDice1()) || (dice == bb.getDice2())) && Move.moveIsLegal(m, bb)) {
                    count++;
                    bb.playMove(m);
                }else
                    System.out.println("Illegal Move!!!" + m.toString()+bb.getDice1() + bb.getDice2());
                

                if (bb.getDice1() == bb.getDice2()) {
                    if (count == 4) {
                        fine = true;
                    }
                } else if (count == 2) {
                    fine = true;
                }

                System.out.println(bb.toString());
            } else {
                fine = true;
            }
       
        } while (!fine);

        bb.setHasToMove(otherPlayer(this.getPlayer_num()));

        return bb;

    }
    
    /**Controlla che il giocatore abbia selezionato una mossa relativa al valore di un dado.
     * Verifica che lo spostamento comandato dalla mossa, sia legale, ovvero corrisponda al valore
     * di uno dei due dadi tirati.
     * Andando a modificare il valore in base al player e al tipo di mossa.
     * 
     * @param from 
     * @param to
     * @param bb
     * @return dice, con valore adeguato rispetto al player
     */
    private int checkDice(int from, int to,BackgammonBoard bb){
        int dice = 0;
        if (from == playertobar(this.getPlayer_num()))
                    if (bb.getHasToMove() == Board.WHITE)
                        dice = Math.abs(to - 25);
                     else
                        dice = Math.abs(to - 0);
                else{
                    if (to == playertohome(this.getPlayer_num())){
                        if (bb.getDice1() == from || bb.getDice2() == from)
                            dice = from;
                        else if(from > bb.getDice1())
                            dice = bb.getDice2();
                        else
                            dice = bb.getDice1();
                                  
                    }else
                        dice = Math.abs(to - from);
                }
        return dice;
    }
}
