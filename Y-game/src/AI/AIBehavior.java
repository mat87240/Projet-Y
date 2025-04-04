package AI;

import map.Entity;
import map.Wall;

import java.util.ArrayList;
import java.util.Set;

public interface AIBehavior {
    void action(Entity entity, ArrayList<Wall> listWalls, float playerX, float playerY);
}
