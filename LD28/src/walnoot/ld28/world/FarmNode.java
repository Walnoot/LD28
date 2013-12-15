package walnoot.ld28.world;

import walnoot.ld28.LD28Game;
import walnoot.ld28.screens.GameScreen;
import walnoot.ld28.world.Resource.ResourceType;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class FarmNode extends Node{
	private static final int RESOURCE_SPAWN_TIME = (int) (2f * LD28Game.UPDATES_PER_SECOND);
	
	private int resourceTimer;
	private GranaryNode granary;
	
	public FarmNode(Vector2 pos, GameScreen screen){
		super(pos, screen, NodeType.FARM.getRegion());
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
	
	@Override
	public void fillDescription(Table descTable, LabelStyle style){
		descTable.add(new Label("Farm", style)).colspan(2);
		descTable.row();
	}
}
