package common;

/**
 * Created by Piotr Borczyk on 20.01.2017.
 */
public class Field {

    private int x;
    private int y;
    private FieldState state;

    private boolean[][] connections = new boolean[3][3];

    public Field() {

    }

    public Field(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = FieldState.EMPTY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public FieldState getState() {
        return state;
    }

    public void setState(FieldState state) {
        this.state = state;
    }

    public void addConnection(int relativeX, int relativeY) {
        connections[relativeX + 1][relativeY + 1] = true;
    }

    public boolean isConnected(int relativeX, int relativeY) {
        return connections[relativeX + 1][relativeY + 1];
    }


}
