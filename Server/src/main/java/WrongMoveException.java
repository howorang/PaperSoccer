import common.Player;

/**
 * Created by howor on 28.01.2017.
 */
public class WrongMoveException extends Exception {
    private int x;
    private int y;
    Player player;

    public WrongMoveException(int x,int y, Player player) {
        super("Move to x: " + x + " y: " + y + " by Player" + player.name() +" is invalid");
    }
}
