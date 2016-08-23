package com.tp.libgdxdemo.application;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tp.libgdxdemo.scene.AnimationScene;
import com.tp.libgdxdemo.view.PlayerInfoDialog;

/**
 * Created by TP on 16/8/23.
 */
public class AnimationActivity extends AndroidApplication
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new AnimationScene(new PlayerInfoDialog(this)), config);
    }
}
