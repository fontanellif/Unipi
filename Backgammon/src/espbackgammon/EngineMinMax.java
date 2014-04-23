/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package espbackgammon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static espbackgammon.BackgammonPlayer.*;


/**Realizzazione dell'algoritmo ExpectedMinMax.
 * E di altre utility per la sua realizzazione.
 *
 * @author filippofontanelli
 */
public class EngineMinMax {

    private static int[][] dierolls = {
        {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6},
        {2, 3}, {2, 4}, {2, 5}, {2, 6}, {3, 4}, {3, 5}, {3, 6}, {4, 5}, {4, 6}, {5, 6}
    };
    /**
     * Stato in cui é di turno il player attuale.
     * Massimizzo il risultato
     */
    public static final int MAX = 1;
    /**
     * Stato Stocastico.
     */
    public static final int CHANCEAFTERMAX = 2;
    /**
     * Stato in cui é di turno il player avversario.
     * Minimizzo il risultato
     */
    public static final int MIN = 3;
     /**
     * Stato Stocastico.
     */
    public static final int CHANCEAFTERMIN = 0;
    /**
     * Identificativo del player avversario.
     */
    private static int otherplayer;
    

    /**Metodo principale dell'algoritmo ExpectedMinMax
     * 
     * @param bb stato del gioco
     * @param depth profonditá
     * @return nuovo stato del gioco, dopo aver eseguito la mossa migliore
     */
    public static BackgammonBoard MinMax(BackgammonBoard bb, int depth, Evalutation eval) {

        
        int bestboard = 0;
        double maxsofar = 0.0;
        double tempmin = 0.0;
        boolean calculated = false;

        
        otherplayer = otherPlayer(bb.getHasToMove());
        /*Mi creo una copia della boardstate attuale su cui lavorare*/
        BackgammonBoard temp = new BackgammonBoard(bb);
        ArrayList<BackgammonBoard> children = null;
        
        children = EngineMinMax.getChildren(bb);
        if (children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {

                tempmin = ExpectedMinMax(children.get(i), depth-1, CHANCEAFTERMAX, 1,eval);
                children.get(i).setValue((float) tempmin);
                
                System.out.print("*");
                /*
                 * Calcolo il massimo valore ritornato da ExpectedMinMax
                 */
                if (tempmin > maxsofar || calculated == false) {
                    bestboard = i;
                    maxsofar = tempmin;
                    calculated = true;
                }
            }
            
            return children.get(bestboard);
        } else {
            return null;
        }
    }

    /**Algoritmo ExpectedMinMax
     * 
     * @param bb stato del gioco
     * @param depth profonditá
     * @param state player relativo allo stato del gioco
     * @param level livello di profonditá
     * @return Exptected Value dello stato
     */
    private static double ExpectedMinMax(BackgammonBoard bb, int depth, int state, int level, Evalutation eval) {

        int nextstate = (state + 1) % 4;
        double value = 0.0;

        if ((bb.isTerminal()) || (depth == 0)) {
            return eval.boardscore(bb, bb.getHasToMove());
        }

        switch (state) {
            case MIN: {
                value = Integer.MAX_VALUE;
               
                for (BackgammonBoard child : EngineMinMax.getChildren(bb)) {
                        value = Math.min(value, ExpectedMinMax(child, depth - 1, nextstate, level + 1,eval));
                    }
            }
            break;
            case MAX: {
                value = Integer.MIN_VALUE;

                for (BackgammonBoard child : EngineMinMax.getChildren(bb)) {
                        value = Math.max(value, ExpectedMinMax(child, depth - 1, nextstate, level + 1,eval));
                    }
            }
            break;
            case CHANCEAFTERMAX:
            case CHANCEAFTERMIN: {

                value = 0;
                int dc1 = 0;
                int dc2 = 0;
                double prob = 0;

                for (int i = 0; i < 21; i++) {
                    dc1 = dierolls[i][0];
                    dc2 = dierolls[i][1];
                    prob = Dice.dicePorobability(dc1,dc2);
                    bb.setDice1(dc1);
                    bb.setDice2(dc2);
                   
                    for (BackgammonBoard child : EngineMinMax.getChildren(bb)) {

                            value += prob * ExpectedMinMax(child, depth - 1, nextstate, level + 1,eval);
                        }
                }
                value /= 21.0;
            }
            break;
            default:
                break;
        }

        return value;

    }

