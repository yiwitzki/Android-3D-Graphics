/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.tp.libgdxdemo.scene;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.tp.libgdxdemo.util.Config;
import com.tp.libgdxdemo.util.GdxTest;
import com.tp.libgdxdemo.util.ObjectInstance;
import com.tp.libgdxdemo.ShaderProgram.SimpleTextureShader;

public class FogTest extends GdxTest implements ApplicationListener, Screen
{
    public PerspectiveCamera cam;
    public CameraInputController inputController;
    public ModelBatch modelBatch, modelBatchShadows;
    public Model model;
    public ModelInstance instance;
    public Environment environment;
    public AssetManager assets;
    public boolean loading;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    private ShaderProgram shaderProgram, shaderProgramShadows;
    private Texture playerTexture, pitchTexture;

    @Override
    public void create()
    {
        initShaders();
        //modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
       // environment.set(new ColorAttribute(ColorAttribute.Fog, 0.13f, 0.13f, 0.13f, 0.1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(30f, 10f, 30f);
        cam.lookAt(0, 0, 0);
        cam.near = 0.1f;
        cam.far = 60f;
        cam.update();

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position
                | Usage.Normal);
        instance = new ModelInstance(model);
        instances.add(instance);
        Gdx.input.setInputProcessor(new InputMultiplexer(this, inputController = new CameraInputController(cam)));

        assets = new AssetManager();
        assets.load(Config.Model.PLAYER_MODEL_NAME, Model.class);
        assets.load("pitch.g3db", Model.class);
        pitchTexture = new Texture(Gdx.files.internal("Vegetation_Grass_Artificial.jpg"));
        loading = true;
    }

    private void doneLoading() {
        Model space = assets.get("pitch.g3db", Model.class);
        ObjectInstance spaceInstance = new ObjectInstance(space);

        Material pitchMaterial = spaceInstance.getMaterial("Vegetation_Grass_Artificial");
        pitchMaterial.set(TextureAttribute.createDiffuse(pitchTexture));


        spaceInstance.transform.trn(0,15,0);
        instances.add(spaceInstance);

        loading = false;
    }

    public ShaderProgram setupShader(final String prefix)
    {
        ShaderProgram.pedantic = false;

        final ShaderProgram shaderProgram = new ShaderProgram(Gdx.files.internal(prefix + "_v.glsl"), Gdx.files.internal(prefix + "_f.glsl"));
        if (!shaderProgram.isCompiled())
        {
            System.err.println("Error with shader " + prefix + ": " + shaderProgram.getLog());
            System.exit(1);
        }
        else
        {
            Gdx.app.log("init", "Shader " + prefix + " compilled " + shaderProgram.getLog());
        }
        return shaderProgram;
    }

    public void initShaders()
    {
        shaderProgram = setupShader("depthmap");
        modelBatch = new ModelBatch(new DefaultShaderProvider()
        {
            @Override
            protected Shader createShader(final Renderable renderable)
            {
                return new SimpleTextureShader(renderable, shaderProgram);
            }
        });
        shaderProgramShadows = setupShader("shader_fog");
//        modelBatchShadows = new ModelBatch(new DefaultShaderProvider()
//        {
//            @Override
//            protected Shader createShader(Renderable renderable)
//            {
//                return super.createShader(renderable);
//            }
//        })
       // modelBatch = new ModelBatch();
    }
    @Override
    public void render()
    {
        //animate();
        if (loading && assets.update())
            doneLoading();

        inputController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);


        modelBatch.begin(cam);

        modelBatch.render(instances, environment);
        modelBatch.end();
        shaderProgram.begin();

    }

    @Override
    public void dispose()
    {
        modelBatch.dispose();
        model.dispose();
    }

    public boolean needsGL20()
    {
        return true;
    }

    public void resume()
    {
    }

    @Override
    public void hide()
    {

    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {

    }

    public void resize(int width, int height)
    {
    }

    public void pause()
    {
    }
}
