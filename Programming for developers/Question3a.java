import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question3a {
    private List<Double> scores;

    public Question3a() {
        scores = new ArrayList<>();
    }

    public void addScore(double score) {
        scores.add(score);
        Collections.sort(scores);
    }

    public double getMedianScore() {
        int size = scores.size();
        if (size == 0) {
            throw new IllegalStateException("No scores available.");
        }

        int middleIndex = size / 2;
        if (size % 2 == 0) {
            double lowerMedian = scores.get(middleIndex - 1);
            double upperMedian = scores.get(middleIndex);
            return (lowerMedian + upperMedian) / 2.0;
        } else {
            return scores.get(middleIndex);
        }
    }

    public static void main(String[] args) {
        Question3a scoreTracker = new Question3a();
        scoreTracker.addScore(85.5);
        scoreTracker.addScore(92.3);
        scoreTracker.addScore(77.8);
        scoreTracker.addScore(90.1);
        double median1 = scoreTracker.getMedianScore();
        System.out.println("Median score 1: " + median1);

        scoreTracker.addScore(81.2);
        scoreTracker.addScore(88.7);
        double median2 = scoreTracker.getMedianScore();
        System.out.println("Median score 2: " + median2);
    }
}
