package walnoot.ld28.world;

import walnoot.ld28.InputHandler;
import walnoot.ld28.InputHandler.Key;
import walnoot.ld28.Util;
import walnoot.ld28.screens.GameScreen;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public enum NodeType{
	STORAGE("Storage", "storage", InputHandler.get().buildStorage){
		@Override
		public Node newNode(Vector2 pos, GameScreen screen){
			return new StorageNode(pos, screen);
		}
	},
	FARM("Farm", "farm", InputHandler.get().buildFarm){
		@Override
		public Node newNode(Vector2 pos, GameScreen screen){
			return new FarmNode(pos, screen);
		}
	},
	LUMBER_CAMP("Lumber Camp", "lumber", InputHandler.get().buildLumber){
		@Override
		public Node newNode(Vector2 pos, GameScreen screen){
			return new LumberNode(pos, screen);
		}
	};
	
	private final String name;
	private final Key key;
	private TextureRegion region;
	
	private NodeType(String name, String spriteName, Key key){
		this.key = key;
		this.name = name + " (" + Keys.toString(key.getFirstKey()) + ")";
		
		region = Util.ATLAS.findRegion(spriteName);
	}
	
	public String toString(){
		return name;
	}
	
	public Key getKey(){
		return key;
	}
	
	public TextureRegion getRegion(){
		return region;
	}
	
	public abstract Node newNode(Vector2 pos, GameScreen screen);
}
