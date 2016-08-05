package com.tp.android3dlibstest.View;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.tp.android3dlibstest.R;

import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;

/**
 * Created by TP on 16/8/5.
 */
public class Renderer extends RajawaliRenderer
{
    private Context context;
    private DirectionalLight directionalLight;
    private Sphere earthSphere;

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

        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColor(0);

        Texture earthTexture = new Texture("Earth", R.drawable.earthtruecolor_nasa_big);
        try{
            material.addTexture(earthTexture);

        } catch (ATexture.TextureException error){
            Log.d("DEBUG", "TEXTURE ERROR");
        }

        earthSphere = new Sphere(1, 24, 24);
        earthSphere.setMaterial(material);
        getCurrentScene().addChild(earthSphere);
        getCurrentCamera().setZ(4.2f);
    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime)
    {
        super.onRender(ellapsedRealtime, deltaTime);
        earthSphere.rotate(Vector3.Axis.Y, 1.0);
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
