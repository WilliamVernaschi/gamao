package jgam.ai;

import jgam.game.BoardSetup;
import jgam.game.PossibleMoves;
import jgam.game.SingleMove;
import jgam.util.IntList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Bruno on 29/05/2017.
 */
public class AIProgramadaPelaEquipeWilliamCarlos implements AI {
    public void init() throws Exception {

    }

    public void dispose() {

    }

    public String getName() {
        return "IA William Carlos";
    }

    public String getDescription() {
        return "IA William Carlos";
    }

    private List<IntList> getAllPossibleDiceRolls() {
    List<IntList> rolls = new ArrayList<>();
    for (int i = 1; i <= 6; i++) {
        for (int j = 1; j <= 6; j++) {
            IntList roll = new IntList(2);
            roll.add(i); roll.add(j);
            rolls.add(roll);
        }
    }
    return rolls;
}

    public SingleMove[] alphaBetaSearch(BoardSetup bs){
        PossibleMoves pm = new PossibleMoves(bs);

        int decision = 0;
        int i = 0;

        double v = Double.NEGATIVE_INFINITY;
        List<BoardSetup> moveList = pm.getPossbibleNextSetups();

        for(BoardSetup nextState : moveList){
            double thisV = maxValue(nextState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
            if(thisV > v){
                decision = i;
            }
            i++;
        }

        return pm.getMoveChain(decision);
    }

    private boolean cutoffTest(BoardSetup bs, int depth){
        return depth == 5;
    }

    private double eval(BoardSetup bs){
        return 0;
    }


    private double maxValue(BoardSetup bs, double a, double b, int depth) {
        if(cutoffTest(bs, depth)) return eval(bs);

        double v = Double.NEGATIVE_INFINITY;
        // Iterate over all possible dice roll outcomes

        for (IntList roll : getAllPossibleDiceRolls()) {

            PossibleMoves pm = new PossibleMoves(bs, roll); 
            List<BoardSetup> moveList = pm.getPossbibleNextSetups();

            for (BoardSetup nextState : moveList) {
                v = Math.max(v, maxValue(nextState, a, b, depth + 1));
                if (v >= b) return v;
                a = Math.max(a, v);
            }
        }
        return v;
    }

    private double minValue(BoardSetup bs, double a, double b, int depth) {
        if(cutoffTest(bs, depth)) return eval(bs);

        double v = Double.POSITIVE_INFINITY;
        
        for (IntList roll : getAllPossibleDiceRolls()) {
            PossibleMoves pm = new PossibleMoves(bs, roll);
            List<BoardSetup> moveList = pm.getPossbibleNextSetups();
            
            for (BoardSetup nextState : moveList) {
                v = Math.min(v, maxValue(nextState, a, b, depth + 1));
                if (v <= a) return v;
                b = Math.min(v, b);
            }
        }


        return v;
    }

    private double heuristic(BoardSetup bs) {
        double evaluation = 0.0;

        int player = bs.getPlayerAtMove();
        int opponent = 3 - player;

        for (int i = 1; i < 25; i++) {
            int numCheckers = bs.getPoint(player, i);
            int opponentNumCheckers = bs.getPoint(opponent, i);

            if (numCheckers >= 3)
                evaluation -= 10.0 * numCheckers;

            if (opponentNumCheckers == 1)
                evaluation += 75.0;
            else if (opponentNumCheckers == 0)
                evaluation += 25.0;
            else
                evaluation -= 100.0;
        }

        int totalPoints = bs.getPoint(opponent, 25);
        evaluation += 50.0 * totalPoints;

        return evaluation;
    }

    public SingleMove[] makeMoves(BoardSetup bs) throws CannotDecideException {
        double evaluation = Double.NEGATIVE_INFINITY;
        int decision = -1;

        PossibleMoves pm = new PossibleMoves(bs);
        List moveList = pm.getPossbibleNextSetups();

        int i = 0;
        for (Iterator iter = moveList.iterator(); iter.hasNext(); i++) {
            BoardSetup boardSetup = (BoardSetup) iter.next();
            double thisEvaluation = heuristic(boardSetup);
            if (thisEvaluation > evaluation) {
                evaluation = thisEvaluation;
                decision = i;
            }
        }

        if (decision == -1)
            return new SingleMove[0];
        else
            return pm.getMoveChain(decision);
    }

    public int rollOrDouble(BoardSetup boardSetup) throws CannotDecideException {
        return ROLL;
    }

    public int takeOrDrop(BoardSetup boardSetup) throws CannotDecideException {
        return TAKE;
    }
}
