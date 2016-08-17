package com.tp.android3dlibstest.View;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.tp.android3dlibstest.R;

import org.rajawali3d.Camera;
import org.rajawali3d.ChaseCamera;
import org.rajawali3d.Object3D;
import org.rajawali3d.OrthographicCamera;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;

/**
 * Created by TP on 16/8/5.
 */
public class Renderer extends RajawaliRenderer
{
    private Context context;
    private DirectionalLight directionalLight;
    private Object3D ballOBJ;
    private final String TAG = "Renderer";
    public Renderer(Context context)
    {
        super(context);
        this.context = context;
        setFrameRate(60);
    }

    @Override
    protected void initScene()
    {
        directionalLight = new DirectionalLight(1f, .2f, -1.0f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(2);
        getCurrentScene().addLight(directionalLight);
        Camera camera = new Camera();
        camera = new Camera();

        getCurrentScene().addCamera(camera);
//
//        Material material = new Material();
//        material.enableLighting(true);
//        material.setDiffuseMethod(new DiffuseMethod.Lambert());
//        material.setColor(0);
//
//        Texture earthTexture = new Texture("Earth", R.drawable.earthtruecolor_nasa_big);
//        try{
//            material.addTexture(earthTexture);
//
//        } catch (ATexture.TextureException error){
//            Log.d("DEBUG", "TEXTURE ERROR");
//        }
//
//        earthSphere = new Sphere(1, 24, 24);
//        earthSphere.setMaterial(material);
//        getCurrentScene().addChild(earthSphere);
//        getCurrentCamera().setZ(4.2f);
        LoaderOBJ loaderOBJ = new LoaderOBJ(this, R.raw.player_obj);
        try
        {
            loaderOBJ.parse();
            ballOBJ = loaderOBJ.getParsedObject();
            Log.d(TAG, "initScene: " + ballOBJ.getNumChildren());
            getCurrentScene().addChild(ballOBJ);
        } catch (ParsingException e)
        {
            e.printStackTrace();
        }
    }



    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime)
    {
        super.onRender(ellapsedRealtime, deltaTime);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset)
    {

    }


    @Override
    public void onTouchEvent(MotionEvent event)
    {
    }
}
