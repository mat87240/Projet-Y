package utils.hitbox;

public class AABB extends Hitbox {
	
	private int width,height;
	
	public AABB(int x, int y, int width, int height) {
		super(x,y);
		this.setWidth(width);
		this.setHeight(height);
	}

	public boolean[] intersect(AABB r) {
	    boolean left   = this.getX() + this.width > r.getX() && this.getX() < r.getX();
	    boolean right  = r.getX() + r.width > this.getX() && r.getX() < this.getX();
	    boolean top    = this.getY() + this.height > r.getY() && this.getY() < r.getY();
	    boolean bottom = r.getY() + r.height > this.getY() && r.getY() < this.getY();

	    return new boolean[]{false, false, false, false};
	}
	
	public boolean intersect(Circle c) {
		int[] nearest_point = {
				Math.max(this.getX(), Math.min(this.getX()+height, c.getX())),
				Math.max(this.getY(), Math.min(this.getY()+width, c.getY()))
				};
		int X = c.getX()-nearest_point[0];
		int Y = c.getY()-nearest_point[1];
		return X*X + Y*Y <= c.getRayon()*c.getRayon();
	}

	public int getWidth() { return width; }
	public void setWidth(int width) {
		if (width<0) {
			throw new Error("Width is negative");
		}
		this.width = width;
	}
	public int getHeight() { return height; }
	public void setHeight(int height) {
		if (height<0) {
			throw new Error("Height is negative");
		}
		this.height = height;
	}
	
}
