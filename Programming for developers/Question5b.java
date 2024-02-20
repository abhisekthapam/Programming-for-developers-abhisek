
/*
 I've implemented a network analysis algorithm to identify devices impacted by a failure between 
 a target device and a power source. The Question5b class represents a network graph using 
 an adjacency matrix. The program initializes the graph, adds edges to represent connections 
 between devices, and prints the network. The findImpactedDevices method identifies devices 
 impacted by a failure, utilizing DFS traversal. It starts from the target device, marking connected 
 devices to the target, and then performs DFS from the power source, checking for connectivity. 
 The result is a list of devices impacted by the failure. In the main function, I instantiate 
 the class, create a network graph, and find the impacted devices in the case of a failure between 
 a specified target and power source. The result is then printed to the console.
 Output : 
0 1 1 0 0 0 0 0 
1 0 0 1 0 0 1 0 
1 0 0 0 1 0 0 0 
0 1 0 0 0 0 0 0 
0 0 1 0 0 1 1 0
0 0 0 0 1 0 0 1
0 1 0 0 1 0 0 0
0 0 0 0 0 1 0 0
Impacted Device List: [7, 5]
Time complexity : O(V^2)
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Question5b {
    int v;
    // Adjacency matrix for representing the network graph.
    int network[][];

    Question5b(int v) {
        this.v = v;
        // Initializing the adjacency matrix with size v x v.
        network = new int[v][v];
    }

    // Method for adding an edge between two vertices in the graph.
    public void addEdge(int source, int destination) {
        network[source][destination] = 1;
        network[destination][source] = 1;
    }

    // Method for printing the network graph represented by the adjacency matrix.
    public void printNetwork() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < network.length; i++) {
            for (int j = 0; j < network[i].length; j++) {
                sb.append(network[i][j]).append(" ");
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
    }

    // Method for getting the set of vertices connected to a given vertex in the
    // graph.
    Set<Integer> getConnectedDevices(int root) {
        // Initializing a HashSet to store connected vertices.
        Set<Integer> connectedDevices = new HashSet<>();
        for (int j = 0; j < v; j++) {
            if (network[root][j] != 0 || network[j][root] != 0) {
                connectedDevices.add(j);
            }
        }
        return connectedDevices;
    }

    // Method for finding the devices impacted by a failure between a target device
    // and a power source.
    public List<Integer> findImpactedDevices(int targetDevice, int source) {
        Set<Integer> connectedDevicesToTarget = getConnectedDevices(targetDevice);
        Set<Integer> connectedDevicesToSource = getConnectedDevices(source);
        List<Integer> impactedDevices = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        visited.add(source);
        visited.addAll(connectedDevicesToSource);

        for (int device : connectedDevicesToTarget) {
            if (!visited.contains(device)) {
                dfsCheckSource(device, visited, impactedDevices, source, targetDevice);
            }
        }
        return impactedDevices;
    }

    // Helper method for performing DFS traversal to find impacted devices.
    private void dfsCheckSource(int node, Set<Integer> visited, List<Integer> impactedDevices, int source,
            int targetDevice) {
        visited.add(node);

        if (node == targetDevice) {
            return;
        }

        boolean connectedToSource = false;

        for (int i = 0; i < v; i++) {
            if ((network[node][i] != 0 || network[i][node] != 0) && !visited.contains(i)) {
                dfsCheckSource(i, visited, impactedDevices, source, targetDevice);
                if (isConnectedToSource(i, source, visited)) {
                    connectedToSource = true;
                }
            }
        }

        if (!connectedToSource) {
            impactedDevices.add(node);
        }
    }

    // Helper method for checking if a node is connected to the source device.
    private boolean isConnectedToSource(int node, int source, Set<Integer> visited) {
        visited.add(node);

        if (node == source) {
            return true;
        }

        for (int i = 0; i < v; i++) {
            if ((network[node][i] != 0 || network[i][node] != 0) && !visited.contains(i)) {
                if (isConnectedToSource(i, source, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int target = 4;
        int powerSource = 0;

        Question5b isp = new Question5b(8);

        isp.addEdge(0, 1);
        isp.addEdge(0, 2);
        isp.addEdge(1, 3);
        isp.addEdge(1, 6);
        isp.addEdge(2, 4);
        isp.addEdge(4, 6);
        isp.addEdge(4, 5);
        isp.addEdge(5, 7);

        isp.printNetwork();

        // Finding impacted devices in case of failure between target and power source
        // devices.
        List<Integer> impDevice = isp.findImpactedDevices(target, powerSource);

        System.out.println("Impacted Device List: " + impDevice);
    }
}
