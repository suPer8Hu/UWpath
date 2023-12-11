import java.io.FileNotFoundException;
import java.util.List;

public interface BackendInterface {

    // GraphADT graph;

    // public void IndividualBackendInterface(GraphADT graph){
    // this.graph = graph;
    // }

    /**
     * read data from a file
     */
    void readData(String filepath);

    /**
     * get the shortest path from a start to a destination building in the dataset
     */
    ShortestPathInterface<String, Double> shortestPath(String start, String end);

    /**
     * get a string with statistics about the dataset that includes the number of
     * nodes (buildings), the number of edges, and the total walking time (sum of
     * weights) for all edges in the graph.
     */
    String statistics();
}
