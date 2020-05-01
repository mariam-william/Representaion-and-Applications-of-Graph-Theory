package graphApplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static graphApplication.EdgeType.DIRECTED_EDGE;
import static graphApplication.EdgeType.UNDIRECTED_EDGE;

public class Controller implements Initializable {
    @FXML
    TextArea adjMatrixRes;
    @FXML
    TextArea incMatrixRes;
    @FXML
    TextArea adjListRes;
    @FXML
    TextArea represMatrixRes;
    @FXML
    TextArea eulerRes;
    @FXML
    TextArea hamiltonRes;
    @FXML
    TextArea minHamiltonRes;
    @FXML
    TextArea minSpanningRes;

    @FXML
    ComboBox graphType;

    @FXML
    Button addVertex;
    @FXML
    Button finishVertex;
    @FXML
    Button addEdge;
    @FXML
    Button finishEdge;
    @FXML
    Button restartProgram;

    @FXML
    TextField edgeFrom;
    @FXML
    TextField edgeTo;
    @FXML
    TextField edgeName;
    @FXML
    TextField edgeWeight;
    @FXML
    TextField vSymbol;

    @FXML
    TableView<Edge> dataTable;
    @FXML
    TableColumn<Edge, String> edgeNameCol = new TableColumn<>("Edge Name");
    @FXML
    TableColumn<Edge, String> edgeFromCol = new TableColumn<>("From");
    @FXML
    TableColumn<Edge, String> edgeToCol = new TableColumn<>("To");
    @FXML
    TableColumn<Edge, String> edgeWeightCol = new TableColumn<>("Weight");

    ObservableList<Edge> edgeData;

    public static String chosen;
    public static boolean flag;
    public static boolean hamilton;

    Graph G = new Graph();


    public Controller() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        edgeNameCol.setCellValueFactory(new PropertyValueFactory<>("edgeName"));
        edgeNameCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Edge, String>>() {
                    @Override
                    public void handle(CellEditEvent<Edge, String> event) {
                        ((Edge) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setEdgeName(event.getNewValue());
                    }
                }
        );
        edgeFromCol.setCellValueFactory(new PropertyValueFactory<>("vertex_First"));
        edgeToCol.setCellValueFactory(new PropertyValueFactory<>("vertex_Second"));
        edgeWeightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        fillTable();

