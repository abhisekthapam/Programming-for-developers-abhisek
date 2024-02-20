/*
I have implemented a simple Edge class to represent graph edges, each having a source, 
destination, and weight which takes a list of edges and the number of vertices 
as parameters, sorts the edges in ascending order based on their weights using Collections.sort. 
It employs a Union-Find data structure (UnionFind class) to efficiently detect cycles. 
Iterating through the sorted edges, the method checks for cycle creation upon adding each edge to 
the result. If no cycle is detected, the edge is added to the result, and the Union-Find data 
structure is updated. The UnionFind class utilizes union by rank and path compression techniques, 
with the find method finding the representative of a vertex's set while compressing the path, and 
the union method combining sets based on ranks to maintain balance. The PriorityQueueMinHeap class 
represents a min-heap priority queue, offering methods to add an edge (add), remove the minimum-weight 
edge (removeMin), and check if the queue is empty (isEmpty). The minHeapify method ensures the min-heap 
property is maintained after removal. In the main method, a list of edges is created, and 
the number of vertices is set. The kruskalMST method is then employed to find the Minimum 
Spanning Tree, and the resulting edges are printed. Additionally, the priority queue implementation 
is tested by adding edges, removing minimum-weight edges, and printing the results.
Output : (Edges in the Minimum Spanning Tree:
2 - 3 : 4
0 - 3 : 5
0 - 1 : 10
Edges removed from Priority Queue:
2 - 3 : 4
0 - 3 : 5
0 - 2 : 6
0 - 1 : 10
1 - 3 : 15)
Time complexity : O(E * log(E))
 */

import java.util.*;

class Edge {
    int source, destination, weight;

    Edge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }
}

public class Question3b {

    // Kruskal's algorithm for finding the Minimum Spanning Tree
    public static List<Edge> kruskalMST(List<Edge> edges, int vertices) {
        List<Edge> result = new ArrayList<>();

        // Sorting the edges based on their weights in ascending order
        Collections.sort(edges, Comparator.comparingInt(e -> e.weight));

        // Creating a Union-Find data structure with union by rank and path compression
        UnionFind unionFind = new UnionFind(vertices);

        // Iterating through sorted edges
        for (Edge edge : edges) {
            int sourceRep = unionFind.find(edge.source);
            int destRep = unionFind.find(edge.destination);

            // Checking if adding this edge creates a cycle
            if (sourceRep != destRep) {
                result.add(edge);
                unionFind.union(sourceRep, destRep);
            }
        }

        return result;
    }

    // Union-Find data structure for efficient cycle detection with union by rank
    // and path compression
    static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];

            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        // Finding operation with path compression
        public int find(int vertex) {
            if (parent[vertex] != vertex) {
                parent[vertex] = find(parent[vertex]);
            }
            return parent[vertex];
        }

        // Union operation with union by rank
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else {
                    parent[rootX] = rootY;
                    rank[rootY]++;
                }
            }
        }
    }

    // Priority queue implementation using a min-heap
    static class PriorityQueueMinHeap {
        private final List<Edge> heap;

        PriorityQueueMinHeap() {
            heap = new ArrayList<>();
        }

        // Adding an edge to the priority queue
        void add(Edge edge) {
            heap.add(edge);
            int current = heap.size() - 1;

            // Restoring the min-heap property by swapping if necessary
            while (current > 0) {
                int parentIndex = (current - 1) / 2;

                if (heap.get(current).weight < heap.get(parentIndex).weight) {
                    Collections.swap(heap, current, parentIndex);
                    current = parentIndex;
                } else {
                    break;
                }
            }
        }

        // Removing the minimum-weight edge from the priority queue
        Edge removeMin() {
            if (heap.isEmpty()) {
                throw new NoSuchElementException("Priority queue is empty");
            }

            // Extracting the minimum-weight edge
            Edge min = heap.get(0);
            heap.set(0, heap.get(heap.size() - 1));
            heap.remove(heap.size() - 1);

            // Restoring the min-heap property after removal
            minHeapify(0);

            return min;
        }

        // Helper method for maintaining the min-heap property
        private void minHeapify(int index) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < heap.size() && heap.get(left).weight < heap.get(smallest).weight) {
                smallest = left;
            }
            if (right < heap.size() && heap.get(right).weight < heap.get(smallest).weight) {
                smallest = right;
            }
            if (smallest != index) {
                Collections.swap(heap, index, smallest);
                minHeapify(smallest);
            }
        }

        // Checking if the priority queue is empty
        boolean isEmpty() {
            return heap.isEmpty();
        }
    }

    // Main method for testing the algorithm and priority queue
    public static void main(String[] args) {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 10));
        edges.add(new Edge(0, 2, 6));
        edges.add(new Edge(0, 3, 5));
        edges.add(new Edge(1, 3, 15));
        edges.add(new Edge(2, 3, 4));

        int vertices = 4;

        // Finding the Minimum Spanning Tree using Kruskal's algorithm
        List<Edge> minimumSpanningTree = kruskalMST(edges, vertices);
        System.out.println("Edges in the Minimum Spanning Tree:");
        for (Edge edge : minimumSpanningTree) {
            System.out.println(edge.source + " - " + edge.destination + " : " + edge.weight);
        }

        // Testing the priority queue implementation
        PriorityQueueMinHeap priorityQueue = new PriorityQueueMinHeap();
        priorityQueue.add(new Edge(0, 1, 10));
        priorityQueue.add(new Edge(0, 2, 6));
        priorityQueue.add(new Edge(0, 3, 5));
        priorityQueue.add(new Edge(1, 3, 15));
        priorityQueue.add(new Edge(2, 3, 4));
        System.out.println("Edges removed from Priority Queue:");
        while (!priorityQueue.isEmpty()) {
            Edge minEdge = priorityQueue.removeMin();
            System.out.println(minEdge.source + " - " + minEdge.destination + " : " + minEdge.weight);
        }
    }
}
