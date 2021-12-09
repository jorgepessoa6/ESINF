/*
* A collection of graph algorithms.
 */
package graphbase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author DEI-ESINF
 */
public class GraphAlgorithms {

    /**
     * Performs breadth-first search of a Graph starting in a Vertex
     *
     * @param g Graph instance
     * @param vInf information of the Vertex that will be the source of the
     * search
     * @return qbfs a queue with the vertices of breadth-first search
     */
    public static <V, E> LinkedList<V> BreadthFirstSearch(Graph<V, E> g, V vert) {

        if (!g.validVertex(vert)) {
            return null;
        }

        LinkedList<V> qbfs = new LinkedList<>();
        ArrayList<V> qaux = new ArrayList<>();
        boolean visited[] = new boolean[g.numVertices()];
        qbfs.add(vert);
        qaux.add(vert);
        visited[g.getKey(vert)] = true;
        while (!qaux.isEmpty()) {
            V vInf = qaux.remove(0);
            for (V vAdj : g.adjVertices(vInf)) {
                if (!visited[g.getKey(vAdj)]) {
                    qbfs.add(vAdj);
                    qaux.add(vAdj);
                    visited[g.getKey(vAdj)] = true;
                }
            }

        }
        return qbfs;
    }

    /**
     * Performs depth-first search starting in a Vertex
     *
     * @param g Graph instance
     * @param vOrig Vertex of graph g that will be the source of the search
     * @param visited set of discovered vertices
     * @param qdfs queue with vertices of depth-first search
     */
    private static <V, E> void DepthFirstSearch(Graph<V, E> g, V vOrig, boolean[] visited, LinkedList<V> qdfs) {
        if (g.validVertex(vOrig)) {
            qdfs.add(vOrig);
            visited[g.getKey(vOrig)] = true;
            for (V vAdj : g.adjVertices(vOrig)) {
                if (!visited[g.getKey(vAdj)]) {
                    DepthFirstSearch(g, vAdj, visited, qdfs);
                }
            }
        }
    }

    /**
     * @param g Graph instance
     * @param vInf information of the Vertex that will be the source of the
     * search
     * @return qdfs a queue with the vertices of depth-first search
     */
    public static <V, E> LinkedList<V> DepthFirstSearch(Graph<V, E> g, V vert) {

        if (!g.validVertex(vert)) {
            return null;
        }
        LinkedList<V> qdfs = new LinkedList<>();
        boolean visited[] = new boolean[g.numVertices()];
        DepthFirstSearch(g, vert, visited, qdfs);

        return qdfs;
    }

    /**
     * Returns all paths from vOrig to vDest
     *
     * @param g Graph instance
     * @param vOrig Vertex that will be the source of the path
     * @param vDest Vertex that will be the end of the path
     * @param visited set of discovered vertices
     * @param path stack with vertices of the current path (the path is in
     * reverse order)
     * @param paths ArrayList with all the paths (in correct order)
     */
    private static <V, E> void allPaths(Graph<V, E> g, V vOrig, V vDest, boolean[] visited,
            LinkedList<V> path, ArrayList<LinkedList<V>> paths) {

        path.push(vOrig);

        int vKey = g.getKey(vOrig);

        visited[vKey] = true;

        for (V vAdj : g.adjVertices(vOrig)) {

            if (vAdj.equals(vDest)) {

                path.push(vAdj);

                LinkedList<V> revpath = revPath(path);

                paths.add(new LinkedList(revpath));  //save clone of reverse path

                path.pop();

            } else {

                vKey = g.getKey(vAdj);

                if (!visited[vKey]) {
                    allPaths(g, vAdj, vDest, visited, path, paths);
                }

            }

        } //Uncheck visited nodes

        V vElem = path.pop();

        vKey = g.getKey(vElem);

        visited[vKey] = false;
    }

    /**
     * @param g Graph instance
     * @param voInf information of the Vertex origin
     * @param vdInf information of the Vertex destination
     * @return paths ArrayList with all paths from voInf to vdInf
     */
    public static <V, E> ArrayList<LinkedList<V>> allPaths(Graph<V, E> g, V vOrig, V vDest) {
        LinkedList<V> path = new LinkedList<>();

        ArrayList<LinkedList<V>> paths = new ArrayList<>();

        boolean[] visited = new boolean[g.numVertices()];

        if (g.validVertex(vOrig) && g.validVertex(vDest)) {
            allPaths(g, vOrig, vDest, visited, path, paths);
        }

        return paths;
    }

