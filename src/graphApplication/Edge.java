package graphApplication;

import javafx.beans.property.SimpleStringProperty;

public class Edge {
    private SimpleStringProperty first;
    private SimpleStringProperty second;
    private SimpleStringProperty name;
    private SimpleStringProperty weightEd;

    Vertex vertex_First;
    Vertex vertex_Second;
    EdgeType type;
    String edgeName;
    int weight;

    public Vertex getVertex_First() {
        return vertex_First;
    }

    public void setVertex_First(Vertex vertex_First) {
        this.vertex_First = vertex_First;
        first.set(vertex_First.symbol);
    }

    public Vertex getVertex_Second() {
        return vertex_Second;
    }

    public void setVertex_Second(Vertex vertex_Second) {
        this.vertex_Second = vertex_Second;
        second.set(vertex_Second.symbol);
    }

    public EdgeType getType() {
        return type;
    }

    public void setType(EdgeType type) {
        this.type = type;
    }

    public String getEdgeName() {
        return edgeName;
    }

    public void setEdgeName(String edgeName) {
        this.edgeName = edgeName;
        name.set(edgeName);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        weightEd.set(String.valueOf(weight));
    }

    public Edge(Vertex vertex_First, Vertex vertex_Second, EdgeType type, String edgeName) {
        this.vertex_First = vertex_First;
        this.vertex_Second = vertex_Second;
        this.type = type;
        this.edgeName = edgeName;
        first = new SimpleStringProperty(vertex_First.symbol);
        second = new SimpleStringProperty(vertex_Second.symbol);
        name = new SimpleStringProperty(edgeName);
    }

    public Edge(Vertex vertex_First, Vertex vertex_Second, EdgeType type, String edgeName, int weight) {
        this.vertex_First = vertex_First;
        this.vertex_Second = vertex_Second;
        this.type = type;
        this.edgeName = edgeName;
        this.weight = weight;
        first = new SimpleStringProperty(vertex_First.symbol);
        second = new SimpleStringProperty(vertex_Second.symbol);
        name = new SimpleStringProperty(edgeName);
        weightEd = new SimpleStringProperty(String.valueOf(weight));
    }

    @Override
    public String toString() {

        return (edgeName);
    }
}
