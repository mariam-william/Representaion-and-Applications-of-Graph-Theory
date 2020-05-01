package graphApplication;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.*;

import static java.lang.Math.PI;
import static java.lang.Math.random;
import static graphApplication.EdgeType.DIRECTED_EDGE;
import static graphApplication.EdgeType.UNDIRECTED_EDGE;

public class Graph {

    public static ArrayList<Vertex> vertices = new ArrayList<>();
    public static ArrayList<Edge> edges = new ArrayList<>();
    ArrayList<Vertex> V_Visited = new ArrayList<>();
    ArrayList<Edge> mst = new ArrayList<>();

    String[][] adjacencyMatrix;
    String[][] incidenceMatrix;
    String[][] representationMatrix;
    int[][] adjacency;
    Edge[] eulerPath; //Euler's Task
    Edge[] eulerCycle; //Euler's Task
    Vertex[] hamiltonsPath;
    Vertex[] hamiltonsCycle;

    HashMap<Vertex, List<Vertex>> adjacencyList;

    public Graph() {
    }

    public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;

    }

    public void restartProgram() {
        vertices.clear();
        edges.clear();
        V_Visited.clear();
        mst.clear();
        adjacencyList.clear();
        adjacencyMatrix = null;
        incidenceMatrix = null;
        representationMatrix = null;
        adjacency = null;
        eulerCycle = null;
        eulerPath = null;
        hamiltonsPath = null;
        hamiltonsCycle = null;
    }

    //-----------------------------------------------------------------------

    /**
     * Graph Representation - Task_1
     **/

    public void makeAdjacencyList() {
        adjacencyList = new HashMap<>();
        for (Vertex vertex : vertices) {
            adjacencyList.put(vertex, new ArrayList<>());
        }
        for (Edge edge : edges) {
            if (edge.type == UNDIRECTED_EDGE) {
                adjacencyList.get(edge.vertex_First).add(edge.vertex_Second);
                adjacencyList.get(edge.vertex_Second).add(edge.vertex_First);
            } else if (edge.type == DIRECTED_EDGE) {
                adjacencyList.get(edge.vertex_First).add(edge.vertex_Second);
            }
        }
    }

    public void makeIncidenceMatrix() {
        incidenceMatrix = new String[vertices.size() + 1][edges.size() + 1];
        setupIncidenceMatrix();

        for (Edge edge : edges) {
            if (edge.type == UNDIRECTED_EDGE) {
                insertUndirectedEdgesIntoIncidenceMatrix(edge);
            } else if (edge.type == DIRECTED_EDGE) {
                insertDirectedEdgesIntoIncidenceMatrix(edge);
            }
        }
    }

    public void makeAdjacencyMatrix() {
        adjacencyMatrix = new String[vertices.size() + 1][vertices.size() + 1];
        matrixSetup(adjacencyMatrix);
        for (Edge edge : edges) {
            if (edge.type == UNDIRECTED_EDGE) {
                insertUndirectedEdgesIntoAdjacencyMatrix(edge);
            } else if (edge.type == DIRECTED_EDGE) {
                insertDirectedEdgesIntoAdjacencyMatrix(edge);
            }
        }
    }

    public void makeRepresentationMatrix() {
        representationMatrix = new String[vertices.size() + 1][vertices.size() + 1];
        matrixSetup(representationMatrix);
        for (Edge edge : edges) {
            if (edge.type == UNDIRECTED_EDGE) {
                insertUndirectedEdgesIntoRepresentationMatrix(edge);
            } else if (edge.type == DIRECTED_EDGE) {
                insertDirectedEdgesIntoRepresentationMatrix(edge);
            }
        }
    }

    private void insertDirectedEdgesIntoIncidenceMatrix(Edge edge) {
        int index_first = vertices.indexOf(edge.vertex_First);
        int index_second = vertices.indexOf(edge.vertex_Second);
        index_first++;
        index_second++;
        int index_edge = edges.indexOf(edge);
        index_edge++;
        int currentValue = Integer.parseInt(incidenceMatrix[index_first][index_edge]);
        incidenceMatrix[index_first][index_edge] = "" + (currentValue + 1);
    }

    private void insertDirectedEdgesIntoAdjacencyMatrix(Edge edge) {
        int index_first = vertices.indexOf(edge.vertex_First);
        int index_second = vertices.indexOf(edge.vertex_Second);
        index_first++;
        index_second++;

        if (!adjacencyMatrix[index_first][index_second].equals("1"))
            incrementMatrixSlotValue(adjacencyMatrix, index_first, index_second);
    }

    private void insertDirectedEdgesIntoRepresentationMatrix(Edge edge) {
        int index_first = vertices.indexOf(edge.vertex_First);
        int index_second = vertices.indexOf(edge.vertex_Second);
        incrementMatrixSlotValue(representationMatrix, ++index_first, ++index_second);
    }

    private void insertUndirectedEdgesIntoIncidenceMatrix(Edge edge) {
        int index_first = vertices.indexOf(edge.vertex_First);
        int index_second = vertices.indexOf(edge.vertex_Second);
        index_first++;
        index_second++;
        int index_edge = edges.indexOf(edge);
        index_edge++;
        int currentValue = Integer.parseInt(incidenceMatrix[index_first][index_edge]);
        incidenceMatrix[index_first][index_edge] = "" + (currentValue + 1);
        currentValue = Integer.parseInt(incidenceMatrix[index_second][index_edge]);
        incidenceMatrix[index_second][index_edge] = "" + (currentValue + 1);
    }

    private void insertUndirectedEdgesIntoAdjacencyMatrix(Edge edge) {
        int index_first = vertices.indexOf(edge.vertex_First);
        int index_second = vertices.indexOf(edge.vertex_Second);
        index_first++;
        index_second++;

        if (!adjacencyMatrix[index_first][index_second].equals("1"))
            incrementMatrixSlotValue(adjacencyMatrix, index_first, index_second);
        if (!adjacencyMatrix[index_second][index_first].equals("1"))
            incrementMatrixSlotValue(adjacencyMatrix, index_second, index_first);
    }

    private void insertUndirectedEdgesIntoRepresentationMatrix(Edge edge) {
        int index_first = vertices.indexOf(edge.vertex_First);
        int index_second = vertices.indexOf(edge.vertex_Second);
        incrementMatrixSlotValue(representationMatrix, ++index_first, ++index_second);
        incrementMatrixSlotValue(representationMatrix, index_second, index_first);
    }

    private void incrementMatrixSlotValue(String[][] matrix, int firstIndex, int secondIndex) {
        int currentValue = Integer.parseInt(matrix[firstIndex][secondIndex]);
        matrix[firstIndex][secondIndex] = "" + (currentValue + 1);
    }

    public void matrixSetup(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {

            for (int j = 0; j < matrix.length; j++) {

                if (i == 0 && j == 0) {
                    matrix[i][j] = "__";
                } else if (i == 0) {
                    matrix[i][j] = vertices.get(j - 1).getSymbol();
                } else if (j == 0) {
                    matrix[i][j] = vertices.get(i - 1).getSymbol();
                } else {
                    matrix[i][j] = "" + 0;
                }
            }
        }
    }

    private void setupIncidenceMatrix() {
        for (int i = 0; i < incidenceMatrix.length; i++) {
            for (int j = 0; j < incidenceMatrix[0].length; j++) {
                if (i == 0 && j == 0) {
                    incidenceMatrix[i][j] = "__";
                } else if (i == 0) {
                    incidenceMatrix[i][j] = edges.get(j - 1).edgeName;
                } else if (j == 0) {
                    incidenceMatrix[i][j] = vertices.get(i - 1).getSymbol();
                } else {
                    incidenceMatrix[i][j] = "" + 0;
                }
            }
        }
    }

