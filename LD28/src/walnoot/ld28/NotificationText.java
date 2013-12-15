package walnoot.ld28;

import com.badlogic.gdx.utils.StringBuilder;

public class NotificationText implements CharSequence{
	public final static NotificationText instance = new NotificationText();
	
	private String[] lines = new String[4];
	private StringBuilder builder = new StringBuilder();
	
	public boolean dirty;
	
	public void addLine(String text){
		for(int i = 0; i < lines.length; i++){
			if(i == lines.length - 1) lines[i] = text;
			else lines[i] = lines[i + 1];
		}
		
		builder.setLength(0);
		
		for(String s : lines){
			if(s == null) continue;
			builder.append(s).append('\n');
		}
		
		dirty = true;
	}
	
	@Override
	public char charAt(int index){
		return builder.charAt(index);
	}
	
	@Override
	public int length(){
		return builder.length;
	}
	
	@Override
	public CharSequence subSequence(int start, int end){
		return builder.subSequence(start, end);
	}
	
	@Override
	public String toString(){
		return builder.toString();
	}
}
