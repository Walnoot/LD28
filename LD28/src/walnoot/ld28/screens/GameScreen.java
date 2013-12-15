package walnoot.ld28.screens;

import walnoot.ld28.InputHandler;
import walnoot.ld28.LD28Game;
import walnoot.ld28.NotificationText;
import walnoot.ld28.Util;
import walnoot.ld28.world.Background;
import walnoot.ld28.world.Connection;
import walnoot.ld28.world.Node;
import walnoot.ld28.world.NodeType;
import walnoot.ld28.world.StorageNode;
import walnoot.ld28.world.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
	private List buildList, modeList;
	private boolean buildListVisible, modeListVisible;
	private Table table;
	private Label label;
	private Table descTable;
	private LabelStyle labelStyle;
	
	private Background background;
	
	public GameScreen(LD28Game game){
		super(game);
		
		ShaderProgram shader =
				new ShaderProgram(Gdx.files.internal("shaders/alpha_test.vertex.glsl"),
						Gdx.files.internal("shaders/alpha_test.frag.glsl"));
		batch = new SpriteBatch(100, shader);
		camera = new OrthographicCamera();
		
		addNode(new StorageNode(Vector2.Zero, this));
		
		preview = new Sprite(Util.ATLAS.findRegion("storage"));
		preview.setSize(1f, 1f);
		preview.setOrigin(0.5f, 0.5f);
		
		background = new Background(camera);
		
		setupUI();
	}
	
	private void setupUI(){
		game.getStage().clear();
		
		TextureRegionDrawable drawable = new TextureRegionDrawable(Util.ATLAS.findRegion("border"));
		ListStyle style = new ListStyle(Util.FONT, Color.WHITE, Color.LIGHT_GRAY, drawable);
		
		table = new Table();
		table.top().left().pad(32f).setFillParent(true);
		game.getStage().addActor(table);
		
		buildList = new List(NodeType.values(), style);
		
		modeList = new List(Mode.values(), style);
		modeList.setSelectedIndex(-1);
		modeList.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				int index = modeList.getSelectedIndex();
				
				if(index != -1){
					if(Mode.values()[index] == Mode.NONE) event.cancel();
					
					mode = Mode.values()[index];
				}
			}
		});
		
		Table dialogTable = new Table();
		dialogTable.bottom().right().setFillParent(true);
		game.getStage().addActor(dialogTable);
		
		labelStyle = new LabelStyle(Util.FONT, Color.WHITE);
		label = new Label(NotificationText.instance, labelStyle);
		label.setWrap(true);
		dialogTable.add(label).width(300f);
		
		descTable = new Table();
		descTable.bottom().left().pad(32f).setFillParent(true);
		game.getStage().addActor(descTable);
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
					Node node = getNode(TMP_2);
					
					descTable.clear();
					
					if(selectedNode != null) selectedNode.setSelected(false);
					selectedNode = node;
					if(node != null){
						selectedNode.setSelected(true);
						
						selectedNode.fillDescription(descTable, labelStyle);
					}
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
				
				if(selectedNode == null) break;
				
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
				preview.setRegion(NodeType.values()[buildList.getSelectedIndex()].getRegion());
				
				if(buildable && InputHandler.get().isJustTouched(Buttons.LEFT)){
					Node newNode = getNodeType().newNode(TMP_2, this);
					addNode(newNode);
					addConnection(new Connection(selectedNode, newNode));
					
					mode = Mode.NONE;
				}
				
				if(InputHandler.get().isJustTouched(Buttons.RIGHT)) mode = Mode.NONE;
				
				break;
			case BUILD_ROAD:
				if(InputHandler.get().isJustTouched(Buttons.LEFT)){
					Node node = getNode(TMP_2);
					
					if(node.position.dst2(selectedNode.position) < MIN_DISTANCE * MIN_DISTANCE){
						NotificationText.instance.addLine("Road too short!");
						break;
					}else if(node.position.dst2(selectedNode.position) > MAX_DISTANCE * MAX_DISTANCE){
						NotificationText.instance.addLine("Road too long!");
						break;
					}
					
					if(node != null && node != selectedNode && !node.connectsTo(selectedNode)){
						addConnection(new Connection(node, selectedNode));
						
						mode = Mode.NONE;
					}
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
		
		if(selectedNode != null){
			if(InputHandler.get().build.isJustPressed()) mode = Mode.BUILD;
			if(InputHandler.get().buildRoad.isJustPressed()) mode = Mode.BUILD_ROAD;
		}
		
		if(InputHandler.get().quit.isJustPressed()) mode = Mode.NONE;
		
		if(mode == Mode.BUILD){
			if(!buildListVisible){
				table.add(buildList);
				buildListVisible = true;
			}
		}else if(buildListVisible){
			table.removeActor(buildList);
			buildListVisible = false;
		}
		
		if(mode == Mode.NONE && selectedNode != null){
			if(!modeListVisible){
				modeList.setSelectedIndex(-1);
				table.add(modeList);
				modeListVisible = true;
			}
		}else if(modeListVisible){
			table.removeActor(modeList);
			modeListVisible = false;
		}
		
		if(NotificationText.instance.dirty) label.setText(NotificationText.instance);
	}
	
	private Node getNode(Vector2 pos){
		for(Node n : nodes){
			if(n.hits(pos)) return n;
		}
		
		return null;
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
		NONE("Options:"), BUILD("Build (B)"), BUILD_ROAD("Build Road (R)"), MOVE_RESOURCE_SELECT("Move resources");
		
		private final String name;
		
		private Mode(String name){
			this.name = name;
		}
		
		@Override
		public String toString(){
			return name;
		}
	}
}
