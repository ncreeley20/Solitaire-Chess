package puzzles.chess.ptui;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.chess.model.ChessModel;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Class representing the PTUI model
 *
 * @author Nick Creeley
 */

public class ChessPTUI implements Observer<ChessModel, String> {
    private ChessModel model;


    /**
     * Initializes the PTUI
     *
     * @param filename the file being used
     * @throws IOException input output excpetion
     */
    public void init(String filename) throws IOException {

        this.model = new ChessModel(filename);
        this.model.addObserver(this);

        System.out.print("Loaded: " + filename.substring(filename.lastIndexOf(File.separator) + 1));
        System.out.println(printBoard(model));
        displayHelp();

    }

    /**
     * Prints the board with the current config
     *
     * @param model the current model being used
     * @return String representation of the board
     */

    public String printBoard(ChessModel model) {

        StringBuilder result = new StringBuilder();

        result.append(System.lineSeparator());

        result.append(" ").append(" ").append(" ");

        for (int col = 0; col < model.getMaxCol(); col++) {
            result.append(col).append(" ");
        }

        result.append(System.lineSeparator());

        result.append(" ").append(" ");

        result.append("--".repeat(Math.max(0, model.getMaxCol())));

        result.append(System.lineSeparator());

        for (int currRow = 0; currRow < model.getMaxRow(); currRow++) {
            result.append(currRow).append("| ");
            for (int currCol = 0; currCol < model.getMaxCol(); currCol++) {
                result.append(model.getPiece(currRow, currCol)).append(" ");
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }

    @Override
    public void update(ChessModel model, String data) {

        this.model = model;

        System.out.println(data);

        System.out.println(printBoard(model));

    }

    private void displayHelp() {
        System.out.println("h(int)              -- hint next move");
        System.out.println("l(oad) filename     -- load new puzzle file");
        System.out.println("s(elect) r c        -- select cell at r, c");
        System.out.println("q(uit)              -- quit the game");
        System.out.println("r(eset)             -- reset the current game");
    }

    public void run() throws IOException {
        Scanner in = new Scanner(System.in);
        for (; ; ) {
            System.out.print("> ");
            String line = in.nextLine();
            String[] words = line.split("\\s+");
            if (words.length > 0) {
                if (words[0].startsWith("q")) {
                    break;
                } else if (words[0].startsWith("r")) {
                    model.reset();
                } else if (words[0].startsWith("l")) {
                    model.load(words[1]);
                } else if (words[0].startsWith("s")) {
                    model.selectOrCapture(new Coordinates(words[1], words[2]));
                } else if (words[0].startsWith("h")) {
                    model.hint();
                } else {
                    displayHelp();
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ChessPTUI filename");
        } else {
            try {
                ChessPTUI ptui = new ChessPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}

