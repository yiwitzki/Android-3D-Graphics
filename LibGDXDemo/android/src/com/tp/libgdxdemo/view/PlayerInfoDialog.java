package com.tp.libgdxdemo.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.tp.libgdxdemo.application.AndroidLauncher;
import com.tp.libgdxdemo.callback.AlertDialogCallback;
import com.tp.libgdxdemo.intf.ICrossPlatformInterface;

/**
 * Created by TP on 16/8/10.
 */
public class PlayerInfoDialog implements ICrossPlatformInterface
{
    private Activity mActivity;
    public PlayerInfoDialog(Activity launcher)
    {
        this.mActivity = launcher;
    }

    @Override
    public void showAlertDialog(final AlertDialogCallback callback)
    {
        mActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Test");
                builder.setMessage("Testing");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        callback.positiveButtonPressed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        callback.negativeButtonPressed();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
