package puzzles.chess.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The Configuration representing a chess board
 *
 * @author Nick Creeley
 */

public class ChessConfig implements Configuration {


    public static final char bishop = 'B', rook = 'R', queen = 'Q',
            knight = 'N', king = 'K', pawn = 'P', empty = '.';

    public static int maxRow;

    public static int maxCol;

    private int piecesLeft;

    private char[][] board;


    /**
     * Creates a new chess configuration with a file
     *
     * @param filename file being used
     * @throws IOException input output exception
     */
    public ChessConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {

            //first line is max row and col
            String line = in.readLine();
            String[] rowCol = line.split("\\s+");

            maxRow = Integer.parseInt(rowCol[0]);

            maxCol = Integer.parseInt(rowCol[1]);

            //now initialize board and fill it with values

            this.board = new char[maxRow][maxCol];

            this.piecesLeft = 0;

            for (int row = 0; row < maxRow; row++) {

                line = in.readLine();
                String[] fields = line.split("\\s+");

                for (int col = 0; col < maxCol; col++) {

                    if (fields[col].charAt(0) != empty) {
                        piecesLeft++;
                    }
                    board[row][col] = fields[col].charAt(0);
                }
            }
        }
    }

    /**
     * Creates a new config using the current boards config
     *
     * @param other      current config being "copied"
     * @param piecesLeft the amount of pieces left
     */
    public ChessConfig(ChessConfig other, int piecesLeft) {

        this.piecesLeft = piecesLeft;
        this.board = new char[maxRow][maxCol];

        for (int row = 0; row < maxRow; row++) {
            System.arraycopy(other.board[row], 0, this.board[row], 0, maxCol);
        }

    }


    @Override
    public boolean isSolution() {
        return piecesLeft == 1;
    }

    @Override
    public Collection<Configuration> getNeighbors() {

        Collection<Configuration> neighbors = new ArrayList<>();

        for (int currRow = 0; currRow < maxRow; currRow++) {
            for (int currCol = 0; currCol < maxCol; currCol++) {

                if (board[currRow][currCol] != empty) {

                    switch (board[currRow][currCol]) {

                        case queen -> {

                            //checks the top, bottom, left and right
                            Collection<Configuration> horiVertNeighbors = horizontalVerticalNeighbors(currRow, currCol, queen);
                            neighbors.addAll(horiVertNeighbors);

                            //checks diagonals
                            Collection<Configuration> diagonalNeighbors = diagonalNeighbors(currRow, currCol, queen);
                            neighbors.addAll(diagonalNeighbors);

                        }

                        case rook -> {

                            Collection<Configuration> horiVertNeighbors = horizontalVerticalNeighbors(currRow, currCol, rook);
                            neighbors.addAll(horiVertNeighbors);
                        }

                        case knight -> {

                            //check left to right first

                            if (currCol + 2 < maxCol && currRow + 1 < maxRow && board[currRow + 1][currCol + 2] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow + 1][currCol + 2] = knight;
                                neighbors.add(neighbor);

                            }

                            if (currCol + 2 < maxCol && currRow - 1 >= 0 && board[currRow - 1][currCol + 2] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow - 1][currCol + 2] = knight;
                                neighbors.add(neighbor);

                            }

                            if (currCol - 2 >= 0 && currRow + 1 < maxRow && board[currRow + 1][currCol - 2] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow + 1][currCol - 2] = knight;
                                neighbors.add(neighbor);
                            }

                            if (currCol - 2 >= 0 && currRow - 1 >= 0 && board[currRow - 1][currCol - 2] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow - 1][currCol - 2] = knight;
                                neighbors.add(neighbor);
                            }

                            //check top to bottom

                            if (currRow + 2 < maxRow && currCol + 1 < maxCol && board[currRow + 2][currCol + 1] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow + 2][currCol + 1] = knight;
                                neighbors.add(neighbor);

                            }

                            if (currRow + 2 < maxRow && currCol - 1 >= 0 && board[currRow + 2][currCol - 1] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow + 2][currCol - 1] = knight;
                                neighbors.add(neighbor);

                            }

                            if (currRow - 2 >= 0 && currCol + 1 < maxCol && board[currRow - 2][currCol + 1] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow - 2][currCol + 1] = knight;
                                neighbors.add(neighbor);

                            }

                            if (currRow - 2 >= 0 && currCol - 1 >= 0 && board[currRow - 2][currCol - 1] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow - 2][currCol - 1] = knight;
                                neighbors.add(neighbor);

                            }
                        }

                        case king -> {

                            //check above first

                            if (currRow - 1 >= 0 && board[currRow - 1][currCol] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow - 1][currCol] = king;
                                neighbors.add(neighbor);

                            }

                            //check below

                            if (currRow + 1 < maxRow && board[currRow + 1][currCol] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow + 1][currCol] = king;
                                neighbors.add(neighbor);

                            }

                            //check to the right

                            if (currCol + 1 < maxCol && board[currRow][currCol + 1] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow][currCol + 1] = king;
                                neighbors.add(neighbor);

                            }

                            //check to the right

                            if (currCol - 1 >= 0 && board[currRow][currCol - 1] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow][currCol - 1] = king;
                                neighbors.add(neighbor);

                            }

                            //check diagonals

                            if (currRow - 1 >= 0 && currCol - 1 >= 0 && board[currRow - 1][currCol - 1] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow - 1][currCol - 1] = king;
                                neighbors.add(neighbor);

                            }

                            if (currRow - 1 >= 0 && currCol + 1 < maxCol && board[currRow - 1][currCol + 1] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow - 1][currCol + 1] = king;
                                neighbors.add(neighbor);

                            }

                            if (currRow + 1 < maxRow && currCol + 1 < maxCol && board[currRow + 1][currCol + 1] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow + 1][currCol + 1] = king;
                                neighbors.add(neighbor);

                            }

                            if (currRow + 1 < maxRow && currCol - 1 >= 0 && board[currRow + 1][currCol - 1] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow + 1][currCol - 1] = king;
                                neighbors.add(neighbor);

                            }

                        }

                        case pawn -> {


                            if (currRow - 1 >= 0 && currCol + 1 < maxCol && board[currRow - 1][currCol + 1] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow - 1][currCol + 1] = pawn;
                                neighbors.add(neighbor);
                            }

                            if (currRow - 1 >= 0 && currCol - 1 >= 0 && board[currRow - 1][currCol - 1] != empty) {

                                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                                neighbor.board[currRow][currCol] = empty;
                                neighbor.board[currRow - 1][currCol - 1] = pawn;
                                neighbors.add(neighbor);
                            }

                        }

                        case bishop -> {

                            Collection<Configuration> diagonalNeighbors = diagonalNeighbors(currRow, currCol, bishop);
                            neighbors.addAll(diagonalNeighbors);

                        }
                    }
                }
            }
        }

        return neighbors;
    }


    /**
     * A method that gets the neighbors of common movements between rook and queen
     *
     * @param currRow current row of piece
     * @param currCol current col of piece
     * @param piece   either a rook or queen
     * @return collection of 4 possible neighbors
     */
    public Collection<Configuration> horizontalVerticalNeighbors(int currRow, int currCol, char piece) {

        Collection<Configuration> neighbors = new ArrayList<>();

        int moveCol = currCol; //used by both left and right

        while (moveCol + 1 < maxCol) {

            if (board[currRow][moveCol + 1] != empty) {

                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                neighbor.board[currRow][currCol] = empty;
                neighbor.board[currRow][moveCol + 1] = piece;

                //add configuration to the neighbors and break
                neighbors.add(neighbor);
                break;
            }
            moveCol++;
        }

        //check left next and reset the currCol

        moveCol = currCol;

        while (moveCol - 1 >= 0) {

            if (board[currRow][moveCol - 1] != empty) {

                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                neighbor.board[currRow][currCol] = empty;
                neighbor.board[currRow][moveCol - 1] = piece;

                //add configuration to neighbors and break
                neighbors.add(neighbor);
                break;
            }
            moveCol--;
        }

        //check top and bottom next

        int moveRow = currRow;

        while (moveRow + 1 < maxRow) {

            if (board[moveRow + 1][currCol] != empty) {

                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                neighbor.board[currRow][currCol] = empty;
                neighbor.board[moveRow + 1][currCol] = piece;

                //add configuration to neighbors and break
                neighbors.add(neighbor);
                break;
            }
            moveRow++;
        }

        //check top and rest moveRow
        moveRow = currRow;

        while (moveRow - 1 >= 0) {

            if (board[moveRow - 1][currCol] != empty) {

                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                neighbor.board[currRow][currCol] = empty;
                neighbor.board[moveRow - 1][currCol] = piece;

                //add configuration to neighbors and break
                neighbors.add(neighbor);
                break;
            }
            moveRow--;
        }

        return neighbors;

    }

    /**
     * A method that finds the diagonal neighbors for pieces that use diagonal movement
     *
     * @param currRow the current row of the piece
     * @param currCol the current column of the piece
     * @param piece   the piece being used (bishop or queen)
     * @return a collection of neighbors
     */

    public Collection<Configuration> diagonalNeighbors(int currRow, int currCol, char piece) {

        Collection<Configuration> neighbors = new ArrayList<>();

        //check diagonals above first

        int moveRow = currRow;

        int moveCol = currCol;

        //check diagonal above and to the right

        while (moveRow - 1 >= 0 && moveCol + 1 < maxCol) {

            if (board[moveRow - 1][moveCol + 1] != empty) {

                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                neighbor.board[currRow][currCol] = empty;
                neighbor.board[moveRow - 1][moveCol + 1] = piece;

                //add configuration to neighbors and break
                neighbors.add(neighbor);
                break;

            }
            moveRow--;
            moveCol++;
        }

        //reset the row and col and check up and to the left
        moveRow = currRow;
        moveCol = currCol;

        while (moveRow - 1 >= 0 && moveCol - 1 >= 0) {

            if (board[moveRow - 1][moveCol - 1] != empty) {

                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                neighbor.board[currRow][currCol] = empty;
                neighbor.board[moveRow - 1][moveCol - 1] = piece;

                //add configuration to neighbors and break
                neighbors.add(neighbor);
                break;

            }
            moveRow--;
            moveCol--;
        }

        //reset the row and col and check down and to the right

        moveRow = currRow;
        moveCol = currCol;

        while (moveRow + 1 < maxRow && moveCol + 1 < maxCol) {

            if (board[moveRow + 1][moveCol + 1] != empty) {

                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                neighbor.board[currRow][currCol] = empty;
                neighbor.board[moveRow + 1][moveCol + 1] = piece;

                //add configuration to neighbors and break
                neighbors.add(neighbor);
                break;

            }
            moveRow++;
            moveCol++;

        }

        //reset the row and col and check down and to the left

        moveRow = currRow;
        moveCol = currCol;

        while (moveRow + 1 < maxRow && moveCol - 1 >= 0) {

            if (board[moveRow + 1][moveCol - 1] != empty) {

                ChessConfig neighbor = new ChessConfig(this, piecesLeft - 1);
                neighbor.board[currRow][currCol] = empty;
                neighbor.board[moveRow + 1][moveCol - 1] = piece;

                //add configuration to neighbors and break
                neighbors.add(neighbor);
                break;

            }
            moveRow++;
            moveCol--;

        }

        return neighbors;
    }

    /**
     * Gets the max column
     *
     * @return int representing max col
     */
    public static int getMaxCol() {
        return maxCol;
    }

    /**
     * Gets the max row
     *
     * @return int representing max row
     */
    public static int getMaxRow() {
        return maxRow;
    }


    /**
     * Gets a piece from the board
     *
     * @param row row of piece
     * @param col column of piece
     * @return the value in the grid position
     */
    public char getPiece(int row, int col) {

        return board[row][col];
    }

    /**
     * Sets a piece in the configuration
     * Removes a piece if the piece being set it not empty
     *
     * @param row   row of piece
     * @param col   column of piece
     * @param value piece being placed
     */
    public void setPiece(int row, int col, char value) {

        if (value != empty) {
            piecesLeft -= 1;
        }
        this.board[row][col] = value;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ChessConfig o) {
            for (int currRow = 0; currRow < maxRow; currRow++) {
                for (int currCol = 0; currCol < maxCol; currCol++) {

                    if (this.board[currRow][currCol] != o.board[currRow][currCol]) {
                        return false;
                    }

                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {

        return this.toString().hashCode();

    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        result.append(System.lineSeparator());

        for (int currRow = 0; currRow < maxRow; currRow++) {
            for (int currCol = 0; currCol < maxCol; currCol++) {
                result.append(board[currRow][currCol]).append(" ");
            }
            result.append(System.lineSeparator());
        }

        return result.toString();
    }
}
