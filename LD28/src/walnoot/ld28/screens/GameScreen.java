package walnoot.ld28.screens;

import walnoot.ld28.InputHandler;
import walnoot.ld28.LD28Game;
import walnoot.ld28.Node;
import walnoot.ld28.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GameScreen extends UpdateScreen{
	private static final float MIN_DISTANCE = 1f;
	private static final float MAX_DISTANCE = 2f;
	
	private static final Vector2 TMP_2 = new Vector2();
	private static final Vector3 TMP_3 = new Vector3();
	
	private final Node root;
	private Node selectedNode;
	private final SpriteBatch batch;
	private final OrthographicCamera camera;
	
	private Sprite preview;
	
	private Stage stage;
	
	private Mode mode = Mode.NONE;
	
	public GameScreen(LD28Game game){
		super(game);
		
		ShaderProgram shader =
				new ShaderProgram(Gdx.files.internal("shaders/alpha_test.vertex.glsl"),
						Gdx.files.internal("shaders/alpha_test.frag.glsl"));
		batch = new SpriteBatch(100, shader);
		camera = new OrthographicCamera();
		
		root = new Node(0f, 0f, null);
		root.setSelected(true);
		
		selectedNode = root;
		
		preview = new Sprite(Util.DOT_TEXTURE);
		preview.setSize(1f, 1f);
		preview.setOrigin(0.5f, 0.5f);
		
		setupUI();
	}
	
	private void setupUI(){
		stage = new Stage(960, 540, true, batch);
		stage.addActor(new Image(Util.HAMMER_TEXTURE));
	}
	
	@Override
	protected void render(){
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		root.render(batch, true);
		root.render(batch, false);
		
		if(mode == Mode.BUILD) preview.draw(batch);
		
		batch.end();
		
		stage.draw();
	}
	
	@Override
	public void update(){
		root.update();
		
		camera.unproject(TMP_3.set(Gdx.input.getX(), Gdx.input.getY(), 0f));
		
		TMP_2.set(TMP_3.x, TMP_3.y);
		
		switch (mode){
			case NONE:
				if(InputHandler.get().isJustTouched(Buttons.LEFT)){
					Node node = root.getNode(TMP_2);
					
					if(node != null){
						selectedNode.setSelected(false);
						selectedNode = node;
						selectedNode.setSelected(true);
					}
				}
				
				break;
			case BUILD:
				TMP_2.sub(selectedNode.position);
				
				if(TMP_2.len2() < MIN_DISTANCE * MIN_DISTANCE) TMP_2.nor().scl(MIN_DISTANCE);
				else if(TMP_2.len2() > MAX_DISTANCE * MAX_DISTANCE) TMP_2.nor().scl(MAX_DISTANCE);
				
				TMP_2.add(selectedNode.position);
				
				preview.setPosition(TMP_2.x - preview.getOriginX(), TMP_2.y - preview.getOriginY());
				
				if(InputHandler.get().isJustTouched(Buttons.LEFT)){
					root.addChild(new Node(TMP_2, selectedNode));
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
		
		if(InputHandler.get().build.isJustPressed()) mode = Mode.BUILD;
		else if(InputHandler.get().quit.isJustPressed()) mode = Mode.NONE;
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
