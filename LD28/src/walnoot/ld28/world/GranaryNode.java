package walnoot.ld28.world;

import walnoot.ld28.screens.GameScreen;
import walnoot.ld28.world.Resource.ResourceType;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;

public class GranaryNode extends Node{
	private ObjectIntMap<ResourceType> resources = new ObjectIntMap<Resource.ResourceType>();
	private ObjectMap<ResourceType, Label> labelMap = new ObjectMap<Resource.ResourceType, Label>();
	
	public GranaryNode(Vector2 pos, GameScreen screen){
		super(pos, screen, NodeType.GRANARY.getRegion());
		
		for(ResourceType type : ResourceType.values()){
			resources.put(type, 0);
		}
	}
	
	public void addResource(ResourceType type, int amount){
		resources.getAndIncrement(type, 0, amount);
		
		if(isSelected()){
			Label label = labelMap.get(type);
			
			if(label != null) label.setText(Integer.toString(resources.get(type, 0)));
		}
	}
	
	@Override
	public void fillDescription(Table descTable, LabelStyle style){
		descTable.add(new Label("Granary", style)).colspan(2);
		descTable.row();
		
		for(ResourceType type : ResourceType.values()){
			Label label = labelMap.get(type);
			
			if(label == null){
				label = new Label("", style);
				labelMap.put(type, label);
			}
			
			label.setText(Integer.toString(resources.get(type, 0)));
			
			descTable.add(new Label(type.getName() + ":", style));
			descTable.add(label).right();
			descTable.row();
		}
	}
}
