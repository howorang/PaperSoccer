/**
 * Created by howor on 27.01.2017.
 */
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import common.Network.MatchMessage;
import common.Network.MoveRequest;
import common.Network.MoveCommand;
import common.Network.WinMessage;
import common.Network.OpponentDisconnected;
import common.Player;
import common.Utils;

import java.util.ArrayList;

public class GameRoom {

    private PlayerConnection playerOneConnection;
    private PlayerConnection playerTwoConnection;
    private Game game;
    private final Listener playerListener;

    public GameRoom(PlayerConnection playerOneConnection, PlayerConnection playerTwoConnection) {
        this.playerOneConnection = playerOneConnection;
        this.playerTwoConnection = playerTwoConnection;
        this.game = new Game();


        MatchMessage message1 = new MatchMessage();
        message1.player = Player.ONE;
        message1.availableFields = new ArrayList<>(Utils.convertToSimpleFields(game.getAvailableFields()));
        playerOneConnection.sendTCP(message1);


        MatchMessage message2 = new MatchMessage();
        message2.player = Player.TWO;
        message2.availableFields = new ArrayList<>(Utils.convertToSimpleFields(game.getAvailableFields()));
        playerTwoConnection.sendTCP(message2);

        playerListener = new Listener() {
            @Override
            public void received(Connection c, Object object) {
                PlayerConnection playerConnection = (PlayerConnection)c;

                if(object instanceof MoveRequest) {
                    MoveRequest moveRequest = (MoveRequest) object;
                    final int x = moveRequest.x;
                    final int y = moveRequest.y;
                    final Player player = moveRequest.player;
                    if (game.validateMove(x, y, player)) {
                        game.registerMove(x, y);
                        MoveCommand moveCommand = new MoveCommand();
                        moveCommand.player = player;
                        moveCommand.availableFields = new ArrayList<>(Utils.convertToSimpleFields(game.getAvailableFields()));
                        moveCommand.x = x;
                        moveCommand.y = y;
                        playerOneConnection.sendTCP(moveCommand);
                        playerTwoConnection.sendTCP(moveCommand);
                        checkAndSendWinMessage();
                    }
                }
            }

            @Override
            public void disconnected (Connection connection) {
                PlayerConnection playerConnection = (PlayerConnection) connection;
                PlayerConnection stillConnected = playerConnection.equals(playerOneConnection)
                        ? playerTwoConnection : playerOneConnection;
                stillConnected.sendTCP(new OpponentDisconnected());
            }
        };

        playerOneConnection.addListener(playerListener);
        playerTwoConnection.addListener(playerListener);
    }

    private void checkAndSendWinMessage() {
        if (game.isHasEnded()) {
            Player winner = game.getWinner();
            WinMessage winMessage = new WinMessage();
            winMessage.winner = winner;
            playerOneConnection.sendTCP(winMessage);
            playerTwoConnection.sendTCP(winMessage);
            cleanUp();
        }
    }

    private void cleanUp() {
        playerOneConnection.removeListener(playerListener);
        playerTwoConnection.removeListener(playerListener);
    }
}
