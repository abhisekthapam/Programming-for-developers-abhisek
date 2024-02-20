/*
I've implemented the Ant Colony Optimization (ACO) algorithm to solve the Traveling Salesman Problem. 
The AntColonyOptimization class initializes pheromones, and in each iteration, multiple ants 
generate tours by probabilistically selecting cities based on pheromone levels and distances. 
The algorithm updates the best tour whenever a shorter one is found, and pheromone levels are 
adjusted on the edges based on the tours taken by ants, favoring shorter paths. The main loop 
repeats for a specified number of iterations, and the algorithm returns the best tour, providing 
an optimal order for visiting cities. The Question5a class showcases the algorithm on a sample 
distance matrix with defined parameters, and the result, representing the best tour, is printed 
to the console.
Output : [2, 0, 1, 3]
Time complexity : O(numIterations * numAnts * numCities^2)
 */

import java.util.ArrayList;
import java.util.Arrays;
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

    public AntColonyOptimization(double[][] distances, int numAnts, int numIterations, double alpha, double beta,
            double evaporationRate) {
        this.distances = distances;
        this.numAnts = numAnts;
        this.numIterations = numIterations;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporationRate = evaporationRate;
        this.numCities = distances.length;
        initializePheromones();
    }

    // Initializing pheromones with a small initial value
    private void initializePheromones() {
        pheromones = new double[numCities][numCities];
        double initialPheromone = 1.0 / Math.pow(numCities, 2);
        for (int i = 0; i < numCities; i++) {
            Arrays.fill(pheromones[i], initialPheromone);
        }
    }

    // Solving the optimization problem using Ant Colony Optimization
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
                    // Using Collections.copy for a more efficient tour copy
                    bestTour = new ArrayList<>(tour);
                }
            }
            updatePheromones(antTours);
        }
        return bestTour;
    }

    // Generating a tour for a single ant
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

    // Selecting the next city for an ant based on pheromone levels and distances
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

    // Updating pheromone levels based on ant tours
    private void updatePheromones(List<List<Integer>> antTours) {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (i != j) {
                    // Evaporating existing pheromones
                    pheromones[i][j] *= 1 - evaporationRate;
                }
            }
        }
        for (List<Integer> tour : antTours) {
            double tourLength = calculateTourLength(tour);
            for (int i = 0; i < numCities - 1; i++) {
                int city1 = tour.get(i);
                int city2 = tour.get(i + 1);
                // Updating pheromones based on the tour length
                pheromones[city1][city2] += 1.0 / tourLength;
                pheromones[city2][city1] += 1.0 / tourLength;
            }
        }
    }

    // Calculating the total length of a tour
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
                { 0, 10, 15, 20 },
                { 10, 0, 35, 25 },
                { 15, 35, 0, 30 },
                { 20, 25, 30, 0 }
        };
        int numAnts = 10;
        int numIterations = 100;
        double alpha = 1.0;
        double beta = 2.0;
        double evaporationRate = 0.5;

        AntColonyOptimization aco = new AntColonyOptimization(distances, numAnts, numIterations, alpha, beta,
                evaporationRate);
        List<Integer> bestTour = aco.solve();
        System.out.println("Best tour: " + bestTour);
    }
}
