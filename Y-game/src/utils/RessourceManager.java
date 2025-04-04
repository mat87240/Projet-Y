package utils;

import java.util.HashMap;

public class RessourceManager implements Runnable{

	private static HashMap<String, String> textInputValue = new HashMap<String, String>();
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static String getTextInputValue(String key) { return textInputValue.get(key); }
	public static void setTextInputValue(String key, String value) { textInputValue.put(key, value); }
}
