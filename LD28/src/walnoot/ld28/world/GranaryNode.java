package walnoot.ld28.world;

import walnoot.ld28.screens.GameScreen;

import com.badlogic.gdx.math.Vector2;

public class GranaryNode extends Node{
	public GranaryNode(Vector2 pos, GameScreen screen){
		super(pos, screen, NodeType.GRANARY.getSpriteName());
	}
}
