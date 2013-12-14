package walnoot.ld28.world;

import walnoot.ld28.Util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;

public class Connection{
	private static final Vector2 TMP = new Vector2();
	
	public final Node first, second;
	private float distance;
	private float[] roadVertices;
	private Texture texture;
	
	public Connection(Node first, Node second){
		this.first = first;
		this.second = second;
		
		distance = first.position.dst(second.position);
		
		first.addConnection(this);
		second.addConnection(this);
		
		buildRoad();
	}
	
	private void buildRoad(){
		roadVertices = new float[5 * 4];
		
		AtlasRegion road = Util.ATLAS.findRegion("test");
		texture = road.getTexture();
		
		TMP.set(first.position).sub(second.position).nor().rotate(90f).scl(0.25f);
		
		float color = Color.WHITE.toFloatBits();
		float length = first.position.dst(second.position) * 2f;
		
		roadVertices[SpriteBatch.X1] = first.position.x - TMP.x;
		roadVertices[SpriteBatch.Y1] = first.position.y - TMP.y;
		roadVertices[SpriteBatch.C1] = color;
		roadVertices[SpriteBatch.U1] = road.getU();
		roadVertices[SpriteBatch.V1] = road.getV();
		roadVertices[SpriteBatch.X2] = first.position.x + TMP.x;
		roadVertices[SpriteBatch.Y2] = first.position.y + TMP.y;
		roadVertices[SpriteBatch.C2] = color;
		roadVertices[SpriteBatch.U2] = road.getU2();
		roadVertices[SpriteBatch.V2] = road.getV();
		roadVertices[SpriteBatch.X3] = second.position.x + TMP.x;
		roadVertices[SpriteBatch.Y3] = second.position.y + TMP.y;
		roadVertices[SpriteBatch.C3] = color;
		roadVertices[SpriteBatch.U3] = road.getU2();
		roadVertices[SpriteBatch.V3] = road.getV2() * length;
		roadVertices[SpriteBatch.X4] = second.position.x - TMP.x;
		roadVertices[SpriteBatch.Y4] = second.position.y - TMP.y;
		roadVertices[SpriteBatch.C4] = color;
		roadVertices[SpriteBatch.U4] = road.getU();
		roadVertices[SpriteBatch.V4] = road.getV2() * length;
	}
	
	public void render(SpriteBatch batch){
		batch.draw(texture, roadVertices, 0, roadVertices.length);
	}
	
	public float getDistance(){
		return distance;
	}
}
