package walnoot.ld28.world;

import walnoot.ld28.Util;
import walnoot.ld28.screens.GameScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Node{
	private boolean selected;
	
	private final Sprite sprite;
	public final Vector2 position = new Vector2();
	protected final GameScreen screen;
	
	private Array<Connection> connections = new Array<Connection>(false, 4);
	
	public Node(Vector2 pos, GameScreen screen, String spriteName){
		this(pos.x, pos.y, screen, spriteName);
	}
	
	public Node(float x, float y, GameScreen screen, String spriteName){
		this.screen = screen;
		position.set(x, y);
		
		sprite = new Sprite(Util.ATLAS.findRegion(spriteName));
		sprite.setSize(1f, 1f);
		sprite.setOrigin(0.5f, 0.5f);
	}
	
	public void render(SpriteBatch batch){
		if(selected) sprite.setColor(System.currentTimeMillis() / 300 % 2 == 0 ? Color.WHITE : Color.GRAY);
		else sprite.setColor(Color.WHITE);
		
		sprite.setPosition(position.x - sprite.getOriginX(), position.y - sprite.getOriginY());
		sprite.draw(batch);
	}
	
	public void update(){
	}
	
	public boolean hits(Vector2 pos){
		return hits(pos, 0f);
	}
	
	public boolean hits(Vector2 pos, float radius){
		return position.dst2(pos) < (radius + 0.5f) * (radius + 0.5f);
	}
	
	public void addConnection(Connection c){
		connections.add(c);
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public Array<Connection> getConnections(){
		return connections;
	}
}
