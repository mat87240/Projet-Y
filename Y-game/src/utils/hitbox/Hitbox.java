package utils.hitbox;

public abstract class Hitbox{

	private int x,y;
	
	public Hitbox(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
	
	public abstract boolean[] intersect(AABB o);
	public abstract boolean intersect(Circle o);
	
	public int getX() { return x; }
	public void setX(int x) {
		if (x < 0) {
			System.err.println("\u001B[31m" + "Error: X is negative, setting to positive." + "\u001B[0m");
			this.x = -x;
		} else {this.x = x;}
	}

	public int getY() { return y; }
	public void setY(int y) {
		if (y < 0) {
			System.err.println("\u001B[31m" + "Error: Y is negative, setting to positive." + "\u001B[0m");
			this.y = -y;
		} else {this.y = y;}
	}

	public int[] getCoordinate() {
		int[] result = new int[2];
		result[0] = this.x;
		result[1] = this.y;
		return result;
	}
	public void setCoordinate(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	
}
