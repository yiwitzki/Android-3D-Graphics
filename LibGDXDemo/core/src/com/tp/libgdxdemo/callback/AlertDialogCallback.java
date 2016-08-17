package com.tp.libgdxdemo.callback;

/**
 * Created by TP on 16/8/17.
 */
public abstract class AlertDialogCallback
{
    public abstract void positiveButtonPressed();
    public void negativeButtonPressed(){}; // This will not be required
    public void cancelled(){}; // This will not be required
}
