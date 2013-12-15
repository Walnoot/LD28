package walnoot.ld28.world;

import walnoot.ld28.LD28Game;
import walnoot.ld28.Pathfinder;
import walnoot.ld28.Pathfinder.PathNode;
import walnoot.ld28.Util;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Unit{
	private static final Vector2 TMP = new Vector2();
	
	private final Sprite sprite;
	private final Node start, end;
	
	public boolean removed;
	
	private PathNode path;
	
	private float progress;
	
	public Unit(Node start, Node end, String spriteName){
		this.start = start;
		this.end = end;
		
		path = Pathfinder.get().getPath(start, end);
		
		sprite = new Sprite(Util.ATLAS.findRegion(spriteName));
		sprite.setSize(0.5f, 0.5f);
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
	}
	
	public void render(SpriteBatch batch){
		if(path != null && path.parent != null)
			TMP.set(path.node.position).lerp(path.parent.node.position, progress / path.distance);
		
		sprite.setPosition(TMP.x - sprite.getOriginX(), TMP.y - sprite.getOriginY());
		sprite.draw(batch);
	}
	
	public void update(){
		if(path != null){
			progress += LD28Game.SECONDS_PER_UPDATE;
			
			while(progress > path.distance){
				progress -= path.distance;
				
				if(path.parent == null){
					reachedDestination(path.node);
				}
				
				path = path.parent;
				
				if(path == null){
					remove();
					break;
				}
			}
		}
	}
	
	protected void reachedDestination(Node node){
		node.unitReached(this);
	}
	
	public void remove(){
		removed = true;
	}
}
