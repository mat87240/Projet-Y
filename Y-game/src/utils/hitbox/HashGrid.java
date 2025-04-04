package utils.hitbox;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class HashGrid {

	private final int cell_size = 10;
	private HashMap<Long, ArrayList<Hitbox>> hashgrid = new HashMap<Long, ArrayList<Hitbox>>();
	
	public HashGrid() {
	}
	
	private long generateKey(int x, int y) {
        int cellX = x / cell_size;
        int cellY = y / cell_size;
        return ((long) cellX << 32) | (cellY & 0xFFFFFFFFL);
	}

	public void add(Hitbox hitbox) {
		long key = this.generateKey(hitbox.getX(), hitbox.getY());
		this.hashgrid.computeIfAbsent(key,k -> new ArrayList<Hitbox>()).add(hitbox);
	}
	
	public void update(Hitbox hitbox, float oldX, float oldY) {
        long oldKey = generateKey((int) oldX, (int) oldY);
        long newKey = generateKey((int) hitbox.getX(), (int) hitbox.getY());
        if (oldKey != newKey) {  // Si l'entité change de cellule
            remove(oldKey, hitbox);
            add(hitbox);
        }
    }
	
	// Supprime une entité de la grille
    public void remove(long key, Hitbox hitbox) {
        ArrayList<Hitbox> cell = this.hashgrid.get(key);
        if (cell != null) {
            cell.remove(hitbox);
            if (cell.isEmpty()) { this.hashgrid.remove(key); };  // Supprime la cellule si elle est vide
        }
    }
    
    public ArrayList<Hitbox> getNearbyEntities(int x, int y) {
    	ArrayList<Hitbox> result = new ArrayList<>();
        int cellX = (int) (x / this.cell_size);
        int cellY = (int) (y / this.cell_size);

        // Vérifie la cellule et ses 8 voisines
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                long key = generateKey(cellX + dx, cellY + dy);
                if (this.hashgrid.containsKey(key)) {
                	result.addAll(this.hashgrid.get(key));
                }
            }
        }
        return result;
    }
	
}
