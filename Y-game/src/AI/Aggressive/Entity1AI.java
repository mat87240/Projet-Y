package AI.Aggressive;

import AI.AIBehavior;
import map.Entity;
import map.Wall;

import java.util.*;
import static main.Main.dt;

public class Entity1AI implements AIBehavior {
	
    private static double currentFrameTime;
    private static final int GRID_SIZE = 64;
    private static final double TIME_BETWEEN = 0.450;

    public static class Node implements Comparable<Node> {
        int x, y;
        double g, h, f;
        Node parent;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.g = Float.MAX_VALUE;
            this.h = 0;
            this.f = Float.MAX_VALUE;
            this.parent = null;
        }

        public String getKey() {
            return x + "," + y;
        }

        // Implement the compareTo method to allow nodes to be compared by their 'f' values
        @Override
        public int compareTo(Node other) {
            // Compare based on the 'f' value (total cost)
            return Double.compare(this.f, other.f);
        }
    }

    @Override
    public void action(Entity entity, ArrayList<Wall> listWalls, float playerX, float playerY) {
        currentFrameTime += dt;

        if (currentFrameTime > TIME_BETWEEN) {
            currentFrameTime = 0;

            int entityGridX = (int) (entity.getX() / GRID_SIZE);
            int entityGridY = (int) (entity.getY() / GRID_SIZE);

            int playerGridX = (int) ((playerX + 32) / GRID_SIZE);  // Adjust player position by size
            int playerGridY = (int) ((playerY + 32) / GRID_SIZE);  // Adjust player position by size

            // Perform pathfinding and get the first point in the path
            Stack<int[]> path = performAStar(entityGridX, entityGridY, playerGridX, playerGridY, listWalls);
            entity.setPath(path);  // Update entity path
        }
    }

    private float calculateHeuristic(Node node, Node goal) {
        return Math.abs(node.x - goal.x) + Math.abs(node.y - goal.y);  // Manhattan distance
    }

    private Stack<int[]> performAStar(int startX, int startY, int goalX, int goalY, ArrayList<Wall> listWalls) {
        Node start = new Node(startX, startY);
        Node goal = new Node(goalX, goalY);
        start.g = 0;
        start.h = calculateHeuristic(start, goal);
        start.f = start.g + start.h;
        
        boolean found = false;
        
        PriorityQueue<Node> openSet = new PriorityQueue<Node>();
        HashSet<Node> closedSet = new HashSet<Node>();
        openSet.add(start);
        
        while (!openSet.isEmpty()) {
        	Node current = openSet.poll();
        	if (current.x == goal.x && current.y == goal.y) {
                return reconstructPath(current);
            }
        	closedSet.add(current);
        	
        	for (Node neighbor : getNeighbors(current, listWalls)) {
                if (closedSet.contains(neighbor)) continue;
                
                double tentativeG = current.g + 1;
                
                if (!openSet.contains(neighbor) || tentativeG < neighbor.g) {
                    neighbor.g = tentativeG;
                    neighbor.h = calculateHeuristic(neighbor, goal);
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = current;
                    
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        Stack<int[]> result = new Stack<int[]>();
        int[] tmp = {startX, startY};
        result.add(tmp);
        // If no path is found, return start as a fallback
        return result;
    }

    private List<Node> getNeighbors(Node node, ArrayList<Wall> blockedTiles) {
        List<Node> neighbors = new ArrayList<>();
        int[] directions = {-1, 1};  // Directions for grid (N, S, E, W)
        
        for (int dir : directions) {
        	for (int i=0;i<2;i++) {
        		int newX = node.x + (dir*(i&1));
                int newY = node.y + (dir*((i+1)&1));

                if (newX >= 0 && newX < GRID_SIZE && newY >= 0 && newY < GRID_SIZE) {
                    Node neighbor = new Node(newX, newY);
                    if (!blockedTiles.contains(neighbor.getKey())) {  // Avoid blocked tiles
                        neighbors.add(neighbor);
                    }
                }
        	}
        }
        return neighbors;
    }

    private Stack<int[]> reconstructPath(Node current) {
        Stack<int[]> path = new Stack<int[]>();
        while (current != null) {
            int[] tmp = {current.x * GRID_SIZE, current.y * GRID_SIZE};  // Scale the positions
            path.push(tmp);
            current = current.parent;
        }
        return path;
    }
}