    /* ======== Children Generator ========= */
    
    /**
     * Generatore di figli.
     * Dato uno stato attuale, calcola e ritorna tutti i possibili stati finali ottenuti
     * combinando le possibili mosse dettate dai dadi.
     * 
     * @param bb stato del gioco
     * @return Tutti i possibili stati di gioco figli
     */
    public static ArrayList<BackgammonBoard> getChildren(BackgammonBoard bb) {

        ArrayList<BackgammonBoard> children = new ArrayList<>();

        /*Appoggio*/
        Set<BackgammonBoard> childSet = new HashSet<>();


        if (bb.getDice1() != bb.getDice2()) {

            /*
             * Caso in cui non ho fatto doppio
             */
            for (Move move : bb.getLegalMove(true, true)) {
                BackgammonBoard child = new BackgammonBoard(bb);

                if (!child.playMove(move)) {
                    throw new IllegalArgumentException("GetChildren - error playmove!! - secondo dado" + move.toString() + "\n" + child.toString());
                }

                if (child.isTerminal()) {
                    childSet.add(child);
                } else {

                    if (move.getDice() == bb.getDice1()) {

                        for (Move move2 : child.getLegalMove(false, true)) {
                            BackgammonBoard childdice = new BackgammonBoard(child);
                            if (!childdice.playMove(move2)) {
                                throw new IllegalArgumentException("GetChildren - error playmove!! - secondo dado" + move2.toString() + "\n" + childdice.toString());
                            }
                            childSet.add(childdice);
                        }

                    } else if (move.getDice() == bb.getDice2()) {

                        for (Move move3 : child.getLegalMove(true, false)) {
                            BackgammonBoard childdice = new BackgammonBoard(child);
                            if (!childdice.playMove(move3)) {
                                throw new IllegalArgumentException("GetChildren - error playmove!! - secondo dado" + move3.toString() + "\n" + childdice.toString());
                            }
                            childSet.add(childdice);
                        }
                    }
                }
            }
        } else {
            /*
             * Caso in cui i dadi sono uguali (doppio)
             */
            for (Move move : bb.getLegalMove(true, false)) {
                BackgammonBoard child = new BackgammonBoard(bb);

                if (!child.playMove(move)) {
                    throw new IllegalArgumentException("GetChildren - error playmove!!");
                }
                if (child.isTerminal()) {
                    childSet.add(child);
                } else {

                    for (Move move2 : child.getLegalMove(true, false)) {
                        BackgammonBoard childdice = new BackgammonBoard(child);
                        if (!childdice.playMove(move2)) {
                            throw new IllegalArgumentException("GetChildren - error playmove!! - secondo dado");
                        }
                        if (childdice.isTerminal()) {
                            childSet.add(childdice);
                        } else {

                            for (Move move3 : childdice.getLegalMove(true, false)) {
                                BackgammonBoard childdice2 = new BackgammonBoard(childdice);
                                if (!childdice2.playMove(move3)) {
                                    throw new IllegalArgumentException("GetChildren - error playmove!! - terzo dado");
                                }
                                if (childdice2.isTerminal()) {
                                    childSet.add(childdice2);
                                } else {

                                    for (Move move4 : childdice2.getLegalMove(true, false)) {
                                        BackgammonBoard childdice3 = new BackgammonBoard(childdice2);

                                        if (!childdice3.playMove(move4)) {
                                            throw new IllegalArgumentException("GetChildren - error playmove!! - quarto dado");
                                        }
                                        childSet.add(childdice3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (BackgammonBoard bbtemp : childSet) {
            children.add(bbtemp);
        }

        return children;
    }
    
    
}
