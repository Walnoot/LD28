package walnoot.ld28.world;

import walnoot.ld28.InputHandler;
import walnoot.ld28.InputHandler.Key;
import walnoot.ld28.screens.GameScreen;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public enum NodeType{
	GRANARY("Granary", "granary", InputHandler.get().buildGranary){
		@Override
		public Node newNode(Vector2 pos, GameScreen screen){
			return new GranaryNode(pos, screen);
		}
	},
	FARM("Farm", "farm", InputHandler.get().buildFarm){
		@Override
		public Node newNode(Vector2 pos, GameScreen screen){
			return new FarmNode(pos, screen);
		}
	};
	
	private final String name;
	private final String spriteName;
	private final Key key;
	
	private NodeType(String name, String spriteName, Key key){
		this.key = key;
		this.name = name + " (" + Keys.toString(key.getFirstKey()) + ")";
		this.spriteName = spriteName;
	}
	
	public String toString(){
		return name;
	}
	
	public String getSpriteName(){
		return spriteName;
	}
	
	public Key getKey(){
		return key;
	}
	
	public abstract Node newNode(Vector2 pos, GameScreen screen);
}
