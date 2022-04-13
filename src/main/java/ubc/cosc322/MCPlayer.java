package ubc.cosc322;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MCPlayer extends LocalPlayer {
    private final long MAX_RUNTIME = 5000;
    private final int NUM_THREADS = 4;

    private final double E_FACTOR = Math.sqrt(2);

    private TreeNode root;

    public MCPlayer() { super("Player", "MC"); }

    @Override
    protected void move() {
        root = new TreeNode(board);

        long endTime = System.currentTimeMillis() + MAX_RUNTIME;

        root.expand();
        ArrayList<TreeNode> rootChildren = root.children;

        root = bestMove(root);
        Move move = root.getMove();
        sendMove(move.currentQueen, move.targetQueen, move.targetArrow);
    }

    private TreeNode bestMove(TreeNode root) {
        int maxWins = -1;
        TreeNode best = null;

        for (TreeNode node : root.children) {
            int wins = node.getVisits() - node.getWins();
            if (wins > maxWins) {
                maxWins = wins;
                best = node;
            }
        }

        return best;
    }

    private void backtrace(TreeNode current, int winner) {
        while (current != null) {
            if (current.state.localPlayer == winner) {
                current.wins++;
            }
            current.visits++;
            current = current.parent;
        }
    }

    private int simulate(TreeNode current) {
        LocalBoard state = current.state.copy();
        int winner = -1;

        while (winner < 0) {
            ArrayList<Move> moves = MoveGenerator.getMoves(state);

            if (moves.size() == 0) {
                return state.getOpponent();
            }

            int moveIdx = (int) (ThreadLocalRandom.current().nextDouble() * moves.size());
            Move move = moves.get(moveIdx);
            state.updateState(move.currentQueen, move.targetQueen, move.targetArrow);
            state.localPlayer = state.getOpponent();
        }

        return winner;
    }

    private TreeNode getMaxLeaf(TreeNode root) {
        TreeNode current = root;

        while (true) {
            assert current != null;
            if (current.children.isEmpty()) break;
            double maxUCB = Double.MIN_VALUE;
            TreeNode maxChild = null;

            for (TreeNode child : current.children) {
                double ucb = child.getUCB();
                if (ucb > maxUCB) {
                    maxUCB = ucb;
                    maxChild = child;
                }
            }

            current = maxChild;
        }

        return current;
    }

    private class MCRun implements Runnable {
        TreeNode root;
        long endTime;
        public int iterations;

        public MCRun(TreeNode root, long endTime) {
            this.root = root;
            this.endTime = endTime;
            iterations = 0;
        }

        @Override
        public void run() {
            while (System.currentTimeMillis() < endTime) {
                TreeNode current = getMaxLeaf(root);
                TreeNode child = current.expand();

                if (child == null) {
                    backtrace(current, current.state.getOpponent());
                    continue;
                }

                int winner = simulate(child);
                backtrace(child, winner);

                iterations++;
            }
        }
    }

    private class TreeNode {
        private LocalBoard state;
        private Move move;
        private int wins = 0;
        private int visits = 0;
        private TreeNode parent;
        private ArrayList<TreeNode> children;

        public TreeNode(LocalBoard state) { this(state, null, null); }

        public TreeNode(LocalBoard state, Move move, TreeNode parent) {
            this.state = state.copy();
            this.move = move;
            this.parent = parent;
            children = new ArrayList<>();
        }

        public TreeNode expand() {
            ArrayList<Move> moves = MoveGenerator.getMoves(state);

            if (moves.size() == children.size()) {
                return null;
            }

            for (Move childMove : moves) {
                LocalBoard childState = state.copy();
                childState.localPlayer = state.localPlayer == 1 ? 2 : 1;
                childState.updateState(childMove);

                children.add(new TreeNode(childState, childMove, this));
            }

            int randIndex = (int) (ThreadLocalRandom.current().nextDouble() * children.size());
            return children.get(randIndex);
        }

        public int getWins() { return wins; }

        public int getVisits() { return visits; }

        public Move getMove() { return move; }

        private double getUCB() {

            if (this.getVisits() == 0) {
                return Double.MAX_VALUE;
            }
            float uct = wins / visits;
            if (parent != null) {
                uct += E_FACTOR * Math.sqrt(Math.log(parent.visits) / visits);
            }

            return uct;
        }
    }
}