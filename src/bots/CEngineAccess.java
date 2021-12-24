package bots;

import checkers.Board;
import checkers.Moves;
import checkers.Piece;


/**
 * bots.CEngineAccess manages the CheckersApp - CEngine interactions
 */
public class CEngineAccess {
    // waitTime: waiting time before making a new move in milliseconds
    public static int waitTime = 500;

    // native request method (requesting the String best move from CEngine)
    public native String request(String pdn, int depth, int col);
    static { System.loadLibrary("CEngine"); }

    /**
     * Runs the CEngineAccess after given waitTime
     */
    public static void run() { BotLogics.runAfterWaiting("CEngine", waitTime); }

    /**
     * Static method used to request and interpret the raw best move from CEngine
     */
    public static void newMove() {
        // requesting the raw best move
        int fixed_depth = 4;
        int col = Moves.MOVE ? 0 : 1;
        String best_move = new CEngineAccess().request(checkers.PDN.getBoardPosition(), fixed_depth, col);

        // interpreting the obtained output
        String[] split = best_move.split(" ");
        Integer[] from_coord = get_real_coord(split[0]);
        Integer[] to_coord = get_real_coord(split[2]);

        // making the move on Board
        Piece pieceToMove = Piece.getPieceOnTile(Board.tiles[from_coord[0]][from_coord[1]]);
        Board.activePiece = pieceToMove;
        Moves.newMove(pieceToMove, Board.tiles[to_coord[0]][to_coord[1]]);
    }

    /**
     * @param coord String coordinate to interpret [x][y]
     * @return integer array from of the given String coordinate {x, y}
     */
    public static Integer[] get_real_coord(String coord) {
        Integer[] real_coord = new Integer[2];
        String[] split_coord = coord.split("");

        try {
            real_coord[0] = Integer.parseInt(split_coord[1]);
            real_coord[1] = Integer.parseInt(split_coord[4]);
            // fixing the y values (adjusting to CheckersApp Board coordinate system)
            real_coord[1] = real_coord[0] % 2 == 0 ? real_coord[1]*2+1 : real_coord[1]*2;
        } catch (NumberFormatException e) { e.printStackTrace(); }
        return real_coord;
    }
}
