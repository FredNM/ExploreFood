package com.frednm.explorefood.common.base;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.frednm.explorefood.data.repository.utils.ConnectivityLiveData;
import com.google.android.material.snackbar.Snackbar;

import com.frednm.explorefood.common.utils.Event;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Question: Why the getContentIfNotHandled() Event method doesn't work !? In fact it's almost always returning null, even when the
 * event has not yet been handled !? This is why I used peekContent() method in handleSnackBar(), but the drawback here is
 * that during a screen rotation it will fire again, so the SnackBar will be shown !!!
 */

abstract public class BaseActivity extends AppCompatActivity {

  //  @Inject
  //  ConnectivityLiveData connectivityLiveData;

    private Observer<Boolean> onConnectedObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.configureDagger();
        super.onCreate(savedInstanceState);
        //connectivityLiveData.observe(this, onConnectedObserver);

     /**   if (onConnectedObserver == null) {
            onConnectedObserver = isConnected -> {
                if (isConnected) {
                    //snackbarDelegate.showSnackbar(SnackbarDelegate.CONNECTED, listener);
                    connectivityLiveData.removeObserver(onConnectedObserver);
                    onConnectedObserver = null;
                }
            };
        } else {
            connectivityLiveData.observe(this, onConnectedObserver);
        } */
    }

    // Read second comment here (https://stackoverflow.com/questions/7621349/do-you-use-onpostcreate-method)
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        //setupSnackbar(getViewModel().getSnackbarError(), Snackbar.LENGTH_LONG);
        super.onPostCreate(savedInstanceState);
    }

    //abstract protected BaseViewModel getViewModel() ;

    private void configureDagger(){
        AndroidInjection.inject(this);
    }


    /**
     * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
     */
    private void setupSnackbar(LiveData<Event<Integer>> snackbarEvent, Integer timeLength) {
        snackbarEvent.observe(this, (event) -> handleSnackBarEvent(event, timeLength));
    }

    private void handleSnackBarEvent(Event<Integer> event, Integer timeLength){
        Log.d("BaseActivity","The event content is "+getString(event.peekContent()));
        if (event.getContentIfNotHandled() != null) {
            Log.d("BaseActivity","The text is "+event.getContentIfNotHandled());
            showSnackbar(getString(event.peekContent()), timeLength);
        }
    }

    private void showSnackbar(String snackbarText, Integer timeLength) {
        Snackbar.make(findViewById(android.R.id.content), snackbarText, timeLength).show();
    }
}
