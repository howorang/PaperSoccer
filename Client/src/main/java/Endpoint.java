import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import common.Network;
import common.Network.MatchMessage;
import common.Network.MoveRequest;
import common.Network.MoveCommand;
import common.Network.WinMessage;
import common.Network.RegisterInLobby;
import common.Network.OpponentDisconnected;
import common.Player;

import java.io.IOException;

/**
 * Created by howor on 27.01.2017.
 */
public class Endpoint {
    private static Endpoint ourInstance = new Endpoint();

    public static Endpoint getInstance() {
        return ourInstance;
    }

    private Client client;

    private LoginWindowController loginWindowController;

    private PlayWindowController playWindowController;

    public void setLoginWindowController(LoginWindowController loginWindowController) {
        this.loginWindowController = loginWindowController;
    }

    public void setPlayWindowController(PlayWindowController playWindowController) {
        this.playWindowController = playWindowController;
    }

    private Endpoint() {
        client = new Client();
        client.start();
        Network.register(client);

        client.addListener(new Listener() {

            @Override
            public void connected (Connection connection) {
                System.out.println("Client connected to server");
            }

            @Override
            public void received (Connection connection, Object object) {
                if(object instanceof MatchMessage) {
                    MatchMessage matchMessage = (MatchMessage)object;
                    loginWindowController.startGame(matchMessage.player,matchMessage.availableFields);
                }

                if(object instanceof MoveCommand) {
                    MoveCommand moveCommand = (MoveCommand) object;
                    playWindowController.move(moveCommand.x,moveCommand.y,
                            moveCommand.player,moveCommand.availableFields);
                }

                if(object instanceof WinMessage) {
                    WinMessage winMessage = (WinMessage) object;
                    playWindowController.win(winMessage.winner);
                }

                if(object instanceof OpponentDisconnected) {
                    playWindowController.disconnect();
                }
            }


            @Override
            public void disconnected (Connection connection) {
                playWindowController.connectionLost();
            }
        });

        new Thread("Connect") {
            public void run () {
                try {
                    client.connect(5000, "localhost", Network.port);
                } catch (IOException e) {
                    e.printStackTrace();
                    loginWindowController.connectingError();
                }
            }
        }.start();

    }

    public void requestMove(int x, int y, Player player) {
        MoveRequest moveRequest = new MoveRequest();
        moveRequest.x = x;
        moveRequest.y = y;
        moveRequest.player = player;
        client.sendTCP(moveRequest);
    }

    public void registerInLobby() {
        client.sendTCP(new RegisterInLobby());
    }
}
