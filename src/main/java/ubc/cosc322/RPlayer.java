package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class RPlayer extends LocalPlayer {
    public RPlayer() {
        super("RANDOM_PLAYER", "RANDOM_PASS");
    }

    @Override
    protected void move() {
        ArrayList<Move> actions = getAvailableMoves();
        int randomIdx = (int) (Math.random() * actions.size());

        List<Integer> queenCurrent = actions.get(randomIdx).currentQueen;
        List<Integer> queenTarget = actions.get(randomIdx).targetQueen;
        List<Integer> arrowTarget = actions.get(randomIdx).targetArrow;

        sendMove(queenCurrent, queenTarget, arrowTarget);
    }
}