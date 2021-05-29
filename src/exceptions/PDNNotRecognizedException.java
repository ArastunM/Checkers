package exceptions;


/**
 * Thrown when given String PDN is not appropriate / recognized
 *
 */
public class PDNNotRecognizedException extends IllegalArgumentException {
    public PDNNotRecognizedException() {}

    public PDNNotRecognizedException(String PDN, int BOARD_SIZE) {
        super(PDN + " is not a recognized PDN for Board of " + BOARD_SIZE + "X" + BOARD_SIZE);
    }
}
