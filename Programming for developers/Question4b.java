
/*
I've created a class Question4b with a method closestValues that finds and returns a 
list of 'x' closest values to a target double value in a binary search tree (BST). The method 
uses a PriorityQueue to maintain the 'x' closest values based on their absolute differences from 
the target. The program initiates an in-order traversal of the BST, updating the PriorityQueue with 
each node's value. The PriorityQueue is then adjusted to keep only the 'x' closest values. In the 
main function, I instantiate the class, create a sample BST, set a target value (3.8), and 
specify the count of closest values (x = 2). The closestValues method is called, and the result 
is printed to the console, demonstrating the 'x' closest values to the target in the given BST.
Output : [3, 4]
Time complexity : O(N * log(x))
 */
import java.util.*;

class TreeNode {
    int val;
    TreeNode left, right;

    public TreeNode(int val) {
        this.val = val;
    }
}

public class Question4b {
    public List<Integer> closestValues(TreeNode root, double target, int x) {
        List<Integer> result = new ArrayList<>();
        if (root == null || x == 0)
            return result;

        // PriorityQueue for maintaining 'x' closest values based on absolute difference
        // from the target
        PriorityQueue<Integer> pq = new PriorityQueue<>(x,
                (a, b) -> Double.compare(Math.abs(b - target), Math.abs(a - target)));

        // Performing in-order traversal to update the PriorityQueue with closest values
        inorder(root, pq, x, target);

        // Transfering values from PriorityQueue to result list
        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }

        return result;
    }

    private void inorder(TreeNode node, PriorityQueue<Integer> pq, int x, double target) {
        if (node == null)
            return;

        // Traversing left subtree
        inorder(node.left, pq, x, target);

        // Processing current node
        pq.offer(node.val);

        // Maintaining 'x' closest values in the PriorityQueue
        if (pq.size() > x) {
            pq.poll();
        }

        // Traversing right subtree
        inorder(node.right, pq, x, target);
    }

    public static void main(String[] args) {
        Question4b solution = new Question4b();

        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);

        // Targeting value and counting of closest values
        double target = 3.8;
        int x = 2;

        // Finding and printing 'x' closest values to the target
        List<Integer> closestValues = solution.closestValues(root, target, x);
        System.out.println("Output: " + closestValues);
    }
}