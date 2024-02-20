package application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Creates graph that manages user relationships to one another and 
 * also provides a way to determine closest distance between friends
 * as well as how many groups of friends exist
 * 
 * @author Team 70
 *
 */
public class Graph implements GraphADT {

  // Fields that hold information about the graph
  private ArrayList<User> allNodes = new ArrayList<User>();
  private int totalEdges = 0;
  private int totalVerticies = 0;

  /*
   * Default no-argument constructor
   */
  public Graph() {}

  /**
   * Method that could be used, but was decided not to be in favor of testing through the GraphTest
   * class
   *
   * @param args
   */
  public static void main(String[] args) {}

  /**
   * Add new vertex to the graph.
   * <p>
   * If vertex is null or already exists, method ends without adding a vertex or throwing an
   * exception.
   * <p>
   * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in the graph
   *
   * @param vertex the vertex to be added
   */
  public void addVertex(String vertex) {
    // Ends method if vertex does not have a value
    if (vertex == null) {
      return;
    }

    // Loop through each vertex to see if it already exists
    // If vertex exists, then don't add it
    for (int i = 0; i < allNodes.size(); i++) {
      if (allNodes.get(i).getName().equals(vertex)) {
        return;
      }
    }

    // Create a new vertex and add it to the graph
    User newVertex = new User(vertex);
    allNodes.add(newVertex);
    totalVerticies += 1;
  }

  /**
   * Remove a vertex and all associated edges from the graph.
   * <p>
   * If vertex is null or does not exist, method ends without removing a vertex, edges, or throwing
   * an exception.
   * <p>
   * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in the graph
   *
   * @param vertex the vertex to be added
   */
  public void removeVertex(String vertex) {
    // Cancel method if the string is null
    if (vertex == null) {
      return;
    }
    // Remove Vertex
    // Loops through until vertex if found and removes it
    for (int i = 0; i < allNodes.size(); i++) {
      if (allNodes.get(i).getName().equals(vertex)) {
        totalEdges -= allNodes.get(i).getFriends().size();
        allNodes.remove(i);
        totalVerticies -= 1;
      }
    }

    // Remove Edge
    // Loops through until a nod's successor equals the vertex of interest
    // if found and remove the vertex from the successor list
    for (int i = 0; i < allNodes.size(); i++) {
      for (int j = 0; j < allNodes.get(i).getFriends().size(); j++) {
        if (allNodes.get(i).getFriends().get(j).getName().equals(vertex)) {
          allNodes.get(i).getFriends().remove(j);
        }
      }
    }

  }

  /**
   * Add the edge from vertex1 to vertex2 to this graph. (edge is undirected and unweighted) If
   * either vertex does not exist, add vertex, and add edge, no exception is thrown. If the edge
   * exists in the graph, no edge is added and no exception is thrown.
   * <p>
   * Valid argument conditions: 1. neither vertex is null 2. both vertices are in the graph 3. the
   * edge is not in the graph
   *
   * @param vertex1 first vertex
   * @param vertex2 successor vertex
   */
  public void addEdge(String vertex1, String vertex2) {
    // End method if a string is null
    if (vertex1 == null || vertex2 == null) {
      return;
    }

    // Create some variables to indicate if a node was found and where
    boolean node1 = false;
    int node1Index = 0;
    boolean node2 = false;
    int node2Index = 0;

    // Loop until both nodes are found
    while (!node1 || !node2) {

      // Loop through all Nodes
      for (int i = 0; i < allNodes.size(); i++) {
        // Check if equal to first Vertex
        if (allNodes.get(i).getName().equals(vertex1)) {
          node1 = true;
          node1Index = i;

          // See if edge exists by looping through succesors and if any of them
          // are vertex 2
          for (int j = 0; j < allNodes.get(i).getFriends().size(); j++) {
            if (allNodes.get(i).getFriends().get(j).getName().equals(vertex2)) {
              return;
            }
          }
        }
        // Check if second vertex exists
        if (allNodes.get(i).getName().equals(vertex2)) {
          node2 = true;
          node2Index = i;
        }
      }

      // If either vertex did not exist, then add it to the graph.
      if (!node1) {
        this.addVertex(vertex1);

      }
      if (!node2) {
        this.addVertex(vertex2);
      }
    }

    // Add the edge to the graph
    allNodes.get(node1Index).getFriends().add(allNodes.get(node2Index));
    allNodes.get(node2Index).getFriends().add(allNodes.get(node1Index));
    totalEdges += 1;

  }

