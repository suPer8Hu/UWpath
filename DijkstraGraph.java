// --== CS400 File Header Information ==--
// Name: <Changlin Hu>
// Email: <chu244@wisc.edu>
// Group and Team: <E15>
// Group TA: <Lakshika Rathi>
// Lecturer: <Gary Dahl>
// Notes to Grader: <N/A>

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     * @param map the map that the graph uses to map a data object to the node
     *        object it is stored in
     */
    public DijkstraGraph(MapADT<NodeType, Node> map) {
        super(map);
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        // Check if both start and end nodes exist in the graph
        if (!containsNode(start) || !containsNode(end)) {
            throw new NoSuchElementException("Start or end node not found in the graph.");
        }
        // Initialize priority queue for nodes to visit and map for visited nodes with their shortest path cost
        PriorityQueue<SearchNode> queue = new PriorityQueue<>();
        MapADT<NodeType, Double> visited = new PlaceholderMap<>();

        // Add the start node to the queue with a cost of 0
        queue.add(new SearchNode(nodes.get(start), 0, null));

        // Iterate over the queue until it's empty
        while (!queue.isEmpty()) {
            SearchNode currentNode = queue.poll();

            // Check if the current node is the end node
            if (currentNode.node.data.equals(end)) {
                return currentNode;
            }

            // Process the current node if it hasn't been visited yet
            if (!visited.containsKey(currentNode.node.data)) {
                visited.put(currentNode.node.data, currentNode.cost);

                // Iterate over the edges leaving the current node
                for (Edge edge : currentNode.node.edgesLeaving) {
                    Node neighbor = edge.successor;
                    double newCost = currentNode.cost + edge.data.doubleValue();

                    // Add the neighbor to the queue if it hasn't been visited or if a shorter path is found
                    if (!visited.containsKey(neighbor.data) || newCost < visited.get(neighbor.data)) {
                        queue.add(new SearchNode(neighbor, newCost, currentNode));
                    }
                }
            }
        }

        // Check if the destination node was reached, if not, throw an exception
        if (!visited.containsKey(end)) {
            throw new NoSuchElementException("No path exists from start to end node.");
        }

        // This return statement will never be reached, but is needed to satisfy the compiler
        return null;
    }


    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        // Use computeShortestPath to get the SearchNode for the end node
        SearchNode endNode = computeShortestPath(start, end);

        // Initialize a LinkedList to store the path from end to start
        LinkedList<NodeType> path = new LinkedList<>();

        // Backtrack from the end node to the start node
        while (endNode != null) {
            // Add the node data at the beginning of the list
            path.addFirst(endNode.node.data);
            // Move to the predecessor
            endNode = endNode.predecessor;
        }

        return path;
    }


    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        try {
            // Use computeShortestPath to get the SearchNode for the end node
            SearchNode endNode = computeShortestPath(start, end);

            // The cost field of endNode represents the cost of the shortest path
            return endNode.cost;
        } catch (NoSuchElementException e) {
            // If there's no path between start and end, or if start/end nodes are not in the graph
            return Double.NaN;
        }
    }


    // TODO: implement 3+ tests in step 4.1

    /**
     * Testcase1 to identify the shortest path from vertices 0 to 4 is 21
     */
    @Test
    public void testCase1() {
        // Initialize the graph instance
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        /**
         * Insert the vertices from 0-8 total 9 vertices
         */
        String[] vertices = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        for (String vertex: vertices) {
            graph.insertNode(vertex);
        }

        // insert the edges between each nodes/vertices
        graph.insertEdge("0", "1", 4);
        graph.insertEdge("0", "7", 8);
        graph.insertEdge("1", "2", 8);
        graph.insertEdge("1", "7", 11);
        graph.insertEdge("2", "8", 2);
        graph.insertEdge("7", "8", 7);
        graph.insertEdge("7", "6", 1);
        graph.insertEdge("2", "3", 7);
        graph.insertEdge("8", "6", 6);
        graph.insertEdge("6", "5", 2);
        graph.insertEdge("2", "5", 4);
        graph.insertEdge("3", "5", 14);
        graph.insertEdge("3", "4", 9);
        graph.insertEdge("5", "4", 10);

        // identify whether the shortest path from 0 to 4 is 21
        Assertions.assertEquals(21, graph.shortestPathCost("0", "4"));
    }


    /**
     * testcase2
     * Check if the computed shortest path cost is as expected
     * Check if the computed path sequence is as expected
     */
    @Test
    public void testCase2() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());

        String[] vertices = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        for (String vertex: vertices) {
            graph.insertNode(vertex);
        }

        // insert the edges between each nodes/vertices
        graph.insertEdge("0", "1", 4);
        graph.insertEdge("0", "7", 8);
        graph.insertEdge("1", "2", 8);
        graph.insertEdge("1", "7", 11);
        graph.insertEdge("2", "8", 2);
        graph.insertEdge("7", "8", 7);
        graph.insertEdge("7", "6", 1);
        graph.insertEdge("2", "3", 7);
        graph.insertEdge("8", "6", 6);
        graph.insertEdge("6", "5", 2);
        graph.insertEdge("2", "5", 4);
        graph.insertEdge("3", "5", 14);
        graph.insertEdge("3", "4", 9);
        graph.insertEdge("5", "4", 10);

        // expected shortest path cost from node 0 to node 8 is 14
        double expectCost = 14;

        // expected path sequence of data from node 0 to node 8
        List<String> expectPath = Arrays.asList("0", "1", "2", "8");

        // Check if the computed shortest path cost is as expected
        Assertions.assertEquals(expectCost, graph.shortestPathCost("0", "8"));

        // Check if the computed path sequence is as expected
        Assertions.assertEquals(expectPath, graph.shortestPathData("0", "8"));

    }

    /**
     * testCase3
     * This test case checks whether a NoSuchElementException is thrown when
     * trying to find the shortest path between two nodes that have no path
     * connecting them.
     */
    @Test
    public void testCase3() {
        // Initialize the graph instance
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());

        // Insert the vertices almost same as the previous graph but insert one more redundant node 9 but
        // there is no path between node 9
        String[] vertices = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        for(String vertex : vertices) {
            graph.insertNode(vertex);
        }

        graph.insertEdge("0", "1", 4);
        graph.insertEdge("0", "7", 8);
        graph.insertEdge("1", "2", 8);
        graph.insertEdge("1", "7", 11);
        graph.insertEdge("2", "8", 2);
        graph.insertEdge("7", "8", 7);
        graph.insertEdge("7", "6", 1);
        graph.insertEdge("2", "3", 7);
        graph.insertEdge("8", "6", 6);
        graph.insertEdge("6", "5", 2);
        graph.insertEdge("2", "5", 4);
        graph.insertEdge("3", "5", 14);
        graph.insertEdge("3", "4", 9);
        graph.insertEdge("5", "4", 10);

        Assertions.assertThrows(NoSuchElementException.class, () -> graph.computeShortestPath("1", "0"));
        Assertions.assertThrows(NoSuchElementException.class, () -> graph.computeShortestPath("4", "1"));
    }
}
