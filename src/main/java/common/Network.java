package common;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;

/**
 * Created by Piotr Borczyk on 18.01.2017.
 */
public class Network {

    public static int port = 54555;

    public Network() {

    }

    public static void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(MatchMessage.class);
        kryo.register(Player.class);
        kryo.register(MoveRequest.class);
        kryo.register(MoveCommand.class);
        kryo.register(ArrayList.class);
        kryo.register(WinMessage.class);
        kryo.register(RegisterInLobby.class);
        kryo.register(OpponentDisconnected.class);
        kryo.register(SimpleField.class);
    }

    public static class MatchMessage {
        public Player player;
        public ArrayList<SimpleField> availableFields;
    }

    public static class MoveRequest {
        public int x,y;
        public Player player;
    }

    public static class MoveCommand {
        public int x,y;
        public Player player;
        public ArrayList<SimpleField> availableFields;
    }

    public static class WinMessage {
        public Player winner;
    }

    public static class RegisterInLobby {

    }

    public static class OpponentDisconnected {

    }

    public static class SimpleField {
        public int x,y;

        public SimpleField() {

        }

        public SimpleField(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}