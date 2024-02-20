import java.io.*;
import java.util.*;

// Class representing a vertex/node in the graph
class Vertex {
    int id;
    String content;
    String author;
    int likes;
    String genre;
    List<Vertex> replies;

    Vertex(int id, String content, String author, String genre) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.likes = 0;
        this.genre = genre;
        this.replies = new ArrayList<>();
    }

    void addReply(Vertex reply) {
        replies.add(reply);
    }
}

// Class representing an edge between two vertices
class Edge {
    Vertex source;
    Vertex destination;

    Edge(Vertex source, Vertex destination) {
        this.source = source;
        this.destination = destination;
    }
}

// Class representing the graph
class Graph {
    List<Vertex> vertices;
    List<Edge> edges;

    Graph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    void addEdge(Vertex source, Vertex destination) {
        Edge edge = new Edge(source, destination);
        edges.add(edge);
        source.addReply(destination);
    }
}

public class PostManager {
    private static Graph postGraph = new Graph();
    private static int postIdCounter = 0;
    private static final String POSTS_FILE = "posts.txt";

    private static final String[] GENRES = { "Fiction", "Non-Fiction", "Poetry" };

    public static void main(String[] args) {
        loadPosts();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nChoose an action:");
            System.out.println("1. Create a new post");
            System.out.println("2. Reply to a post");
            System.out.println("3. Like a post");
            System.out.println("4. View all posts");
            System.out.println("5. Save posts to file");
            System.out.println("6. Recommend posts");
            System.out.println("7. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createPost(scanner);
                    break;
                case 2:
                    replyToPost(scanner);
                    break;
                case 3:
                    likePost(scanner);
                    break;
                case 4:
                    viewAllPosts();
                    break;
                case 5:
                    savePosts();
                    break;
                case 6:
                    recommendPosts(scanner);
                    break;
                case 7:
                    System.out.println("Exiting program.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createPost(Scanner scanner) {
        System.out.println("Enter your name:");
        String author = scanner.nextLine();

        System.out.println("Choose a genre:");
        for (int i = 0; i < GENRES.length; i++) {
            System.out.println((i + 1) + ". " + GENRES[i]);
        }
        int genreChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (genreChoice < 1 || genreChoice > GENRES.length) {
            System.out.println("Invalid genre choice.");
            return;
        }
        String genre = GENRES[genreChoice - 1];

        System.out.println("Enter your new post:");
        String postContent = scanner.nextLine();
        Vertex post = new Vertex(++postIdCounter, postContent, author, genre);
        postGraph.addVertex(post);
        System.out.println("Post created successfully!");
    }

    private static void replyToPost(Scanner scanner) {
        System.out.println("Enter the ID of the post you want to reply to:");
        int parentId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter your name:");
        String author = scanner.nextLine();
        System.out.println("Enter your reply:");
        String replyContent = scanner.nextLine();
        if (postGraph.vertices.stream().anyMatch(v -> v.id == parentId)) {
            Vertex parentPost = postGraph.vertices.stream().filter(v -> v.id == parentId).findFirst().get();
            Vertex reply = new Vertex(++postIdCounter, replyContent, author, parentPost.genre);
            postGraph.addEdge(parentPost, reply);
            System.out.println("Reply added successfully!");
        } else {
            System.out.println("Invalid post ID!");
        }
    }

    private static void likePost(Scanner scanner) {
        System.out.println("Enter the ID of the post you want to like:");
        int postId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (postGraph.vertices.stream().anyMatch(v -> v.id == postId)) {
            Vertex post = postGraph.vertices.stream().filter(v -> v.id == postId).findFirst().get();
            post.likes++;
            System.out.println("Post liked successfully!");
        } else {
            System.out.println("Invalid post ID!");
        }
    }

    private static void viewAllPosts() {
        System.out.println("All Posts:");
        for (Vertex post : postGraph.vertices) {
            printPost(post);
            System.out.println("Replies:");
            for (Vertex reply : post.replies) {
                printPost(reply);
            }
            System.out.println();
        }
    }

    private static void printPost(Vertex post) {
        System.out.println("Post ID: " + post.id);
        System.out.println("Author: " + post.author);
        System.out.println("Genre: " + post.genre);
        System.out.println("Content: " + post.content);
        System.out.println("Likes: " + post.likes);
    }

    private static void savePosts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(POSTS_FILE))) {
            for (Vertex post : postGraph.vertices) {
                savePost(post, writer);
                for (Vertex reply : post.replies) {
                    savePost(reply, writer);
                }
            }
            System.out.println("Posts saved to file successfully!");
        } catch (IOException e) {
            System.out.println("Error saving posts to file: " + e.getMessage());
        }
    }

    private static void savePost(Vertex post, PrintWriter writer) {
        writer.println("Post ID: " + post.id);
        writer.println("Author: " + post.author);
        writer.println("Genre: " + post.genre);
        writer.println("Content: " + post.content);
        writer.println("Likes: " + post.likes);
        writer.println();
    }

    private static void loadPosts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(POSTS_FILE))) {
            String line;
            int id = 0;
            String author = null;
            String genre = null;
            String content = null;
            int likes = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Post ID:")) {
                    id = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("Author:")) {
                    author = line.split(":")[1].trim();
                } else if (line.startsWith("Genre:")) {
                    genre = line.split(":")[1].trim();
                } else if (line.startsWith("Content:")) {
                    content = line.split(":")[1].trim();
                } else if (line.startsWith("Likes:")) {
                    likes = Integer.parseInt(line.split(":")[1].trim());
                    Vertex post = new Vertex(id, content, author, genre);
                    post.likes = likes;
                    postGraph.addVertex(post);
                }
            }
            postIdCounter = id;
            System.out.println("Posts loaded from file successfully!");
        } catch (IOException e) {
            System.out.println("Error loading posts from file: " + e.getMessage());
        }
    }

    private static void recommendPosts(Scanner scanner) {
        System.out.println("Enter your name:");
        String userName = scanner.nextLine();
        Set<String> likedGenres = getLikedGenres(userName);
        if (likedGenres.isEmpty()) {
            System.out.println("No liked posts found for user " + userName);
            return;
        }
        System.out.println("Recommended Posts:");
        for (Vertex post : postGraph.vertices) {
            if (!post.author.equals(userName) && likedGenres.contains(post.genre)) {
                printPost(post);
            }
        }
    }

    private static Set<String> getLikedGenres(String userName) {
        Set<String> likedGenres = new HashSet<>();
        for (Vertex post : postGraph.vertices) {
            if (post.author.equals(userName) && post.likes > 0) {
                likedGenres.add(post.genre);
            }
        }
        return likedGenres;
    }
}
