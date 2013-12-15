package walnoot.ld28.world;

import walnoot.ld28.LD28Game;
import walnoot.ld28.screens.GameScreen;
import walnoot.ld28.world.Resource.ResourceType;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class ResourceNode extends Node{
	private static final int RESOURCE_SPAWN_TIME = (int) (2f * LD28Game.UPDATES_PER_SECOND);
	
	private int resourceTimer;
	private StorageNode storage;
	
	public ResourceNode(Vector2 pos, GameScreen screen, TextureRegion region){
		super(pos, screen, region);
	}
	
	@Override
	public void update(){
		if(++resourceTimer == RESOURCE_SPAWN_TIME){
			resourceTimer = 0;
			
			if(storage != null){
				Resource resource = new Resource(this, storage, getType());
				screen.addUnit(resource);
			}
		}
	}
	
	protected abstract ResourceType getType();
	
	@Override
	public void addConnection(Connection c){
		super.addConnection(c);
		
		if(c.first instanceof StorageNode) storage = (StorageNode) c.first;
		if(c.second instanceof StorageNode) storage = (StorageNode) c.second;
	}
	
	@Override
	public void fillDescription(Table descTable, LabelStyle style){
		descTable.add(new Label("Farm", style)).colspan(2);
		descTable.row();
	}
}
