package checkers;

import bots.CEngineAccess;
import bots.RandomBot;

import javax.swing.*;
import java.util.ArrayList;


/**
 * Moves contains move and capture logics of Pieces of the CheckersApp
 *
 * @author Arastun Mammadli
 */
public class Moves {
    // determines which side is taking turns
    // true -> white | false -> black
    public static boolean MOVE = true;

    public Moves() {}

    /**
     * called to request a new player move in CheckersApp
     * @param piece Piece that would be moved
     * @param targetTile target tile Piece would be moved to
     */
    public static void newMove(Piece piece, JButton targetTile) {
        // current and target coordinates
        JButton currentTile = piece.getParentTile();
        Integer[] currentCoord = Board.getTileCoord(currentTile);
        Integer[] targetCoord = Board.getTileCoord(targetTile);

        // checking if moving from currentCoord to targetCoord is possible
        if (GameLogics.isMovePossible(currentCoord, targetCoord, piece.getIsKing()))
            move(piece, currentTile, targetTile); // moving the piece

        // checking if capturing by moving from currentCoord to targetCoord is possible
        // isCapturePossible and capture methods are different depending on piece type (piece/king)
        if (!piece.getIsKing()) {
            if (GameLogics.isCapturePossible(currentCoord, targetCoord))
                capture(piece, currentTile, targetTile);
        } else {
            if (GameLogics.isKingCapturePossible(currentCoord, targetCoord))
                kingCapture(piece, currentTile, targetTile);
        }
    }

    /**
     * @param piece Piece to make the move
     * @param currentTile tile to move form
     * @param targetTile tile to move to
     */
    private static void move(Piece piece, JButton currentTile, JButton targetTile) {
        currentTile.remove(piece.getSelf());
        targetTile.add(piece.getSelf());
        piece.setParentTile(targetTile);
        nextMove();
    }
    /**
     * @param piece Piece to make the capture
     * @param currentTile tile to move from
     * @param targetTile tile to move to
     */
    private static void capture(Piece piece, JButton currentTile, JButton targetTile) {
        currentTile.remove(piece.getSelf());
        targetTile.add(piece.getSelf());
        piece.setParentTile(targetTile);

        getPieceToCapture(Board.getTileCoord(currentTile),
                Board.getTileCoord(targetTile)).removePiece();
        // if capture is still possible the capturing side is given another move
        MOVE = GameLogics.isCapturePossible(Board.getTileCoord
                (piece.getParentTile()), piece.getIsKing()) != MOVE;
        nextMove();
    }
    /**
     * @param piece King Piece to make the capture
     * @param currentTile tile to move from
     * @param targetTile tile to move to
     */
    private static void kingCapture(Piece piece, JButton currentTile, JButton targetTile) {
        ArrayList<Piece> piecesToCapture = getPiecesToCapture(Board.getTileCoord(currentTile),
                Board.getTileCoord(targetTile));
        while (piecesToCapture.size() != 0) // deleting all captured pieces
            piecesToCapture.remove(0).removePiece();

        currentTile.remove(piece.getSelf());
        targetTile.add(piece.getSelf());
        piece.setParentTile(targetTile);
        // if capture is still possible the capturing side is given another move
        Moves.MOVE = GameLogics.isCapturePossible() != Moves.MOVE;
        Moves.nextMove();
    }

    /**
     * @param currentCoord current coordinate
     * @param targetCoord target coordinate
     * @return Piece that can be captured
     */
    public static Piece getPieceToCapture(Integer[] currentCoord, Integer[] targetCoord) {
        int coordX = (currentCoord[0] + targetCoord[0]) / 2;
        int coordY = (currentCoord[1] + targetCoord[1]) / 2;
        JButton tileToCheck = Board.tiles[coordX][coordY];
        return Piece.getPieceOnTile(tileToCheck);
    }
    /**
     * @param currentCoord current coordinate
     * @param targetCoord target coordinate
     * @return ArrayList of Pieces that can be captured moving from currentCoord to targetCoord
     */
    private static ArrayList<Piece> getPiecesToCapture(Integer[] currentCoord, Integer[] targetCoord) {
        ArrayList<Piece> piecesToCapture = new ArrayList<>();
        for (Piece piece : Board.pieces) {
            Integer[] checkCoord = Board.getTileCoord(piece.getParentTile());
            if (piece.getColor() != Moves.MOVE && GameLogics.isWithinDiagonal(currentCoord, targetCoord, checkCoord))
                piecesToCapture.add(piece);
        } return piecesToCapture;
    }

    /**
     * @return true if the game is over, false otherwise
     */
    private static boolean isGameOver() {
        return !(GameLogics.isMovePossible() || GameLogics.isCapturePossible());
    }

    /**
     * called when new move is made to adjust necessary settings for the next move
     */
    public static void nextMove() {
        MOVE = !MOVE;
        if (Board.activePiece != null && GameLogics.isKinged(Board.activePiece)) Board.activePiece.king();
        CheckersApp.sidePanel.updateMoveLabels(MOVE);
        CheckersApp.pageRefresh();

        if (isGameOver()) CheckersApp.gameOver();
        else {
            if (CheckersApp.GAME_MODE.equals("2 BOT MODE")
                    || (!Moves.MOVE && CheckersApp.GAME_MODE.equals("PLAYER VS BOT")))
                RandomBot.run();
            else if (!Moves.MOVE && CheckersApp.GAME_MODE.equals("PLAYER VS CENG")) {
                CEngineAccess.run();
            }
        }
    }
}