//-----------------------------------------------------------------

    /**
     * Euler's Path-Cycle - Task_2
     **/

    private boolean isSafe(Edge edge, Edge[] path, int pos) {
        boolean doesNotShareVertex = false;
        if (edge.vertex_First == path[pos - 1].vertex_First) {
            doesNotShareVertex = true;
        } else if (edge.vertex_First == path[pos - 1].vertex_Second) {
            doesNotShareVertex = true;
        } else if (edge.vertex_Second == path[pos - 1].vertex_First) {
            doesNotShareVertex = true;
        } else if (edge.vertex_Second == path[pos - 1].vertex_Second) {
            doesNotShareVertex = true;
        }
        if (!doesNotShareVertex)
            return doesNotShareVertex;
        for (int i = 0; i < pos; i++)
            if (path[i] == edge)
                return false;

        return true;
    }

    private boolean eulerCycle(Edge[] path, int pos) {
        if (pos == edges.size()) {

            boolean connected = false;
            if (path[0].vertex_First == path[pos - 1].vertex_First) {
                connected = true;
            } else if (path[0].vertex_First == path[pos - 1].vertex_Second) {
                connected = true;
            } else if (path[0].vertex_Second == path[pos - 1].vertex_First) {
                connected = true;
            } else if (path[0].vertex_Second == path[pos - 1].vertex_Second) {
                connected = true;
            }
            if (connected) {
                path[pos] = path[0];
                return true;
            } else {
                return false;
            }
        }


        for (Edge value : edges) {

            if (isSafe(value, path, pos)) {
                path[pos] = value;

                if (eulerCycle(path, pos + 1))
                    return true;
                path[pos] = null;
            }
        }
        return false;
    }

    private boolean eulerPath(Edge[] path, int pos) {
        if (pos == edges.size()) {
            return true;
        }


        for (Edge value : edges) {

            if (isSafe(value, path, pos)) {
                path[pos] = value;

                if (eulerPath(path, pos + 1))
                    return true;
                path[pos] = null;
            }
        }
        return false;
    }

    public boolean showEulerCycle(Edge start) {
        eulerCycle = null;
        eulerCycle = new Edge[edges.size() + 1];
        for (int i = 0; i < edges.size(); i++)
            eulerCycle[i] = null;

        eulerCycle[0] = start;
        return eulerCycle(eulerCycle, 1);
    }

    public boolean showEulerPath(Edge start) {
        eulerPath = null;
        eulerPath = new Edge[edges.size()];
        for (int i = 0; i < edges.size(); i++)
            eulerPath[i] = null;

        eulerPath[0] = start;
        return eulerPath(eulerPath, 1);
    }

