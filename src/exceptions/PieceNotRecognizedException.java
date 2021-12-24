package exceptions;

import javax.swing.*;


/**
 * Thrown when given object can not be recognized as a Checkers Piece
 */
public class PieceNotRecognizedException extends IllegalArgumentException {
    public PieceNotRecognizedException() {}

    public PieceNotRecognizedException(JButton object) {
        super(object.toString() + " can not be recognized as a Checkers Piece");
    }
}
