package utils.graph;

import map.Room;

import java.util.ArrayList;
import java.util.Random;

public class WallGen {
    private Random random;

    public WallGen() {
        this.random = new Random();
    }

    // Method to generate nodes (wall positions) inside rooms
    public void generateWallNodes(ArrayList<Room> rooms, ArrayList<Node> wallNodes) {
        for (Room room : rooms) {
            // Only generate nodes in rooms of type 0 (regular rooms)
            if (room.getType() == 0) {
                generateWallNodesInRoom(room, wallNodes);
            }
        }
    }

    private void generateWallNodesInRoom(Room room, ArrayList<Node> wallNodes) {
        int roomX = room.getX();
        int roomY = room.getY();
        int roomWidth = room.getSizeX();
        int roomHeight = room.getSizeY();

        // Calculate the area of the room
        int roomArea = roomWidth/64 * roomHeight/64;

        int nodeCount = roomArea / 50; // 1 node per 50 tiles of area

        // Make sure there's at least one wall
        nodeCount = Math.max(nodeCount, 1);

        // Generate the wall nodes based on the calculated node count
        for (int i = 0; i < nodeCount; i++) {
            int nodeX = roomX + random.nextInt(0, roomWidth);
            int nodeY = roomY + random.nextInt(0, roomHeight);

            // Create a new Node at the generated position
            Node wallNode = new Node(nodeX, nodeY);

            // Add the generated node to the list of wall nodes
            wallNodes.add(wallNode);
        }
    }
}
