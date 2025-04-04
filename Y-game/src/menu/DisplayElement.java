package menu;

public abstract class DisplayElement {
	
	private int absoluteX, absoluteY, relativeX, relativeY;
	
	public abstract void render();
	
	public int getAbsoluteX() { return this.absoluteX; }
	public void setAbsoluteX(int absoluteX) { 
		this.absoluteX = this.absoluteX>=0 ? absoluteX : this.absoluteX; 
	}
	
	public int getAbsoluteY() { return this.absoluteY; }
	public void setAbsoluteY(int absoluteY) { 
		this.absoluteY = this.absoluteY>=0 ? absoluteY : this.absoluteY; 
	}
	public int getRelativeX() { return this.relativeX; }
	public void setRelativeX(int relativeX) { 
		this.relativeX = this.relativeX>=0 ? relativeX : this.relativeX;
		this.setAbsoluteX(this.absoluteX+this.relativeX);
	}
	public int getRelativeY() { return this.relativeY; }
	public void setRelativeY(int relativeY) { 
		this.relativeY = this.relativeY>=0 ? relativeY : this.relativeY;
		this.setAbsoluteY(this.absoluteY+this.relativeY);
	}

}
