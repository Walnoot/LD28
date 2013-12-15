package walnoot.ld28.screens;

import walnoot.ld28.LD28Game;
import walnoot.ld28.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class IntroScreen extends UpdateScreen{
	private static final int NEW_LINE_TIME = (int) (2f * LD28Game.UPDATES_PER_SECOND);
	
	private Table table;
	private LabelStyle labelStyle;
	
	private int timer, index;
	private String[] text;
	
	public IntroScreen(LD28Game game){
		super(game);
		
		text = Gdx.files.internal("intro.txt").readString().split("\n");
		
		game.getStage().clear();
		
		table = new Table();
		table.top().left().pad(16f).setFillParent(true);
		game.getStage().addActor(table);
		
		labelStyle = new LabelStyle(Util.FONT, Color.WHITE);
	}
	
	@Override
	protected void render(){
		Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void update(){
		if(++timer == NEW_LINE_TIME){
			timer = 0;
			
			if(index < text.length) addLine(text[index++]);
			else game.setScreen(new GameScreen(game));
		}
	}
	
	private void addLine(String string){
		table.add(new Label(string, labelStyle)).left();
		table.row();
	}
}
