package walnoot.ld28.screens;

import walnoot.ld28.InputHandler;
import walnoot.ld28.LD28Game;
import walnoot.ld28.Util;
import walnoot.ld28.world.Background;
import walnoot.ld28.world.Connection;
import walnoot.ld28.world.GranaryNode;
import walnoot.ld28.world.Node;
import walnoot.ld28.world.NodeType;
import walnoot.ld28.world.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends UpdateScreen{
	private static final float MIN_DISTANCE = 1f;
	private static final float MAX_DISTANCE = 2f;
	
	private static final Vector2 TMP_2 = new Vector2();
	private static final Vector3 TMP_3 = new Vector3();
	
	private Node selectedNode;
	private Array<Node> nodes = new Array<Node>(false, 32);
	private Array<Connection> connections = new Array<Connection>(false, 32);
	private Array<Unit> units = new Array<Unit>(false, 32);
	
	private final SpriteBatch batch;
	private final OrthographicCamera camera;
	
	private Sprite preview;
	
	private Mode mode = Mode.NONE;
	private List buildList;
	
	private Background background;
	
	public GameScreen(LD28Game game){
		super(game);
		
		ShaderProgram shader =
				new ShaderProgram(Gdx.files.internal("shaders/alpha_test.vertex.glsl"),
						Gdx.files.internal("shaders/alpha_test.frag.glsl"));
		batch = new SpriteBatch(100, shader);
		camera = new OrthographicCamera();
		
		addNode(new GranaryNode(Vector2.Zero, this));
		
		preview = new Sprite(Util.ATLAS.findRegion("granary"));
		preview.setSize(1f, 1f);
		preview.setOrigin(0.5f, 0.5f);
		
		background = new Background(camera);
		
		setupUI();
	}
	
	private void setupUI(){
		game.getStage().clear();
		
		BitmapFont font = new BitmapFont();
		TextureRegionDrawable drawable = new TextureRegionDrawable(Util.ATLAS.findRegion("border"));
		ListStyle style = new ListStyle(font, Color.WHITE, Color.GRAY, drawable);
		
		Table table = new Table();
		table.top().left().setFillParent(true);
		game.getStage().addActor(table);
		
		buildList = new List(NodeType.values(), style);
		table.add(buildList);
	}
	
	@Override
	protected void render(){
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		
		background.render(batch);
		
		for(Connection c : connections){
			c.render(batch);
		}
		
		for(Node n : nodes){
			n.render(batch);
		}
		
		for(Unit u : units){
			u.render(batch);
		}
		
		if(mode == Mode.BUILD) preview.draw(batch);
		
		batch.end();
	}
	
	@Override
	public void update(){
		for(Node n : nodes){
			n.update();
		}
		
		for(Unit u : units){
			u.update();
		}
		
		for(int i = 0; i < units.size; i++){
			Unit unit = units.get(i);
			
			if(!unit.removed) unit.update();
			if(unit.removed) units.removeIndex(i--);
		}
		
		camera.unproject(TMP_3.set(Gdx.input.getX(), Gdx.input.getY(), 0f));
		
		TMP_2.set(TMP_3.x, TMP_3.y);
		
		switch (mode){
			case NONE:
				if(InputHandler.get().isJustTouched(Buttons.LEFT)){
					Node node = null;
					
					for(Node n : nodes){
						if(n.hits(TMP_2)){
							node = n;
							break;
						}
					}
					
					if(selectedNode != null) selectedNode.setSelected(false);
					selectedNode = node;
					if(node != null) selectedNode.setSelected(true);
				}
				
				break;
			case BUILD:
				for(int i = 0; i < NodeType.values().length; i++){
					NodeType type = NodeType.values()[i];
					
					if(type.getKey().isJustPressed()){
						buildList.setSelectedIndex(i);
						break;
					}
				}
				
				TMP_2.sub(selectedNode.position);
				
				if(TMP_2.len2() < MIN_DISTANCE * MIN_DISTANCE) TMP_2.nor().scl(MIN_DISTANCE);
				else if(TMP_2.len2() > MAX_DISTANCE * MAX_DISTANCE) TMP_2.nor().scl(MAX_DISTANCE);
				
				TMP_2.add(selectedNode.position);
				
				boolean buildable = true;
				
				for(Node n : nodes){
					if(n.hits(TMP_2, 0.5f)) buildable = false;
				}
				
				preview.setPosition(TMP_2.x - preview.getOriginX(), TMP_2.y - preview.getOriginY());
				preview.setColor(buildable ? Color.WHITE : Color.RED);
				
				if(buildable && InputHandler.get().isJustTouched(Buttons.LEFT)){
					Node newNode = getNodeType().newNode(TMP_2, this);
					addNode(newNode);
					addConnection(new Connection(selectedNode, newNode));
					
					mode = Mode.NONE;
				}
				
				if(InputHandler.get().isJustTouched(Buttons.RIGHT)) mode = Mode.NONE;
				
				break;
			default:
				break;
		}
		
		TMP_2.set(0f, 0f);
		
		if(InputHandler.get().up.isPressed()) TMP_2.add(0f, 1f);
		if(InputHandler.get().down.isPressed()) TMP_2.add(0f, -1f);
		if(InputHandler.get().left.isPressed()) TMP_2.add(-1f, 0f);
		if(InputHandler.get().right.isPressed()) TMP_2.add(1f, 0f);
		
		TMP_2.nor().scl(LD28Game.SECONDS_PER_UPDATE * camera.zoom);
		camera.position.add(TMP_2.x, TMP_2.y, 0f);
		
		camera.zoom += InputHandler.get().getScrollAmount();
		camera.zoom = MathUtils.clamp(camera.zoom, 1f, 10f);
		
		if(InputHandler.get().build.isJustPressed() && selectedNode != null) mode = Mode.BUILD;
		else if(InputHandler.get().quit.isJustPressed()) mode = Mode.NONE;
		
		buildList.setVisible(mode == Mode.BUILD);
	}
	
	private NodeType getNodeType(){
		return NodeType.values()[buildList.getSelectedIndex()];
	}
	
	public void addNode(Node n){
		nodes.add(n);
	}
	
	public void addConnection(Connection c){
		connections.add(c);
	}
	
	public void addUnit(Unit u){
		units.add(u);
	}
	
	@Override
	public void resize(int width, int height){
		camera.viewportWidth = 2f * width / height;
		camera.viewportHeight = 2f;
	}
	
	private enum Mode{
		NONE, BUILD;
	}
}
