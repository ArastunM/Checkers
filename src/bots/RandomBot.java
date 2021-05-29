package bots;

import checkers.Board;
import checkers.Piece;
import checkers.GameLogics;
import checkers.Moves;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;


/**
 * RandomBot is a randomized Checkers bot making a random legal move on request
 *
 * @author Arastun Mammadli
 */
public class RandomBot {
    // waitTime: waiting time before making a new move in milliseconds
    public static int waitTime = 500;

    public RandomBot() {}

    /**
     * Runs the Bot after given waitTime
     */
    public static void run() {
        BotLogics.runAfterWaiting("RandomBot", waitTime);
    }

    /**
     * Static method used to make a random checkers move (move or capture)
     */
    public static void newMove() {
        if (GameLogics.isCapturePossible()) {
            Piece piece = getRandomPieceThatCanCapture(); Board.activePiece = piece;
            Integer[] currentCoord = Board.getTileCoord(piece.getParentTile());

            Integer[] targetCoord = getRandomCaptureTarget(currentCoord, piece.getIsKing());
            JButton targetTile = Board.tiles[targetCoord[0]][targetCoord[1]];
            Moves.newMove(piece, targetTile);

        } else if (GameLogics.isMovePossible()) {
            Piece pieceThatCanMove = getRandomPieceThatCanMove(); Board.activePiece = pieceThatCanMove;
            Integer[] currentCoord = Board.getTileCoord(pieceThatCanMove.getParentTile());

            Integer[] targetCoord = getRandomMoveTarget(currentCoord, pieceThatCanMove.getIsKing());
            JButton targetTile = Board.tiles[targetCoord[0]][targetCoord[1]];
            Moves.newMove(pieceThatCanMove, targetTile);
        }
    }

    /**
     * @return random piece that can make a legal move
     */
    private static Piece getRandomPieceThatCanMove() {
        ArrayList<Piece> piecesThatCanMove = BotLogics.getPiecesThatCanMove();
        int randomIndex = new Random().nextInt(piecesThatCanMove.size());
        return piecesThatCanMove.get(randomIndex);
    }
    /**
     * @return random piece that can make a legal capture
     */
    private static Piece getRandomPieceThatCanCapture() {
        ArrayList<Piece> piecesThatCanCapture = BotLogics.getPiecesThatCanCapture();
        int randomIndex = new Random().nextInt(piecesThatCanCapture.size());
        return piecesThatCanCapture.get(randomIndex);
    }

    /**
     * @param currentCoord current coordinate
     * @param isKing true if the piece is a King, false otherwise
     * @return random legal move target coordinate
     */
    private static Integer[] getRandomMoveTarget(Integer[] currentCoord, boolean isKing) {
        ArrayList<Integer[]> moveTargets = BotLogics.getMoveTargets(currentCoord, isKing);
        int randomIndex = new Random().nextInt(moveTargets.size());
        return moveTargets.get(randomIndex);
    }
    /**
     * @param currentCoord current coordinate
     * @param isKing true if the piece is a King, false otherwise
     * @return random legal capture target coordinate
     */
    private static Integer[] getRandomCaptureTarget(Integer[] currentCoord, boolean isKing) {
        ArrayList<Integer[]> captureTargets = BotLogics.getCaptureTargets(currentCoord, isKing);
        int randomIndex = new Random().nextInt(captureTargets.size());
        return captureTargets.get(randomIndex);
    }
}
