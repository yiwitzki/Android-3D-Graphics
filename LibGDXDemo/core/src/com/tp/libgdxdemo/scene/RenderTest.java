package com.tp.libgdxdemo.scene;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
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
 * Created by TP on 16/8/30.
 */
public class RenderTest extends InputAdapter implements ApplicationListener
{

    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    private Array<ModelInstance> playerArray = new Array<ModelInstance>();
    public Environment environment;
    private ObjectInstance pitchInstance;
    private Array<Texture> playerTexture;
    private Texture pitchTexture;
    private Vector3 position = new Vector3();
    private int selecting = -1;
    public boolean loading;


    ICrossPlatformInterface mInterface;

    public RenderTest(ICrossPlatformInterface iCrossPlatformInterface)
    {
        this.mInterface = iCrossPlatformInterface;
    }

    @Override
    public void create()
    {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 254f, 254f, 65f, 1.f));
        environment.add(new DirectionalLight().set(254f, 254f, 65f, -1f, -0.8f, -0.2f));
        //environment.add(new SpotLight().set(0.8f, 0.8f, 0.8f, new Vector3()));
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 20f, 20f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 1300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(new InputMultiplexer(this, camController));
        loadTexture();
        assets = new AssetManager();
        assets.load(Config.Model.PLAYER_MODEL_NAME, Model.class);
        assets.load("models//EuroArena.g3db", Model.class);
        loading = true;
    }

    private void doneLoading()
    {
        createPlayer();
        createPitch();
        loading = false;
    }
    private void loadTexture()
    {
        if (playerTexture == null)
            playerTexture = new Array<Texture>();
        for (int i = 0; i < 4; i++)
        {
            playerTexture.add(new Texture(Gdx.files.internal(Config.Texture.PLAYER_TEXTURE[i])));
        }
        pitchTexture = new Texture(Gdx.files.internal("texture//Vegetation_Grass_Artificial.jpg"));
    }
    private void createPlayer()
    {
        Model player = assets.get(Config.Model.PLAYER_MODEL_NAME, Model.class);
        for (int i = 0; i < player.meshes.size; i++)
            Gdx.app.debug("node", player.meshes.get(i).toString());


        for (int i = 0; i < 4; i++)
        {
            ObjectInstance instance = new ObjectInstance(player, Config.Model.PLAYER_NODE_NAME);
            Material material = instance.getMaterial("PlayerFront");
            material.set(new ColorAttribute(ColorAttribute.Diffuse, 255f,255f,255f,1));
            material.set(TextureAttribute.createDiffuse(playerTexture.get(i)));
            playerArray.add(instance);
            instance.transform.setToRotation(Vector3.X, 90).trn(-15 + i * 10,0f,0);
            instances.add(instance);
        }
    }
    private void createPitch()
    {
        Model pitch = assets.get("models//EuroArena.g3db", Model.class);
        pitchInstance = new ObjectInstance(pitch);
//        Material pitchMaterial = pitchInstance.getMaterial("Vegetation_Grass_Artificial");
//        pitchMaterial.set(TextureAttribute.createDiffuse(pitchTexture));
        pitchInstance.transform.trn(0,0,0);
        instances.add(pitchInstance);
    }
    @Override
    public void render()
    {
        if (loading && assets.update())
            doneLoading();
        camController.update();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
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
    public void dispose()
    {
        modelBatch.dispose();
        assets.dispose();
        playerTexture.clear();
        instances.clear();
    }

    @Override
    public void resize(int width, int height)
    {

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
            {
                if (!instances.get(selecting).equals(pitchInstance))
                {
                    mInterface.showAlertDialog(new AlertDialogCallback()
                    {
                        @Override
                        public void positiveButtonPressed()
                        {

                        }
                    });
                }
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
            final ObjectInstance instance = (ObjectInstance) instances.get(i);
            if (instance.equals(pitchInstance))
                continue;
            instance.transform.getTranslation(position);
            position.add(instance.center);

            final float len = ray.direction.dot(position.x-ray.origin.x, position.y-ray.origin.y, position.z-ray.origin.z);
            if (len < 0f)
                continue;

            float dist2 = position.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);


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
}
