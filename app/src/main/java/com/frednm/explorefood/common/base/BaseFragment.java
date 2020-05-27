package com.frednm.explorefood.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;

import com.frednm.explorefood.common.utils.Event;
import com.frednm.explorefood.navigation.NavigationCommand;
import com.google.android.material.snackbar.Snackbar;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;

abstract public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.configureDagger(); // TODO Super important, à mettre bel et bien dans le onCreate !!!
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();
        setupSnackbar(getViewModel().getSnackbarError(), Snackbar.LENGTH_LONG);
    }

    abstract protected BaseViewModel getViewModel() ;

    private void configureDagger(){
        AndroidSupportInjection.inject(this); // AndroidInjection.inject(requireActivity());
    }

    /**
     * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
     */
    private void setupSnackbar(LiveData<Event<Integer>> snackbarEvent, Integer timeLength) {
        snackbarEvent.observe(requireActivity(), (event) -> handleSnackBarEvent(event, timeLength));
    }

    private void handleSnackBarEvent(Event<Integer> event, Integer timeLength){
        // Log.d("BaseActivity","The event content is "+getString(event.peekContent()));
        if (event.getContentIfNotHandled() != null) {
            Log.d("BaseActivity","The text is "+event.getContentIfNotHandled());
            showSnackbar(getString(event.peekContent()), timeLength);
        }
    }

    private void showSnackbar(String snackbarText, Integer timeLength) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content), snackbarText, timeLength).show();
    }

    /**
     * Observe a [NavigationCommand] [Event] [LiveData].
     * When this [LiveData] is updated, [Fragment] will navigate to its destination
     */
    private void observeNavigation(BaseViewModel viewModel) {
        viewModel.navigation.observe(getViewLifecycleOwner(), (event) -> handleObserveNavigation(event));
    }

    private void handleObserveNavigation(Event<NavigationCommand> event) {
        if (event != null) {
            if (event.getContentIfNotHandled() != null) {
                if (event.getContentIfNotHandled() instanceof NavigationCommand.To) {
                    // findNavController().navigate(command.directions, getExtras()); // Kotlin
                    // NavHostFragment.findNavController(this).navigate(command.directions, getExtras()); // Java
                }
                if (event.getContentIfNotHandled() instanceof NavigationCommand.Back) {
                    // findNavController().navigateUp() // Kotlin
                    NavHostFragment.findNavController(this).navigateUp(); // Java
                }
            }
        }
    }

    /**
     * [FragmentNavigatorExtras] mainly used to enable Shared Element transition
     */
    public FragmentNavigator.Extras getExtras() {
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                //        .addSharedElement(imageView, "header_image")
                //        .addSharedElement(titleView, "header_title")
                .build();

        return extras;
    }

}
