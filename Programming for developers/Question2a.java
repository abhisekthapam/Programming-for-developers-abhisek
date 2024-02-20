/*
I initialize 'total' and 'n' to track the dress count 
and machine number. Iterating through each machine, I update 
'total' and check if even distribution is possible. If not, I 
return -1. For possible distribution, I calculate the average dresses 
per machine. Using 'moves' and 'diff', I iterate through machines to 
find the minimum moves needed for equalization. The function returns 
the minimum moves. In the main method, I provide an input array, call 
the function, and print the result indicating the minimum moves needed 
to equalize dresses.
Output : 3
Time complexity : O(n)
 */

public class Question2a {

    // Function for calculating minimum moves to equalize dresses
    static int minimumMovesToEqualize(int[] machines) {
        int total = 0;
        int n = machines.length;

        // Calculating total number of dresses
        for (int machine : machines) {
            total += machine;
        }

        // Checking if equal distribution is possible
        if (total % n != 0) {
            return -1;
        }

        int avg = total / n;
        int moves = 0;
        int diff = 0;

        // Iterating through machines to find minimum moves
        for (int machine : machines) {
            diff += machine - avg;
            moves = Math.max(moves, Math.abs(diff));
        }
        return moves;
    }

    public static void main(String[] args) {
        int[] machines = { 1, 0, 5 };

        // Calling the function and printing the result
        System.out.println("Minimum number of moves to equalize dresses: " + minimumMovesToEqualize(machines));
    }
}
