package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 * Board implements all Checker board related methods
 *
 * @author Arastun Mammadli
 */
public class Board implements ActionListener {
    // stores the BOARD_SIZE of CheckersApp in a local private instance
    private static final int BOARD_SIZE = CheckersApp.BOARD_SIZE;
    // tiles: contains all of the board tiles with an index of their respective coordinates
    public static JButton[][] tiles = new JButton[BOARD_SIZE][BOARD_SIZE];

    // pieces: contains all pieces on the Board
    public static ArrayList<Piece> pieces = new ArrayList<>();
    // activePiece: refers to the currently selected, active piece
    public static Piece activePiece;

    /**
     * Constructs the Board for the CheckersApp
     */
    public Board() { createTiles(); }

    /**
     * Creates and stores all of the tiles to be placed on the Board,
     * based on the BOARD_SIZE
     */
    private void createTiles() {
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                JButton tile = new JButton();
                tile.addActionListener(this); // adding an actionListener
                tiles[x][y] = tile;
                paintTile(x, y); // painting current tile
                CheckersApp.panel.add(tile);
            }
        }
    }

    /**
     * Paints the tile based on their coordinate (black or white)
     * @param x height, x coordinate of the tile on Board
     * @param y width, y coordinate of the tile on Board
     */
    private static void paintTile(int x, int y) {
        if ((x + y) % 2 == 0)
            tiles[x][y].setBackground(CheckersApp.TILE_TYPE[1]);
        else
            tiles[x][y].setBackground(CheckersApp.TILE_TYPE[0]);
    }

    /**
     * Repaints the whole Board to original tile colors
     */
    public static void rePaint() {
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                paintTile(x, y);
            }
        }
    }

    /**
     * Retrieves respective coordinates of tiles
     * @param tile tile to retrieve coordinates of
     * @return -1 if tile does not exist,
     * coordinate of the tile otherwise
     */
    public static Integer[] getTileCoord(JButton tile) {
        Integer[] coord = new Integer[]{-1, -1};

        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (tile.equals(tiles[x][y])) {
                    coord[0] = x; coord[1] = y;
                    return coord;
                }
            }
        } return coord;
    }

    /**
     * Checks if the given tile is empty (of pieces)
     * @param tile tile to check
     * @return true if the tile is empty, false otherwise
     */
    public static boolean tileIsEmpty(JButton tile) {
        for (Piece piece : pieces) {
            if (piece.getParentTile().equals(tile))
                return false;
        } return true;
    }

    /**
     * @param color color to check for
     * @return ArrayList of tiles painted with given color
     */
    public static ArrayList<JButton> getTileOfNot(Color color) {
        ArrayList<JButton> matchingTiles = new ArrayList<>();
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (!tiles[x][y].getBackground().equals(color))
                    matchingTiles.add(tiles[x][y]);
            }
        } return matchingTiles;
    }

    /**
     * @param coordinate coordinate to get the tile from
     * @return tile in given coordinate
     */
    public static JButton getTileOf(Integer[] coordinate) {
        return tiles[coordinate[0]][coordinate[1]];
    }

    /**
     * @param coordinate coordinate to check
     * @return true if coordinate is in Board, false otherwise
     */
    public static boolean isInBoard(Integer[] coordinate) {
        return (-1 < coordinate[0] && coordinate[0] < CheckersApp.BOARD_SIZE) &&
                (-1 < coordinate[1] && coordinate[1] < CheckersApp.BOARD_SIZE);
    }

    /**
     * @return integer Board position evaluation
     */
    public static int getPositionEvaluation() {
        int evaluation = 0; int pieceValue;
        for (Piece piece : pieces) {
            pieceValue = piece.getColor() ? 1 : -1;
            pieceValue *= piece.getIsKing() ? 8 : 1;
            evaluation += pieceValue;
        } return evaluation;
    }

    /**
     * Checks for actions performed on GUI.
     * If an empty tile is selected and there is an active piece,
     * newMove method is called
     * @param e performed action / event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton tile = (JButton) e.getSource();

        if (tileIsEmpty(tile) && activePiece != null
                && activePiece.getColor() == Moves.MOVE) {
            Board.rePaint();
            Moves.newMove(activePiece, tile);
        }
        // activePiece is set to null after a newMove attempt
        activePiece = null;
    }
}
