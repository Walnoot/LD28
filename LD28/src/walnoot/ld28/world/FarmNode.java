package walnoot.ld28.world;

import walnoot.ld28.LD28Game;
import walnoot.ld28.screens.GameScreen;
import walnoot.ld28.world.Resource.ResourceType;

import com.badlogic.gdx.math.Vector2;

public class FarmNode extends Node{
	private static final int RESOURCE_SPAWN_TIME = (int) (2f * LD28Game.UPDATES_PER_SECOND);
	
	private int resourceTimer;
	private GranaryNode granary;
	
	public FarmNode(Vector2 pos, GameScreen screen){
		super(pos, screen, NodeType.FARM.getSpriteName());
	}
	
	@Override
	public void update(){
		if(++resourceTimer == RESOURCE_SPAWN_TIME){
			resourceTimer = 0;
			
			if(granary != null){
				Resource resource = new Resource(this, granary, ResourceType.GRAIN);
				screen.addUnit(resource);
			}
		}
	}
	
	@Override
	public void addConnection(Connection c){
		super.addConnection(c);
		
		if(c.first instanceof GranaryNode) granary = (GranaryNode) c.first;
		if(c.second instanceof GranaryNode) granary = (GranaryNode) c.second;
	}
}
