import common.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;


/**
 * Created by howor on 28.01.2017.
 */

@RunWith(value = Parameterized.class)
public class WrongMoveTests {
    private int x;
    private int y;
    private Player player;

    private Game game;

    public WrongMoveTests(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    @Parameters
     public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {400, 600, Player.ONE},
                {400, 600, Player.TWO},
                {4, 6, Player.TWO},
                {-700,-600, Player.ONE},
                {-700,-600, Player.TWO}
        });
    }

    @Before
    public void init() {
        game = new Game();
    }

    @Test(expected = WrongMoveException.class)
    public void testWrongMoves() throws WrongMoveException {
        game.registerMove(x,y,player);
    }
}
