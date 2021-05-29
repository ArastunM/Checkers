package bots;

import checkers.*;
import javax.swing.*;
import java.util.ArrayList;


/**
 * BotLogics provides methods to retrieve legal move possibilities
 * necessary for any programmed bot scripts
 *
 * @author Arastun Mammadli
 */
public class BotLogics {
    /**
     * @return ArrayList of Pieces on Board that can make any move
     */
    public static ArrayList<Piece> getPiecesThatCanMove() {
        ArrayList<Piece> piecesThatCanMove = new ArrayList<>();
        for (Piece piece : Board.pieces) {
            Integer[] currentCoord = Board.getTileCoord(piece.getParentTile());
            if (piece.getColor() == Moves.MOVE && GameLogics.isMovePossible(currentCoord, piece.getIsKing())) {
                piecesThatCanMove.add(piece);
            }
        }
        return piecesThatCanMove;
    }

    /**
     * @return ArrayList of Pieces on Board that can make any capture
     */
    public static ArrayList<Piece> getPiecesThatCanCapture() {
        ArrayList<Piece> piecesThatCanCapture = new ArrayList<>();
        for (Piece piece : Board.pieces) {
            Integer[] currentCord = Board.getTileCoord(piece.getParentTile());
            if (piece.getColor() == Moves.MOVE && GameLogics.isCapturePossible(currentCord, piece.getIsKing())) {
                piecesThatCanCapture.add(piece);
            }
        }
        return piecesThatCanCapture;
    }

    /**
     * @param currentCoord current coordinate
     * @param isKing       true if the piece is a King, false otherwise
     * @return ArrayList of move target coordinate for a piece on currentCoord
     */
    public static ArrayList<Integer[]> getMoveTargets(Integer[] currentCoord, boolean isKing) {
        return isKing ? getKingMoveTargets(currentCoord) : getMoveTargets(currentCoord);
    }

    private static ArrayList<Integer[]> getMoveTargets(Integer[] currentCoord) {
        int value = Moves.MOVE ? -1 : 1;
        ArrayList<Integer[]> moveTargets = new ArrayList<>();
        moveTargets.add(new Integer[]{currentCoord[0] + value, currentCoord[1] + 1});
        moveTargets.add(new Integer[]{currentCoord[0] + value, currentCoord[1] - 1});

        moveTargets.removeIf(target -> !((Board.isInBoard(target)) && Board.tileIsEmpty(Board.getTileOf(target))));
        return moveTargets;
    }

    private static ArrayList<Integer[]> getKingMoveTargets(Integer[] currentCoord) {
        ArrayList<Integer[]> moveTargets = new ArrayList<>();
        for (JButton tile : Board.getTileOfNot(CheckersApp.TILE_TYPE[0])) {
            if (GameLogics.isMovePossible(currentCoord, Board.getTileCoord(tile), true))
                moveTargets.add(Board.getTileCoord(tile));

        }
        moveTargets.removeIf(target -> !(Board.tileIsEmpty(Board.getTileOf(target))));
        return moveTargets;
    }

    /**
     * @param currentCoord current coordinate
     * @param isKing       true if the piece is a King, false otherwise
     * @return ArrayList of capture target coordinates for a piece on currentCoord
     */
    public static ArrayList<Integer[]> getCaptureTargets(Integer[] currentCoord, boolean isKing) {
        return isKing ? getKingCaptureTargets(currentCoord) : getCaptureTargets(currentCoord);
    }

    private static ArrayList<Integer[]> getCaptureTargets(Integer[] currentCoord) {
        ArrayList<Integer[]> captureTargets = new ArrayList<>();
        for (JButton tile : Board.getTileOfNot(CheckersApp.TILE_TYPE[0])) {
            if (GameLogics.isCapturePossible(currentCoord, Board.getTileCoord(tile)))
                captureTargets.add(Board.getTileCoord(tile));
        }
        captureTargets.removeIf(target -> (!Board.tileIsEmpty(Board.getTileOf(target))));
        return captureTargets;
    }

    private static ArrayList<Integer[]> getKingCaptureTargets(Integer[] currentCoord) {
        ArrayList<Integer[]> captureTargets = new ArrayList<>();
        for (JButton tile : Board.getTileOfNot(CheckersApp.TILE_TYPE[0])) {
            if (GameLogics.isKingCapturePossible(currentCoord, Board.getTileCoord(tile)))
                captureTargets.add(Board.getTileCoord(tile));
        }
        captureTargets.removeIf(target -> (!Board.tileIsEmpty(Board.getTileOf(target))));
        return captureTargets;
    }

    /**
     * @param botType bot to run after waiting
     * @param time time to wait in milliseconds
     */
    public static void runAfterWaiting(String botType, int time) {
        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    if (botType.equals("RandomBot"))
                        RandomBot.newMove();
                    // implement statements for other bots...
                    super.cancel();
                }
            }, time
        );
    }
}
