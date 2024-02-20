
/*
I've crafted a method named minTimeToBuildAllEngines that accepts an array of 
input engine build times and a split cost as parameters. Initially, I establish a 
priority queue named engines to systematically store the engine build times in ascending order. 
Subsequently, I iterate through the input engine build times, and for each, I add its 
corresponding time to the priority queue. The core logic of the function revolves around a 
while loop, persisting until there are at least two engine build times in the queue. Within this loop, 
I remove the smallest build time (the top element) and retrieve the second smallest build time. 
I then combine the second smallest build time with the provided split cost and reintroduce it to 
the priority queue. This process iterates until only one element remains in the queue, signifying 
the minimum time required to build all engines. Moving to the main method, I supply an example 
array of engine build times and a split cost, instantiate an object of Question1B, invoke the 
minTimeToBuildAllEngines method, and finally, print the result to the console.
Output : 4
Time complexity : O(N log N)
 */
import java.util.PriorityQueue;

class Question1B {
    public int minTimeToBuildAllEngines(int[] inputEngines, int splitCost) {
        // Create a priority queue to store engine build times.
        PriorityQueue<Integer> engines = new PriorityQueue<>();

        // Add each engine's build time to the queue.
        for (int engine : inputEngines) {
            engines.offer(engine);
        }

        // While there are at least two input in the queue:
        while (engines.size() > 1) {
            // Remove the smallest build time (the top element).
            engines.poll();

            // Get the second smallest build time.
            int secondSmallestEngine = engines.poll();

            // Combine the second smallest build time with the split cost
            // and add it back to the queue.
            engines.offer(secondSmallestEngine + splitCost);
        }

        // The final result is the build time of the last remaining engine.
        return engines.poll();
    }
    public static void main(String[] args) {
        int engines[] = { 1, 2, 3 };
        int splitCost = 1;
        Question1B object = new Question1B();
        System.out.println("Minimum time cost to complete all engine builds: "
                + object.minTimeToBuildAllEngines(engines, splitCost));
    }
}