    /**
     * Computes shortest-path distance from a source vertex to all reachable
     * vertices of a graph g with nonnegative edge weights This implementation
     * uses Dijkstra's algorithm
     *
     * @param g Graph instance
     * @param vOrig Vertex that will be the source of the path
     * @param visited set of discovered vertices
     * @param pathKeys minimum path vertices keys
     * @param dist minimum distances
     */
    protected static <V, E> void shortestPathLength(Graph<V, E> g, V vOrig, V[] vertices,
            boolean[] visited, int[] pathKeys, double[] dist) {
        int vkey = g.getKey(vOrig);

        dist[vkey] = 0;

        while (vkey != -1) {

            vOrig = vertices[vkey];

            visited[vkey] = true;

            for (V vAdj : g.adjVertices(vOrig)) {

                int vkeyAdj = g.getKey(vAdj);

                Edge<V, E> edge = g.getEdge(vOrig, vAdj);

                if (!visited[vkeyAdj] && dist[vkeyAdj] > dist[vkey] + edge.getWeight()) {

                    dist[vkeyAdj] = dist[vkey] + edge.getWeight();

                    pathKeys[vkeyAdj] = vkey;

                }
            }

            double minDist = Double.MAX_VALUE;

            vkey = -1;

            for (int i = 0; i < g.numVertices(); i++) {

                if (!visited[i] && dist[i] < minDist) {

                    minDist = dist[i];

                    vkey = i;
                }
            }
        }
    }

