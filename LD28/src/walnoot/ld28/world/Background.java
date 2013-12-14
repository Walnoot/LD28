package walnoot.ld28.world;

import walnoot.ld28.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Background{
	private static final Vector3 TMP = new Vector3();
	private static final float SCALE = 0.5f;
	
	private final OrthographicCamera camera;
	private final float[] vertices = new float[20];
	
	public Background(OrthographicCamera camera){
		this.camera = camera;
		
		float color = Color.WHITE.toFloatBits();
		
		vertices[SpriteBatch.C1] = color;
		vertices[SpriteBatch.C2] = color;
		vertices[SpriteBatch.C3] = color;
		vertices[SpriteBatch.C4] = color;
		
		vertices[SpriteBatch.U1] = 0f;
		vertices[SpriteBatch.U2] = 1f;
		vertices[SpriteBatch.U3] = 1f;
		vertices[SpriteBatch.U4] = 0f;
		
		vertices[SpriteBatch.V1] = 0f;
		vertices[SpriteBatch.V2] = 0f;
		vertices[SpriteBatch.V3] = 1f;
		vertices[SpriteBatch.V4] = 1f;
		
		Util.BACKGROUND.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Util.BACKGROUND.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public void render(SpriteBatch batch){
		TMP.set(0f, Gdx.graphics.getHeight(), 0f);
		camera.unproject(TMP);
		
		float minX = TMP.x;
		float minY = TMP.y;
		
		TMP.x = Gdx.graphics.getWidth();
		TMP.y = 0f;
		
		camera.unproject(TMP);
		
		float maxX = TMP.x;
		float maxY = TMP.y;
		
		vertices[SpriteBatch.X1] = minX;
		vertices[SpriteBatch.Y1] = minY;
		
		vertices[SpriteBatch.X2] = maxX;
		vertices[SpriteBatch.Y2] = minY;
		
		vertices[SpriteBatch.X3] = maxX;
		vertices[SpriteBatch.Y3] = maxY;
		
		vertices[SpriteBatch.X4] = minX;
		vertices[SpriteBatch.Y4] = maxY;
		
		vertices[SpriteBatch.U1] = vertices[SpriteBatch.X1] * SCALE;
		vertices[SpriteBatch.V1] = vertices[SpriteBatch.Y1] * SCALE;
		
		vertices[SpriteBatch.U2] = vertices[SpriteBatch.X2] * SCALE;
		vertices[SpriteBatch.V2] = vertices[SpriteBatch.Y2] * SCALE;
		
		vertices[SpriteBatch.U3] = vertices[SpriteBatch.X3] * SCALE;
		vertices[SpriteBatch.V3] = vertices[SpriteBatch.Y3] * SCALE;
		
		vertices[SpriteBatch.U4] = vertices[SpriteBatch.X4] * SCALE;
		vertices[SpriteBatch.V4] = vertices[SpriteBatch.Y4] * SCALE;
		
		batch.draw(Util.BACKGROUND, vertices, 0, vertices.length);
	}
}
