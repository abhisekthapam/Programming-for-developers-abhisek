import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Question2b {

    public static List<Integer> findSecretRecipients(int totalIndividuals, int[][] sharingIntervals,
            int initialPerson) {
        Set<Integer> secretRecipients = new HashSet<>();
        secretRecipients.add(initialPerson);

        for (int[] interval : sharingIntervals) {
            int intervalStart = interval[0];
            int intervalEnd = interval[1];

            if (intervalStart <= initialPerson && initialPerson <= intervalEnd) {
                for (int i = 0; i < totalIndividuals; i++) {
                    secretRecipients.add(i);
                }
            }
        }

        return new ArrayList<>(secretRecipients);
    }

    public static void main(String[] args) {
        int totalIndividuals = 5;
        int[][] sharingIntervals = { { 0, 2 }, { 1, 3 }, { 2, 4 } };
        int initialPerson = 0;

        List<Integer> secretRecipients = findSecretRecipients(totalIndividuals, sharingIntervals, initialPerson);
        System.out.println("Individuals who eventually know the secret:");
        for (int recipient : secretRecipients) {
            System.out.print(recipient + " ");
        }
    }
}