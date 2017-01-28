import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import common.Network;
import common.Network.RegisterInLobby;

import java.io.IOException;
import java.util.Stack;

/**
 * Created by Piotr Borczyk on 20.01.2017.
 */
public class PaperSoccerServer {
    private Server server;
    private Stack<PlayerConnection> playerConnections = new Stack<>();

    public static void main(String[] args) throws IOException {
        new PaperSoccerServer();
    }

    public PaperSoccerServer() throws IOException {
        server = new Server() {
            @Override
            protected Connection newConnection() {
                return new PlayerConnection();
            }
        };

        Network.register(server);

        server.addListener(new Listener() {

            @Override
            public void connected(Connection connection) {
                System.out.println("Player connected");
                PlayerConnection playerConnection = (PlayerConnection) connection;
                playerConnections.add(playerConnection);
            }

            @Override
            public void received(Connection c, Object object) {
                PlayerConnection playerConnection = (PlayerConnection)c;

                if (object instanceof RegisterInLobby) {
                    playerConnections.add(playerConnection);
                    System.out.println("Player re-registered");
                }
            }

            @Override
            public void disconnected(Connection c) {
                System.out.println("Player disconnected");
                playerConnections.remove(c);
            }

        });

        new Thread(() -> {
            while (true) {
                if(playerConnections.size() == 2) {
                    new GameRoom(playerConnections.pop(),playerConnections.pop());
                    System.out.println("Matched");
                }
            }
        },"Matcher Thread").start();

        server.bind(Network.port);
        server.start();
    }
}
