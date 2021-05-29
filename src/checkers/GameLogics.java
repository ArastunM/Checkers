package checkers;

import bots.BotLogics;


/**
 * GameLogics contains contains necessary logic methods to determine
 * whether a capture move or capture is possible by Pieces / Kings.
 *
 * Structure of methods / contents
 * Part 1 - small-scale condition methods
 * Part 2 - move possibility methods
 * Part 3 - capture possibility methods
 *
 * @author Arastun Mammadli
 */
public class GameLogics {
    /**
     * @param piece Piece to check
     * @return true if the Piece has kinged (reached its end of the Board), false otherwise
     */
    public static boolean isKinged(Piece piece) {
        Integer[] pieceCoord = Board.getTileCoord(piece.getParentTile());
        return pieceCoord[0] == (Moves.MOVE ? CheckersApp.BOARD_SIZE - 1 : 0);
    }
    /**
     * @param currentCoord current coordinate
     * @param targetCoord target coordinate
     * @return true if currentCord and targetCoord are in a diagonal, false otherwise
     */
    public static boolean isDiagonal(Integer[] currentCoord, Integer[] targetCoord) {
        return currentCoord[0] + currentCoord[1] == targetCoord[0] + targetCoord[1]
                || currentCoord[0] - targetCoord[0] == currentCoord[1] - targetCoord[1];
    }
    /**
     * @param currentCoord current coordinate
     * @param targetCoord target coordinate
     * @return true if path from currentCoord to targetCoord is blocked, false otherwise
     */
    public static boolean isPathOpen(Integer[] currentCoord, Integer[] targetCoord) {
        for (Piece piece : Board.pieces) {
            Integer[] checkCoord = Board.getTileCoord(piece.getParentTile());
            if (piece.getColor() == Moves.MOVE && isWithinDiagonal(currentCoord, targetCoord, checkCoord)) {
                return false;
            }
        } return true;
    }
    /**
     * @param startCoord starting coordinate
     * @param endCoord ending coordinate
     * @param checkCoord coordinate to check
     * @return true if startCoord, endCoord and checkCoord are all in the same diagonal, false otherwise
     */
    public static boolean isWithinDiagonal(Integer[] startCoord, Integer[] endCoord, Integer[] checkCoord) {
        boolean heightCondition = ((startCoord[0] < checkCoord[0]) && (checkCoord[0] < endCoord[0]))
                || ((endCoord[0] < checkCoord[0]) && (checkCoord[0] < startCoord[0]));
        boolean diagonalCondition = isDiagonal(startCoord, checkCoord) && isDiagonal(endCoord, checkCoord)
                && isDiagonal(startCoord, endCoord);
        return heightCondition && diagonalCondition;
    }
    /**
     * @param currentCoord current coordinate
     * @param targetCoord target coordinate
     * @return true if currentCoord and targetCoord are within move distance, false otherwise
     */
    public static boolean isMoveDistance(Integer[] currentCoord, Integer[] targetCoord) {
        boolean verticalCondition = currentCoord[0] == targetCoord[0] + (Moves.MOVE ? 1 : -1);
        boolean horizontalCondition = (currentCoord[1] == targetCoord[1] + 1
                || currentCoord[1] == targetCoord[1] - 1);
        return verticalCondition && horizontalCondition;
    }
    /**
     * @param currentCoord current coordinate
     * @param targetCoord target coordinate
     * @return true if currentCoord and targetCoord are within king move distance, false otherwise
     */
    public static boolean isKingMoveDistance(Integer[] currentCoord, Integer[] targetCoord) {
        return isDiagonal(currentCoord, targetCoord) && isPathOpen(currentCoord, targetCoord);
    }
    /**
     * @param currentCoord current coordinate
     * @param targetCoord target coordinate
     * @return true if currentCoord and targetCoord are within capturing distance
     */
    public static boolean isCaptureDistance(Integer[] currentCoord, Integer[] targetCoord) {
        return (currentCoord[0] == targetCoord[0] + 2 || currentCoord[0] == targetCoord[0] - 2)
                && (currentCoord[1] == targetCoord[1] + 2 || currentCoord[1] == targetCoord[1] - 2);
    }
    /**
     * @param checkCoord coordinate to check
     * @param targetCoord target coordinate
     * @return true if a capture to targetCoord, capturing piece at checkCoord can be made, false otherwise
     * @throws ArrayIndexOutOfBoundsException thrown when checkCoord is outside the limits of Board
     */
    public static boolean isCaptureAvailable(Integer[] checkCoord, Integer[] targetCoord)
            throws ArrayIndexOutOfBoundsException {
        return !Board.tileIsEmpty(Board.getTileOf(checkCoord))
                && Piece.getPieceOnTile(Board.getTileOf(checkCoord)).getColor() != Moves.MOVE
                && Board.tileIsEmpty(Board.getTileOf(targetCoord));
    }

