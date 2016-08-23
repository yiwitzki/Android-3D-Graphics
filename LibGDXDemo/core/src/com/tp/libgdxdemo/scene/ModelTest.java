package com.tp.libgdxdemo.scene;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
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
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.tp.libgdxdemo.intf.ICrossPlatformInterface;
import com.tp.libgdxdemo.util.Config;
import com.tp.libgdxdemo.util.ObjectInstance;

public class ModelTest implements ApplicationListener
{

	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public AssetManager assets;
	public Array<ObjectInstance> instances = new Array<ObjectInstance>();
	public Environment environment;
	private SpriteBatch batch;
	private Texture texture;
	private ObjectInstance playerInstance;
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
		//environment.set(new ColorAttribute(ColorAttribute.Fog, 0.13f, 0.13f, 0.13f, 1f));
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 20f, 20f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
		batch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("alonso.jpeg"));
		assets = new AssetManager();
		assets.load(Config.Model.PLAYER_MODEL_NAME, Model.class);
        assets.load("spacesphere.obj", Model.class);
		loading = true;
	}
	private void doneLoading() {
		Model ship = assets.get(Config.Model.PLAYER_MODEL_NAME, Model.class);
        Model space = assets.get("spacesphere.obj", Model.class);
        ObjectInstance spaceInstance = new ObjectInstance(space);
		playerInstance = new ObjectInstance(ship, Config.Model.PLAYER_NODE_NAME);

        playerInstance.transform.setToRotation(Vector3.X, 90).trn(0,0f,0);
        instances.add(playerInstance);
        instances.add(spaceInstance);
		Gdx.app.debug("material", playerInstance.model.nodes.size + "");
		for (int i = 0; i < playerInstance.nodes.size; i++)
			Gdx.app.debug("material", playerInstance.nodes.get(i).id);

		loading = false;
	}
	@Override
	public void render ()
	{
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
	}

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

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

	}
}
