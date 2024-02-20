/*
I initiate by creating a Set called secretRecipients to store 
those aware of the secret, starting with the initialPerson. 
Simultaneously, I initialize a boolean array, alreadyReceived, to 
track those who have been informed. I iterate through sharingIntervals, 
creating a new Set, newRecipients, for potential recipients within each 
interval. Assessing individuals in the current secret recipients, I 
update newRecipients if they fall within the current interval and 
haven't received the secret. The main set of secret recipients is then 
updated with newRecipients' contents. After iterating through all 
intervals, I return the list of secret recipients as an ArrayList. 
In the main method, I set input parameters, call the 
findSecretRecipients function, and print the result, displaying 
individuals who eventually know the secret.
Output : 0 1 2 3 4 
Time complexity : O(number of sharing intervals * the total number of individuals)
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Question2b {

    // Function for finding individuals who eventually know the secret
    public static List<Integer> findSecretRecipients(int totalIndividuals, int[][] sharingIntervals,
            int initialPerson) {
        // Setting for storing individuals who know the secret
        Set<Integer> secretRecipients = new HashSet<>();
        secretRecipients.add(initialPerson);

        // Array for tracking individuals who have already received the secret
        boolean[] alreadyReceived = new boolean[totalIndividuals];
        alreadyReceived[initialPerson] = true;

        // Iterating through each sharing interval
        for (int[] interval : sharingIntervals) {
            // Creating a new set to store updated recipients
            Set<Integer> newRecipients = new HashSet<>(secretRecipients);

            // Iterating through individuals who currently know the secret
            for (int person : secretRecipients) {
                // Checking if the current person is within the sharing interval
                if (interval[0] <= person && person <= interval[1]) {
                    // Iterating through the range specified by the interval
                    for (int i = interval[0]; i <= interval[1]; i++) {
                        // Adding new individuals to the set if they haven't received the secret before
                        if (!alreadyReceived[i]) {
                            newRecipients.add(i);
                            alreadyReceived[i] = true;
                        }
                    }
                }
            }

            // Updating the set of secret recipients with the new set
            secretRecipients = newRecipients;
        }

        // Converting the set to a list and return the result
        return new ArrayList<>(secretRecipients);
    }

    // Main method for testing the function
    public static void main(String[] args) {
        int totalIndividuals = 5;
        int[][] sharingIntervals = { { 0, 2 }, { 1, 3 }, { 2, 4 } };
        int initialPerson = 0;

        // Finding secret recipients and print the result
        List<Integer> secretRecipients = findSecretRecipients(totalIndividuals, sharingIntervals, initialPerson);
        System.out.println("Individuals who eventually know the secret:");
        for (int recipient : secretRecipients) {
            System.out.print(recipient + " ");
        }
    }
}
