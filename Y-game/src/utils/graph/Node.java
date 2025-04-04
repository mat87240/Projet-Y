package utils.graph;

public class Node {

	private int x,y;
	private int type;

	public Node(int x, int y) {
		this.setX(x);
		this.setY(y);
		this.setType(0);
	}

	private void setX(int x) {
		if (x < 0) { throw new Error("x is negative"); }
		this.x = x;
	}

	private void setY(int y) {
		if (y < 0) { throw new Error("y is negative"); }
		this.y = y;
	}

	private void setType(int type) {
		if (y < 0) { throw new Error("type is negative"); }
		this.type = type;
	}

	public int[] getCoordinate() {
		int[] result = new int[2];
		result[0] = this.x;
		result[1] = this.y;
		return result;
	}

	private int getType() {
		return this.type;
	}

	public boolean isNearby(int x, int y, int threshold) {
		int X = this.x - x;
		int Y = this.y - y;
		return X*X+Y*Y<=threshold*threshold;
	}

}