package walnoot.ld28.world;

public class Resource extends Unit{
	private final ResourceType type;
	
	public Resource(Node start, Node end, ResourceType type){
		super(start, end, type.getName());
		this.type = type;
	}
	
	@Override
	protected void reachedDestination(Node node){
		if(node instanceof GranaryNode){
			((GranaryNode) node).addResource(type, 1);
		}
	}
	
	public enum ResourceType{
		GRAIN("grain"), WOOD("wood");
		
		private final String name;
		
		private ResourceType(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
	}
}
