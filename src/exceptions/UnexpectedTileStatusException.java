package exceptions;

import javax.swing.*;


/**
 * Thrown upon unexpected Checkers Board Tile status
 */
public class UnexpectedTileStatusException extends IllegalArgumentException {
    public UnexpectedTileStatusException() {}

    public UnexpectedTileStatusException(JButton tile) {
        super("given tile: " + tile.toString() + " has unexpected status");
    }
}
