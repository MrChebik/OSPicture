package ru.mrchebik.structure.graph;


import ru.mrchebik.structure.queue.Queue;

import java.lang.reflect.Array;

/**
 * Created by mrchebik on 14.05.17.
 */
public class Graph<T> {
    private final int MAX_VERTS = 20;
    private Vertex<T> vertexList[];
    private int adjMat[][];
    private int nVerts;

    private Queue theQueue;

    @SuppressWarnings("unchecked")
    public Graph() {
        vertexList = (Vertex<T>[]) Array.newInstance(Vertex.class, MAX_VERTS);

        adjMat = new int[MAX_VERTS][MAX_VERTS];
        nVerts = 0;
        for (int j = 0; j < MAX_VERTS; j++) {
            for (int k = 0; k < MAX_VERTS; k++) {
                adjMat[j][k] = 0;
            }
        }

        theQueue = new Queue(MAX_VERTS);
    }

    public void addVertex(T label) {
        vertexList[nVerts++] = new Vertex<T>(label);
    }

    public void addEdge(int start, int end) {
        adjMat[start][end] = 1;
        adjMat[end][start] = 1;
    }

    public void displayVertex(int v) {
        System.out.println(vertexList[v].label);
    }

    public void bfs() {
        vertexList[0].wasVisited = true;
        displayVertex(0);
        theQueue.insert(0);
        int v2;

        while (!theQueue.isEmpty()) {
            int v1 = (int) theQueue.remove();

            while ((v2 = getAdjUnvisitedVertex(v1)) != -1) {
                vertexList[v2].wasVisited = true;
                displayVertex(v2);
                theQueue.insert(v2);
            }
        }

        for (int j = 0; j < nVerts; j++) {
            vertexList[j].wasVisited = false;
        }
    }

    protected int getAdjUnvisitedVertex(int v) {
        for (int j = 0; j < nVerts; j++) {
            if (adjMat[v][j] == 1 && !vertexList[j].wasVisited) {
                return j;
            }
        }
        return -1;
    }
}
