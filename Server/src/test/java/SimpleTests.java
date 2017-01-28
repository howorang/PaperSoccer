import common.Player;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by howor on 28.01.2017.
 */


public class SimpleTests {

    private Game game;

    @Before
    public void initGame() {
        game = new Game();
    }

    @Test
    public void testInit() {
        assertFalse(game.isHasEnded());
        assertNull(game.getWinner());
    }

    @Test
    public void testPlayerSwitch() throws WrongMoveException {
        game.registerMove(4,6,Player.ONE);
        assertEquals(game.getCurrentPlayer(), Player.TWO);
        game.registerMove(4,7,Player.TWO);
        assertEquals(game.getCurrentPlayer(),Player.ONE);
    }

    @Test
    public void testMoveValidationSuccess() throws WrongMoveException {
        game.registerMove(4,6,Player.ONE);
    }

    @Test(expected = WrongMoveException.class)
    public void testMoveValidationFailure() throws WrongMoveException {
        game.registerMove(600,700,Player.ONE);
    }

    @Test
    public void winTest() throws WrongMoveException {
        game.registerMove(4,6,Player.ONE);
        game.registerMove(4,7,Player.TWO);
        game.registerMove(4,8,Player.ONE);
        game.registerMove(4,9,Player.TWO);
        game.registerMove(4,10,Player.ONE);
        game.registerMove(4,11,Player.TWO);
        game.registerMove(4,12,Player.ONE);
        assertTrue(game.isHasEnded());
        assertTrue(game.getWinner().equals(Player.ONE));
    }

    @Test
    public void bounceFromCheckedTest() {

    }

    @Test
    public void bounceFromWallTest() {

    }


}
