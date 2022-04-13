package ubc.cosc322;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class MoveGenerator {
    public ArrayList<Move> getMoves(LocalBoard board) {
        ArrayList<Move> list = new ArrayList<>();
        ArrayList<List<Integer>> allQueens = getAllQueenCurrents(board);

        while (!allQueens.isEmpty()) {
            List<Integer> currentQueen = allQueens.remove(0);
            ArrayList<List<Integer>> allQueenTargets = getTargets(currentQueen.get(0), currentQueen.get(1), board);

            while (!allQueenTargets.isEmpty()) {
                List<Integer> targetQueen = allQueenTargets.remove(0);
                ArrayList<List<Integer>> allArrowTargets = getTargets(targetQueen.get(0), targetQueen.get(1), board);
                allArrowTargets.add(currentQueen);

                while (!allArrowTargets.isEmpty()) {
                    List<Integer> targetArrow = allArrowTargets.remove(0);
                    Move newMove = new Move(currentQueen, targetQueen, targetArrow);
                    list.add(newMove);
                }
            }
        }
        return list;
    }

    private ArrayList<List<Integer>> getAllQueenCurrents(LocalBoard board) {
        ArrayList<List<Integer>> queenCurrents = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                List<Integer> position = Arrays.asList(i, j);
                if (board.getPositionValue(position) == board.localPlayer) {
                    queenCurrents.add(position);
                }
            }
        }
        return queenCurrents;
    }

    private ArrayList<List<Integer>> getTargets(int x, int y, LocalBoard board) {
        ArrayList<List<Integer>> targets = new ArrayList<>();

        boolean isUpBlocked = false;
        boolean isDownBlocked = false;
        boolean isRightBlocked = false;
        boolean isLeftBlocked = false;
        boolean isRightUpBlocked = false;
        boolean isRightDownBlocked = false;
        boolean isLeftUpBlocked = false;
        boolean isLeftDownBlocked = false;

        for (int dist = 1; dist < 11; dist++) {
            int up = y + dist;
            int down = y - dist;
            int right = x + dist;
            int left = x - dist;

            if (!isUpBlocked) {
                if (up > 10 || board.getPositionValue(x, up) != 0) {
                    isUpBlocked = true;
                } else {
                    targets.add(Arrays.asList(x, up));
                }
            }

            if (!isDownBlocked) {
                if (down < 1 || board.getPositionValue(x, down) != 0) {
                    isDownBlocked = true;
                } else {
                    targets.add(Arrays.asList(x, down));
                }
            }

            if (!isRightBlocked) {
                if (right > 10 || board.getPositionValue(right, y) != 0) {
                    isRightBlocked = true;
                } else {
                    targets.add(Arrays.asList(right, y));
                }
            }

            if (!isLeftBlocked) {
                if (left < 1 || board.getPositionValue(left, y) != 0) {
                    isLeftBlocked = true;
                } else {
                    targets.add(Arrays.asList(left, y));
                }
            }

            if (!isRightUpBlocked) {
                if (right > 10 || up > 10 || board.getPositionValue(right, up) != 0) {
                    isRightUpBlocked = true;
                } else {
                    targets.add(Arrays.asList(right, up));
                }
            }

            if (!isRightDownBlocked) {
                if (right > 10 || down < 1 || board.getPositionValue(right, down) != 0) {
                    isRightDownBlocked = true;
                } else {
                    targets.add(Arrays.asList(right, down));
                }
            }

            if (!isLeftUpBlocked) {
                if (left < 1 || up > 10 || board.getPositionValue(left, up) != 0) {
                    isLeftUpBlocked = true;
                } else {
                    targets.add(Arrays.asList(left, up));
                }
            }

            if (!isLeftDownBlocked) {
                if (left < 1 || down < 1 || board.getPositionValue(left, down) != 0) {
                    isLeftDownBlocked = true;
                } else {
                    targets.add(Arrays.asList(left, down));
                }
            }
        }

        return targets;
    }
}