  /**
   * either vertex does not exist, or if an edge from vertex1 to vertex2 does not exist, no edge is
   * removed and no exception is thrown.
   * <p>
   * Valid argument conditions: 1. neither vertex is null 2. both vertices are in the graph 3. the
   * edge from vertex1 to vertex2 is in the graph
   *
   * @param vertex1 first vertex
   * @param vertex2 successor vertex
   */
  public void removeEdge(String vertex1, String vertex2) {
    // If any vertex does not exist, end the method
    if (vertex1 == null || vertex2 == null) {
      return;
    }

    // Loops through until a node's neighbor equals the vertex of interest
    // if found, remove the vertex from the neighbor's list
    for (int i = 0; i < allNodes.size(); i++) {
      // compares for vertex 1 to vertex 2
      if (allNodes.get(i).getName().equals(vertex1)) {
        for (int j = 0; j < allNodes.get(i).getFriends().size(); j++) {
          if (allNodes.get(i).getFriends().get(j).getName().equals(vertex2)) {
            allNodes.get(i).getFriends().remove(j);
            totalEdges -= 1;
          }
        }
      }
      // compares vertex 2 to vertex 1
      if (allNodes.get(i).getName().equals(vertex2)) {
        for (int j = 0; j < allNodes.get(i).getFriends().size(); j++) {
          if (allNodes.get(i).getFriends().get(j).getName().equals(vertex1)) {
            allNodes.get(i).getFriends().remove(j);
            totalEdges -= 1;
          }
        }
      }
    }
  }

  /**
   * Returns a List that contains all the vertices
   */
  public List<String> getAllVertices() {
    // Create List and then add all nodes the that before returning it
    List<String> allVertices = new ArrayList<String>();

    for (int i = 0; i < allNodes.size(); i++) {
      allVertices.add(allNodes.get(i).getName());
    }

    return allVertices;
  }

  /**
   * Get all the neighbor (adjacent) vertices of a vertex
   *
   * @param vertex current vertex
   */
  public List<String> getAdjacentVerticesOf(String vertex) {
    // Make new list to hold all new strings
    ArrayList<String> stringList = new ArrayList<String>();

    // loops through all vertexes in graph
    for (int i = 0; i < allNodes.size(); i++) {

      // Find vertex that equals vertex of interest
      if (allNodes.get(i).getName().equals(vertex)) {

        // Loop through all edges that the vertex of interest in pointing to
        for (int j = 0; j < allNodes.get(i).getFriends().size(); j++) {
          stringList.add(allNodes.get(i).getFriends().get(j).getName());
        }
      }
    }

    return stringList;
  }

  /**
   * Returns the number of edges in this graph.
   */
  public int size() {
    return totalEdges;
  }

  /**
   * Returns the number of vertices in this graph.
   */
  public int order() {
    return totalVerticies;
  }

  /**
   * Returns the corresponding node
   * 
   * @param name name of node
   * @return node corresponding to string
   */
  public User getNode(String name) {
    for (User u : allNodes) {
      if (name.equals(u.getName())) {
        return u;
      }
    }

    return null;
  }

  /**
   * Finds the Shortest Path between two users
   * 
   * @param user1
   * @param user2
   * @return List of Users
   */
  public LinkedList<String> shortestPath(User user1, User user2) {
	// Holds the shortest path
    LinkedList<String> path = new LinkedList<>();

    // Initiate variables that keep track of visited, found, and predecessors
    LinkedList<User> queue = new LinkedList<>();
    boolean[] visited = new boolean[allNodes.size()];
    User[] pred = new User[allNodes.size()];
    boolean found = false;

    // Set this node to visited and add to queue
    visited[allNodes.indexOf(user1)] = true;
    queue.add(0, user1);

    // Loop through until the queue is empty
    while (!queue.isEmpty()) {
      User curr = queue.remove(queue.size() - 1);

      // loop through each friend of the current user in queue
      for (User friend : curr.getFriends()) {

    	// Check if they are visited
        if (!visited[allNodes.indexOf(friend)]) {
          // If they are not, then update that they have and 
          // add them to the queue
          visited[allNodes.indexOf(friend)] = true;
          queue.add(0, friend);
          pred[allNodes.indexOf(friend)] = curr;

          // If user2 is found, then break
          if (friend.equals(user2)) {
            found = true;
            break;
          }
        }
      }

      if (found) {
        break;
      }
    }

    // If they are found, then loop until path is created
    if (found) {
      User crawl = user2;
      while (!crawl.equals(user1)) {
        path.add(0, crawl.getName());
        crawl = pred[allNodes.indexOf(crawl)];
      }
      path.add(0, user1.getName());
    }

    return path;
  }

  /**
   * Finds how Groups exist
   * 
   * @return the number of groups in the graph
   */
  public int getConnectedComponents() {
	// Variables that keep track of visited and number of groups
    int connectedComponents = 0;
    boolean[] visited = new boolean[allNodes.size()];

    // Loop through each individual in the graph
    for (int i = 0; i < allNodes.size(); i++) {
      // Perform depth first search on each unvisited user as this
      // will mark entire group as visited and one group
      if (!visited[i]) {
        depthFirstSearch(allNodes.get(i), visited);
        connectedComponents++;
      }
    }

    return connectedComponents;
  }

  /**
   * Searches through the graph 
   * @param user current User
   * @param visited Visited users
   */
  private void depthFirstSearch(User user, boolean[] visited) {
    visited[allNodes.indexOf(user)] = true;

    // Search through each friend and mark as visited before recursively
    // visiting all of these friend's friends while marking each as visited
    for (User friend : user.getFriends()) {
      if (!visited[allNodes.indexOf(friend)]) {
        depthFirstSearch(friend, visited);
      }
    }

  }

}


