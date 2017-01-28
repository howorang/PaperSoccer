import common.Field;
import common.FieldState;
import common.Player;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Piotr Borczyk on 20.01.2017.
 */
public class Game {

    private Field[][] fields = new Field[9][13];
    private Player currentPlayer = Player.ONE;
    private boolean hasEnded = false;
    private Player winner = null;
    private List<Field> availableFields = null;
    private Field lastMove = null;
    private Field moveField;

    public Game() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 13; y++) {
                fields[x][y] = new Field(x, y);
            }
        }

        moveField = fields[4][6];
        updateAvailableFields(moveField);
    }

    public boolean isHasEnded() {
        return hasEnded;
    }

    public Player getWinner() {
        return winner;
    }

    public List<Field> getAvailableFields() {
        return availableFields;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void registerMove(int x, int y,Player player) throws WrongMoveException {
        if(!validateMove(x,y,player)) {
            throw new WrongMoveException(x,y,player);
        }
        checkWinCondition(x,y);
        moveField = fields[x][y];
        if (lastMove != null) {
            lastMove.addConnection(x - lastMove.getX(), lastMove.getY() - y);
            fields[x][y].addConnection(lastMove.getX() - x, y - lastMove.getY());
        }
        updateAvailableFields(moveField);

        if(!bounce(moveField)) {
            switchPlayers();
        }

        lastMove = moveField;
        moveField.setState(FieldState.CHECKED);
    }

    private void switchPlayers() {
        if (currentPlayer == Player.ONE) {
            currentPlayer = Player.TWO;
        } else {
            currentPlayer = Player.ONE;
        }
    }

    private boolean validateMove(int x, int y,Player player) {
        boolean playerCheck = player == currentPlayer;
        boolean fieldPositionCheck = !isOverBoundary(x,y);
        boolean isAvailableCheck = false;
        if(fieldPositionCheck) {
            isAvailableCheck = checkFieldAvailability(fields[x][y]);
        }
        return playerCheck && fieldPositionCheck && isAvailableCheck;
    }

    private void checkWinCondition(int x, int y) {
        if(y == 0 || y == 12) {
            hasEnded = true;
            winner = currentPlayer;
        }
    }

    private void updateAvailableFields(Field moveField) {
        final int x = moveField.getX();
        final int y = moveField.getY();
        List<Pair<Integer,Integer>> toCheck = Arrays.asList(
                new Pair<>(x - 1,y),
                new Pair<>(x + 1, y),
                new Pair<>(x,y + 1),
                new Pair<>(x,y - 1),
                new Pair<>(x - 1,y - 1),
                new Pair<>(x - 1,y + 1),
                new Pair<>(x + 1,y + 1),
                new Pair<>(x + 1,y - 1));
        List<Pair<Integer,Integer>> inBoundsIndexes = toCheck.stream().filter(p -> !isOverBoundary(p.getKey(),p.getValue())).collect(Collectors.toList());
        List<Field> fieldsToCheck = new ArrayList<>();
        inBoundsIndexes.forEach(p -> fieldsToCheck.add(fields[p.getKey()][p.getValue()]));
        availableFields = fieldsToCheck.stream().filter(this::checkFieldAvailability).collect(Collectors.toList());
    }


    private boolean checkFieldAvailability(Field field) {
        if (isAlong(moveField, field)) return false;
        if(isAlongWall(moveField,field)) return false;
        return true;
    }

    private boolean isAlongWall(Field moveField, Field field) {
        if(isOnWall(moveField)) {
            if(field.getX() == 0 || field.getX() == 8) {
                return true;
            }

            if(field.getY() == 1 || field.getY() == 11) {
                return true;
            }
        }

        return false;
    }

    private boolean isAlong(Field moveField, Field field) {
        int relativeX = field.getX() - moveField.getX();
        int relativeY = moveField.getY() - field.getY();
        return moveField.isConnected(relativeX, relativeY);
    }

    private boolean bounce(Field field) {
        boolean isThroughChecked = field.getState().equals(FieldState.CHECKED);
        boolean isWall = isOnWall(field);
        return isWall || isThroughChecked;

    }

    private boolean isOnWall(Field field) {
        final int x = field.getX();
        final int y = field.getY();

        if (y == 1 || y == 11) {
            if (x != 4) {
                return true;
            }
        }

        if (x == 0 || x == 8) {
            return true;
        }
        return false;
    }


    private boolean isOverBoundary(int x, int y) {
        if (y == 0 || y == 12) {
            if (x < 3 || x > 5) {
                return true;
            }
        }
        if (x < 0 || x > 8) {
            return true;
        }
        if (y < 0 || y > 12) {
            return true;
        }
        return false;
    }

}


