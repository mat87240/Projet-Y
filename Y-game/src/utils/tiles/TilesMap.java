package utils.tiles;

import map.Map;
import map.Room;
import map.Wall;

import java.util.ArrayList;

public class TilesMap {
    private static int[][] matriceRoom;
    private static int tileSize = 64;
    private static int nbTilesWall = 0;
    private static int sizeX;
    private static int sizeY;
    private static int origineX;
    private static int origineY;

    public static int[][] creerMatrice(Map map) {
        // Calculate the size of the map, etc.
        sizeX = map.getListWalls().get(0).getX() + map.getListWalls().get(0).getSizeX();
        sizeY = map.getListWalls().get(0).getY() + map.getListWalls().get(0).getSizeY();
        origineX = map.getListWalls().get(0).getX();
        origineY = map.getListWalls().get(0).getY();

        for (int i = 1; i < map.getListWalls().size(); i++) {
            Wall wall = map.getListWalls().get(i);

            if (wall.getX() + wall.getSizeX() > sizeX) { sizeX = wall.getX() + wall.getSizeX(); }
            if (wall.getY() + wall.getSizeY() > sizeY) { sizeY = wall.getY() + wall.getSizeY(); }
            if (wall.getX() < origineX) { origineX = wall.getX(); }
            if (wall.getY() < origineY) { origineY = wall.getY(); }
        }

        origineX = origineX / tileSize;
        origineY = origineY / tileSize;
        sizeX = sizeX / tileSize - origineX;
        sizeY = sizeY / tileSize - origineY;

        matriceRoom = new int[sizeY][sizeX];

        for (Wall wall : map.getListWalls()) {
            int wallStartX = wall.getX() / tileSize - origineX;
            int wallEndX = wall.getX() / tileSize - origineX + wall.getSizeX() / tileSize;
            int wallStartY = wall.getY() / tileSize - origineY;
            int wallEndY = wall.getY() / tileSize - origineY + wall.getSizeY() / tileSize;

            for (int y = wallStartY; y < Math.min(sizeY, wallEndY); y++) {
                for (int x = wallStartX; x < Math.min(sizeX, wallEndX); x++) {
                    if (x >= 0 && y >= 0) {
                        matriceRoom[y][x] = 1;  // Avoid horizontal flip
                        nbTilesWall += 1;
                    }
                }
            }
        }

        detecterIntersections();

        return matriceRoom;
    }

    private static void detecterIntersections() {
        for (int y = 0; y < matriceRoom.length; y++) {
            for (int x = 0; x < matriceRoom[0].length; x++) {
                if (matriceRoom[y][x] != 0) { // Vérifie que c'est un mur
                    boolean hasHorizontalNeighbor = false;
                    boolean hasVerticalNeighbor = false;
                    int voisins = 0;

                    // Vérifie les 4 directions
                    if (x > 0 && matriceRoom[y][x - 1] != 0) {
                        voisins++;
                        hasHorizontalNeighbor = true;
                    }
                    if (x < matriceRoom[0].length - 1 && matriceRoom[y][x + 1] != 0) {
                        voisins++;
                        hasHorizontalNeighbor = true;
                    }
                    if (y > 0 && matriceRoom[y - 1][x] != 0) {
                        voisins++;
                        hasVerticalNeighbor = true;
                    }
                    if (y < matriceRoom.length - 1 && matriceRoom[y + 1][x] != 0) {
                        voisins++;
                        hasVerticalNeighbor = true;
                    }

                    // Vérifie si on a au moins un voisin HORIZONTAL et un VERTICAL
                    if (hasHorizontalNeighbor && hasVerticalNeighbor) {
                        matriceRoom[y][x] = voisins;
                    }
                }
            }
        }
    }


    private static int determineRotation(int x, int y) {
        boolean hasLeft = (x > 0 && matriceRoom[y][x - 1] != 0);
        boolean hasRight = (x < matriceRoom[0].length - 1 && matriceRoom[y][x + 1] != 0);
        boolean hasTop = (y > 0 && matriceRoom[y - 1][x] != 0);
        boolean hasBottom = (y < matriceRoom.length - 1 && matriceRoom[y + 1][x] != 0);

        int count = (hasLeft ? 1 : 0) + (hasRight ? 1 : 0) + (hasTop ? 1 : 0) + (hasBottom ? 1 : 0);

        if (count == 1) {
            if (hasRight || hasLeft) return 0;  // Horizontal wall
            if (hasTop || hasBottom) return 90;  // Vertical wall
        }

        if (count == 2) {
            if (hasRight && hasTop) return 0;
            if (hasLeft && hasTop) return 90;
            if (hasLeft && hasBottom) return 180;
            if (hasRight && hasBottom) return 270;

            if (hasRight && hasLeft) return 0;  // Horizontal wall
            if (hasTop && hasBottom) return 90;  // Vertical wall
        }

        if (count == 3) {
            if (!hasTop) return 0;
            if (!hasRight) return 270;
            if (!hasBottom) return 180;
            if (!hasLeft) return 90;
        }
        return 0;  // Default case for cross-shaped walls
    }

    public static ArrayList<Wall> updateWallIds(Map map) {
        int[][] matrice = creerMatrice(map);
        ArrayList<Wall> listeWall = new ArrayList<>();

        for (int y = 0; y < matrice.length; y++) {
            for (int x = 0; x < matrice[0].length; x++) {


                if (matrice[y][x] != 0) {

                    int rotation = determineRotation(x, y);

                    int id = 0;

                    if (rotation == 90) {
                        id = 1;
                    }
                    if (rotation == 180) {
                        id = 2;
                    }
                    if (rotation == 270) {
                        id = 3;
                    }

                    Wall wall = new Wall((x + origineX)*64, (y + origineY)*64, 64, 64, 1, matrice[y][x], id);
                    listeWall.add(wall);
                }
            }
        }
        return listeWall;
    }

    public static int[][] getMatriceRoom() {
        return matriceRoom;
    }

    public static int getNbTilesWall() {
        return nbTilesWall;
    }

    public static void setNbTilesWall(int nbTilesWall) {
        TilesMap.nbTilesWall = nbTilesWall;
    }
}