//-----------------------------------------------------------------

    /**
     * Hamilton's Path-Cycle - Task_2
     **/

    public void Hamilton() {
        hamiltonsPath = new Vertex[edges.size()];
        hamiltonsCycle = new Vertex[edges.size()];
        adjacency = new int[vertices.size()][vertices.size()];
        for (int index = 0; index < adjacencyMatrix.length; index++) {

            for (int secondIndex = 0; secondIndex < adjacencyMatrix.length; secondIndex++) {
                if (index != 0 && secondIndex != 0) {
                    adjacency[index - 1][secondIndex - 1] = Integer.parseInt(adjacencyMatrix[index][secondIndex]);
                }
            }
        }
    }

    boolean isSafe(Vertex vertex, int[][] graph, Vertex[] path, int pos) {
        if (graph[vertices.indexOf(path[pos - 1])][vertices.indexOf(vertex)] == 0)
            return false;

        for (int i = 0; i < pos; i++)
            if (path[i] == vertex)
                return false;

        return true;
    }

    boolean hamCycleUtil(Vertex[] path, int pos) {
        if (pos == vertices.size()) {

            if (adjacency[vertices.indexOf(path[pos - 1])][vertices.indexOf(path[0])] == 1) {
                path[pos] = path[0];
                return true;
            } else
                return false;
        }

        for (Vertex value : vertices) {

            if (isSafe(value, adjacency, path, pos)) {
                path[pos] = value;

                if (hamCycleUtil(path, pos + 1))
                    return true;
                path[pos] = null;
            }
        }
        return false;
    }

    boolean hamPathUtil(Vertex[] path, int pos) {
        if (pos == vertices.size()) {
            return true;
        }

        for (int vertex = 1; vertex < vertices.size(); vertex++) {

            if (isSafe(vertices.get(vertex), adjacency, path, pos)) {
                path[pos] = vertices.get(vertex);

                if (hamPathUtil(path, pos + 1))
                    return true;
                path[pos] = null;
            }
        }
        return false;
    }

    boolean showHamiltonsCycle(Vertex start) {
        hamiltonsCycle = null;
        hamiltonsCycle = new Vertex[vertices.size() + 1];
        for (int i = 0; i < vertices.size(); i++)
            hamiltonsCycle[i] = null;

        hamiltonsCycle[0] = start;

        return hamCycleUtil(hamiltonsCycle, 1);
    }

    boolean showHamiltonsPath(Vertex start) {
        hamiltonsPath = null;
        hamiltonsPath = new Vertex[vertices.size()];
        for (int i = 0; i < vertices.size(); i++)
            hamiltonsPath[i] = null;

        hamiltonsPath[0] = start;
        return hamPathUtil(hamiltonsPath, 1);
    }

    //-----------------------------------------------------------------

    /**
     * Minimum Hamilton's Circuit - Task_3
     **/

    public Edge findLeastW(Vertex v) {
        int minw = Integer.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < edges.size(); i++) {
            //AB
            if (edges.get(i).vertex_First == v && !(V_Visited.contains(edges.get(i).vertex_Second))) {
                if (edges.get(i).weight < minw) {
                    minw = edges.get(i).weight;
                    index = i;
                }
            } else if (edges.get(i).vertex_First == v && edges.get(i).vertex_Second == V_Visited.get(0)) {
                if (V_Visited.size() == vertices.size())
                    index = i;
            } else if (edges.get(i).vertex_Second == v && (!V_Visited.contains(edges.get(i).vertex_First))) {
                if (edges.get(i).weight < minw) {
                    minw = edges.get(i).weight;
                    index = i;
                }
            } else if (edges.get(i).vertex_Second == v && edges.get(i).vertex_First == V_Visited.get(0)) {
                if (V_Visited.size() == vertices.size())
                    index = i;
            }

        }
        return edges.get(index);
    }

    public void tsp() {
        //initialization
        V_Visited.add(vertices.get(0));
        for (int i = 0; V_Visited.size() < vertices.size() + 1; i++) {
            Edge current = findLeastW(V_Visited.get(i));
            if (V_Visited.get(i) == current.vertex_First)
                V_Visited.add(current.vertex_Second);
            else if (V_Visited.get(i) == current.vertex_Second)
                V_Visited.add(current.vertex_First);
            if (V_Visited.get(i + 1) == V_Visited.get(0))
                break;
        }
    }
    //-----------------------------------------------------------------------

    /**
     * Graph Colouring - Task_4
     **/

    public void drawColoredGraph() {

        int color_numbers = 0;
        char[] colors = new char[vertices.size()];
        char label_color = 'A';

        for (int i = 0; i < vertices.size(); i++) {
            if (colors[i] == 0) {
                colors[i] = label_color;
                color_numbers++;
                label_color = (char) ((int) label_color + 1);
            }
            for (int j = 0; j < vertices.size(); j++) {
                if (colors[j] == 0 && adjacency[i][j] == 0) {
                    boolean adj = false;
                    for (int k = 0; k < vertices.size(); k++) {
                        if (colors[k] == colors[i]) {
                            if (adjacency[k][j] == 1) {
                                adj = true;
                                break;
                            }
                        }
                    }
                    if (!adj)
                        colors[j] = colors[i];
                }
            }
        }

        Pane root = new Pane();
        Stage stage = new Stage();
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Graph Coloring");
        System.out.println("Colors: " + Arrays.toString(colors));
        Color[] fills = new Color[color_numbers];
        Circle[] nodes = new Circle[vertices.size()];
        for (int i = 0; i < color_numbers; i++) {
            fills[i] = new Color(random(), random(), random(), 1);
        }
        for (int i = 0; i < vertices.size(); i++) {
            nodes[i] = new Circle();
            nodes[i].setCenterX(400 + 200 * Math.cos(2 * PI / vertices.size() * i));
            nodes[i].setCenterY(300 + 200 * Math.sin(2 * PI / vertices.size() * i));
            nodes[i].setRadius(20);
            nodes[i].setFill(fills[(int) (colors[i]) - 65]);
            nodes[i].setStroke(Color.BLACK);
            Label t = new Label("" + vertices.get(i).getSymbol());
            t.layoutXProperty().bind(nodes[i].centerXProperty().add(-5));
            t.layoutYProperty().bind(nodes[i].centerYProperty().add(-5));
            root.getChildren().addAll(nodes[i], t);
            int finalI = i;
            nodes[i].setOnMouseDragged(new EventHandler<MouseEvent>() {
                Circle c = nodes[finalI];

                @Override
                public void handle(MouseEvent event) {
                    c.setCenterX(event.getX());
                    c.setCenterY(event.getY());
                }
            });
        }
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if (adjacency[i][j] == 1) {
                    Line l = new Line();
                    l.startXProperty().bind(nodes[i].centerXProperty());
                    l.startYProperty().bind(nodes[i].centerYProperty());
                    l.endXProperty().bind(nodes[j].centerXProperty());
                    l.endYProperty().bind(nodes[j].centerYProperty());
                    root.getChildren().add(0, l);
                }
            }
        }
        stage.show();
    }

    //-----------------------------------------------------------------------

    /**
     * Minimum Spanning Tree - Task_5
     **/

    public void runKruskal() {
        PriorityQueue<Edge> pq = new PriorityQueue<>(edges.size(), Comparator.comparingInt(o -> o.weight));

        //add all the edges to priority queue, //sort the edges on weights
        for (Edge value : edges) {
            pq.add(value);
        }

        //create a parent []
        int[] parent = new int[vertices.size()];

        //initialize and assign each vertex as a parent of itself
        makeSet(parent);

        //process vertices - 1 edges
        int index = 0;

        while (index < vertices.size() - 1) {
            if (pq.isEmpty())
                break;
            Edge edge = pq.remove();
            //check if adding this edge creates a cycle
            int source = (vertices.indexOf(edge.vertex_First));
            int x_set = find(parent, source);

            int dest = (vertices.indexOf(edge.vertex_Second));
            int y_set = find(parent, dest);

            if (x_set == y_set) {
                //ignore, will create cycle
            } else {
                //add it to our final result
                mst.add(edge);
                index++;
                union(parent, x_set, y_set);
            }
        }

    }

    private void makeSet(int[] parent) {
        //Make set- creating a new element with a parent pointer to itself.
        for (int i = 0; i < vertices.size(); i++) {
            parent[i] = i;
        }
    }

    private int find(int[] parent, int vertex) {
        //chain of parent pointers from x upwards through the tree
        // until an element is reached whose parent is itself

        if (parent[vertex] != vertex)
            return find(parent, parent[vertex]);
        ;
        return vertex;
    }

    private void union(int[] parent, int x, int y) {
        int x_set_parent = find(parent, x);
        int y_set_parent = find(parent, y);
        //make x as parent of y
        parent[y_set_parent] = x_set_parent;
    }

}
