package ubc.cosc322;

import java.util.List;

public class Move {
    public List<Integer> currentQueen;
    public List<Integer> targetQueen;
    public List<Integer> targetArrow;

    public Move(List<Integer> currentQueen, List<Integer> targetQueen, List<Integer> targetArrow) {
        this.currentQueen = currentQueen;
        this.targetQueen = targetQueen;
        this.targetArrow = targetArrow;
    }
}