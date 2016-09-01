package com.tp.libgdxdemo.application;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tp.libgdxdemo.ShadowMapping;
import com.tp.libgdxdemo.scene.InteractScene;
import com.tp.libgdxdemo.scene.MainScreen;
import com.tp.libgdxdemo.scene.ModelTest;
import com.tp.libgdxdemo.scene.RenderTest;
import com.tp.libgdxdemo.view.PlayerInfoDialog;

/**
 * Created by TP on 16/8/17.
 */
public class CreateSceneActivity extends AndroidApplication
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new ModelTest(new PlayerInfoDialog(this)), config);
    }
}
