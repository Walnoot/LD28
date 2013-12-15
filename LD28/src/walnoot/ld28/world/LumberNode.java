package walnoot.ld28.world;

import walnoot.ld28.screens.GameScreen;
import walnoot.ld28.world.Resource.ResourceType;

import com.badlogic.gdx.math.Vector2;

public class LumberNode extends ResourceNode{
	public LumberNode(Vector2 pos, GameScreen screen){
		super(pos, screen, NodeType.LUMBER_CAMP.getRegion());
	}
	
	@Override
	protected ResourceType getType(){
		return ResourceType.WOOD;
	}
}