    /**
     * Extracts from pathKeys the minimum path between voInf and vdInf The path
     * is constructed from the end to the beginning
     *
     * @param g Graph instance
     * @param voInf information of the Vertex origin
     * @param vdInf information of the Vertex destination
     * @param pathkeys minimum path vertices keys
     * @param path stack with the minimum path (correct order)
     */
    private static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest, V[] verts, int[] pathKeys, LinkedList<V> path) {

        if (!vOrig.equals(vDest)) {

            path.push(vDest);

            int vKey = g.getKey(vDest);

            int prevVKey = pathKeys[vKey];

            vDest = verts[prevVKey];

            getPath(g, vOrig, vDest, verts, pathKeys, path);

        } else {
            path.push(vOrig);
        }
    }

    //shortest-path between vOrig and vDest
    public static <V, E> double shortestPath(Graph<V, E> g, V vOrig, V vDest, LinkedList<V> shortPath) {

        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return 0;
        }

        int nverts = g.numVertices();

        boolean[] visited = new boolean[nverts];

        int[] pathKeys = new int[nverts];

        double[] dist = new double[nverts];

        V[] vertices = g.allkeyVerts();

        for (int i = 0; i < nverts; i++) {

            dist[i] = Double.MAX_VALUE;

            pathKeys[i] = -1;

        }

        shortestPathLength(g, vOrig, vertices, visited, pathKeys, dist);

        double lengthPath = dist[g.getKey(vDest)];

        if (lengthPath != Double.MAX_VALUE) {

            getPath(g, vOrig, vDest, vertices, pathKeys, shortPath);

            return lengthPath;

        }

        return 0;

    }

    //shortest-path between voInf and all other
    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig, ArrayList<LinkedList<V>> paths, ArrayList<Double> dists) {

        if (!g.validVertex(vOrig)) {
            return false;
        }

        int nverts = g.numVertices();
        boolean[] visited = new boolean[nverts]; //default value: false
        int[] pathKeys = new int[nverts];
        double[] dist = new double[nverts];
        V[] vertices = g.allkeyVerts();

        for (int i = 0; i < nverts; i++) {
            dist[i] = Double.MAX_VALUE;
            pathKeys[i] = -1;
        }

        shortestPathLength(g, vOrig, vertices, visited, pathKeys, dist);

        dists.clear();
        paths.clear();
        for (int i = 0; i < nverts; i++) {
            paths.add(null);
            dists.add(null);
        }
        for (int i = 0; i < nverts; i++) {
            LinkedList<V> shortPath = new LinkedList<>();
            if (dist[i] != Double.MAX_VALUE) {
                getPath(g, vOrig, vertices[i], vertices, pathKeys, shortPath);
            }
            paths.set(i, shortPath);
            dists.set(i, dist[i]);
        }
        return true;
    }

    /**
     * Reverses the path
     *
     * @param path stack with path
     */
    private static <V, E> LinkedList<V> revPath(LinkedList<V> path) {

        LinkedList<V> pathcopy = new LinkedList<>(path);
        LinkedList<V> pathrev = new LinkedList<>();

        while (!pathcopy.isEmpty()) {
            pathrev.push(pathcopy.pop());
        }

        return pathrev;
    }

    /**
     * Performs depth-first search of the graph starting at vertex. Calls
     * package recursive version of the method.
     *
     * @param graph Graph object
     * @param vertex Vertex of graph that will be the source of the search
     * @return queue of vertices found by search (including vertex), null if
     * vertex does not exist
     *
     */
    public static <V, E> LinkedList<V> BFS(AdjacencyMatrixGraph<V, E> graph, V vertex) {

        int index = graph.toIndex(vertex);
        if (index == -1) {
            return null;
        }

        LinkedList<V> resultQueue = new LinkedList<V>();
        LinkedList<Integer> auxQueue = new LinkedList<Integer>();

        resultQueue.add(graph.vertices.get(index));
        auxQueue.add(index);

        while (!auxQueue.isEmpty()) {
            index = auxQueue.remove();
            for (int i = 0; i < graph.numVertices; i++) {
                if (graph.edgeMatrix[index][i] != null) {
                    if (!resultQueue.contains(graph.vertices.get(i))) {
                        resultQueue.add(graph.vertices.get(i));
                        auxQueue.add(i);
                    }
                }
            }
        }
        return resultQueue;
    }

    /**
     * Performs depth-first search of the graph starting at vertex. Calls
     * package recursive version of the method.
     *
     * @param graph Graph object
     * @param vertex Vertex of graph that will be the source of the search
     * @return queue of vertices found by search (empty if none), null if vertex
     * does not exist
     */
    public static <V, E> LinkedList<V> DFS(AdjacencyMatrixGraph<V, E> graph, V vertex) {
        if (!graph.checkVertex(vertex)) {
            return null;
        }

        int index = graph.toIndex(vertex);

        boolean[] knownVertices = new boolean[graph.numVertices];
        Arrays.fill(knownVertices, Boolean.FALSE);

        // Arrange linked list and add first vertex
        LinkedList<V> verticesQueue = new LinkedList<>();

        // Start recursion
        DFS(graph, index, knownVertices, verticesQueue);

        System.out.println(verticesQueue);

        return verticesQueue;
    }

    /**
     * Actual depth-first search of the graph starting at vertex. The method
     * adds discovered vertices (including vertex) to the queue of vertices
     *
     * @param graph Graph object
     * @param index Index of vertex of graph that will be the source of the
     * search
     * @param known previously discovered vertices
     * @param verticesQueue queue of vertices found by search
     *
     */
    static <V, E> void DFS(AdjacencyMatrixGraph<V, E> graph, int index, boolean[] knownVertices, LinkedList<V> verticesQueue) {
        V currentVertice = graph.vertices.get(index);

        knownVertices[index] = true;
        verticesQueue.add(currentVertice);
        for (V connectedVertice : graph.directConnections(currentVertice)) {
            int verticeIndex = graph.toIndex(connectedVertice);
            if (!knownVertices[verticeIndex]) {
                DFS(graph, verticeIndex, knownVertices, verticesQueue);
            }
        }
    }

    /**
     * Transforms a graph into its transitive closure uses the Floyd-Warshall
     * algorithm
     *
     * @param graph Graph object
     * @param dummyEdge object to insert in the newly created edges
     * @return the new graph
     */
    public static <V, E> AdjacencyMatrixGraph<V, E> transitiveClosure(AdjacencyMatrixGraph<V, E> graph, E dummyEdge) {
        AdjacencyMatrixGraph<V, E> clone = (AdjacencyMatrixGraph<V, E>) graph.clone();

        for (int k = 0; k < clone.numVertices; k++) {
            for (int i = 0; i < clone.numVertices; i++) {
                if (i != k && clone.edgeMatrix[i][k] != null) {
                    for (int j = 0; j < clone.numVertices; j++) {
                        if (i != j && k != j && clone.edgeMatrix[k][j] != null) {
                            clone.edgeMatrix[i][j] = dummyEdge;
                        }
                    }
                }
            }
        }
        return clone;
    }
}
