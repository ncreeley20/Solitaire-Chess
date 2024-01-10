package puzzles.common.solver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A Solver using BFS
 * Uses Configuration interface to solve different puzzles
 *
 * @author Nick Creeley
 */

public class Solver {
    private final Configuration initConfig;

    private Configuration nextStep;

    /**
     * Creates the Solver function with initial config
     *
     * @param initConfig stores the initial config
     */
    public Solver(Configuration initConfig) {

        this.initConfig = initConfig;

        this.nextStep = null;

    }

    /**
     * Solves the configuration puzzle using BFS
     */
    public void solve(boolean print) {

        //create counters

        int totalConfigs = 1;

        int uniqueConfigs;

        Configuration goal = null;

        //create queue with initConfig

        Queue<Configuration> queue = new LinkedList<>();
        queue.add(initConfig);

        //create predecessor map with initConfig

        HashMap<Configuration, Configuration> predecessors = new HashMap<>();
        predecessors.put(initConfig, null);

        //loop until goal is found

        while (!queue.isEmpty()) {

            Configuration current = queue.remove();

            if (current.isSolution()) {
                goal = current;
                break;
            }
            for (Configuration neighbor : current.getNeighbors()) {
                //update totalConfigs
                totalConfigs++;
                if (!predecessors.containsKey(neighbor)) {
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        //set unique configs to size of predecessor map and print both counters

        uniqueConfigs = predecessors.size();

        //creates path and prints it out if there is one

        List<Configuration> path = new LinkedList<>();

        if (goal != null) {
            Configuration currConfig = goal;
            while (currConfig != null) {
                path.add(0, currConfig);
                currConfig = predecessors.get(currConfig);
            }

            //sets the next step

            nextStep = path.get(1);

            //checks if you want to print or not

            if(print){

                System.out.println("Total configs: " + totalConfigs);

                System.out.println("Unique configs: " + uniqueConfigs);

                for (int i = 0; i < path.size(); i++) {
                    System.out.println("Step " + i + ": " + path.get(i));
                }
            }

        } else if (print) {
            System.out.println("Total configs: " + totalConfigs);

            System.out.println("Unique configs: " + uniqueConfigs);

            System.out.println("No solution");
        }
    }

    /**
     * If there is a solution gets the next step in puzzle
     * @return null if no solution or config of next step
     */
    public Configuration getNextStep(){

        return nextStep;
    }
}
