    package map;

    import AI.Aggressive.Entity1AI;
    import player.Player;
    import utils.graph.Node;
    import utils.graph.Pair;
    import utils.tiles.TilesMap;
    import utils.graph.Graph;
    import utils.graph.WallGen;
    import utils.web.WebClient;

    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Random;

    public class Map {
        private ArrayList<Room> roomList;
        private static ArrayList<Entity> listEntities;
        private ArrayList<Wall> listWalls;
        private ArrayList<Bullet> bullets;

        private HashSet blockedTiles;

        private Room currentRoom;

        public Map() {
            this.listEntities = new ArrayList<>();
            this.roomList = new ArrayList<>();
            this.listWalls = new ArrayList<>();
            this.bullets = new ArrayList<>();
            this.currentRoom = null;
            createMap();
        }

        public void createMap() {
            int tileSize = 64;

            Random random = new Random();
            int seed = (random.nextInt(1) + 1000);

            Graph generatedMap = new Graph();
            generatedMap.generate(40, seed);

            ArrayList<Node> listNodes = generatedMap.getNodes();
            ArrayList<Pair> listConnect = generatedMap.getPair();

            int roomID = 1; // Start ID from 1

            for (Node node : listNodes) {
                int[] nodeCoord = node.getCoordinate();

                int roomWidth, roomHeight;

                if (roomID == 1) {
                    roomWidth = 10;
                    roomHeight = 10;
                } else {
                    roomWidth = (random.nextInt(31) + 10);
                    roomHeight = (random.nextInt(31) + 10);
                }

                Room newRoom = new Room(roomID++, 0, nodeCoord[0]*64, nodeCoord[1]*64, roomWidth*64, roomHeight*64);
                addRoom(newRoom);
            }

            for (Pair connection : listConnect) {
                int[] pair = connection.getPair();
                Room roomA = getRoomById(pair[0] + 1);
                Room roomB = getRoomById(pair[1] + 1);

                int startX = roomA.getX() + roomA.getSizeX() / 2;
                int startY = roomA.getY() + roomA.getSizeY() / 2;
                int endX = roomB.getX() + roomB.getSizeX() / 2;
                int endY = roomB.getY() + roomB.getSizeY() / 2;

                int pathWidth = (random.nextInt(4) + 2)*64;

                if (Math.random() > 0.5) {
                    // First move horizontally, then vertically
                    Room horizontalPath = new Room(roomID++, 1, Math.min(startX, endX), startY, Math.abs(startX - endX), pathWidth);
                    Room verticalPath = new Room(roomID++, 1, endX, Math.min(startY, endY), pathWidth, Math.abs(startY - endY));

                    addRoom(horizontalPath);
                    addRoom(verticalPath);
                } else {
                    // First move vertically, then horizontally
                    Room verticalPath = new Room(roomID++, 1, startX, Math.min(startY, endY), pathWidth, Math.abs(startY - endY));
                    Room horizontalPath = new Room(roomID++, 1, Math.min(startX, endX), endY, Math.abs(startX - endX), pathWidth);

                    addRoom(verticalPath);
                    addRoom(horizontalPath);
                }
            }

            addRoomWalls();

            // Generate walls for each room (not just connected rooms)
            WallGen walls = new WallGen();
            ArrayList<Node> wallNodes = new ArrayList<>();

            // Now create walls from the generated wall nodes
            for (int i = 1; i < roomList.size(); i++) {
                Room room = roomList.get(i);
                walls.generateWallNodes(new ArrayList<>(List.of(room)), wallNodes);
            }

            for (Node wallNode : wallNodes) {
                int[] nodeCoord = wallNode.getCoordinate();
                int wallWidth = (random.nextInt(4) + 3) * tileSize;  // Random wall width
                int wallHeight = (random.nextInt(4) + 3) * tileSize; // Random wall height

                // Split the wall into tiles
                for (int y = 0; y < wallHeight; y += tileSize) {  // Loop for each row of tiles
                    for (int x = 0; x < wallWidth; x += tileSize) {  // Loop for each column of tiles
                        // Create a tile for each part of the wall
                        Wall wall = new Wall(nodeCoord[0] + x, nodeCoord[1] + y, tileSize, tileSize, 1, 0, 1);

                        // Check if the tile is within the bounds of the map
                        boolean isInsideMap = false;
                        for (Room room : roomList) {
                            if (wall.getX() >= room.getX() && wall.getX() + wall.getSizeX() <= room.getX() + room.getSizeX() &&
                                    wall.getY() >= room.getY() && wall.getY() + wall.getSizeY() <= room.getY() + room.getSizeY()) {
                                isInsideMap = true;
                                break;
                            }
                        }

                        // Check if the tile overlaps with any room of type 1 (corridors)
                        boolean shouldPlaceTile = isInsideMap; // Only proceed if the tile is inside the map

                        for (Room room : roomList) {
                            if (room.getType() == 1) { // Only check rooms of type 1 (corridors)
                                boolean overlaps =
                                        wall.getX() < room.getX() + room.getSizeX() && // Tile's left edge < Room's right edge
                                                wall.getX() + wall.getSizeX() - 64 >= room.getX() && // Tile's right edge > Room's left edge
                                                wall.getY() < room.getY() + room.getSizeY() && // Tile's top edge < Room's bottom edge
                                                wall.getY() + wall.getSizeY() - 64 >= room.getY();  // Tile's bottom edge > Room's top edge

                                if (overlaps) {
                                    shouldPlaceTile = false; // If the tile overlaps with a corridor, skip placing it
                                    break; // Exit loop, no need to check further rooms
                                }
                            }
                        }

                        // Only add the tile if it is inside the map and doesn't overlap with any room of type 1
                        if (shouldPlaceTile) {
                            addWall(wall); // Add the individual wall tile to the map
                        }
                    }
                }
            }


            this.listWalls = TilesMap.updateWallIds(this);
            for (Wall wall : listWalls) {
                wall.assignImage();

            }
        }

        public void addRoomWalls() {
            for (Room room : roomList) {
                float roomX = room.getX();
                float roomY = room.getY();
                float roomSizeX = room.getSizeX();
                float roomSizeY = room.getSizeY();

                int tileSize = 64; // Each wall tile is 64x64

                // Add top and bottom walls (horizontal tiling)
                for (int x = 0; x <= roomSizeX+64; x += tileSize) {
                    Wall bottomWall = new Wall ((int) (roomX+x-64), (int) (roomY-64),tileSize,tileSize,0,0,0);
                    Wall topWall = new Wall((int) (roomX + x-64), (int) (roomY + roomSizeY), tileSize, tileSize, 0, 0,2);
                    if (isInsideRoom(topWall)){addWall(topWall);}
                    if (isInsideRoom(bottomWall)){addWall(bottomWall);}

                }

                // Add left and right walls (vertical tiling)
                for (int y = 0; y <= roomSizeY+64; y += tileSize) {
                    Wall leftWall = new Wall((int) roomX-64, (int) (roomY + y)-64, tileSize, tileSize, 0, 0,1);
                    Wall rightWall = new Wall((int) (roomX + roomSizeX), (int) (roomY + y)-64, tileSize, tileSize, 0, 0,3);
                    if (isInsideRoom(rightWall)){addWall(rightWall);}
                    if (isInsideRoom(leftWall)){addWall(leftWall);}
                }
            }
        }

        public boolean isInsideRoom(Wall wall) {
            for (Room room : this.roomList) {
                boolean overlaps =
                        wall.getX() < room.getX() + room.getSizeX() && // Wall's left edge < Room's right edge
                                wall.getX() + wall.getSizeX()-64 >= room.getX() && // Wall's right edge > Room's left edge
                                wall.getY() < room.getY() + room.getSizeY() && // Wall's top edge < Room's bottom edge
                                wall.getY() + wall.getSizeY()-64 >= room.getY();  // Wall's bottom edge > Room's top edge

                if (overlaps) {
                    return false;
                }
            }
            return true;
        }

        public void updateEntities(Player player) {
            player.update(listWalls, player.getX(), player.getY());

            for (Entity entity : listEntities){
                entity.update(listWalls, player.getX(), player.getY());
            }

            for (int i = bullets.size() - 1; i >= 0; i--) {
                Bullet bullet = bullets.get(i);
                bullet.update();
                boolean bulletRemoved = false;

                // Check if the bullet intersects any wall
                for (Wall wall : listWalls) {
                    boolean[] intersection = bullet.getHitbox().intersect(wall.getHitbox());
                    if (intersection[0] || intersection[1] || intersection[2] || intersection[3]) {
                        bullets.remove(i);
                        bulletRemoved = true;
                        break; // Stop checking further collisions
                    }
                }

                // If the bullet was removed, skip checking entities
                if (bulletRemoved) continue;

                for (Entity entity : listEntities) {
                    boolean[] intersection = bullet.getHitbox().intersect(entity.getHitbox());
                    if (intersection[0] || intersection[1] || intersection[2] || intersection[3]) {
                        entity.takeDamage(bullet.getDamage());
                        bullets.remove(i);

                        if (entity.getHealth() <= 0) {
                            listEntities.remove(entity); // Remove the entity from the map
                        }
                        break; // Stop checking further collisions for this bullet
                    }
                }
            }
        }

        public void addRoom(Room room) {
            roomList.add(room);
        }
        public void addWall(Wall wall) { listWalls.add(wall); }

        public ArrayList<Room> getRoomList() {
            return roomList;
        }

        public Room getCurrentRoom() {
            return currentRoom;
        }

        public void updateCurrentRoom(float playerX, float playerY) {
            for (Room room : roomList) {
                float roomX = room.getX();
                float roomY = room.getY();
                float roomSizeX = room.getSizeX();
                float roomSizeY = room.getSizeY();

                float playerCenterX = playerX + 32;
                float playerCenterY = playerY + 32;

                if (playerCenterX >= roomX && playerCenterX <= roomX + roomSizeX &&
                        playerCenterY >= roomY && playerCenterY <= roomY + roomSizeY) {
                    currentRoom = room;
                    break;
                }
            }
        }

        public void addEntity(Entity entity) { listEntities.add(entity); }
        public static ArrayList<Entity> getListEntities() { return listEntities; }
        public ArrayList<Wall> getListWalls() { return listWalls; }

        public Room getRoomById(int roomId) {
            for (Room room : this.roomList) {
                if (room.getID() == roomId) {
                    return room;
                }
            }
            return null;
        }

        public void addBullet(Bullet bullet) {bullets.add(bullet);}
        public ArrayList<Bullet> getBullets() {return bullets;}

        public Room getRandomRoom() {
            Random random = new Random();
            int randomIndex = random.nextInt(roomList.size()); // Get a random index from the roomList
            return roomList.get(randomIndex); // Return the room at that random index
        }

    }
