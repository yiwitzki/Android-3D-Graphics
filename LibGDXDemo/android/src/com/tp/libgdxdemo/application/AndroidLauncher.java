package com.tp.libgdxdemo.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tp.libgdxdemo.R;

public class AndroidLauncher extends Activity
{
    private Button createSceneBtn;
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }
    private void initView()
    {
        createSceneBtn = (Button) findViewById(R.id.create_scene_btn);
    }

	public void createScene(View v)
	{
		Intent intent = new Intent(AndroidLauncher.this, CreateSceneActivity.class);
		startActivity(intent);
	}
}
