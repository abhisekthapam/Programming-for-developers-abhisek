import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class AntColonyOptimization {
    private double[][] distances;
    private int numAnts;
    private int numIterations;
    private double[][] pheromones;
    private double alpha; 
    private double beta;
    private double evaporationRate;
    private int numCities;

    public AntColonyOptimization(double[][] distances, int numAnts, int numIterations, double alpha, double beta, double evaporationRate) {
        this.distances = distances;
        this.numAnts = numAnts;
        this.numIterations = numIterations;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporationRate = evaporationRate;
        this.numCities = distances.length;
        initializePheromones();
    }

    private void initializePheromones() {
        pheromones = new double[numCities][numCities];
        double initialPheromone = 1.0 / (numCities * numCities);
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromones[i][j] = initialPheromone;
            }
        }
    }

    public List<Integer> solve() {
        List<Integer> bestTour = null;
        double bestTourLength = Double.MAX_VALUE;
        Random random = new Random();
        for (int iter = 0; iter < numIterations; iter++) {
            List<List<Integer>> antTours = new ArrayList<>();
            for (int ant = 0; ant < numAnts; ant++) {
                List<Integer> tour = generateTour(random);
                antTours.add(tour);
                double tourLength = calculateTourLength(tour);
                if (tourLength < bestTourLength) {
                    bestTourLength = tourLength;
                    bestTour = new ArrayList<>(tour);
                }
            }
            updatePheromones(antTours);
        }
        return bestTour;
    }

    private List<Integer> generateTour(Random random) {
        List<Integer> tour = new ArrayList<>(numCities);
        boolean[] visited = new boolean[numCities];
        int startCity = random.nextInt(numCities);
        tour.add(startCity);
        visited[startCity] = true;
        for (int i = 1; i < numCities; i++) {
            int nextCity = selectNextCity(tour.get(i - 1), visited, random);
            tour.add(nextCity);
            visited[nextCity] = true;
        }
        return tour;
    }

    private int selectNextCity(int currentCity, boolean[] visited, Random random) {
        double[] probabilities = new double[numCities];
        double totalProbability = 0;
        for (int i = 0; i < numCities; i++) {
            if (!visited[i]) {
                double pheromone = Math.pow(pheromones[currentCity][i], alpha);
                double distance = 1.0 / Math.pow(distances[currentCity][i], beta);
                probabilities[i] = pheromone * distance;
                totalProbability += probabilities[i];
            }
        }
        double threshold = random.nextDouble() * totalProbability;
        double cumulativeProbability = 0;
        for (int i = 0; i < numCities; i++) {
            if (!visited[i]) {
                cumulativeProbability += probabilities[i];
                if (cumulativeProbability >= threshold) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void updatePheromones(List<List<Integer>> antTours) {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (i != j) {
                    pheromones[i][j] *= (1 - evaporationRate);
                }
            }
        }
        for (List<Integer> tour : antTours) {
            double tourLength = calculateTourLength(tour);
            for (int i = 0; i < numCities - 1; i++) {
                int city1 = tour.get(i);
                int city2 = tour.get(i + 1);
                pheromones[city1][city2] += 1.0 / tourLength;
                pheromones[city2][city1] += 1.0 / tourLength;
            }
        }
    }

    private double calculateTourLength(List<Integer> tour) {
        double length = 0;
        for (int i = 0; i < numCities - 1; i++) {
            int city1 = tour.get(i);
            int city2 = tour.get(i + 1);
            length += distances[city1][city2];
        }
        length += distances[tour.get(numCities - 1)][tour.get(0)]; 
        return length;
    }
}

public class Question5a {
    public static void main(String[] args) {
        double[][] distances = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };
        int numAnts = 10;
        int numIterations = 100;
        double alpha = 1.0;
        double beta = 2.0;
        double evaporationRate = 0.5;

        AntColonyOptimization aco = new AntColonyOptimization(distances, numAnts, numIterations, alpha, beta, evaporationRate);
        List<Integer> bestTour = aco.solve();
        System.out.println("Best tour: " + bestTour);
    }
}
