package checkers;

import bots.BotLogics;

import java.awt.*;
import java.util.ArrayList;


/**
 * GameDesign provides additional game design related features for CheckersApp
 *
 * @author Arastun Mammadli
 */
public class GameDesign {
    // custom path colors
    private static final Color movePathColor = new Color(204, 255, 255, 255);
    private static final Color capturePathColor = new Color(255, 102, 102);

    // Board tile color variations
    public static Color[] defaultTile = new Color[] {Color.WHITE, Color.BLACK};
    public static Color[] greenComboTile = new Color[]
            {new Color(238,238,210), new Color(118,150,86)};
    public static Color[] woodComboTile = new Color[]
            {new Color(240,217,181), new Color(181,136,99)};

    public GameDesign() {}

    /**
     * @param piece Piece to show move and capture paths of
     */
    public static void showPathsOf(Piece piece) {
        Board.rePaint(); // repainting back to original Board

        if (GameLogics.isCapturePossible() && GameLogics.isCapturePossible
                (Board.getTileCoord(piece.getParentTile()), piece.getIsKing()))
            showCapturePathOf(piece);
        else
            showMovePathOf(piece);

        CheckersApp.pageRefresh(); // updating Board to show changes
    }

    /**
     * Colors possible move paths (tiles) of piece in Board as movePathColor
     * @param piece Piece to color move paths of
     */
    private static void showMovePathOf(Piece piece) {
        Integer[] currentCoord = Board.getTileCoord(piece.getParentTile());
        ArrayList<Integer[]> moveTargets = BotLogics.getMoveTargets(currentCoord, piece.getIsKing());
        for (Integer[] target : moveTargets) {
            Board.getTileOf(target).setBackground(movePathColor);
        }
    }
    /**
     * Colors possible capture paths (tiles) of piece in Board as capturePathColor
     * @param piece Piece to color capture paths of
     */
    private static void showCapturePathOf(Piece piece) {
        Integer[] currentCoord = Board.getTileCoord(piece.getParentTile());
        ArrayList<Integer[]> captureTargets = BotLogics.getCaptureTargets(currentCoord, piece.getIsKing());
        for (Integer[] target : captureTargets) {
            Board.getTileOf(target).setBackground(capturePathColor);
        }
    }
}
