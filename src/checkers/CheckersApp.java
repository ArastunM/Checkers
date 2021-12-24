package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


/**
 * CheckersApp provides central instances and functions of the Checkers app.
 * The app is launched from this file
 *
 * @author Arastun Mammadli
 */
public class CheckersApp {
    // BOARD_SIZE: size of the Checkers board (width and height)
    public static int BOARD_SIZE = 8; // MUST BE EVEN

    // SIDEBAR_HEIGHT: size of the sidebar (default: .5)
    public static final double SIDE_PANEL_HEIGHT = .5;
    // STARTING_PDN: starting position of the game in PDN from
    public static final String STARTING_PDN = PDN.getStartingPosition();
    // GAME_MODE: String representation of current game mode
    public static String GAME_MODE = "2 PLAYER MODE";
    // TILE_COLOR: color of the Board tile (other than white)
    public static Color[] TILE_TYPE = GameDesign.defaultTile;

    // OLD_FRAME_DIM: old dimensions of the frame (before resizing update)
    public static Integer[] OLD_FRAME_DIM = {0, 0};
    // OLD_PIECE_WIDTH: old width of the Piece (before resizing update)
    public static int OLD_PIECE_WIDTH;

    // constructing the frame and panels
    public static JFrame frame = new JFrame();
    public static JPanel panel = new JPanel();
    public static SidePanel sidePanel = new SidePanel();

    // image sources
    public static String whitePiece = "/images/whitePiece.png";
    public static String blackPiece = "/images/blackPiece.png";
    public static String whiteKing = "/images/whiteKing.png";
    public static String blackKing = "/images/blackKing.png";

    /**
     * CheckersApp constructor used to launch the app
     */
    public CheckersApp() {
        // setting main panel's parameters
        panel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        // setting up the board on the panel
        new Board();
        // setting up pieces on the board
        CheckersApp.restartGame(STARTING_PDN);

        // setting frame parameters
        pageResize(frame);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(sidePanel.panel, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Checkers");
        frame.pack();
        frame.setVisible(true);
        frame.setSize(BOARD_SIZE * 100,
                (int) ((BOARD_SIZE + SIDE_PANEL_HEIGHT) * 100));
    }

    /**
     * Static method used to restart the game
     * @param pdn starting position of the new game
     */
    public static void restartGame(String pdn) {
        while (Board.pieces.size() != 0)
            Board.pieces.get(0).removePiece();
        CheckersApp.GAME_MODE = "2 PLAYER MODE";
        Board.rePaint();
        PDN.interpretPDN(pdn);
        Moves.MOVE = false; Moves.nextMove();
    }

    /**
     * Static method used to terminate the game
     */
    public static void gameOver() {
        String winner = Moves.MOVE ? "black" : "white";
        String message = "GAME OVER, " + winner + " wins!";
        JOptionPane.showMessageDialog(null, message);
        restartGame(STARTING_PDN);
    }

    /**
     * Static method used to refresh the game panel
     */
    public static void pageRefresh() {
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Adjusts image icons upon frame resize of more than 100 pixels
     * @param frame frame to be resized
     */
    public static void pageResize(JFrame frame) {
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                if (Math.abs(OLD_FRAME_DIM[0] - frame.getWidth()) > 100 ||
                        Math.abs(OLD_FRAME_DIM[1] - frame.getHeight()) > 100) {

                    OLD_FRAME_DIM[0] = frame.getWidth(); OLD_FRAME_DIM[1] = frame.getHeight();
                    OLD_PIECE_WIDTH = Board.pieces.get(0).getSelf().getWidth();
                    Piece.refitAllIcons();
                }
            }
        });
    }

    /**
     * Constructs the CheckersApp
     * @param args empty
     */
    public static void main(String[] args) { new CheckersApp(); }
}
