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

        PriorityQueue<Integer> pq = new PriorityQueue<>(x,
                (a, b) -> Double.compare(Math.abs(b - target), Math.abs(a - target)));

        inorder(root, pq, x, target);

        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }

        return result;
    }

    private void inorder(TreeNode node, PriorityQueue<Integer> pq, int x, double target) {
        if (node == null)
            return;

        inorder(node.left, pq, x, target);

        pq.offer(node.val);

        if (pq.size() > x) {
            pq.poll();
        }

        inorder(node.right, pq, x, target);
    }

    public static void main(String[] args) {
        Question4b solution = new Question4b();
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        double target = 3.8;
        int x = 2;
        List<Integer> closestValues = solution.closestValues(root, target, x);
        System.out.println("Output: " + closestValues);
    }
}
