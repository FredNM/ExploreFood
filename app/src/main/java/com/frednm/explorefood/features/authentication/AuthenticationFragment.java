package com.frednm.explorefood.features.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.frednm.explorefood.MainActivity;
import com.frednm.explorefood.R;
import com.frednm.explorefood.databinding.FragmentAuthenticationBinding;
import com.frednm.explorefood.databinding.FragmentHomeBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class AuthenticationFragment extends Fragment {

    //FOR DATA
    private static final int RC_SIGN_IN = 293;
    private static final String FOR_AUTHENTICATION = "FOR_AUTHENTICATION";
    private FragmentAuthenticationBinding binding;
    CoordinatorLayout coordinatorLayout;
    Button login;

    public AuthenticationFragment() {
        // Required empty public constructor
    }

    public static AuthenticationFragment newInstance(String param1, String param2) {
        AuthenticationFragment fragment = new AuthenticationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false);
        login = binding.authenticationButtonLogin;
        coordinatorLayout = binding.authenticationCoordinatorLayout;
        login.setOnClickListener((view) -> onClickLoginButton());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.updateUIWhenResuming(); // Juste pour changer le button "Login" en "Connected"
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // --------------------
    // ACTIONS
    // --------------------

    public void onClickLoginButton() {
        if (this.isCurrentUserLogged()){
            // TODO S'il est connecté go directement sur HomeFragment ! Tel que j'ai re-écrit la logique dans le MainActivity, si ce Fragment apparait
            // alors l'utilisateur n'est pas connecté, donc cette condition ne sert à rien !
        } else {
            this.startSignInActivity();
        }
    }

    // --------------------
    // NAVIGATION
    // --------------------

    private void startSignInActivity(){
        startActivityForResult( // Le résulat est géré dans onActivityResult
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), //EMAIL
                                        new AuthUI.IdpConfig.GoogleBuilder().build()) )// GOOGLE
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.goyave)
                        .build(),
                RC_SIGN_IN);
    }

    // --------------------
    // UI
    // --------------------

    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void updateUIWhenResuming(){
        this.login.setText(this.isCurrentUserLogged() ? getString(R.string.login_text_logged) : getString(R.string.login_text_not_logged));
    }

    // --------------------
    // UTILS
    // --------------------

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

    private void restartActivity() {
        Intent intent = requireActivity().getIntent();
        requireActivity().finish();
        startActivity(intent);
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                updateSharedPreferences(true);
                showSnackBar(coordinatorLayout, getString(R.string.connection_succeed));
                NavHostFragment.findNavController(this).navigate(AuthenticationFragmentDirections.fragmentAuthenticationToActionHome());
            } else { // ERRORS
                updateSharedPreferences(false);
                if (response == null) {
                    showSnackBar(coordinatorLayout, getString(R.string.error_authentication_canceled));
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(coordinatorLayout, getString(R.string.error_no_internet));
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(coordinatorLayout, getString(R.string.error_unknown_error));
                }
            }
            // this.restartActivity();
        }
    }

    private void updateSharedPreferences(Boolean isConnected) {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(FOR_AUTHENTICATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.pref_authentication),isConnected);
        editor.apply();
    }
}
