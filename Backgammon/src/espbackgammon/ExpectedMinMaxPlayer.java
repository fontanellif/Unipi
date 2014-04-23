package espbackgammon;

/**
 * Player che utilizza come logica di gioco l'algoritmo ExpectedMinMax
 *
 * @author filippofontanelli
 */
public class ExpectedMinMaxPlayer extends BackgammonPlayer{

    private Evalutation eval;
     
    public ExpectedMinMaxPlayer(int player_num,Evalutation eval) {
        super(player_num);
        this.eval = eval;
    }

    @Override
    public BackgammonBoard playturn(BackgammonBoard bb) {
        return EngineMinMax.MinMax(bb, 2,eval);
    }
    
}
