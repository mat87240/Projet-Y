package menu;

public class TextInput extends DisplayElement{

	private String text, id, placeholder;
	
	public TextInput(int relativeX, int relativeY, Menu menu, String id) {
		this.setAbsoluteX(relativeX + menu.getAbsoluteX());
		this.setAbsoluteY(relativeY + menu.getAbsoluteY());
		this.id = id;
		this.placeholder = "";
		
	}
	
	public TextInput(int relativeX, int relativeY, Menu menu, String id, String placeholder) {
		this.setAbsoluteX(relativeX + menu.getAbsoluteX());
		this.setAbsoluteY(relativeY + menu.getAbsoluteY());
		this.id = id;
		this.placeholder = placeholder;
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	public String getID() { return this.id; }
	public String getPlaceholder() { return placeholder; }

}
