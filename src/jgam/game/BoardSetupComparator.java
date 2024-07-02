package jgam.game;
import java.util.Comparator;

public class BoardSetupComparator implements Comparator<BoardSetup> {
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
    @Override
    public int compare(BoardSetup b1, BoardSetup b2) {
        // First compare by priority
        return Double.compare(heuristic(b1), heuristic(b2));

    }

}
