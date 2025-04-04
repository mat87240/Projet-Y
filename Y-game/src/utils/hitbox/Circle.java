package utils.hitbox;

public class Circle extends Hitbox{

	private int rayon;
	
	public Circle(int x, int y, int r) {
		super(x,y);
		this.setRayon(r);
	}
	
	public boolean[] intersect(AABB h) {
		int[] nearest_point = {
				Math.max(h.getX(), Math.min(h.getX()+h.getHeight(), this.getX())),
				Math.max(h.getY(), Math.min(h.getY()+h.getWidth(), this.getY()))
				};
		int X = this.getX()-nearest_point[0];
		int Y = this.getY()-nearest_point[1];
		boolean is_intersecting = X*X + Y*Y <= this.rayon*this.rayon;
		
	    // Déterminer les côtés touchés
	    boolean left   = (this.getX() < h.getX()) && is_intersecting;
	    boolean right  = (this.getX() > h.getX() + h.getWidth()) && is_intersecting;
	    boolean top    = (this.getY() < h.getY()) && is_intersecting;
	    boolean bottom = (this.getY() > h.getY() + h.getHeight()) && is_intersecting;

	    return new boolean[]{top, right, bottom, left};
	}
	
	public boolean intersect(Circle c) {
		int X = this.getX() - c.getX();
		int Y = this.getY() - c.getY();
		int Z = this.rayon + c.rayon;
		return X*X+Y*Y <= Z*Z;
	}
	
	
	public int getRayon() { return this.rayon; }
	public void setRayon(int r) {
		if (r<0) { throw new Error("rayon is negative"); }
		this.rayon = r;
	}

}
