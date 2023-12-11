import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Backend implements BackendInterface{

    // Initialize and constructor
    private DijkstraGraph<String, Double> graph;

    public Backend(DijkstraGraph<String, Double> graph) {
        this.graph = graph;
    }

    /**
     * Implementation of the BackendInterface
     */
    @Override
    public void readData(String filepath) {
        try (Stream<String> lines = Files.lines(Paths.get(filepath))) {
            lines.forEach(line -> {
                try {
                    if (line.contains("--")) {
                        // use regular expressions to match the lines
                        String[] parts = line.split("\" -- \"|\" \\[seconds=|\\];");
                        if (parts.length >= 3) {
                            String node1 = parts[0].replace("\"", "");
                            String node2 = parts[1].replace("\"", "");
                            Double weight = Double.parseDouble(parts[2]);

                            graph.insertNode(node1);
                            graph.insertNode(node2);
                            graph.insertEdge(node1, node2, weight);
                            graph.insertEdge(node2, node1, weight);
                        }
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("Parsing error in line: " + line + " - " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    @Override
    public ShortestPathInterface<String, Double> shortestPath(String start, String end) {
        if (!graph.containsNode(start) || !graph.containsNode(end)) {
            return new ShortestPathImplementation(Collections.emptyList(), Collections.emptyList(), 0.0);
        }

        // get the path by calling the shortestPathData method from DijkstraGraph
        List<String> path = graph.shortestPathData(start, end);

        Double cost = graph.shortestPathCost(start, end);

        // create a empty list passing in the weighted of each edge segment
        // List<Double> walkingTimes = Collections.emptyList();
        List<Double> walkingTimes = calculateWalkingTimes(path);

        // return the instance of ShortestPathImplementation
        return new ShortestPathImplementation(path, walkingTimes, cost);
    }

    /**
     * Record each edge weight in a list
     * @param path
     * @return
     */
    private List<Double> calculateWalkingTimes(List<String> path) {
        List<Double> walkingTimes = new ArrayList<>();
        if (path != null && path.size() > 1) {
            for (int i = 0; i < path.size() - 1; i++) {
                String node1 = path.get(i);
                String node2 = path.get(i+1);
                Double edgeWeight = graph.getEdge(node1, node2);
                walkingTimes.add(edgeWeight);
            }
        }
        return walkingTimes;
    }

    @Override
    public String statistics() {
        // Gathering the information about the graph
        int nodeCount = graph.getNodeCount();
        int edgeCount = graph.getEdgeCount();

        return "Nodes: " + nodeCount + "Edges: " + edgeCount;
    }
}
