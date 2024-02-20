/*
I've implemented a solution to determine the minimum number of moves required to collect 
all keys in a maze-like grid. Using breadth-first search (BFS), the findMinimumMovesToCollectAllKeys 
function explores valid moves in the grid, updating the state of the current position, collected keys, 
and steps taken. The algorithm continues until all target keys are collected, and the minimum number 
of steps is returned. The program initializes a queue of states, employs bitwise operations to 
represent and compare keys efficiently, and ensures valid moves through the isValidMove function. 
The main function demonstrates the solution on a provided grid, showcasing the minimum number of 
moves needed to collect all keys.
Output : 8
Time complexity : O(rows * cols * 2^6 + E)
 */

import java.util.LinkedList;
import java.util.Queue;

public class Question4a {
    // Encapsulating state information in a State class for clarity
    static class State {
        int x, y, keys, steps;

        State(int x, int y, int keys, int steps) {
            this.x = x;
            this.y = y;
            this.keys = keys;
            this.steps = steps;
        }
    }

    public static int findMinimumMovesToCollectAllKeys(String[] grid) {
        int rows = grid.length;
        int cols = grid[0].length();
        int targetKeys = 0;
        int startX = 0, startY = 0;

        // Identifying the starting position and target keys
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = grid[i].charAt(j);
                if (cell == 'S') {
                    startX = i;
                    startY = j;
                } else if (cell == 'E') {
                    targetKeys |= (1 << ('f' - 'a'));
                } else if (cell >= 'a' && cell <= 'f') {
                    targetKeys |= (1 << (cell - 'a'));
                }
            }
        }

        Queue<State> queue = new LinkedList<>();
        boolean[][][] visited = new boolean[rows][cols][1 << 6];
        queue.offer(new State(startX, startY, 0, 0));

        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        while (!queue.isEmpty()) {
            State current = queue.poll();
            int x = current.x;
            int y = current.y;
            int keys = current.keys;
            int steps = current.steps;

            // Checking if all keys are collected
            if (keys == targetKeys) {
                return steps;
            }

            // Exploring valid neighbors
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                // Checking if the move is valid and not visited
                if (isValidMove(newX, newY, rows, cols, grid) && !visited[newX][newY][keys]) {
                    char cell = grid[newX].charAt(newY);
                    State newState = updateState(grid, current, newX, newY, cell);
                    visited[newX][newY][newState.keys] = true;
                    queue.offer(newState);
                }
            }
        }
        return -1;
    }

    private static boolean isValidMove(int x, int y, int rows, int cols, String[] grid) {
        return x >= 0 && x < rows && y >= 0 && y < cols && grid[x].charAt(y) != 'W';
    }

    private static State updateState(String[] grid, State current, int newX, int newY, char cell) {
        int newKeys = current.keys;
        if (cell >= 'a' && cell <= 'f') {
            newKeys |= (1 << (cell - 'a'));
        }
        return new State(newX, newY, newKeys, current.steps + 1);
    }

    public static void main(String[] args) {
        String[] grid = { "SPqPP", "WWWPW", "bPAPB" };
        int result = findMinimumMovesToCollectAllKeys(grid);
        System.out.println("Minimum number of moves: " + result);
    }
}
