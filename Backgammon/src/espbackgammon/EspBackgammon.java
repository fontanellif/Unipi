package espbackgammon;

/** Classe Principale del gioco.
 *
 * @author filippofontanelli
 */
public class EspBackgammon {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        /*creo una nuova board*/
        BackgammonBoard bb = new BackgammonBoard();
        BackgammonPlayer user = new HumanPlayer(Board.WHITE);

        BackgammonPlayer minmaxW = new ExpectedMinMaxPlayer(Board.WHITE, new BackgammonEvalutationSecond());
        BackgammonPlayer minmaxB = new ExpectedMinMaxPlayer(Board.BLACK, new BackgammonEvalutation());
        /*lancio 2 dadi*/
        Dice d = new Dice();

        do {
            System.out.println("Lancio dei dadi per decidere che inizierá la partita.....");
            d.toss();
            System.out.println("White Player: " + d.getDc1() + ";\nBlack Player: " + d.getDc2() + ";");
            if (d.getDc1() > d.getDc2()) {
                bb.setHasToMove(Board.WHITE);
                System.out.println("Il primo a giocare sará il player White");
            } else {
                bb.setHasToMove(Board.BLACK);
                System.out.println("Il primo a giocare sará il player Black");

            }

        } while (d.getDc1() == d.getDc2());


        int moveB = 0;
        int moveW = 0;
        long time0 = 0;
        long time1 = 0;

        while (!bb.isTerminal()) {
            d.toss();

            if (bb.getHasToMove() == Board.BLACK) {
                //bb = BackgammonPlayer.initplayturn(bb, d);

                //bb = user.playturn(bb);
                bb = BackgammonPlayer.initplayturn(bb, d);

                Time t = new Time();
                t.start();
                BackgammonBoard tmp = minmaxB.playturn(bb);
                t.stop();
                time0 += t.toValue();
                System.out.println("Time= " + t.toString());
                bb = (tmp != null) ? tmp : bb;
                moveB++;
                bb.setHasToMove(Board.WHITE);

            } else {

                bb = BackgammonPlayer.initplayturn(bb, d);

                bb = user.playturn(bb);
                /*           
                 bb = BackgammonPlayer.initplayturn(bb, d);

                 Time t = new Time();
                 t.start();
                 BackgammonBoard tmp = minmaxW.playturn(bb);
                 t.stop();
                 time1 += t.toValue();
                 System.out.println("Time= " + t.toString());
                 bb = (tmp != null) ? tmp : bb;
                 moveW++;
                 bb.setHasToMove(Board.BLACK);
                 */
            }


            System.out.println("Win: " + bb.getPlayboard().whoWin());
            if (bb.getPlayboard().whoWin() != 0) {
                System.out.println("ERROR");
            }
        }
        System.out.println(bb.toString());
        System.out.println("TimeBlack: " + time0 + ",TimeWhite: " + time1);
        System.out.println("Moves Black: " + moveB + ",Moves White: " + moveW);
        System.out.println("Win: " + bb.getPlayboard().whoWin());
    }
}
