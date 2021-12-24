package checkers;

import exceptions.PieceNotRecognizedException;
import exceptions.UnexpectedTileStatusException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Piece contains instances and methods related to individual pieces of CheckersApp
 *
 * @author Arastun Mammadli
 */
public class Piece implements ActionListener {
    // self: reference to the JButton of the piece
    private final JButton self;
    // parentTile: reference to the tile piece is located at
    private JButton parentTile;
    // color: color of the piece (white/black)
    // true -> white | false -> black
    private final boolean color;
    // isKing: denotes whether a piece has been promoted to a king
    private boolean isKing;

    /**
     * Constructs a new Piece based on its location, x/y coordinates and color
     * @param x height, x coordinate of the Piece on Board
     * @param y width, y coordinate of the Piece on Board
     * @param color color of the Piece
     */
    public Piece(int x, int y, boolean color, boolean isKing) {
        // setting up a button for the Piece
        self = new JButton();
        self.setAlignmentX(Component.CENTER_ALIGNMENT);
        self.setOpaque(false);
        self.setContentAreaFilled(false);
        self.setBorderPainted(false);
        self.addActionListener(this);

        // setting the piece on Board
        Board.tiles[x][y].add(self);
        parentTile = Board.tiles[x][y];
        this.color = color; // assigning color
        this.isKing = isKing; // assigning piece type

        refitIcon(); // refitting image icon
        Board.pieces.add(this); // adding Piece to Board
    }

    /**
     * @param button button instance to search for
     * @return Piece containing the given button as its self instance
     */
    public static Piece getPieceOf(JButton button) {
        for (Piece piece : Board.pieces) {
            if (piece.getSelf().equals(button))
                return piece;
        } throw new PieceNotRecognizedException(button);
    }
    /**
     * @param tile tile to search for on Board
     * @return Piece located on given Board tile
     */
    public static Piece getPieceOnTile(JButton tile) {
        for (Piece piece : Board.pieces) {
            if (piece.getParentTile().equals(tile))
                return piece;
        } throw new UnexpectedTileStatusException(tile);
    }

    /**
     * removes the Piece object from the game
     */
    public void removePiece() {
        if (Board.activePiece != null &&
                Board.activePiece.equals(this))
            Board.activePiece = null;
        // removing from the panel / parent tile
        this.getParentTile().remove(this.getSelf());
        // removing from the pieces list
        Board.pieces.remove(this);
    }
    /**
     * Kings the Piece
     */
    public void king() {
        isKing = true;
        refitIcon();
    }

    /**
     * refits the Piece image icon to updated frame dimensions
     */
    public void refitIcon() {
        String imagePath = color ? (isKing ? CheckersApp.whiteKing : CheckersApp.whitePiece)
                : (isKing ? CheckersApp.blackKing : CheckersApp.blackPiece);
        Image image = new ImageIcon(getClass().getResource(imagePath)).getImage();

        if (!(self.getWidth() == 0 && self.getHeight() == 0) || CheckersApp.OLD_PIECE_WIDTH != 0) {
            int pieceSize = self.getWidth() == 0 ? CheckersApp.OLD_PIECE_WIDTH : self.getWidth();
            image = image.getScaledInstance(pieceSize, pieceSize, Image.SCALE_SMOOTH);
        } self.setIcon(new ImageIcon(image));
    }

    /**
     * refits all Piece image icons
     */
    public static void refitAllIcons() {
        for (Piece piece : Board.pieces) { piece.refitIcon(); }
    }

    /**
     * Checks for actions performed on GUI.
     * Sets a piece as active if selected
     * @param e performed action / event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        Board.activePiece = getPieceOf(button);

        if (Board.activePiece.getColor() == Moves.MOVE)
            GameDesign.showPathsOf(Board.activePiece);
    }

    // getter methods
    public JButton getSelf() { return self; }
    public JButton getParentTile() { return parentTile; }
    public boolean getColor() { return color; }
    public boolean getIsKing() { return isKing; }
    // setter method
    public void setParentTile(JButton parentTile) { this.parentTile = parentTile; }
}
