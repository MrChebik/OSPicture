package ru.mrchebik.structure.graph;

/**
 * Created by mrchebik on 14.05.17.
 */
class Vertex<T> {
    T label;
    boolean wasVisited;

    Vertex(T label) {
        this.label = label;
        wasVisited = false;
    }
}
