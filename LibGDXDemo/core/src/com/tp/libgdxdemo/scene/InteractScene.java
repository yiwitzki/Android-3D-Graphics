package com.tp.libgdxdemo.scene;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.tp.libgdxdemo.callback.AlertDialogCallback;
import com.tp.libgdxdemo.intf.ICrossPlatformInterface;
import com.tp.libgdxdemo.util.Config;
import com.tp.libgdxdemo.util.ObjectInstance;

/**
 * Created by TP on 16/8/19.
 */
public class InteractScene extends InputAdapter implements ApplicationListener
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
    private Vector3 position = new Vector3();
    private int selecting = -1;

    ICrossPlatformInterface mInterface;
    public InteractScene(ICrossPlatformInterface iCrossPlatformInterface)
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
        Gdx.input.setInputProcessor(new InputMultiplexer(this, camController));
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        selecting = getObject(screenX, screenY);
        Gdx.app.debug("material", "touch down" + selecting);
        return selecting >= 0;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        Gdx.app.debug("material", "touch up");
        if (selecting >= 0) {
            if (selecting == getObject(screenX, screenY))
                if (instances.get(selecting).equals(playerInstance))
                {
                    mInterface.showAlertDialog(new AlertDialogCallback()
                    {
                        @Override
                        public void positiveButtonPressed()
                        {

                        }
                    });
                }
            selecting = -1;
            return true;
        }
        return false;
    }

    private int getObject(int screenX, int screenY)
    {
        Ray ray = cam.getPickRay(screenX, screenY);

        int result = -1;
        float distance = -1;

        for (int i = 0; i < instances.size; ++i) {
            final ObjectInstance instance = instances.get(i);
            if (!instance.equals(playerInstance))
                continue;
            instance.transform.getTranslation(position);
            position.add(instance.center);

            final float len = ray.direction.dot(position.x-ray.origin.x, position.y-ray.origin.y, position.z-ray.origin.z);
            Gdx.app.debug("material", "get object" + len);
            if (len < 0f)
                continue;

            float dist2 = position.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);

            Gdx.app.debug("material", "get object" + dist2);

            if (distance >= 0f && dist2 > distance)
                continue;
            Gdx.app.debug("material", "get object" + instance.radius);
            if (dist2 <= instance.radius * instance.radius) {
                result = i;
                distance = dist2;
            }
        }
        return result;
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
