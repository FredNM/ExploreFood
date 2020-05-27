package com.frednm.explorefood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.frednm.explorefood.common.base.BaseActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends BaseActivity {

    private NavHostFragment navHostFragment;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private View mainContainer;

    public static final String FOR_BOTTOM_BAR = "FOR_BOTTOM_BAR";
    public static final String FOR_LOCATION = "FOR_LOCATION";
    private static final String FOR_AUTHENTICATION = "FOR_AUTHENTICATION";

    // For GPS location
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationClient; // utilisé pour obtenir position GPS ! On doit avoir mis dans build.gradle le gms ... location
    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_REQUEST_ENABLE_GPS = 121;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 131;
    private static final int ERROR_DIALOG_REQUEST = 141;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainContainer = findViewById(R.id.main_container);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        setupNavigation();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this); // For location position
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return true;
    }

    //TODO Si onSupportNavigateUp() ne marche pas, tester onOptionsItemSelected !
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.popBackStack();
                return true;
            case R.id.action_rtl:
                // RTL support can be add if android:supportsRtl="true" is in the application in Manifest
                if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                    getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // RTL layout support
                } else {
                    getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // Disable RTL layout support
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupNavigation() {
        // Just to setup the navigation in the Bottom App Bar

        // 1-- Get the navController
        navController = Navigation.findNavController(mainContainer);

        // 2-- Configure the starting destination
        conditionalStartingDestination();

        // 3 -- Set the navigation on the BottomAppBar
        NavigationUI.setupWithNavController(bottomNavigationView, navController); // annotation si Java

        navController.addOnDestinationChangedListener(((controller, destination, arguments) -> handleDestinationChanges(destination)));
    }

    private void conditionalStartingDestination() {
        NavInflater navInflater = navController.getNavInflater();
        NavGraph navGraph = navInflater.inflate(R.navigation.nav_graph_main); // Get the nav graph

        SharedPreferences sharedPref = getSharedPreferences(FOR_AUTHENTICATION, Context.MODE_PRIVATE);
        boolean isConnected = sharedPref.getBoolean(getString(R.string.pref_authentication),false);

        if (isConnected) {
            navGraph.setStartDestination(R.id.action_home);
        } else {
            navGraph.setStartDestination(R.id.fragment_authentication);
            bottomNavigationView.setVisibility(View.GONE); // Cache la BAP si écran login affiché
        }

        navController.setGraph(navGraph); // Important de le faire, car on n'a pas mis le nav_graph au niveau du activity_main.xml
        // navController.setGraph(navGraph, bundle); // Si on a des paramètres à passer dans le Bundle
    }

    // private void handleDestinationChanges(NavController controller, NavDestination destination, Bundle arguments) {
    private void handleDestinationChanges(NavDestination destination) {
        Log.d("MainActivity", "The destination is : " + destination.getLabel());
        switch(destination.getId()) {
            case R.id.action_home:
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                bottomNavigationView.setVisibility(View.VISIBLE); // Reactive la BAP qu'on a caché quand on était sur le AuthenticationFragment
                break;
            case R.id.recipe_home:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                updateSharedPreferences("research"); // Les noms d'action mis ici dans le sharedPreferences sont récupérés dans le RecipeFragment
                // pour savoir comment charger les données
                break; // Sans le break, il va tous les exécuter !!!
            case R.id.action_favorite:
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                updateSharedPreferences("favorite");
                break;
            case R.id.action_basket:
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                updateSharedPreferences("basket");
                break;
            case R.id.fragment_detail_recipe:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Si on est sur le detail fragment, met flèche navigation back ! Ca ne
                // marche pas!
                break;
            case R.id.fragment_restaurant:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            default:
                Log.d("MainActivity", "The destination from id is : default ");
                break;
        }
    }

    private void updateSharedPreferences(String action) {
        // 1- save recipeName in SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences(FOR_BOTTOM_BAR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_bottom_view_action),action);
        editor.apply();
    }
    // TODO Bind Bottom App Bar, and implement navigation on Click, using Nav Component

    // ============================ For Google Map and Location =========================================

    // --- PARTIE 1 : Verifier si on a bien donner droit d'obtenir la position
    @Override
    protected void onResume() {
        super.onResume();
        if(checkMapServices()){
            if(mLocationPermissionGranted){
                getLastKnownLocation();
            }
            else{
                getLocationPermission();
            }
        }
    }

    // Méthode simple : si les google services sont installés, et que le Map est enable, il renvoie true
    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    // 1- Cette méthode sert à tester si oui ou non les Google Services sont utilisables (et donc installés) sur le phone en cours !
    // Si tel n'est pas le cas je pense qu'il propose de l'installer
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    // 2- Cette méthode est chargée de vérififer si l'utilisateur a déjà eu à donner la permission d'utiliser le GPS sur l'appli actuelle
    // Si tel n'est pas le cas, un AlertDialog va s'afficher, lui donnant la possibilité de donner ce droit là
    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    // 3- Appelé dans isMapsEnabled() au cas où il n'y a pas encore autorisation d'utiliser le GPS sur l'appli ! En fait, dans notre phone, au
    // niveau de settings, on a moyen d'activer ou pas utilisationd de GPS sur le phone ! C'est ça qu'on fait ici !
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        // Quand l'utilisateur va clicker sur Yes, une page va s'ouvrir (l'activité qu'on lance dans cet intent), et il pourra donc
                        // activer ou pas le GPS. Le résultat de son action sera traité dans on onActivityResult
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            //getChatrooms();
            getLastKnownLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(mLocationPermissionGranted){
                    getLastKnownLocation();
                }
                else{
                    getLocationPermission();
                }
            }
        }
    }

    public void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation: called.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    if (location != null ) {
                        Log.d(TAG, "onComplete: latitude: " + location.getLatitude());
                        Log.d(TAG, "onComplete: longitude: " + location.getLongitude());
                        SharedPreferences sharedPref = getSharedPreferences(FOR_LOCATION, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putLong(getString(R.string.pref_location_latitude), Double.doubleToLongBits(location.getLatitude()));
                        editor.putLong(getString(R.string.pref_location_longitude), Double.doubleToLongBits(location.getLongitude()));
                        editor.apply();
                    }
                }
            }
        });

    }

}
