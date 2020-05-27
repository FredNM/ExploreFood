package com.frednm.explorefood.common.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDirections;

import com.frednm.explorefood.common.utils.Event;
import com.frednm.explorefood.navigation.NavigationCommand;


public class BaseViewModel extends ViewModel {


    // FOR ERROR HANDLER
    protected MutableLiveData<Event<Integer>> _snackbarError = new MutableLiveData<>();
    public LiveData<Event<Integer>> snackbarError = _snackbarError;

    public LiveData<Event<Integer>> getSnackbarError() {
        return snackbarError;
    }

    // FOR NAVIGATION
    private MutableLiveData<Event<NavigationCommand>> _navigation = new MutableLiveData<>();
    public LiveData<Event<NavigationCommand>> navigation  = _navigation;

    /**
     * Convenient method to handle navigation from a [ViewModel]
     */
    protected void navigate(NavDirections directions) {
        _navigation.setValue(new Event(new NavigationCommand.To(directions)));
    }
}
