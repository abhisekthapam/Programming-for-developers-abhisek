public class Question1a {
    public static int findMinimumCost(int[][] venueThemes) {
        if (venueThemes == null || venueThemes.length == 0 || venueThemes[0].length == 0) {
            return 0;
        }

        int numVenues = venueThemes.length;
        int numThemes = venueThemes[0].length;

        int[][] minCosts = new int[numVenues][numThemes];

        System.arraycopy(venueThemes[0], 0, minCosts[0], 0, numThemes);

        for (int venueIndex = 1; venueIndex < numVenues; venueIndex++) {
            for (int themeIndex = 0; themeIndex < numThemes; themeIndex++) {
                minCosts[venueIndex][themeIndex] = Integer.MAX_VALUE;
                for (int prevThemeIndex = 0; prevThemeIndex < numThemes; prevThemeIndex++) {
                    if (prevThemeIndex != themeIndex) {
                        minCosts[venueIndex][themeIndex] = Math.min(minCosts[venueIndex][themeIndex],
                                minCosts[venueIndex - 1][prevThemeIndex]);
                    }
                }
                minCosts[venueIndex][themeIndex] += venueThemes[venueIndex][themeIndex];
            }
        }

        int minCost = Integer.MAX_VALUE;
        for (int cost : minCosts[numVenues - 1]) {
            minCost = Math.min(minCost, cost);
        }
        return minCost;
    }

    public static void main(String[] args) {
        int[][] venueThemes = { { 1, 3, 2 }, { 4, 6, 8 }, { 3, 1, 5 } };
        int minCost = findMinimumCost(venueThemes);
        System.out.println("The minimum cost of venue decoration is: " + minCost);
    }
}
