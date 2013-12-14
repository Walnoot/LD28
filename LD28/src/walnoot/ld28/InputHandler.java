package walnoot.ld28;

import static com.badlogic.gdx.Input.Keys.*;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor{
	private static final InputHandler instance = new InputHandler();
	
	public static InputHandler get(){
		return instance;
	}
	
	public Key up = new Key(W, UP);
	public Key down = new Key(S, DOWN);
	public Key left = new Key(A, LEFT);
	public Key right = new Key(D, RIGHT);
	
	public Key build = new Key(B);
	public Key quit = new Key(ESCAPE);
	
	public Key buildFarm = new Key(F);
	public Key buildGranary = new Key(G);
	
	private ArrayList<Key> keys;
	private boolean keyDown;
	private int scrollAmount;
	private boolean[] justTouched = new boolean[3];
	
	private InputHandler(){
	}
	
	/**
	 * Make sure to call after game logic update() is called
	 */
	public void update(){
		for(int i = 0; i < keys.size(); i++){
			keys.get(i).update();
		}
		
		keyDown = false;
		scrollAmount = 0;
		
		for(int i = 0; i < justTouched.length; i++){
			justTouched[i] = false;
		}
	}
	
	public Key newKey(int... keys){
		return new Key(keys);
	}
	
	public boolean isAnyKeyDown(){
		return keyDown;
	}
	
	public int getScrollAmount(){
		return scrollAmount;
	}
	
	public boolean isJustTouched(){
		return isJustTouched(Buttons.LEFT);
	}
	
	public boolean isJustTouched(int button){
		return justTouched[button];
	}
	
	public boolean keyDown(int keyCode){
		for(int i = 0; i < keys.size(); i++){
			if(keys.get(i).has(keyCode)) keys.get(i).press();
		}
		
		keyDown = true;
		
		return false;
	}
	
	public boolean keyUp(int keyCode){
		for(int i = 0; i < keys.size(); i++){
			if(keys.get(i).has(keyCode)) keys.get(i).release();
		}
		
		return false;
	}
	
	public boolean keyTyped(char character){
		return false;
	}
	
	public boolean touchDown(int x, int y, int pointer, int button){
		justTouched[button] = true;
		return false;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button){
		return false;
	}
	
	public boolean touchDragged(int x, int y, int pointer){
		return false;
	}
	
	public boolean touchMoved(int x, int y){
		return false;
	}
	
	public boolean scrolled(int amount){
		scrollAmount += amount;
		
		return false;
	}
	
	public boolean mouseMoved(int screenX, int screenY){
		return false;
	}
	
	public class Key{
		private final int[] keyCodes;
		private boolean pressed, justPressed;
		
		public Key(int... keyCodes){
			this.keyCodes = keyCodes;
			
			if(keys == null) keys = new ArrayList<Key>();
			keys.add(this);
		}
		
		private void update(){
			justPressed = false;
		}
		
		public boolean has(int keyCode){
			for(int i = 0; i < keyCodes.length; i++){
				if(keyCodes[i] == keyCode) return true;
			}
			
			return false;
		}
		
		private void press(){
			pressed = true;
			justPressed = true;
		}
		
		private void release(){
			pressed = false;
		}
		
		public boolean isPressed(){
			return pressed;
		}
		
		public boolean isJustPressed(){
			return justPressed;
		}
		
		public int getFirstKey(){
			return keyCodes[0];
		}
	}
}
