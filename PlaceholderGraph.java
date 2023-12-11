import java.util.List;

public class PlaceholderGraph<NodeType, EdgeType extends Number> implements GraphADT<NodeType, EdgeType> {
    private BaseGraph<NodeType, EdgeType> baseGraph;

    public PlaceholderGraph() {
        this.baseGraph = new BaseGraph<>(new PlaceholderMap<>());
    }

    @Override
    public boolean insertNode(NodeType data) {
        return baseGraph.insertNode(data);
    }

    @Override
    public boolean removeNode(NodeType data) {
        return baseGraph.removeNode(data);
    }

    @Override
    public boolean containsNode(NodeType data) {
        return baseGraph.containsNode(data);
    }

    @Override
    public int getNodeCount() {
        return baseGraph.getNodeCount();
    }

    @Override
    public boolean insertEdge(NodeType pred, NodeType succ, EdgeType weight) {
        return baseGraph.insertEdge(pred, succ, weight);
    }

    @Override
    public boolean removeEdge(NodeType pred, NodeType succ) {
        return baseGraph.removeEdge(pred, succ);
    }

    @Override
    public boolean containsEdge(NodeType pred, NodeType succ) {
        return baseGraph.containsEdge(pred, succ);
    }

    @Override
    public EdgeType getEdge(NodeType pred, NodeType succ) {
        return baseGraph.getEdge(pred, succ);
    }

    @Override
    public int getEdgeCount() {
        return baseGraph.getEdgeCount();
    }

    @Override
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        // Implement Dijkstra's algorithm or similar algorithm if needed
        throw new UnsupportedOperationException("Method not implemented yet.");
    }

    @Override
    public double shortestPathCost(NodeType start, NodeType end) {
        // Implement calculation for the path cost
        throw new UnsupportedOperationException("Method not implemented yet.");
    }
}
