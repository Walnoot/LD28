package walnoot.ld28;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Node{
	private static final Vector2 TMP = new Vector2();
	
	private boolean selected;
	
	private final Sprite sprite;
	public final Vector2 position = new Vector2();
	
	private Array<Node> children;
	private final Node parent;
	
	private float[] vertices;
	
	public Node(Vector2 pos, Node parent){
		this(pos.x, pos.y, parent);
	}
	
	public Node(float x, float y, Node parent){
		this.parent = parent;
		position.set(x, y);
		
		sprite = new Sprite(Util.DOT_TEXTURE);
		sprite.setSize(1f, 1f);
		sprite.setOrigin(0.5f, 0.5f);
		
		if(parent != null) buildRoad();
	}
	
	private void buildRoad(){
		vertices = new float[5 * 4];
		
		TMP.set(position).sub(parent.position).nor().rotate(90f).scl(0.25f);
//		TMP.set(0f, 0.25f);
		
		float color = Color.WHITE.toFloatBits();
		
		vertices[SpriteBatch.X1] = position.x - TMP.x;
		vertices[SpriteBatch.Y1] = position.y - TMP.y;
		vertices[SpriteBatch.C1] = color;
		vertices[SpriteBatch.U1] = 0f;
		vertices[SpriteBatch.V1] = 0f;
		vertices[SpriteBatch.X2] = position.x + TMP.x;
		vertices[SpriteBatch.Y2] = position.y + TMP.y;
		vertices[SpriteBatch.C2] = color;
		vertices[SpriteBatch.U2] = 1f;
		vertices[SpriteBatch.V2] = 0f;
		vertices[SpriteBatch.X3] = parent.position.x + TMP.x;
		vertices[SpriteBatch.Y3] = parent.position.y + TMP.y;
		vertices[SpriteBatch.C3] = color;
		vertices[SpriteBatch.U3] = 1f;
		vertices[SpriteBatch.V3] = 1f;
		vertices[SpriteBatch.X4] = parent.position.x - TMP.x;
		vertices[SpriteBatch.Y4] = parent.position.y - TMP.y;
		vertices[SpriteBatch.C4] = color;
		vertices[SpriteBatch.U4] = 0f;
		vertices[SpriteBatch.V4] = 1f;
	}
	
	public void render(SpriteBatch batch, boolean roads){
		if(!roads){
			sprite.setPosition(position.x - sprite.getOriginX(), position.y - sprite.getOriginY());
			sprite.draw(batch);
		}else if(parent != null) batch.draw(Util.TEST_TEXTURE, vertices, 0, vertices.length);
		
		if(children != null){
			for(Node child : children){
				child.render(batch, roads);
			}
		}
	}
	
	public void update(){
	}
	
	public void addChild(Node child){
		if(children == null) children = new Array<Node>(false, 4);
		
		children.add(child);
	}
	
	public Node getNode(Vector2 pos){
		if(TMP.set(position).sub(pos).len2() < 0.5f * 0.5f) return this;
		else{
			if(children != null){
				for(Node child : children){
					Node node = child.getNode(pos);
					if(node != null) return node;
				}
			}
			
			return null;
		}
	}
	
	public void setSelected(boolean selected){
		sprite.setColor(selected ? Color.RED : Color.WHITE);
		
		this.selected = selected;
	}
	
	public boolean isSelected(){
		return selected;
	}
}
