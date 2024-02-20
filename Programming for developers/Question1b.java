public class Question1b {
    public static int minTimeToBuild(int[] engines, int splitCost) {
        int totalTime = 0;
        for (int engineTime : engines) {
            if (splitCost + engineTime / 2 < engineTime) {
                totalTime += splitCost;
            } else {
                totalTime += engineTime;
            }
        }
        return totalTime;
    }

    public static void main(String[] args) {
        int[] engines = {1, 2, 3};
        int splitCost = 1;
        System.out.println(minTimeToBuild(engines, splitCost));
    }
}