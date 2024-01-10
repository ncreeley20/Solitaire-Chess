package puzzles.chess.solver;

import puzzles.chess.model.ChessConfig;
import puzzles.common.solver.Solver;

import java.io.IOException;

/**
 * Main program for running chess simulation.
 *
 * @author Nick Creeley
 */

public class Chess {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Chess filename");
        }
        else{

            try{
                System.out.println("File: " + args[0]);

                ChessConfig init = new ChessConfig(args[0]);

                System.out.println(init);

                Solver solver = new Solver(init);
                solver.solve(true);
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }

        }
    }
}
