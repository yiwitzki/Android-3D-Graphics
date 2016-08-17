package com.tp.libgdxdemo;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import com.tp.libgdxdemo.callback.AlertDialogCallback;
import com.tp.libgdxdemo.intf.ICrossPlatformInterface;

public class ModelTest extends ApplicationAdapter {

	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public AssetManager assets;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	public Environment environment;
	private SpriteBatch batch;
	private Texture texture;
	private ModelInstance playerInstance;
	public boolean loading;

	ICrossPlatformInterface mInterface;
	public ModelTest(ICrossPlatformInterface iCrossPlatformInterface)
	{
		this.mInterface = iCrossPlatformInterface;
	}

	@Override
	public void create ()
	{
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		environment.set(new ColorAttribute(ColorAttribute.Fog, 0.13f, 0.13f, 0.13f, 1f));
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(200f, 1000f, -50f);
		cam.lookAt(0,0,0);
		cam.near = 30f;
		cam.far = 1300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
		batch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("alonso.jpeg"));
		assets = new AssetManager();
		assets.load("PlayerV4.1.g3db", Model.class);

		loading = true;
	}
	private void doneLoading() {
		Model ship = assets.get("PlayerV4.1.g3db", Model.class);
		playerInstance = new ModelInstance(ship);
		instances.add(playerInstance);

		Gdx.app.debug("material", playerInstance.model.nodes.size + "");
		for (int i = 0; i < playerInstance.materials.size; i++)
			Gdx.app.debug("material", playerInstance.materials.get(i).id);
		mInterface.showAlertDialog(new AlertDialogCallback()
		{
			@Override
			public void positiveButtonPressed()
			{

			}
		});
		loading = false;
	}
	@Override
	public void render () {
		if (loading && assets.update())
			doneLoading();
		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
		batch.begin();
		if (playerInstance != null)
		{
			Material material = playerInstance.getMaterial("PlayerFront");
			material.set(TextureAttribute.createDiffuse(texture));
		}

		batch.end();

//		ShapeRenderer circleRender = new ShapeRenderer();
//		circleRender.begin(ShapeRenderer.ShapeType.Line);
//		circleRender.setColor(Color.GOLD);
//		circleRender.circle(100, 100, 200);
//		circleRender.end();

	}

	@Override
	public void dispose ()
	{
		modelBatch.dispose();
		batch.dispose();
		assets.dispose();
		texture.dispose();
		instances.clear();
	}

	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);
	}
}
