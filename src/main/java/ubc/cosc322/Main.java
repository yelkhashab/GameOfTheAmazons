package ubc.cosc322;

import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

public class Main {
    /**
     * @param args for username and password (any string is allowed)
     */
    public static void main(String[] args) {
        // Uncomment one of the following lines based on the desired player type:
        // HumanPlayer player = new HumanPlayer();
        // Spectator player = new Spectator();
        // RPlayer player = new RPlayer();
        MCPlayer player = new MCPlayer();

        if (player.getGameGUI() == null) {
            player.Go();
        } else {
            BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(player::Go);
        }
    }
}