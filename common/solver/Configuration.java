package puzzles.common.solver;

import java.util.Collection;

/**
 * The representation of a single configuration for a puzzle.
 * The BFS common solver uses all these methods to solve a puzzle. Therefore,
 * all the puzzles must implement this interface.
 */
public interface Configuration {
    /**
     * Is the current configuration a solution?
     * @return true if the configuration is a puzzle's solution; false, otherwise
     */
    boolean isSolution();

    /**
     * Get the collection of neighbors from the current configuration.
     * @return All the neighbors
     */
    Collection<Configuration> getNeighbors();

    //////////////////////////////////////////////////////////////////////////////////////
    // The predecessor map from the BFS Solver forces any puzzle configuration          //
    // to override the equals and hashCode methods.                                     //
    // Failure to do so, it may result ending up in an infinite loop if the BFS solver  //
    // is unable to detect already visited configurations.                              //
    //////////////////////////////////////////////////////////////////////////////////////
    boolean equals(Object other);
    int hashCode();
    String toString();
}
