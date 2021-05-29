package checkers;

import exceptions.PDNNotRecognizedException;

import java.util.Locale;


/**
 * Portable Draughts Notation is the standard computer-processable format for recording checker games.
 * PDN accommodates this standard to automate load and store procedures of CheckersApp
 *
 * / -> next row
 * x -> black pieces
 * X -> white pieces
 * Other characters -> empty tile
 * Sample: xxxx/xxxx/xxxx/4/4/XXXX/XXXX/XXXX
 *
 * @author Arastun Mammadli
 */
public class PDN {
    private static final int[] structureEven = getStructureEven();
    private static final int[] structureOdd = getStructureOdd();

    public PDN() {}

    /**
     * Interprets the PDN, setting up the Board accordingly
     * @param PDN String PDN to interpret
     */
    public static void interpretPDN(String PDN) {
        if (!isAppropriate(PDN))
            throw new PDNNotRecognizedException(PDN, CheckersApp.BOARD_SIZE);

        String[] rowsOfPDN = PDN.split("/");
        for (int x = 0; x < rowsOfPDN.length; x++) {
            String[] columnsOfPDN = rowsOfPDN[x].split("");
            for (int y = 0; y < columnsOfPDN.length; y++) {
                switch (columnsOfPDN[y]) {
                    case "x" -> new Piece(x, fixY(x, y), false, false);
                    case "X" -> new Piece(x, fixY(x, y), true, false);
                    case "k" -> new Piece(x, fixY(x, y), false, true);
                    case "K" -> new Piece(x, fixY(x, y), true, true);
                }
            }
        }
    }
    /**
     * @return PDN starting position for CheckersApp based on BOARD_SIZE
     */
    public static String getStartingPosition() {
        StringBuilder PDNBuilder = new StringBuilder();
        int rowsOfBothColorPieces = (CheckersApp.BOARD_SIZE / 2) - 1;

        for (int x = 0; x < CheckersApp.BOARD_SIZE; x++) {
            if (x < rowsOfBothColorPieces)
                PDNBuilder.append("x".repeat(CheckersApp.BOARD_SIZE / 2));
            else if (x >= CheckersApp.BOARD_SIZE - rowsOfBothColorPieces)
                PDNBuilder.append("X".repeat(CheckersApp.BOARD_SIZE / 2));
            PDNBuilder.append("/");
        } return PDNBuilder.substring(0, PDNBuilder.length() - 1);
    }

    /**
     * @return PDN of current Board position for CheckersApp based on BOARD_SIZE
     */
    public static String getBoardPosition() {
        StringBuilder PDNBuilder = new StringBuilder(); Piece piece;
        for (int x = 0; x < CheckersApp.BOARD_SIZE; x++) {
            for (int y = 0; y < CheckersApp.BOARD_SIZE; y++) {
                if (!Board.tileIsEmpty(Board.tiles[x][y])) {
                    piece = Piece.getPieceOnTile(Board.tiles[x][y]);
                    String column = piece.getIsKing() ? "K" : "X";
                    column = piece.getColor() ? column : column.toLowerCase(Locale.ROOT);
                    PDNBuilder.append(column);
                }
                else if (Board.tiles[x][y].getBackground().equals(CheckersApp.TILE_TYPE[1]))
                    PDNBuilder.append(CheckersApp.BOARD_SIZE / 2);

            } PDNBuilder.append("/");
        } return PDNBuilder.substring(0, PDNBuilder.length() - 1);
    }

    /**
     * @param PDN String PDN to check
     * @return true if given PDN is appropriate for current BOARD_SIZE, false otherwise
     */
    public static boolean isAppropriate(String PDN) {
        String[] rowsOfPDN = PDN.split("/");
        if (rowsOfPDN.length != CheckersApp.BOARD_SIZE)
            return false;

        for (String column : rowsOfPDN) {
            if (column.toLowerCase(Locale.ROOT).contains("x")
                    || column.toLowerCase(Locale.ROOT).contains("k"))
                if (column.split("").length != CheckersApp.BOARD_SIZE / 2)
                    return false;
        } return true;
    }

    /**
     * Static function used to fix y coordinate of Piece to be added
     * @param x height, x coordinate of the Piece on Board
     * @param y width, y coordinate of the Piece on Board
     * @return fixed width, y coordinate of the Piece on Board
     */
    private static int fixY(int x, int y) {
        return x % 2 == 0 ? structureEven[y] : structureOdd[y];
    }

    // getStructureEven and getStructureOdd generate the Board structure arrays based on BOARD_SIZE
    private static int[] getStructureEven() {
        int[] updatedRowEven = new int[CheckersApp.BOARD_SIZE / 2];
        for (int i = 0; i < CheckersApp.BOARD_SIZE; i += 2) { updatedRowEven[i / 2] = i; }
        return updatedRowEven;
    }
    private static int[] getStructureOdd() {
        int[] updatedRowEven = new int[CheckersApp.BOARD_SIZE / 2];
        for (int i = 1; i < CheckersApp.BOARD_SIZE; i += 2) { updatedRowEven[i / 2] = i; }
        return updatedRowEven;
    }
}