        dataTable.setTooltip(new Tooltip("Displays entire graph's data you entered"));
        graphType.setTooltip(new Tooltip("You should specify graph's type if directed or undirected"));
        addVertex.setTooltip(new Tooltip("Press this button to add the vertex you entered"));
        finishVertex.setTooltip(new Tooltip("Press this button to finish adding vertices"));
        addEdge.setTooltip(new Tooltip("Press this button after filling Edge's data above"));
        finishEdge.setTooltip(new Tooltip("Press this button to finish adding edges and display results"));
        restartProgram.setTooltip(new Tooltip("To restart entire program and delete previous results"));
        edgeFrom.setTooltip(new Tooltip("Name of vertex where edge starts from"));
        edgeTo.setTooltip(new Tooltip("Name of vertex where edge ends at"));
        edgeName.setTooltip(new Tooltip("Name of the edge"));
        edgeWeight.setTooltip(new Tooltip("Enter weight of the edge"));
        vSymbol.setTooltip(new Tooltip("Here you enter the name of vertex"));
        adjMatrixRes.setTooltip(new Tooltip("Displays the Adjacency matrix for graph data you entered"));
        incMatrixRes.setTooltip(new Tooltip("Displays the Incidence matrix for graph data you entered"));
        adjListRes.setTooltip(new Tooltip("Displays the Adjacency List for graph data you entered"));
        represMatrixRes.setTooltip(new Tooltip("Displays the Representation matrix for graph data you entered"));
        eulerRes.setTooltip(new Tooltip("Displays Euler's path and Euler's circuit if found"));
        hamiltonRes.setTooltip(new Tooltip("Displays Hamilton's path and Hamilton's circuit if found"));
        minHamiltonRes.setTooltip(new Tooltip("Displays minimum Hamilton's circuit if found"));
        minSpanningRes.setTooltip(new Tooltip("Displays the Minimum Spanning Tree"));
    }

    public void fillTable() {
        edgeData = FXCollections.observableArrayList();
        edgeData.addAll(G.edges);
        dataTable.setItems(edgeData);
    }

    public void addVertex() {
        if (!vSymbol.getText().isEmpty()) {
            G.vertices.add(new Vertex(vSymbol.getText()));
            vSymbol.clear();
            finishVertex.setDisable(false);
        }
    }

    public void finishVertexButton() {
        edgeTo.setDisable(false);
        edgeFrom.setDisable(false);
        edgeName.setDisable(false);
        graphType.setDisable(false);
        edgeWeight.setDisable(false);
        vSymbol.setDisable(true);
        finishVertex.setDisable(true);
        addVertex.setDisable(true);
    }

    public void enableAddVertex() {
        if (!vSymbol.getText().isEmpty())
            addVertex.setDisable(false);
    }

    public void addEdge() {
        if (!flag) {
            chosen = graphType.getSelectionModel().getSelectedItem().toString();
            graphType.setDisable(true);
        }
        if (chosen.equals("Undirected"))
            flag = true;
        else
            flag = false;


        int idx = searchVertex(edgeFrom.getText());
        int idx2 = searchVertex(edgeTo.getText());
        int cost = Integer.parseInt(edgeWeight.getText());
        if (idx != -1 || idx2 != -1) {
            if (flag)
                G.edges.add(new Edge(G.vertices.get(idx), G.vertices.get(idx2), UNDIRECTED_EDGE, edgeName.getText(), cost));
            else
                G.edges.add(new Edge(G.vertices.get(idx), G.vertices.get(idx2), DIRECTED_EDGE, edgeName.getText(), cost));
        }
        // flag = false;
        resetEdge();
        finishEdge.setDisable(false);
    }

    public void enableAddEdge() {
        if (!edgeTo.getText().isEmpty() || !edgeFrom.getText().isEmpty() || !edgeName.getText().isEmpty() || !edgeWeight.getText().isEmpty() || !chosen.isEmpty())
            addEdge.setDisable(false);
    }

    public void edgeCalculation() throws Exception {
        G.makeRepresentationMatrix();
        G.makeAdjacencyList();
        G.makeIncidenceMatrix();
        G.makeAdjacencyMatrix();
        printAdjacencyList();
        printMatrix(adjMatrixRes, "Adjacency matrix:", G.adjacencyMatrix);
        printMatrix(incMatrixRes, "Incidence matrix:", G.incidenceMatrix);
        printMatrix(represMatrixRes, "Representation matrix:", G.representationMatrix);

        GraphDrawer pl = new GraphDrawer();
        StackPane root = new StackPane();
        root.setPadding(new Insets(20));
        Stage stage = new Stage();
        pl.pane = new Pane();
        root.getChildren().add(pl.pane);
        Scene sc = new Scene(root, 600, 600);
        stage.setScene(sc);
        stage.setTitle("Graph Representation");
        pl.start(stage);
    }

    public void resetEdge() {
        edgeFrom.clear();
        edgeTo.clear();
        edgeName.clear();
        edgeWeight.clear();
    }

    public int searchVertex(String vertex) {
        for (int i = 0; i < G.vertices.size(); i++) {
            if (G.vertices.get(i).getSymbol().equals(vertex))
                return i;
        }
        return -1;
    }

    public void printMatrix(TextArea area, String type, String[][] matrix) {
        area.setText(type + "\n");
        for (int i = 0; i < matrix.length; i++) {
            area.appendText("\n");
            for (int j = 0; j < matrix[0].length; j++) {
                area.appendText(matrix[i][j] + "\t");
            }
        }
    }

    public void printAdjacencyList() {
        adjListRes.setText("Adjacency List: \n");

        for (Map.Entry<Vertex, List<Vertex>> entry : G.adjacencyList.entrySet()) {
            adjListRes.appendText("Vertex: " + entry.getKey().getSymbol() + "\t");
            adjListRes.appendText(" List: ");
            for (Vertex vertex : entry.getValue()) {
                adjListRes.appendText(vertex.getSymbol() + "\t");
            }
            adjListRes.appendText("\n");
        }
    }

    public void eulersPath() {
        G.eulerPath = new Edge[G.edges.size()];
        G.eulerCycle = new Edge[G.edges.size()];
        eulerRes.setText("Euler path: ");
        if (G.showEulerPath(G.edges.get(0))) {
            for (int i = 0; i < G.eulerPath.length; i++) {
                eulerRes.appendText(G.eulerPath[i].toString());
                if (i != G.eulerPath.length - 1)
                    eulerRes.appendText(" -> ");
            }
        } else
            eulerRes.appendText("No euler path");

        eulerRes.appendText("\nEuler circuit: ");
        if (G.showEulerCycle(G.edges.get(0))) {
            for (int i = 0; i < G.eulerCycle.length; i++) {
                eulerRes.appendText(G.eulerCycle[i].toString());
                if (i != G.eulerCycle.length - 1)
                    eulerRes.appendText(" -> ");
            }
        } else
            eulerRes.appendText("No euler circuit");
    }

    public void hamiltonsPath() {
        G.Hamilton();
        hamiltonRes.setText("Hamilton's path: ");
        if (G.showHamiltonsPath(G.vertices.get(0))) {
            for (int i = 0; i < G.hamiltonsPath.length; i++) {
                hamiltonRes.appendText(G.hamiltonsPath[i].toString());
                if (i != G.hamiltonsPath.length - 1)
                    hamiltonRes.appendText(" -> ");
            }
        } else
            hamiltonRes.appendText("No hamilton path");

        hamiltonRes.appendText("\nHamilton's circuit: ");
        if (G.showHamiltonsCycle(G.vertices.get(0))) {
            for (int i = 0; i < G.hamiltonsCycle.length; i++) {
                hamiltonRes.appendText(G.hamiltonsCycle[i].toString());
                if (i != G.hamiltonsCycle.length - 1)
                    hamiltonRes.appendText(" -> ");
            }
            hamilton = true;
        } else {
            hamiltonRes.appendText("No hamilton's circuit");
            hamilton = false;
        }
    }

    public void minHamiltonsCircuit() {
        G.tsp();
        minHamiltonRes.setText("Min Hamilton circuit is ");
        for (int i = 0; i < G.V_Visited.size(); i++)
            minHamiltonRes.appendText(G.V_Visited.get(i).getSymbol() + " -> ");
        minHamiltonRes.appendText("end ");
    }

    public void minSpanningTree() {
        G.runKruskal();
        minSpanningRes.setText("Minimum Spanning Tree:\n");
        for (int i = 0; i < G.mst.size(); i++) {
            Edge edge = G.mst.get(i);
            minSpanningRes.appendText("Edge-" + (i + 1) + " source: " + edge.vertex_First.symbol +
                    " destination: " + edge.vertex_Second.symbol +
                    " weight: " + edge.weight + "\n");
        }
    }

    public void coloredGraph() {
        G.drawColoredGraph();
    }

    public void displayResults() {
        addEdge.setDisable(true);
        finishEdge.setDisable(true);
        edgeFrom.setDisable(true);
        edgeTo.setDisable(true);
        edgeName.setDisable(true);
        edgeWeight.setDisable(true);
        try {
            fillTable();
            dataTable.setEditable(true);
            restartProgram.setDisable(false);
            edgeCalculation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (chosen.equals("Undirected")) {
            eulersPath();
            hamiltonsPath();
            if (hamilton)
                minHamiltonsCircuit();
            else
                minHamiltonRes.setText("No minimum Hamilton Circuit");
            minSpanningTree();
            coloredGraph();
        }
    }

    public void restart() {
        flag = false;
        vSymbol.setDisable(false);
        restartProgram.setDisable(true);
        adjListRes.clear();
        adjMatrixRes.clear();
        incMatrixRes.clear();
        represMatrixRes.clear();
        eulerRes.clear();
        hamiltonRes.clear();
        minSpanningRes.clear();
        minHamiltonRes.clear();
        dataTable.getItems().clear();
        G.restartProgram();
        G = new Graph();

    }
}