package utils;

public class Vector {
	
	private double x,y,length;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
		this.setLength();
	}
	
	public Vector(double theta) {
		this.x = Math.cos(theta);
		this.y = Math.sin(theta);
	}
	
	public static Vector add(Vector v1, Vector v2) {
		return new Vector(v1.x+v2.x, v1.y+v2.y);
	}
	public void add(Vector v) {
		this.x += v.x; 
		this.y += v.y;
		this.setLength();
	}
	
	public static Vector normalize(Vector v) {
		return new Vector(v.x/v.length, v.y/v.length);
	}
	public void normalize() {
		this.x /= this.length;
		this.y /= this.length;
		this.length = 1;
	}
	
	public static Vector scalar(Vector v, double k) {
		return new Vector(v.x*k, v.y*k);
	}
	
	public void scalar(double k) {
		this.x *= k;
		this.y *= k;
		this.length *= k;
	}
	
	public static double dotProduct(Vector v1, Vector v2) {
		return v1.x*v2.x+v1.y*v2.y;
	}

	private void setLength() {
		this.length = Math.sqrt(this.x*this.x+this.y*this.y);
	}
}