    /**
     * @return true if any move is possible in the game, false otherwise
     */
    public static boolean isMovePossible() {
        for (Piece piece : Board.pieces) {
            Integer[] currentCoord = Board.getTileCoord(piece.getParentTile());
            if (piece.getColor() == Moves.MOVE && isMovePossible(currentCoord, piece.getIsKing())) {
                return true;
            }
        } return false;
    }
    /**
     * @param currentCoord current coordinate
     * @param isKing if current Piece is a King
     * @return true if a move from currentCoord would be possible, false otherwise
     */
    public static boolean isMovePossible(Integer[] currentCoord, boolean isKing) {
        return BotLogics.getMoveTargets(currentCoord, isKing).size() > 0;
    }
    /**
     * @param currentCoord current coordinate
     * @param targetCoord target coordinate
     * @param isKing if current Piece is a King
     * @return true if a move from currentCoord to targetCoord would be possible, false otherwise
     */
    public static boolean isMovePossible(Integer[] currentCoord, Integer[] targetCoord, boolean isKing) {
        return !isCapturePossible() &&
                (isKing ? isKingMoveDistance(currentCoord, targetCoord) : isMoveDistance(currentCoord, targetCoord));
    }

    /**
     * @return true if any capture is possible in the game, false otherwise
     */
    public static boolean isCapturePossible() {
        for (Piece piece : Board.pieces) {
            Integer[] currentCord = Board.getTileCoord(piece.getParentTile());
            if (piece.getColor() == Moves.MOVE && isCapturePossible(currentCord, piece.getIsKing())) {
                return true;
            }
        } return false;
    }
    /**
     * @param currentCoord current coordinate
     * @return true if a capture moving from currentCoord is possible, false otherwise
     */
    public static boolean isCapturePossible(Integer[] currentCoord, boolean isKing) {
        return BotLogics.getCaptureTargets(currentCoord, isKing).size() > 0;
    }
    /**
     * @param currentCoord current coordinate
     * @param targetCoord target coordinate
     * @return true if a capture moving from currentCoord to targetCoord is possible, false otherwise
     */
    public static boolean isCapturePossible(Integer[] currentCoord, Integer[] targetCoord) {
        int coordX = (currentCoord[0] + targetCoord[0]) / 2; int coordY = (currentCoord[1] + targetCoord[1]) / 2;
        Integer[] checkCoord = new Integer[]{coordX, coordY};
        try {
            return isCaptureAvailable(checkCoord, targetCoord) && isCaptureDistance(currentCoord, targetCoord);
        } catch (ArrayIndexOutOfBoundsException e) { return false; }
    }
    /**
     * @param currentCoord current coordinate
     * @param targetCoord target coordinate
     * @return true if a capture moving from currentCoord to targetCoord is possible, false otherwise
     */
    public static boolean isKingCapturePossible(Integer[] currentCoord, Integer[] targetCoord) {
        for (Piece piece : Board.pieces) {
            Integer[] checkCoord = Board.getTileCoord(piece.getParentTile());
            if (piece.getColor() != Moves.MOVE && isWithinDiagonal(currentCoord, targetCoord, checkCoord)
                    && isPathOpen(currentCoord, targetCoord))
                return true;
        } return false;
    }
}
