package puzzles.chess.model;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Model that represents a game of solitare chess
 *
 * @author Nick Creeley
 */
public class ChessModel {
    /**
     * the collection of observers of this model
     */
    private final List<Observer<ChessModel, String>> observers = new LinkedList<>();

    /**
     * the current configuration
     */
    private ChessConfig currentConfig;

    private Coordinates selected;

    private String currentFile;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<ChessModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * Creates the chess model instance
     *
     * @param filename file being loaded
     * @throws IOException input output exception
     */

    public ChessModel(String filename) throws IOException {

        this.currentConfig = new ChessConfig(filename);

        this.selected = null;

        this.currentFile = filename;

    }

    /**
     * Finds or captures a selected position
     *
     * @param position Cord of the piece looking to move
     */

    public void selectOrCapture(Coordinates position) {

        //check if the selected spot is valid

        if (currentConfig.getPiece(position.row(), position.col()) == ChessConfig.empty) {

            if (selected == null) {
                alertObservers("Invalid selection " + position);
            } else {
                alertObservers("Cannot capture from " + selected + " to " + position);
                selected = null;
            }
        } else {

            //check if other has already been selected

            if (selected == null) {
                this.selected = position;
                alertObservers("Selected " + position);
            } else {

                //check if the current movement is legal

                Coordinates start = selected;

                Coordinates end = position;

                char piece = currentConfig.getPiece(selected.row(), selected.col());

                switch (piece) {

                    case ChessConfig.king -> {

                        Coordinates shift = new Coordinates(start.row() - end.row(), start.col() - end.col());

                        int rowShift = Math.abs(shift.row());

                        int colShift = Math.abs(shift.col());

                        if ((rowShift == 0 || rowShift == 1) && (colShift == 0 || colShift == 1)) {

                            currentConfig.setPiece(start.row(), start.col(), ChessConfig.empty);
                            currentConfig.setPiece(end.row(), end.col(), ChessConfig.king);

                            alertObservers("Captured from " + start + " to " + end);

                        } else {
                            alertObservers("Cannot capture from " + start + " to " + end);
                        }
                    }
                    case ChessConfig.queen -> {

                        Coordinates shift = new Coordinates(start.row() - end.row(), start.col() - end.col());

                        int rowShift = shift.row();

                        int colShift = shift.col();

                        if (Math.abs(rowShift) == Math.abs(colShift) || rowShift == 0 || colShift == 0) {

                            currentConfig.setPiece(start.row(), start.col(), ChessConfig.empty);
                            currentConfig.setPiece(end.row(), end.col(), ChessConfig.queen);

                            alertObservers("Captured from " + start + " to " + end);

                        } else {
                            alertObservers("Cannot capture from " + start + " to " + end);
                        }

                    }
                    case ChessConfig.knight -> {

                        Coordinates shift = new Coordinates(start.row() - end.row(), start.col() - end.col());

                        int rowShift = Math.abs(shift.row());

                        int colShift = Math.abs(shift.col());

                        if ((rowShift == 2 && colShift == 1) || (rowShift == 1 && colShift == 2)) {

                            currentConfig.setPiece(start.row(), start.col(), ChessConfig.empty);
                            currentConfig.setPiece(end.row(), end.col(), ChessConfig.knight);

                            alertObservers("Captured from " + start + " to " + end);

                        } else {
                            alertObservers("Cannot capture from " + start + " to " + end);
                        }

                    }
                    case ChessConfig.pawn -> {

                        Coordinates shift = new Coordinates(start.row() - end.row(), start.col() - end.col());

                        int rowShift = shift.row();

                        int colShift = shift.col();

                        if (rowShift > 0 && Math.abs(colShift) == 1) {

                            currentConfig.setPiece(start.row(), start.col(), ChessConfig.empty);
                            currentConfig.setPiece(end.row(), end.col(), ChessConfig.pawn);

                            alertObservers("Captured from " + start + " to " + end);

                        } else {
                            alertObservers("Cannot capture from " + start + " to " + end);
                        }

                    }
                    case ChessConfig.bishop -> {

                        Coordinates shift = new Coordinates(start.row() - end.row(), start.col() - end.col());

                        int rowShift = shift.row();

                        int colShift = shift.col();

                        if (Math.abs(rowShift) == Math.abs(colShift)) {

                            currentConfig.setPiece(start.row(), start.col(), ChessConfig.empty);
                            currentConfig.setPiece(end.row(), end.col(), ChessConfig.bishop);

                            alertObservers("Captured from " + start + " to " + end);

                        } else {
                            alertObservers("Cannot capture from " + start + " to " + end);
                        }

                    }
                    case ChessConfig.rook -> {

                        Coordinates shift = new Coordinates(start.row() - end.row(), start.col() - end.col());

                        int rowShift = shift.row();

                        int colShift = shift.col();

                        if (rowShift == 0 || colShift == 0) {

                            currentConfig.setPiece(start.row(), start.col(), ChessConfig.empty);
                            currentConfig.setPiece(end.row(), end.col(), ChessConfig.rook);

                            alertObservers("Captured from " + start + " to " + end);

                        } else {
                            alertObservers("Cannot capture from " + start + " to " + end);
                        }
                    }
                }
                this.selected = null;
            }
        }

    }


    /**
     * Resets the current model back to its beginning state
     */
    public void reset() throws IOException {

        load(currentFile);

        alertObservers("Puzzle reset!");

    }

    /**
     * Loads a new file as the current game board
     *
     * @param filename the file being used
     * @throws IOException input/output exception
     */

    public void load(String filename) throws IOException {

        try {
            this.currentConfig = new ChessConfig(filename);

            this.selected = null;

            this.currentFile = filename;

            alertObservers("Loaded: " + filename.substring(filename.lastIndexOf(File.separator) + 1));
        } catch (FileNotFoundException e) {
            alertObservers("Could not find: " + filename.substring(filename.lastIndexOf(File.separator) + 1));
        }
    }

    /**
     * Solves one step of the puzzle if there is a current way to do so.
     */
    public void hint() {

        if (currentConfig.isSolution()) {
            alertObservers("Already solved!");
        } else {
            Solver solver = new Solver(currentConfig);

            solver.solve(false);

            Configuration nextstep = solver.getNextStep();

            if (nextstep == null) {
                alertObservers("No solution!");

            } else {
                currentConfig = (ChessConfig) nextstep;
                alertObservers("Next Step!");
            }
        }

    }

    /**
     * Gets the piece at a certain spot
     *
     * @param row row of the piece
     * @param col column of the piece
     * @return piece of the
     */
    public char getPiece(int row, int col) {
        return currentConfig.getPiece(row, col);
    }


    /**
     * Gets the max column of current config
     *
     * @return max column
     */
    public int getMaxCol() {
        return currentConfig.getMaxCol();
    }

    /**
     * Gets the max row of the current config
     *
     * @return max row
     */
    public int getMaxRow() {
        return currentConfig.getMaxRow();
    }
}
