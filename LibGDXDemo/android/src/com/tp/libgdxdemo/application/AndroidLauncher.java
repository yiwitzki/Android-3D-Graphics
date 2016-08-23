package com.tp.libgdxdemo.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tp.libgdxdemo.R;

public class AndroidLauncher extends Activity
{
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

	public void createScene(View v)
	{
		Intent intent = new Intent(AndroidLauncher.this, CreateSceneActivity.class);
		startActivity(intent);
	}

    public void interactWithScene(View v)
    {
        Intent intent = new Intent(AndroidLauncher.this, InteractWithSceneActivity.class);
        startActivity(intent);
    }
    public void animationScene(View v)
    {
        Intent intent = new Intent(AndroidLauncher.this, AnimationActivity.class);
        startActivity(intent);
    }
}
