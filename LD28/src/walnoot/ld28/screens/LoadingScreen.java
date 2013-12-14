package walnoot.ld28.screens;

import walnoot.ld28.LD28Game;
import walnoot.ld28.Util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class LoadingScreen extends UpdateScreen{
	private static final String TEST = "test.png";
	private static final String HAMMER = "hammer.png";
	private static final String DOT = "dot.png";
	
	private AssetManager assetManager;
	
	public LoadingScreen(LD28Game game){
		super(game);
		
		assetManager = new AssetManager();
		assetManager.load(DOT, Texture.class);
		assetManager.load(HAMMER, Texture.class);
		assetManager.load(TEST, Texture.class);
	}
	
	@Override
	protected void render(){
	}
	
	@Override
	public void update(){
		if(assetManager.update()){
			Util.DOT_TEXTURE = assetManager.get(DOT);
			Util.DOT_TEXTURE.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			Util.HAMMER_TEXTURE = assetManager.get(HAMMER);
			Util.HAMMER_TEXTURE.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			Util.TEST_TEXTURE = assetManager.get(TEST);
			Util.TEST_TEXTURE.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			game.setScreen(new GameScreen(game));
		}
	}
}
