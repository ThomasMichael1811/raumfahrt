package de.raumfahrt.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class MonitorGraph {

    private final List<MonitorNode> nodes;
    private final List<MonitorEdge> edges;
    private double worldWidthCm;
    private double worldHeightCm;
    private String primaryNodeId;

    public MonitorGraph(List<MonitorNode> nodes, List<MonitorEdge> edges) {
        this.nodes = new ArrayList<>(nodes);
        this.edges = new ArrayList<>(edges);
        this.primaryNodeId = findPrimary();
        validate();
        computeBounds();
    }

    private String findPrimary() {
        List<String> primaries =
                nodes.stream().filter(n -> n.primary()).map(MonitorNode::id).collect(Collectors.toList());
        if (primaries.size() == 1) {
            return primaries.get(0);
        }
        if (primaries.isEmpty()) {
            throw new IllegalArgumentException("Mindestens ein Monitor muss als Primary markiert sein");
        }
        String msg = "Es darf genau einen Primary-Monitor geben (gefunden: " + primaries.size() + ")";
        throw new IllegalArgumentException(msg);
    }

    private void validate() {
        validatePrimaryCount();
        validateGraphConnected();
        validateNodeCountRange();
    }

    private void validatePrimaryCount() {
        long primaryCount = nodes.stream().filter(MonitorNode::primary).count();
        if (primaryCount != 1) {
            String msg2 = "Es muss genau einen Primary-Monitor geben (" + primaryCount + " gefunden)";
            throw new IllegalArgumentException(msg2);
        }
    }

    private void validateGraphConnected() {
        if (nodes.size() > 1) {
            Set<String> visited = new HashSet<>();
            Deque<String> stack = new ArrayDeque<>();
            stack.push(primaryNodeId);
            visited.add(primaryNodeId);

            while (!stack.isEmpty()) {
                String current = stack.pop();
                for (MonitorEdge edge : edges) {
                    String neighbor = getNeighbor(current, edge);
                    if (neighbor != null && !visited.contains(neighbor)) {
                        visited.add(neighbor);
                        stack.push(neighbor);
                    }
                }
            }
            if (visited.size() != nodes.size()) {
                throw new IllegalArgumentException(buildNotConnectedMessage(visited.size(), nodes.size()));
            }
        }
    }

    private String getNeighbor(String current, MonitorEdge edge) {
        if (edge.from().equals(current)) {
            return edge.to();
        }
        if (edge.to().equals(current)) {
            return edge.from();
        }
        return null;
    }

    private void validateNodeCountRange() {
        if (nodes.size() < 1 || nodes.size() > 4) {
            throw new IllegalArgumentException("Anzahl Monitore muss zwischen 1 und 4 liegen (" + nodes.size() + ")");
        }
    }

    private String buildNotConnectedMessage(int visitedCount, int totalCount) {
        return "Graph ist nicht zusammenhängend: " + visitedCount + " von " + totalCount + " Nodes erreichbar";
    }

    private void computeBounds() {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for (MonitorNode node : nodes) {
            double cx = node.calibration().viewingDistanceCm(); // placeholder; actual centroid from edges
            double cy = 0.0; // will be refined
            minX = Math.min(minX, cx);
            maxX = Math.max(maxX, cx);
            minY = Math.min(minY, cy);
            maxY = Math.max(maxY, cy);
        }

        if (nodes.isEmpty()) {
            worldWidthCm = 0;
            worldHeightCm = 0;
        } else {
            worldWidthCm = maxX - minX;
            worldHeightCm = maxY - minY;
        }
    }

    // --- Accessors ---

    public List<MonitorNode> nodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<MonitorEdge> edges() {
        return Collections.unmodifiableList(edges);
    }

    public double worldWidthCm() {
        return worldWidthCm;
    }

    public double worldHeightCm() {
        return worldHeightCm;
    }

    public String primaryNodeId() {
        return primaryNodeId;
    }

    public MonitorNode getNode(String id) {
        return nodes.stream()
                .filter(n -> n.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unbekannter Monitor: " + id));
    }

    public List<MonitorNode> getNeighbors(String id) {
        return edges.stream()
                .filter(e -> e.from().equals(id) || e.to().equals(id))
                .flatMap(e -> {
                    List<MonitorNode> result = new ArrayList<>();
                    if (e.from().equals(id)) {
                        result.add(nodes.stream()
                                .filter(n -> n.id().equals(e.to()))
                                .findFirst()
                                .orElse(null));
                    } else {
                        result.add(nodes.stream()
                                .filter(n -> n.id().equals(e.from()))
                                .findFirst()
                                .orElse(null));
                    }
                    return result.stream();
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public static MonitorGraph of(List<MonitorNode> nodes, List<MonitorEdge> edges) {
        return new MonitorGraph(nodes, edges);
    }
}
