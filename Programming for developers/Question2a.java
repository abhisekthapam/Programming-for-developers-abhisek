public class Question2a {

    static int minimumMovesToEqualize(int[] machines) {
       int total = 0;
       int n = machines.length;

       for (int machine : machines) {
           total += machine;
       }

       if (total % n != 0) {
           return -1;
       }

       int avg = total / n;
       int moves = 0;
       int diff = 0;

       for (int i = 0; i < n; i++) {
           diff += machines[i] - avg;
           moves = Math.max(moves, Math.abs(diff));
           machines[i] = avg;
       }

       return moves;
   }
   public static void main(String[] args) {
       int[] machines = {1, 0, 5};
       System.out.println("Minimum number of moves to equalize dresses: "+minimumMovesToEqualize(machines));
   }
}