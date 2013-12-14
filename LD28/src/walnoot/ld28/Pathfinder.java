package walnoot.ld28;

import walnoot.ld28.world.Connection;
import walnoot.ld28.world.Node;

import com.badlogic.gdx.utils.Array;

public class Pathfinder{
	private static final Pathfinder instance = new Pathfinder();
	
	private Array<PathNode> openList = new Array<PathNode>(false, 64);
	private Array<PathNode> closedList = new Array<PathNode>(false, 64);
	
	public static Pathfinder get(){
		return instance;
	}
	
	public PathNode getPath(Node start, Node end){
		openList.size = 0;
		closedList.size = 0;
		
		openList.add(new PathNode(end, null, 0f));
		
		while(openList.size > 0){
			PathNode current = getLowestG();
			
			if(current.node == start) return current;
			
			openList.removeValue(current, true);
			closedList.add(current);
			
			for(Connection connection : current.node.getConnections()){
				Node nextNode = connection.first == current.node ? connection.second : connection.first;
				
				checkNewNode(new PathNode(nextNode, current, connection.getDistance()));
			}
		}
		
		return null;
	}
	
	private void checkNewNode(PathNode node){
		if(isOnList(closedList, node) != null) return;
		
		PathNode openNode = isOnList(openList, node);
		if(openNode == null) openList.add(node);
		else{
			if(node.g < openNode.g){
				openNode.parent = node.parent;
				openNode.g = node.g;
			}
		}
	}
	
	private PathNode isOnList(Array<PathNode> nodes, PathNode node){
		for(PathNode n : nodes){
			if(n.node == node.node) return n;
		}
		
		return null;
	}
	
	private PathNode getLowestG(){
		float lowestG = Integer.MAX_VALUE;
		PathNode lowestNode = null;
		
		for(int i = 0; i < openList.size; i++){
			PathNode node = openList.get(i);
			float g = node.g;
			
			if(g < lowestG){
				lowestG = g;
				lowestNode = node;
			}
		}
		
		return lowestNode;
	}
	
	public class PathNode{
		public final Node node;
		public PathNode parent;
		
		private float g;
		public float distance;
		
		private PathNode(Node node, PathNode parent, float distance){
			this.node = node;
			this.parent = parent;
			this.distance = distance;
			
			if(parent != null) g = parent.g + distance;
		}
	}
}
